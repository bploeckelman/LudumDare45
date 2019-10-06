package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.effects.BallPath;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.Utils;

public class Ball {

    public static float MAXSPEED = 500f;

    public Circle bounds = new Circle();
    public Vector2 vel = new Vector2();
    public Vector2 pos = new Vector2();
    public Color color;
    public TextureRegion keyframe;
    public GameScreen screen;
    public float dtLeft;

    private BallPath path;
    private float accum;

    public Ball(GameScreen screen, float radius) {
        this(screen, radius, Utils.getRandomColor());
    }

    public Ball(GameScreen screen, float radius, Color color) {
        // TODO: tweak Utils.getRandomColor() to exclude dumb colors
        if (color == Color.BLACK || color == Color.CLEAR) {
            color = Color.SALMON;
        }
        this.color = color;
        this.bounds.set(0, 0, radius);
        this.screen = screen;

        this.keyframe = screen.assets.whiteCircle;
        this.dtLeft = 0;

        this.path = new BallPath(screen.game, color);
    }

    public void initialize(Vector2 position, Vector2 velocity) {
        bounds.set(position.x, position.y, bounds.radius);
        pos.set(position.x,position.y);
        vel.set(velocity);
    }

    public void update(float dt) {
        vel.y -= Config.gravity * dt;
        vel.scl(.999f);

        if (vel.len() > MAXSPEED){
            vel.nor().scl(MAXSPEED);
        }
        if (vel.len() < 10){
            if (vel.epsilonEquals(0,0)) vel.set(0, 1);
            vel.nor().scl(10f);

        }

        pos.set(bounds.x, bounds.y);

        accum += dt;
        path.update(this, dt);
    }

    public void renderTrailMesh() {
        path.renderMeshOnly();
    }

    public void render(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(keyframe, bounds.x - bounds.radius, bounds.y - bounds.radius, 2f * bounds.radius, 2f * bounds.radius);
        batch.setColor(Color.WHITE);
    }

}
