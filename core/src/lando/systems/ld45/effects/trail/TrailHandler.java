package lando.systems.ld45.effects.trail;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld45.Assets;
import lando.systems.ld45.objects.Ball;

public class TrailHandler {

    public float minDistance = 0.2f;
    public float minVelocityThreshold = 200f;

    private Trail trail;
    private TrailResolver simplifier = new TrailResolver();
    private Pool<Vector2> vec2Pool = Pools.get(Vector2.class, 100);
    private Array<Vector2> simplified;
    private FixedList<Vector2> inputPoints;
    private Vector2 lastPointSampled = new Vector2();
    private Color color;
    private boolean wasFastEnoughLastTime = false;

    public TrailHandler(Color color, int maxInputPoints, Assets assets) {
        this.inputPoints = new FixedList<>(maxInputPoints, Vector2.class);
        this.simplified = new Array<>(true, maxInputPoints, Vector2.class);
        this.color = color;
        this.trail = new Trail(color, assets);
        resolve(); //copy initial empty list
    }

    public void clear() {
        inputPoints.forEach(vec2Pool::free);
        inputPoints.clear();
        resolve();
        trail.update(simplified);
    }

    public void update(Ball ball, float dt) {
        boolean fastEnoughThisTime = true;//ball.vel.len() >= minVelocityThreshold;
        boolean fastEnough = fastEnoughThisTime && wasFastEnoughLastTime;
        wasFastEnoughLastTime = fastEnoughThisTime;
        if (!fastEnough) {
            clear();
            return;
        }

        if (!inputPoints.isEmpty()) {
            float dist2 = ball.pos.dst2(lastPointSampled);
            if (dist2 < minDistance * minDistance) {
                return;
            }
        }

        inputPoints.insert(vec2Pool.obtain().set(ball.pos));
        lastPointSampled.set(ball.pos);

        resolve();

        trail.update(simplified);
    }

    public void render(OrthographicCamera camera) {
        trail.render(camera);
    }

    public void renderDebug(ShapeRenderer shapes) {
        Array<Vector2> input = inputPoints;

        // draw the raw input
        shapes.begin(ShapeRenderer.ShapeType.Line);
        {
            shapes.setColor(Color.YELLOW);
            for (int i = 0; i < input.size - 1; ++i) {
                Vector2 p0 = input.get(i);
                Vector2 p1 = input.get(i + 1);
                shapes.line(p0.x, p0.y, p1.x, p1.y);
            }
        }
        shapes.end();

        // render the smoothed and simplified path
        shapes.begin(ShapeRenderer.ShapeType.Line);
        {
            shapes.setColor(Color.RED);
            Array<Vector2> out = simplified;
            for (int i = 0; i < out.size - 1; ++i) {
                Vector2 p0 = out.get(i);
                Vector2 p1 = out.get(i + 1);
                shapes.line(p0.x, p0.y, p1.x, p1.y);
            }
        }
        shapes.end();

        //render perpendiculars segments
        Vector2 perp = new Vector2();
        shapes.begin(ShapeRenderer.ShapeType.Line);
        {
            for (int i = 1; i < input.size - 1; i++) {
                Vector2 p0 = input.get(i);
                Vector2 p1 = input.get(i + 1);

                perp.set(p0).sub(p1).nor();
                perp.set(perp.y, -perp.x);

                shapes.setColor(Color.CYAN);
                perp.scl(10f);
                shapes.line(p0.x, p0.y, p0.x + perp.x, p0.y + perp.y);

                shapes.setColor(Color.BLUE);
                perp.scl(-1f);
                shapes.line(p0.x, p0.y, p0.x + perp.x, p0.y + perp.y);
            }
        }
        shapes.end();
    }

    private void resolve() {
        simplifier.resolve(inputPoints, simplified);
    }

}
