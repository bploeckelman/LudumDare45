package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld45.Config;
import lando.systems.ld45.screens.GameScreen;

public class Bumper extends GameObject {

    private float bumperSize;
    private float hitTime = 1;

    private float currentHitTime = 0;
    private float currentBumperSize = 0;
    private float rotation = 0;

    public Bumper(GameScreen screen) {
        this(screen, 10);
    }

    public Bumper(GameScreen screen, float size) {
        super(screen, screen.assets.bumper, size, size);

        this.bumperSize = this.currentBumperSize = size;

        pos.x = MathUtils.random(10, Config.gameWidth - 20);
        pos.y = MathUtils.random(10, Config.gameHeight - 20);
    }

    public void hit() {
        currentHitTime = hitTime;
    }

    @Override
    public void update(float dt) {
        // temp
        if (MathUtils.random(1000) < 3) {
            hit();
        }

        if (currentHitTime > 0) {
            currentHitTime -= dt;
            size.x = size. y = currentBumperSize = bumperSize * (1 + Interpolation.bounceIn.apply(currentHitTime / hitTime));
            rotation += 180 * dt;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        float half = currentBumperSize / 2;
        batch.draw(image, pos.x - half, pos.y - half, half, half,
                currentBumperSize, currentBumperSize, 1, 1, rotation);
    }
}
