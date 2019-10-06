package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.screens.GameScreen;

public class Bumper extends GameObject {

    public static final long SCORE_VALUE = 2;

    private float bumperSize;

    private float currentBumperSize = 0;

    public static Bumper getBumper(GameScreen screen, int gfxPack) {
        return new Bumper(screen, gfxPack, 32);
    }

    public Bumper(GameScreen screen, int gfxPack, float size) {
        super(screen, screen.assets.bumpers[gfxPack], size, size);

        this.bumperSize = this.currentBumperSize = size;

        setPosition(MathUtils.random(10, Config.gameWidth - 20), MathUtils.random(10, Config.gameHeight - 200));

        setCircleBounds(pos.x, pos.y, bumperSize/2f);
    }

    @Override
    public void update(float dt, Vector2 mousePosition) {
        super.update(dt, mousePosition);

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
        setCircleBounds(pos.x, pos.y, bumperSize/2f);

    }

    @Override
    public void render(SpriteBatch batch) {
        float half = currentBumperSize / 2;
        batch.draw(image, pos.x - half, pos.y - half, currentBumperSize, currentBumperSize);
    }
}
