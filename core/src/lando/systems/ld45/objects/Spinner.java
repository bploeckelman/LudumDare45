package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.screens.GameScreen;

public class Spinner extends GameObject {

    private float rotation = 0;

    public boolean left = true;

    public static Spinner getSpinner(GameScreen screen, int level, int gfxPack) {
        return new Spinner(screen, level, gfxPack, 32);
    }

    public Spinner(GameScreen screen, int level, int gfxPack, float size) {
        super(screen, screen.assets.spinners[gfxPack][level], size, size);
    }

    @Override
    public void update(float dt, Vector2 mousePosition) {
        super.update(dt, mousePosition);

        if (currentHitTime > 0) {
            float dr = 720*dt;
            if (currentHitTime < (hitTime/2)) {
                dr = Interpolation.pow2Out.apply(1 - (currentHitTime / hitTime / 2));
            }
            rotation += (left) ? dr : -dr;
        }
    }

    int hitCount = 0;
    @Override
    public void hit() {
        if (hitCount % 5 == 0) {
            super.hit();
        }
        hitCount++;
    }

    @Override
    public void render(SpriteBatch batch) {
        float half = size.x / 2;
        batch.draw(image, pos.x - half, pos.y - half, half, half,
                size.x, size.y, (left) ? 1 : -1, 1, rotation);
    }
}
