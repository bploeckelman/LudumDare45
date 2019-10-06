package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.ui.typinglabel.TypingLabel;

public class WinnerScreen extends BaseScreen {

    private TypingLabel label;

    public WinnerScreen(Game game) {
        super(game);

        label = new TypingLabel(assets.font,
                "{JUMP=.2}{WAVE=0.9;1.2;1.75}FUCK YOU, WON{ENDWAVE}{ENDJUMP}",
                0f, 0f);
        label.setWidth(Config.gameWidth);
        label.setFontScale(2f);
        label.setY(Config.gameHeight / 2f + 100f);
    }

    @Override
    public void update(float dt) {
        label.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.setScreen(new TitleScreen(game));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);

        label.render(batch);

        batch.end();
    }
}
