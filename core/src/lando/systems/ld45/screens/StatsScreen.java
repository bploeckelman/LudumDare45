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
    private TypingLabel themeLabel;
    private TypingLabel leftCreditLabel;
    private TypingLabel thanksLabel;

    static PlayerState state = Game.game.player;

    static String title = "{SICK}STATS{ENDSICK}";
    static String theme = "Total Score: " + state.getTotalScore();
    static String thanks = "Balls dropped: " + state.ballsDropped;
    static String developers = "Total time: " + state.totalTime;
    Color textColor = new Color(Color.WHITE);
    Color textBorderColor = new Color(Color.GRAY);

    public StatsScreen(Game game) {
        super(game);
        titleLabel = new TypingLabel(assets.fontMap.get(ArtPack.a), title, 0f, 0f);
        titleLabel.setWidth(Config.gameWidth);
        titleLabel.setFontScale(2f);
        titleLabel.setY(Config.gameHeight / 2f + 280f);

        themeLabel = new TypingLabel(assets.fontMap.get(ArtPack.b), theme, 25f, 0f);
        themeLabel.setWidth(Config.gameWidth / 2 - 50f);
        themeLabel.setFontScale(1.5f);
        themeLabel.setY(Config.gameHeight / 2f + 230f);

        leftCreditLabel = new TypingLabel(assets.fontMap.get(ArtPack.c), developers, 75f, Config.gameHeight / 2f + 130f);
        leftCreditLabel.setWidth(Config.gameWidth / 2 - 150f);
        leftCreditLabel.setLineAlign(Align.left);
        leftCreditLabel.setFontScale(1f);

        thanksLabel = new TypingLabel(assets.fontMap.get(ArtPack.d), thanks, 0f, 100f);
        thanksLabel.setWidth(Config.gameWidth);
        thanksLabel.setLineAlign(Align.center);
        thanksLabel.setFontScale(1f);
    }

    @Override
    public void update(float dt) {
        titleLabel.update(dt);
        themeLabel.update(dt);
        leftCreditLabel.update(dt);
        thanksLabel.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.setScreen(new TitleScreen(game));
        }
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
        themeLabel.render(batch);
        leftCreditLabel.render(batch);
        thanksLabel.render(batch);
        batch.end();
    }

    private void renderTextWithBorder(String string, float scale, float width, float x, float y) {
        BitmapFont font = Game.getCurrentFont();
        GlyphLayout layout = assets.layout;
        font.getData().setScale(scale);

        layout.setText(font, string, textBorderColor, width, Align.center, true);
        font.draw(batch, layout, x +2, y + 2);
        layout.setText(font, string, textColor, width, Align.center, true);
        font.draw(batch, layout, x, y);
    }
}
