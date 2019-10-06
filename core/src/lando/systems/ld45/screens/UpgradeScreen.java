package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.Game;

public class UpgradeScreen extends  BaseScreen {

    public UpgradeScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);

        renderUIElements(batch);

        batch.end();
    }
}
