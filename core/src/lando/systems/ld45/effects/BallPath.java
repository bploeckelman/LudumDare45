package lando.systems.ld45.effects;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Game;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.FixedList;
import lando.systems.ld45.objects.Ball;

public class BallPath {

    private static final float MIN_DISTANCE_BETWEEN_SAMPLES = 5.0f;
    private static final float MIN_VELOCITY_THRESHOLD_TO_DRAW_PATH = 0f; // TODO: if this is set, it just nukes the trail rather than fade it if the ball gets slow enough
    private static final int DEFAULT_MAX_PATH_LENGTH = 20;
    private static final int NUM_COMPONENTS_POSITION = 2;
    private static final int NUM_COMPONENTS_TEXTURE = 2;
    private static final int NUM_COMPONENTS_COLOR = 4;
    private static final int NUM_COMPONENTS_PER_VERTEX = NUM_COMPONENTS_POSITION + NUM_COMPONENTS_TEXTURE + NUM_COMPONENTS_COLOR;
    private static final int MAX_TRIANGLES = 1000;
    private static final int MAX_NUM_VERTICES = MAX_TRIANGLES * 3;

    private Game game;
    private Color color;

    private FixedList<Vector2> ballPosSamples;
    private Array<Vector2> ballPosSamplesSimplified;
    private Vector2 lastBallPosSampled;
    private Vector2 perp;

    private Mesh mesh;
    private ShaderProgram shader;

    private float[] vertices;
    private int verticesIndex;

    private boolean wasMovingFastEnoughLastTime;

    public BallPath(Game game, Color color) {
        this(game, color, DEFAULT_MAX_PATH_LENGTH);
    }

    public BallPath(Game game, Color color, int maxPathLength) {
        this.game = game;
        this.color = new Color(color);

        this.ballPosSamples = new FixedList<>(maxPathLength, Vector2.class);
        this.ballPosSamplesSimplified = new Array<>(true, maxPathLength, Vector2.class);
        this.lastBallPosSampled = new Vector2();
        this.perp = new Vector2();

        this.shader = game.assets.ballTrailShader;
        this.mesh = new Mesh(false, MAX_NUM_VERTICES, 0,
                new VertexAttribute(VertexAttributes.Usage.Position,           NUM_COMPONENTS_POSITION, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, NUM_COMPONENTS_TEXTURE,  "a_texCoord0"),
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked,      NUM_COMPONENTS_COLOR,    "a_color")
        );

        this.verticesIndex = 0;
        this.vertices = new float[MAX_NUM_VERTICES * NUM_COMPONENTS_PER_VERTEX];

        this.wasMovingFastEnoughLastTime = false;

        // copy initial empty list
        simplifyAndSmoothPath();
    }

    public void update(Ball ball, float dt) {
        addBallPositionSamples(ball);
        simplifyAndSmoothPath();
        populateVertexArrayFromSamples(ball.bounds.radius);
    }

    public void setColor(Color color){
        this.color.set(color);
    }

    public void renderIsolated(float time) {
        if (verticesIndex == 0) return;

        shader.begin();
        {
            if (game.getScreen() instanceof GameScreen){
                if (game.artPack == ArtPack.a){
                    game.assets.crossHatchGradientTexture.bind(0);
                } else {
                    game.assets.ballTrailTexture.bind(0);
                }
            }

            shader.setUniformMatrix("u_projTrans", game.getScreen().worldCamera.combined);
            shader.setUniformi("u_texture", 0);
            shader.setUniformf("u_time", time);

            int vertexCount = verticesIndex / NUM_COMPONENTS_PER_VERTEX;
            mesh.setVertices(vertices);
            mesh.render(shader, GL20.GL_TRIANGLE_STRIP, 0, vertexCount);
        }
        shader.end();

        verticesIndex = 0;
    }

    public void renderMeshOnly() {
        if (verticesIndex == 0) return;

        int vertexCount = verticesIndex / NUM_COMPONENTS_PER_VERTEX;
        mesh.setVertices(vertices);
        mesh.render(shader, GL20.GL_TRIANGLE_STRIP, 0, vertexCount);

        verticesIndex = 0;
    }

    // ------------------------------------------------------------------------

    /**
     * Determine whether to sample the ball position to extend the path, and does so if appropriate
     *
     * @param ball
     */
    private void addBallPositionSamples(Ball ball) {
        // if the ball isn't moving fast enough, it has no trail
        boolean movingFastEnoughThisTime = ball.vel.len() >= MIN_VELOCITY_THRESHOLD_TO_DRAW_PATH;
        boolean movingFastEnough = movingFastEnoughThisTime && wasMovingFastEnoughLastTime;
        wasMovingFastEnoughLastTime = movingFastEnoughThisTime;
        if (!movingFastEnough) {
            clearSamples();
            return;
        }

        // if the ball hasn't moved far enough to sample another point, then don't sample another point
        if (!ballPosSamples.isEmpty()) {
            float dist2 = ball.pos.dst2(lastBallPosSampled);
            if (dist2 < MIN_DISTANCE_BETWEEN_SAMPLES * MIN_DISTANCE_BETWEEN_SAMPLES) {
                return;
            }
        }

        // ball state is good, store a new position sample for the path
        ballPosSamples.insert(game.vector2Pool.obtain().set(ball.pos));

        // save the newly sampled position for our next distance check
        lastBallPosSampled.set(ball.pos);
    }

    private void clearSamples() {
        // clear sampled ball positions
        ballPosSamples.forEach(game.vector2Pool::free);
        ballPosSamples.clear();

        // copy cleared array in order clear path
        simplifyAndSmoothPath();
    }

    private void populateVertexArrayFromSamples(float thickness) {
        verticesIndex = 0;
        int numVertices = ballPosSamplesSimplified.size;
        for (int i = 0; i < numVertices - 1; ++i) {
            int i2 = i + 1;

            float interp1 = (float) i  / (float) numVertices;
            float interp2 = (float) i2 / (float) numVertices;

            Vector2 p1 = ballPosSamplesSimplified.get(i);
            Vector2 p2 = ballPosSamplesSimplified.get(i2);

            // normalize direction
            perp.set(p1).sub(p2).nor();

            // perpendicularized
            perp.set(-perp.y, perp.x);

            // scale thickness to taper the tail of the path
            float thick = (2f * thickness) * (1f - (i / (float) numVertices));

            // scale alpha to fade out towards tail of path
            float alpha = Interpolation.exp5Out.apply(1f, 0f, interp1);

            // extrude by thickness
            perp.scl(thick / 2f);

            // populate vertex array with values to create triangle strip...

            // vertex 1
            vertices[verticesIndex++] = p1.x + perp.x;
            vertices[verticesIndex++] = p1.y + perp.y;
            vertices[verticesIndex++] = interp1;
            vertices[verticesIndex++] = 1f;
            vertices[verticesIndex++] = color.r;
            vertices[verticesIndex++] = color.g;
            vertices[verticesIndex++] = color.b;
            vertices[verticesIndex++] = alpha;

            // vertex 2
            vertices[verticesIndex++] = p1.x - perp.x;
            vertices[verticesIndex++] = p1.y - perp.y;
            vertices[verticesIndex++] = interp1;
            vertices[verticesIndex++] = 0f;
            vertices[verticesIndex++] = color.r;
            vertices[verticesIndex++] = color.g;
            vertices[verticesIndex++] = color.b;
            vertices[verticesIndex++] = alpha;

            // NOTE: these are duplicated (which was needed when it was a closed loop)

            // vertex 3
//            vertices[verticesIndex++] = p2.x + perp.x;
//            vertices[verticesIndex++] = p2.y + perp.y;
//            vertices[verticesIndex++] = interp2;
//            vertices[verticesIndex++] = 1f;
//            vertices[verticesIndex++] = color.r;
//            vertices[verticesIndex++] = color.g;
//            vertices[verticesIndex++] = color.b;
//            vertices[verticesIndex++] = alpha;
//
//            // vertex 4
//            vertices[verticesIndex++] = p2.x - perp.x;
//            vertices[verticesIndex++] = p2.y - perp.y;
//            vertices[verticesIndex++] = interp2;
//            vertices[verticesIndex++] = 0f;
//            vertices[verticesIndex++] = color.r;
//            vertices[verticesIndex++] = color.g;
//            vertices[verticesIndex++] = color.b;
//            vertices[verticesIndex++] = alpha;
        }
    }

    // ------------------------------------------------------------------------
    // Path position sample simplification
    // ------------------------------------------------------------------------

    private static final int SMOOTHING_ITERATIONS = 1;
    private static final float SIMPLIFICATION_TOLERANCE = 0f;

    private Array<Vector2> tmp = new Array<>();

    private void simplifyAndSmoothPath() {
        simplifyAndSmoothPath(ballPosSamples, ballPosSamplesSimplified);
    }

    private void simplifyAndSmoothPath(Array<Vector2> input, Array<Vector2> output) {
        output.clear();

        // if there's nothing to simplify or smooth, just copy
        if (input.size <= 2) {
            output.addAll(input);
            return;
        }

        // simplify the path
        if (SIMPLIFICATION_TOLERANCE > 0 && input.size > 3) {
            simplify(input, SIMPLIFICATION_TOLERANCE * SIMPLIFICATION_TOLERANCE, tmp);

            // set input to simplified working values for smoothing step
            input = tmp;
        }

        // smooth the path
        if (SMOOTHING_ITERATIONS <= 0) {
            // no smoothing, just copy input to output
            output.addAll(input);
        } else if (SMOOTHING_ITERATIONS == 1) {
            // 1 iteration, smooth input to output
            smooth(input, output);
        } else {
            // multiple iterations.. ping-pong between input and output arrays
            int iters = SMOOTHING_ITERATIONS;
            do {
                smooth(input, output);

                tmp.clear();
                tmp.addAll(output);

                Array<Vector2> old = output;
                input = tmp;
                output = old;
            } while (--iters > 0);
        }
    }

    /**
     * Simple distance-based simplification, adapted from simplify.js
     *
     * @param points
     * @param squaredDistanceTolerance
     * @param out
     */
    private void simplify(Array<Vector2> points, float squaredDistanceTolerance, Array<Vector2> out) {
        int len = points.size;

        Vector2 point = new Vector2();
        Vector2 prevPoint = points.get(0);

        out.clear();
        out.add(prevPoint);

        for (int i = 1; i < len; ++i) {
            point = points.get(i);
            if (point.dst2(prevPoint) > squaredDistanceTolerance) {
                out.add(point);
                prevPoint = point;
            }
        }

        if (!prevPoint.equals(point)) {
            out.add(point);
        }
    }

    /**
     * Path smoothing
     *
     * @param input the input sample points
     * @param output the smoothed output sample points
     */
    private void smooth(Array<Vector2> input, Array<Vector2> output) {
        //expected size
        output.clear();
        output.ensureCapacity(input.size*2);

        //first element
        output.add(input.get(0));

        //average elements
        for (int i=0; i<input.size-1; i++) {
            Vector2 p0 = input.get(i);
            Vector2 p1 = input.get(i+1);

            Vector2 Q = new Vector2(0.75f * p0.x + 0.25f * p1.x, 0.75f * p0.y + 0.25f * p1.y);
            Vector2 R = new Vector2(0.25f * p0.x + 0.75f * p1.x, 0.25f * p0.y + 0.75f * p1.y);
            output.add(Q);
            output.add(R);
        }

        //last element
        output.add(input.get(input.size-1));
    }

}
