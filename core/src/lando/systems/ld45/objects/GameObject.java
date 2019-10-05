package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

    public Vector2 pos = new Vector2();

    private Vector2 _size;
    private TextureRegion _image;

    public GameObject(TextureRegion image) {
        _image = image;
        _size = new Vector2(image.getRegionWidth(), image.getRegionHeight());
    }

    public void update(float dt) {
    }

    public void render(SpriteBatch batch) {
        batch.draw(_image, pos.x, pos.y, _size.x, _size.y);
    }
}
