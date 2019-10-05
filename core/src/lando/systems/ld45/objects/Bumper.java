package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld45.Config;
import lando.systems.ld45.screens.GameScreen;

public class Bumper extends GameObject {

    private float bumperSize;

    private float currentBumperSize = 0;

    public static Bumper getBumper(GameScreen screen, int gfxPack) {
        return new Bumper(screen, gfxPack, 32);
    }

    public Bumper(GameScreen screen, int gfxPack, float size) {
        super(screen, screen.assets.bumpers[gfxPack], size, size);

        this.bumperSize = this.currentBumperSize = size;

        pos.x = MathUtils.random(10, Config.gameWidth - 20);
        pos.y = MathUtils.random(10, Config.gameHeight - 20);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (currentHitTime > 0) {
            if (currentBumperSize > (hitTime / 2)) {
                currentBumperSize = bumperSize + (bumperSize * Interpolation.bounceIn.apply(currentHitTime / hitTime / 2));
            } else {
                currentBumperSize = bumperSize * (1 + Interpolation.bounceOut.apply(currentHitTime / hitTime));
            }
        } else {
            currentBumperSize = bumperSize;
        }
        size.x = size. y = currentBumperSize;
    }

    @Override
    public void render(SpriteBatch batch) {
        float half = currentBumperSize / 2;
        batch.draw(image, pos.x - half, pos.y - half, currentBumperSize, currentBumperSize);
    }
}
