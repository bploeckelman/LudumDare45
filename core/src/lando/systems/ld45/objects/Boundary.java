package lando.systems.ld45.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.collision.Segment2D;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.AssetType;


public class Boundary {
    private static final int NUM_COMPONENTS_POSITION = 2;
    private static final int NUM_COMPONENTS_TEXTURE = 2;
    private static final int NUM_COMPONENTS_COLOR = 0;
    private static final int NUM_COMPONENTS_PER_VERTEX = NUM_COMPONENTS_POSITION + NUM_COMPONENTS_TEXTURE + NUM_COMPONENTS_COLOR;
    private static final int MAX_TRIANGLES = 200;
    private static final int MAX_NUM_VERTICES = MAX_TRIANGLES * 3;

    private float[] vertices;
    private int verticesIndex;
    private Mesh mesh;
    private ShaderProgram shader;


    public Array<Segment2D> segments;
    GameScreen screen;
    public Rectangle buildArea;
    float margin = 10f;
    float accum;

    public Boundary(GameScreen screen){
        this.screen = screen;
        segments = new Array<>();
        segments.add(new Segment2D(screen.worldCamera.viewportWidth/2f - 50, 40, margin, 110));
        segments.add(new Segment2D(margin, 110, margin, screen.worldCamera.viewportHeight - margin));
        segments.add(new Segment2D(margin, screen.worldCamera.viewportHeight - margin, screen.worldCamera.viewportWidth-margin, screen.worldCamera.viewportHeight - margin));
        segments.add(new Segment2D(screen.worldCamera.viewportWidth - margin, screen.worldCamera.viewportHeight- margin, screen.worldCamera.viewportWidth - margin, 110));
        segments.add(new Segment2D(screen.worldCamera.viewportWidth- margin, 110, screen.worldCamera.viewportWidth/2f + 50, 40));
        buildArea = new Rectangle(90, 200, (int)screen.worldCamera.viewportWidth-180, (int)screen.worldCamera.viewportHeight-290);

        this.shader = screen.assets.borderShader;
        this.mesh = new Mesh(false, MAX_NUM_VERTICES, 0,
                new VertexAttribute(VertexAttributes.Usage.Position,           NUM_COMPONENTS_POSITION, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, NUM_COMPONENTS_TEXTURE,  "a_texCoord0")
        );

        this.verticesIndex = 0;
        this.vertices = new float[MAX_NUM_VERTICES * NUM_COMPONENTS_PER_VERTEX];

        buildVertexArray();
    }

    Vector2 tempVec = new Vector2();
    private void buildVertexArray(){

        verticesIndex = 0;
        int vertsInCircle = 20;
        float lineWidth = 10;

        for (int i = vertsInCircle-1; i >= 0; i--){
            float angle = 90f/vertsInCircle;
            Vector2 center = segments.get(0).start;
            Vector2 normal = segments.get(0).normal;
            tempVec.set(normal).scl(lineWidth).rotate(angle*i);
            tempVec.add(center);
            float x1 = tempVec.x;
            float y1 = tempVec.y;

            tempVec.set(normal).scl(-lineWidth).rotate(angle*-i);
            tempVec.add(center);
            float x2 = tempVec.x;
            float y2 = tempVec.y;
            float u = lineWidth / (segments.get(0).start.dst(segments.get(0).end)+lineWidth*2f) * ((float)i/vertsInCircle);

            vertices[verticesIndex++] = x1;
            vertices[verticesIndex++] = y1;
            vertices[verticesIndex++] = u;
            vertices[verticesIndex++] = 0;

            vertices[verticesIndex++] = x2;
            vertices[verticesIndex++] = y2;
            vertices[verticesIndex++] = u;
            vertices[verticesIndex++] = 1;
        }

        for (int i = 0; i < vertsInCircle/2; i++){
            float angle = segments.get(1).normal.angle(segments.get(0).normal)/ vertsInCircle;
            Vector2 center = segments.get(0).end;
            Vector2 normal = segments.get(0).normal;
            tempVec.set(normal).scl(lineWidth).rotate(-angle*i);
            tempVec.add(center);
            float x1 = tempVec.x;
            float y1 = tempVec.y;

            tempVec.set(normal).scl(-lineWidth).rotate(-angle*vertsInCircle/2);
            tempVec.add(center);
            float x2 = tempVec.x;
            float y2 = tempVec.y;
            float u = 1f - (lineWidth / (segments.get(0).start.dst(segments.get(0).end)+lineWidth*2f) * ((float)i/vertsInCircle));

            vertices[verticesIndex++] = x1;
            vertices[verticesIndex++] = y1;
            vertices[verticesIndex++] = u;
            vertices[verticesIndex++] = 0;

            vertices[verticesIndex++] = x2;
            vertices[verticesIndex++] = y2;
            vertices[verticesIndex++] = u;
            vertices[verticesIndex++] = 1;
        }

        for (int segment = 1; segment < segments.size -1; segment++){
            for (int i = vertsInCircle/2 -1; i>=0; i--){
                float angle = segments.get(segment).normal.angle(segments.get(segment-1).normal)/ -vertsInCircle;
                Vector2 center = segments.get(segment).start;
                Vector2 normal = segments.get(segment).normal;
                tempVec.set(normal).scl(lineWidth).rotate(-angle*i);
                tempVec.add(center);
                float x1 = tempVec.x;
                float y1 = tempVec.y;

                tempVec.set(normal).scl(-lineWidth).rotate(angle*-vertsInCircle/2);
                tempVec.add(center);
                float x2 = tempVec.x;
                float y2 = tempVec.y;
                float u = lineWidth / (segments.get(0).start.dst(segments.get(0).end)+lineWidth*2f) * ((float)i/vertsInCircle);

                vertices[verticesIndex++] = x1;
                vertices[verticesIndex++] = y1;
                vertices[verticesIndex++] = u;
                vertices[verticesIndex++] = 0;

                vertices[verticesIndex++] = x2;
                vertices[verticesIndex++] = y2;
                vertices[verticesIndex++] = u;
                vertices[verticesIndex++] = 1;
            }

            for (int i = 0; i < vertsInCircle/2; i++){
                float angle = segments.get(segment+1).normal.angle(segments.get(segment).normal)/ vertsInCircle;
                Vector2 center = segments.get(segment).end;
                Vector2 normal = segments.get(segment).normal;
                tempVec.set(normal).scl(lineWidth).rotate(-angle*i);
                tempVec.add(center);
                float x1 = tempVec.x;
                float y1 = tempVec.y;

                tempVec.set(normal).scl(-lineWidth).rotate(angle*-vertsInCircle/2);
                tempVec.add(center);
                float x2 = tempVec.x;
                float y2 = tempVec.y;
                float u = 1f - (lineWidth / (segments.get(segment).start.dst(segments.get(segment).end)+lineWidth*2f) * ((float)i/vertsInCircle));

                vertices[verticesIndex++] = x1;
                vertices[verticesIndex++] = y1;
                vertices[verticesIndex++] = u;
                vertices[verticesIndex++] = 0;

                vertices[verticesIndex++] = x2;
                vertices[verticesIndex++] = y2;
                vertices[verticesIndex++] = u;
                vertices[verticesIndex++] = 1;
            }
        }

        for (int i = vertsInCircle/2 -1; i>=0; i--){
            float angle = segments.get(segments.size-1).normal.angle(segments.get(segments.size-2).normal)/ -vertsInCircle;
            Vector2 center = segments.get(segments.size-1).start;
            Vector2 normal = segments.get(segments.size -1).normal;
            tempVec.set(normal).scl(lineWidth).rotate(-angle*i);
            tempVec.add(center);
            float x1 = tempVec.x;
            float y1 = tempVec.y;

            tempVec.set(normal).scl(-lineWidth).rotate(angle*-vertsInCircle/2);
            tempVec.add(center);
            float x2 = tempVec.x;
            float y2 = tempVec.y;
            float u = lineWidth / (segments.get(0).start.dst(segments.get(0).end)+lineWidth*2f) * ((float)i/vertsInCircle);

            vertices[verticesIndex++] = x1;
            vertices[verticesIndex++] = y1;
            vertices[verticesIndex++] = u;
            vertices[verticesIndex++] = 0;

            vertices[verticesIndex++] = x2;
            vertices[verticesIndex++] = y2;
            vertices[verticesIndex++] = u;
            vertices[verticesIndex++] = 1;
        }

        for (int i = 0; i < vertsInCircle; i++){
            float angle = -90f/vertsInCircle;
            Vector2 center = segments.get(segments.size-1).end;
            Vector2 normal = segments.get(segments.size-1).normal;
            tempVec.set(normal).scl(lineWidth).rotate(angle*i);
            tempVec.add(center);
            float x1 = tempVec.x;
            float y1 = tempVec.y;

            tempVec.set(normal).scl(-lineWidth).rotate(angle*-i);
            tempVec.add(center);
            float x2 = tempVec.x;
            float y2 = tempVec.y;
            float u = lineWidth / (segments.get(0).start.dst(segments.get(0).end)+lineWidth*2f) * ((float)i/vertsInCircle);

            vertices[verticesIndex++] = x1;
            vertices[verticesIndex++] = y1;
            vertices[verticesIndex++] = u;
            vertices[verticesIndex++] = 0;

            vertices[verticesIndex++] = x2;
            vertices[verticesIndex++] = y2;
            vertices[verticesIndex++] = u;
            vertices[verticesIndex++] = 1;
        }
    }

    public void update(float dt){
        accum += dt;
    }

    public void renderMeshOnly() {
//        buildVertexArray();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (verticesIndex == 0) return;

        shader.begin();

        switch (screen.game.player.artPack){
            case a:
                screen.assets.lineAAnimation.getKeyFrame(accum).bind(0);
                break;
            case b:
                screen.assets.lineB.bind(0);
                break;
            case c:
                screen.assets.lineC.bind(0);
                break;
            case d:
                screen.assets.lineD.bind(0);
                break;
        }
        shader.setUniformMatrix("u_projTrans", screen.shaker.getCombinedMatrix());
        shader.setUniformi("u_texture", 0);
        shader.setUniformf("u_time", accum);

        int vertexCount = verticesIndex / NUM_COMPONENTS_PER_VERTEX;
        mesh.setVertices(vertices);
        mesh.render(shader, GL20.GL_TRIANGLE_STRIP, 0, vertexCount);

//        verticesIndex = 0;
        shader.end();
    }

    public void render(SpriteBatch batch){
        float width = 20;
        for (Segment2D segment : segments){
            batch.draw(screen.assets.assetMap.get(screen.game.player.artPack).get(AssetType.boundary_line).getKeyFrame(accum), segment.start.x, segment.start.y - width/2f, 0, width/2f, segment.delta.len(), width, 1, 1, segment.getRotation());
        }
    }

    public void renderEditMode(SpriteBatch batch){
        batch.setColor(1f, 1f, 1f, .8f);
        screen.assets.buildArea.draw(batch, buildArea.x, buildArea.y, buildArea.width, buildArea.height);
        batch.setColor(Color.WHITE);
    }
}
