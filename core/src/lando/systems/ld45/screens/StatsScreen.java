package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.state.PlayerState;
import lando.systems.ld45.ui.typinglabel.TypingLabel;
import lando.systems.ld45.utils.ArtPack;

public class StatsScreen extends BaseScreen {

    private TypingLabel titleLabel;
    private TypingLabel leftCreditLabel;

    static PlayerState state = Game.game.player;

    private String title;
    private String stats;
    Color textColor = new Color(Color.WHITE);
    Color textBorderColor = new Color(Color.GRAY);

    public StatsScreen(Game game) {
        super(game);

        title = "STATISTICS";
        stats = "Balls dropped: " + state.ballsDropped + " \nTotal time: " + toTimeString(state.totalTime) + " \nTotal Score: " + state.getTotalScore();

        titleLabel = new TypingLabel(assets.fontMap.get(ArtPack.a), title, 0f, 0f);
        titleLabel.setWidth(Config.gameWidth);
        titleLabel.setFontScale(2f);
        titleLabel.setY(Config.gameHeight / 2f + 280f);

        leftCreditLabel = new TypingLabel(assets.fontMap.get(ArtPack.c), stats, 75f, Config.gameHeight / 2f + 130f);
        leftCreditLabel.setWidth(Config.gameWidth / 2 - 150f);
        leftCreditLabel.setLineAlign(Align.left);
        leftCreditLabel.setFontScale(1f);
    }

    @Override
    public void update(float dt) {
        titleLabel.update(dt);
        leftCreditLabel.update(dt);

//        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
//            game.setScreen(new TitleScreen(game));
//        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);
        batch.draw(assets.asuka, 25f, 110f, Config.gameWidth / 2 - 50f, 350f);
        batch.draw(assets.romeo, Config.gameWidth / 2 + 25f, 110f, Config.gameWidth / 2 - 50f, 350f);
        batch.setColor(0f, 0f, 0f, 0.6f);
        batch.draw(assets.whitePixel, 25f, 110f, Config.gameWidth / 2 - 50f, 350f);
        batch.draw(assets.whitePixel, Config.gameWidth / 2 + 25f, 110f, Config.gameWidth / 2 - 50f, 350f);
        batch.setColor(Color.WHITE);
        titleLabel.render(batch);
        leftCreditLabel.render(batch);
        batch.end();
    }

    private StringBuilder sb = new StringBuilder(20);
    private String toTimeString(double time) {
        sb.setLength(0);
        int minutes = (int)time / 60;
        int seconds = (int)time % 60;

        sb.append(minutes);
        sb.append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        return sb.toString();
    }
}
