package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.effects.trail.TrailHandler;
import lando.systems.ld45.screens.GameScreen;

public class Ball {

    public Circle bounds = new Circle();
    public Vector2 vel = new Vector2();
    public Vector2 pos = new Vector2();
    public TrailHandler trail;
    public TextureRegion keyframe;
    public GameScreen screen;
    public float dtLeft;

    public Ball(GameScreen screen, float radius) {
        this.screen = screen;
        this.bounds.set(Config.gameWidth / 2f + MathUtils.random(-50f, 50f), Config.gameHeight / 2f + MathUtils.random(-50f, 50f), radius);
        this.pos.set(bounds.x, bounds.y);
        this.trail = new TrailHandler(Color.WHITE, 30, screen.assets);
        this.vel.set(MathUtils.random(-200f, 200f),
                     MathUtils.random(-200f, 200f));
        this.keyframe = screen.assets.whiteCircle;
        this.dtLeft = 0;
    }

    public void update(float dt) {
        vel.y -= Config.gravity*dt;
        vel.scl(.999f);
//        bounds.x += vel.x * dt;
//        bounds.y += vel.y * dt;

        pos.set(bounds.x, bounds.y);

        trail.update(this, dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x - bounds.radius, bounds.y - bounds.radius, 2f * bounds.radius, 2f * bounds.radius);
    }

}
