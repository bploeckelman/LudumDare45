package lando.systems.ld45.backgrounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.screens.GameScreen;

public class FlatBackground extends Background {
    public FlatBackground(GameScreen screen) {
        super(screen);
    }


    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(.2f, .2f, .2f, 1f);
        batch.draw(gameScreen.assets.pixel, 0, 0, gameScreen.worldCamera.viewportWidth, gameScreen.worldCamera.viewportHeight);
        batch.setColor(Color.WHITE);
    }
}
