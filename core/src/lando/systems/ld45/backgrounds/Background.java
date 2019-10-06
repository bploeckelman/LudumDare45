package lando.systems.ld45.backgrounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.screens.GameScreen;

public class Background {

    GameScreen gameScreen;
    Array<Collision> collisions;

    public Background(GameScreen screen){
        this.gameScreen = screen;
        this.collisions = new Array<>();

    }

    public void addCollision(float x, float y, float size, float ttl, Color color) {
        if (collisions.size < 40) {
            collisions.add(new Collision(x, y, size, ttl, color));
        }
    }

    public void update(float dt) {
        for (int i = collisions.size-1; i >= 0; i--){
            Collision collision = collisions.get(i);
            collision.update(dt);
            if (collision.finished){
                collisions.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {

    }
}
