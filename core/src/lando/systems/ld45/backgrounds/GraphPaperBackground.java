package lando.systems.ld45.backgrounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.screens.GameScreen;

public class GraphPaperBackground extends Background {


    public GraphPaperBackground(GameScreen screen){
        super(screen);

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(gameScreen.assets.gridPaper, 0, 0, gameScreen.worldCamera.viewportWidth, gameScreen.worldCamera.viewportHeight, 0, 0, gameScreen.worldCamera.viewportWidth/200, gameScreen.worldCamera.viewportHeight/200);
    }
}
