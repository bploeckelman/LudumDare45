package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.ui.typinglabel.TypingLabel;

public class TitleScreen extends BaseScreen {

    public TypingLabel label;

    public TitleScreen(Game game) {
        super(game);

        this.label = new TypingLabel(assets.font,
//                                     "{JUMP=.2}{RAINBOW} SCORE GOALS {ENDRAINBOW}{ENDJUMP}\n"
//                                               + "{WIND=;;;0.7;}{GRADIENT=red;blue} TO GET LETTERS {ENDGRADIENT}{ENDWIND}\n\n"
//                                               + "{JUMP=.2}{RAINBOW} SPELL BLAST! {ENDRAINBOW}{ENDJUMP}\n"
//                                               + "{WAVE=0.9;1.2;1.75}{GRADIENT=red;blue} TO {SHAKE}WIN {ENDGRADIENT}{ENDSHAKE}{ENDWAVE}",
                                        "{JUMP=.2}{WAVE=0.9;1.2;1.75}{GRADIENT=red;blue}{RAINBOW}CLICK TO START{ENDRAINBOW}{ENDGRADIENT}{ENDWAVE}{ENDJUMP}"
                                                + " \n\nB to add balls\n\nC to clear balls\n\nE edit mode",
                                     0f, 0f);
        this.label.setWidth(Config.gameWidth);
        this.label.setFontScale(2f);
        this.label.setY(Config.gameHeight / 2f + 100f);
    }

    @Override
    public void update(float dt) {
       label.update(dt);

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
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
