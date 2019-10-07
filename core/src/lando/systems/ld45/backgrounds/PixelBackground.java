package lando.systems.ld45.backgrounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.screens.GameScreen;

public class PixelBackground extends Background {

    Color color1 = new Color(.3f, .1f, 0, 1f);
    Color color2 = new Color(0f, .1f, .3f, 1f);

    public PixelBackground(GameScreen screen) {
        super(screen);
    }

    @Override
    public void addCollision(float x, float y, float size, float ttl, Color color) {
        if (collisions.size < explosionSize) {
            collisions.add(new Collision(x, y, size, ttl, color));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (int x = 0; x <= gameScreen.worldCamera.viewportWidth; x+= 50){
            for (int y = 0; y <= gameScreen.worldCamera.viewportHeight; y+= 50){
                batch.setColor(((x+y)/50)%2 == 0 ? color1 : color2);
                for(Collision collision : collisions){
                    if (collision.pos.x >= x && collision.pos.x < x + 50 &&
                        collision.pos.y >= y && collision.pos.y < y + 50){
                        batch.setColor(.3f, .3f, 0, 1f);
                    }
                }
                batch.draw(gameScreen.assets.whitePixel, x, y, 50, 50);
            }
        }
        batch.setColor(Color.WHITE);
    }
}
