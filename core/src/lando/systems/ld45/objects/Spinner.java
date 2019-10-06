package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
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
        circleBounds = new Circle(-10, -10, size/2f);
    }

    @Override
    public void update(float dt, Vector2 mousePosition) {
        super.update(dt, mousePosition);
        float dr = 30 * dt;
        if (currentHitTime > 0) {
            dr = 360*dt;
            if (currentHitTime < (hitTime/2)) {
                dr = Interpolation.pow2Out.apply(1 - (currentHitTime / hitTime / 2));
            }
        }
        rotation += (left) ? dr : -dr;
        circleBounds.set(pos.x, pos.y, size.x/2f);

    }

    int hitCount = 0;
    @Override
    public void hit() {
        super.hit();
    }

    @Override
    public void render(SpriteBatch batch) {
        float half = size.x / 2;
        batch.draw(image, pos.x - half, pos.y - half, half, half,
                size.x, size.y, (left) ? 1 : -1, 1, rotation);
    }
}
