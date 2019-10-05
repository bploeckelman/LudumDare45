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

    public Ball(GameScreen screen, float radius) {
        this.screen = screen;
        this.bounds.set(Config.gameWidth / 2f, Config.gameHeight / 2f, radius);
        this.pos.set(bounds.x, bounds.y);
        this.trail = new TrailHandler(Color.WHITE, 300, screen.assets);
        this.vel.set(MathUtils.random(-200f, 200f),
                     MathUtils.random(-200f, 200f));
        this.keyframe = screen.assets.whiteCircle;
    }

    public void update(float dt) {
        bounds.x += vel.x * dt;
        bounds.y += vel.y * dt;

        if (bounds.x < bounds.radius || bounds.x > screen.worldCamera.viewportWidth  - bounds.radius) {
            screen.particle.addCloudParticles(bounds.x, bounds.y, 1f);
            vel.x *= -1;
        }
        if (bounds.y < bounds.radius || bounds.y > screen.worldCamera.viewportHeight - bounds.radius) {
            screen.particle.addCloudParticles(bounds.x, bounds.y, 1f);
            vel.y *= -1;
        }

        pos.set(bounds.x, bounds.y);

        trail.update(this, dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x - bounds.radius, bounds.y - bounds.radius, 2f * bounds.radius, 2f * bounds.radius);
    }

}
