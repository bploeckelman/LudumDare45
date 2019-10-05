package lando.systems.ld45.effects.trail;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld45.Assets;

public class Trail {

    private ImmediateModeRenderer20 gl20;
    private Array<Vector2> texcoord = new Array<>();
    private Array<Vector2> tristrip = new Array<>();
    private Array<Color> colors = new Array<>();
    private Vector2 perp = new Vector2();
    private int batchSize;

    private Pool<Vector2> vec2Pool = Pools.get(Vector2.class, 1000);
    private Pool<Color> colorPool = Pools.get(Color.class, 1000);

    public Color color;
    public Texture texture;
    public float thickness = 20f;
    public float endcap = 1f;

    public Trail(Color color, Assets assets) {
        this.gl20 = new ImmediateModeRenderer20(false, true, 1);
        this.texture = assets.pathGradientTexture;
        this.color = color;
    }

    public void render(OrthographicCamera cam) {
        if (tristrip.isEmpty()) {
            return;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        texture.bind();

        gl20.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);
        {
            for (int i = 0; i < tristrip.size; ++i) {
                // restart the batch once
                if (i == batchSize) {
                    gl20.end();
                    gl20.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);
                }

                float alpha = 0.5f * (1f - (i / (float) tristrip.size));
                Color color = colors.get(i);
                Vector2 vertex = tristrip.get(i);
                Vector2 texCoord = texcoord.get(i);

                gl20.color(color.r, color.g, color.b, alpha);
                gl20.texCoord(texCoord.x, 0f);
                gl20.vertex(vertex.x, vertex.y, 0f);
            }
        }
        gl20.end();
    }

    private int generate(Array<Vector2> input, int mult) {
        int initialNumVertices = tristrip.size;

        if (endcap <= 0) {
            tristrip.add(input.get(0));
        } else {
            Vector2 p0 = input.get(0);
            Vector2 p1 = input.get(1);
            perp.set(p0).sub(p1).scl(endcap);
            tristrip.add(vec2Pool.obtain().set(p0.x + perp.x, p0.y + perp.y));
        }
        texcoord.add(vec2Pool.obtain().set(0f, 0f));
        colors.add(colorPool.obtain().set(color));

        for (int i=0; i<input.size-1; i++) {
            Vector2 p0 = input.get(i);
            Vector2 p1 = input.get(i+1);

            //get direction and normalize it
            perp.set(p0).sub(p1).nor();

            //get perpendicular
            perp.set(-perp.y, perp.x);

            // scale the thickness to taper the tail of the trail
            float thick = thickness * (1f - (i / (float) input.size));

            //move outward by thickness
            perp.scl(thick / 2f);

            //decide on which side we are using
            perp.scl(mult);

            //add the tip of perpendicular
            tristrip.add(vec2Pool.obtain().set(p0.x + perp.x, p0.y + perp.y));
            //0.0 -> end, transparent
            texcoord.add(vec2Pool.obtain().set(0f, 0f));
            colors.add(colorPool.obtain().set(color));

            //add the center point
            tristrip.add(vec2Pool.obtain().set(p0.x, p0.y));
            //1.0 -> center, opaque
            texcoord.add(vec2Pool.obtain().set(1f, 0f));
            colors.add(colorPool.obtain().set(color));
        }

        //final point
        if (endcap <= 0) {
            tristrip.add(input.get(input.size - 1));
        } else {
            Vector2 p0 = input.get(input.size-2);
            Vector2 p1 = input.get(input.size-1);
            perp.set(p1).sub(p0).scl(endcap);
            tristrip.add(vec2Pool.obtain().set(p1.x + perp.x, p1.y + perp.y));
        }
        //end cap is transparent
        texcoord.add(new Vector2(0f, 0f));
        colors.add(colorPool.obtain().set(color));

        return tristrip.size - initialNumVertices;
    }

    public void update(Array<Vector2> input) {
        tristrip.forEach(vec2Pool::free);
        texcoord.forEach(vec2Pool::free);
        colors.forEach(colorPool::free);

        tristrip.clear();
        texcoord.clear();
        colors.clear();

        // need two or more points to make a trail
        if (input.size < 2) {
            return;
        }

        // generate triangles for each half of the trail
        batchSize = generate(input, 1);
        int bSize = generate(input, -1);
    }

}
