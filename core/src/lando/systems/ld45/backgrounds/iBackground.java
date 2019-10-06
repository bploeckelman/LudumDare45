package lando.systems.ld45.backgrounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface iBackground {

    void addCollision(float x, float y, float size, float ttl, Color color);
    void update(float dt);
    void render(SpriteBatch batch);
}
