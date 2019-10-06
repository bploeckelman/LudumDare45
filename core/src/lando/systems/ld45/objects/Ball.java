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

    public static float MAXSPEED = 400f;

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

        this.screen = screen;
        this.bounds.set(Config.gameWidth / 2f + MathUtils.random(-200f, 200f), Config.gameHeight - 50 - MathUtils.random(50f), radius);
        this.pos.set(bounds.x, bounds.y);
        this.color = color;
        this.vel.set(MathUtils.random(-200f, 200f),
                     MathUtils.random(-200f, 200f));
        this.keyframe = screen.assets.whiteCircle;
        this.dtLeft = 0;

        this.path = new BallPath(screen.game, color);
    }

    public void update(float dt) {
        vel.y -= Config.gravity * dt;
        vel.scl(.999f);

        if (vel.len() > MAXSPEED){
            vel.nor().scl(MAXSPEED);
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
