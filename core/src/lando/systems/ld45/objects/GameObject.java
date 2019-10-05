package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Game;
import lando.systems.ld45.screens.GameScreen;

public abstract class GameObject {

    public Vector2 pos = new Vector2();

    protected GameScreen screen;
    private Vector2 size;
    private TextureRegion image;

    public GameObject(GameScreen screen, TextureRegion image) {
        this.screen = screen;

        this.image = image;
        size = new Vector2(image.getRegionWidth(), image.getRegionHeight());
    }

    public void update(float dt) {
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, pos.x, pos.y, size.x, size.y);
    }
}
