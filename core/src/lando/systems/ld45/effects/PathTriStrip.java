package lando.systems.ld45.effects;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Game;

public class PathTriStrip {
    public enum Side { top, bottom }

    private Array<Vector2> texcoord = new Array<>();
    private Array<Vector2> tristrip = new Array<>();
    private Array<Color> colors = new Array<>();
    private Vector2 perp = new Vector2();
    private ImmediateModeRenderer20 gl20;
    private int batchSize;

    public float thickness = 20f;


    public static final int POSITION_COMPONENTS = 2;
    public static final int COLOR_COMPONENTS = 4;
    public static final int TEXTURE_COMPONENTS = 2;
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS + TEXTURE_COMPONENTS;
    public static final int MAX_TRIS = 2500;
    public static final int MAX_VERTS = MAX_TRIS * 3;

    public float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];
    private int index = 0;
    private Mesh mesh;
    private ShaderProgram shader;
    private Game game;
    private Color color;
    float accum;

    public PathTriStrip(Game game, Color color) {
        this.accum = 0;
        this.game = game;
        this.shader = game.assets.ballTrailShader;
        mesh = new Mesh(false, MAX_VERTS, 0,
                        new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                        new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, TEXTURE_COMPONENTS, "a_texCoord0"),
                        new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));

        gl20 = new ImmediateModeRenderer20(false, true, 1);
        this.color = color;
//        generate(input, Side.top);
//        generate(input, Side.bottom);
//        generateMesh(input);
    }

    public void update(float dt) {
        accum += dt;
    }

    public void render(OrthographicCamera camera) {
        if (tristrip.size <= 0) return;

        gl20.begin(camera.combined, GL20.GL_TRIANGLE_STRIP);
        {
            for (int i = 0; i < tristrip.size; ++i) {
                if (i == batchSize) {
                    gl20.end();
                    gl20.begin(camera.combined, GL20.GL_TRIANGLE_STRIP);
                }
                Vector2 point = tristrip.get(i);
                Vector2 tex = texcoord.get(i);
                Color color = colors.get(i);
                gl20.color(color);
                gl20.texCoord(tex.x, 0f);
                gl20.vertex(point.x, point.y, 0f);
            }
        }
        gl20.end();
    }

    public void renderMesh(Vector2[] points) {
        generateMesh(points);
        flush();
    }

    Color tempColor = new Color();
    private void generateMesh(Vector2[] points) {
        for (int i = 0; i < points.length - 1; i++){
            float interp = (i) / (float)(points.length);

            int nextIndex = (i+1);// % points.length;
            Vector2 firstPoint = points[i];
            Vector2 nextPoint = points[nextIndex];
            // normalized direction
            perp.set(firstPoint).sub(nextPoint).nor();

            // perpendicularized
            perp.set(-perp.y, perp.x);

            // scale thickness to taper the tail of the trail
            float thick = thickness * (1f - (i / (float) points.length));

            // extrude by thickness
            perp.scl(thick / 2f);

            tempColor = color;

            // first vertex
            verts[index++] = firstPoint.x + perp.x;
            verts[index++] = firstPoint.y + perp.y;
            verts[index++] = interp;
            verts[index++] = 1;
            verts[index++] = tempColor.r;
            verts[index++] = tempColor.g;
            verts[index++] = tempColor.b;
            verts[index++] = tempColor.a;

            // second
            verts[index++] = firstPoint.x - perp.x;
            verts[index++] = firstPoint.y - perp.y;
            verts[index++] = interp;
            verts[index++] = 0;
            verts[index++] = tempColor.r;
            verts[index++] = tempColor.g;
            verts[index++] = tempColor.b;
            verts[index++] = tempColor.a;

            tempColor = color;

            //third
            verts[index++] = nextPoint.x + perp.x;
            verts[index++] = nextPoint.y + perp.y;
            verts[index++] = (i+1)/(float)points.length;
            verts[index++] = 1;
            verts[index++] = tempColor.r;
            verts[index++] = tempColor.g;
            verts[index++] = tempColor.b;
            verts[index++] = tempColor.a;

            verts[index++] = nextPoint.x - perp.x;
            verts[index++] = nextPoint.y - perp.y;
            verts[index++] = (i+1)/(float)points.length;
            verts[index++] = 0;
            verts[index++] = tempColor.r;
            verts[index++] = tempColor.g;
            verts[index++] = tempColor.b;
            verts[index++] = tempColor.a;

        }
    }

    private void flush() {
        if (index == 0) return;
        mesh.setVertices(verts);
        int vertexCount = index/NUM_COMPONENTS;
        shader.begin();
        game.assets.ballTrailTexture.bind(0);
        shader.setUniformMatrix("u_projTrans", game.getScreen().worldCamera.combined);
        shader.setUniformi("u_texture", 0);
        shader.setUniformf("u_time", accum);
        mesh.render(shader, GL20.GL_TRIANGLE_STRIP, 0, vertexCount);
        shader.end();
        index = 0;
    }

    public void generate(Array<Vector2> input, Side side) {
        // add segments along the input path
        for (int i = 0; i < input.size - 1; ++i) {
            addSegmentFromInputPointsAtIndex(input, side, i);
        }

        // since the path is continuous, re-add the 0th points again at the end of the loop to connect it
        addSegmentFromInputPointsAtIndex(input, side, 0);

        batchSize += tristrip.size;
    }

    private void addSegmentFromInputPointsAtIndex(Array<Vector2> input, Side side, int index) {
        // get the interpolation value [0,1] for the current index along the whole path for generating a color
        float interp = (index) / (float)(input.size);

        Vector2 p1 = input.get(index);
        Vector2 p2 = input.get(index+1);

        // normalized direction
        perp.set(p1).sub(p2).nor();

        // perpendicularized
        perp.set(-perp.y, perp.x);

        // NOTE: using using something like this scales the thickness based on the position along the path
//            float thicc = thickness * (1f - ((i) / (float)(input.size)));

        // extrude by thickness
        perp.scl(thickness / 2f);

        // decide which side we're using
        perp.scl((side == Side.top) ? 1 : -1);

        // add the tip of perpendicular
        tristrip.add(new Vector2(p1.x + perp.x, p1.y + perp.y));
        // 0.0 -> end, transparent
        texcoord.add(new Vector2(0f, 0f));

        colors.add(new Color(0.5f, 0f, 0.5f, 1f)); // PURPLE

        // add the center point
        tristrip.add(new Vector2(p1.x, p1.y));
        // 1.0 -> center, opaque
        texcoord.add(new Vector2(1f, 0f));

        colors.add(new Color(0.5f, 0f, 0.5f, 1f)); // PURPLE
    }

}

