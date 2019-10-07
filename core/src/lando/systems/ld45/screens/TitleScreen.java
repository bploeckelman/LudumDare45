package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.ui.typinglabel.TypingLabel;

public class TitleScreen extends BaseScreen {

    public TypingLabel label;

    public TitleScreen(Game game) {
        super(game);

        this.label = new TypingLabel(Game.getCurrentFont(),
                "{JUMP=.2}{WAVE=0.9;1.2;1.75}{GRADIENT=red;blue}{RAINBOW}CLICK TO START{ENDRAINBOW}{ENDGRADIENT}{ENDWAVE}{ENDJUMP}",
                0f, 0f);
        this.label.setWidth(Config.gameWidth);
        this.label.setFontScale(2f);
        this.label.setY(Config.gameHeight / 2f + 100f);
    }

    @Override
    public void update(float dt) {
        label.update(dt);

        if (Gdx.input.justTouched()) {
            game.setScreen(new DemoScreen(game), assets.doorwayShader, 1f);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);
        batch.setColor(Color.WHITE);
        batch.draw(assets.title, 0,0, worldCamera.viewportWidth, worldCamera.viewportHeight);
        batch.setColor(Color.WHITE);

        label.render(batch);

        batch.end();
    }
}
