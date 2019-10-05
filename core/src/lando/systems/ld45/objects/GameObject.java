package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.screens.GameScreen;

public abstract class GameObject {

    public Vector2 pos = new Vector2();

    protected Vector2 size;
    protected TextureRegion image;

    protected float hitTime = 1;
    protected float currentHitTime = 0;

    protected GameScreen screen;

    public GameObject(GameScreen screen, TextureRegion image) {
        this(screen, image, image.getRegionWidth(), image.getRegionHeight());
    }

    public GameObject(GameScreen screen, TextureRegion image, float width, float height) {
        this.screen = screen;

        this.image = image;
        size = new Vector2(width, height);
    }

    public void update(float dt) {
        // temp
        if (MathUtils.random(100) < 2) {
            hit();
        }

        if (currentHitTime > 0) {
            currentHitTime -= dt;
        } else {
            currentHitTime = 0;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
    }

    public void hit() {
        currentHitTime = hitTime;
    }
}
