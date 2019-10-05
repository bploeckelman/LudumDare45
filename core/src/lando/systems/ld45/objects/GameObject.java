package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.screens.GameScreen;

public abstract class GameObject {

    public Vector2 pos = new Vector2();

    protected GameScreen screen;
    protected Vector2 size;
    protected TextureRegion image;

    public GameObject(GameScreen screen, TextureRegion image) {
        this(screen, image, image.getRegionWidth(), image.getRegionHeight());
    }

    public GameObject(GameScreen screen, TextureRegion image, float width, float height) {
        this.screen = screen;

        this.image = image;
        size = new Vector2(width, height);
    }

    public void update(float dt) {
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, pos.x, pos.y, size.x, size.y);
    }
}
