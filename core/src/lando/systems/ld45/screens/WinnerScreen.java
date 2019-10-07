package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.ui.typinglabel.TypingLabel;

public class WinnerScreen extends BaseScreen {

    private TypingLabel titleLabel;
    private TypingLabel themeLabel;

    static String title = "B a l l  o f  D u t y : P a c h i n k o   E d i t i o n";
    static String theme = "Made for Ludum Dare 45:\nTheme: Start with nothing";
    static String thanks = "Thanks for playing our game!";
    static String developers = "Developed by:\nDoug Graham\nBrian Ploeckelman\nBrian Rossman\nJeffrey Hwang\nJake";
    static String artists = "Art by:\nSomeone";
    static String emotionalSupport = "Emotional Support:\nAsuka the Shiba\n" + "Romeo the Poodle";
    static String music = "Sound by:\nSomeone";
    static String libgdx = "Made with <3 and LibGDX";
    static String disclaimer = "Disclaimer!!!\nNo balls were harmed in making of this game.";
    Color textColor = new Color(Color.WHITE);
    Color textBorderColor = new Color(Color.GRAY);

    public WinnerScreen(Game game) {
        super(game);
        titleLabel = new TypingLabel(assets.font, title, 0f, 0f);
        titleLabel.setWidth(Config.gameWidth);
        titleLabel.setFontScale(2f);
        titleLabel.setY(Config.gameHeight / 2f + 280f);

        themeLabel = new TypingLabel(assets.font, theme, 0f, 0f);
        themeLabel.setWidth(Config.gameWidth);
        themeLabel.setFontScale(1.5f);
        themeLabel.setY(Config.gameHeight / 2f + 200f);

    }

    @Override
    public void update(float dt) {
        titleLabel.update(dt);
        themeLabel.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.setScreen(new TitleScreen(game));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);

        titleLabel.render(batch);
        themeLabel.render(batch);
        renderTextWithBorder(developers + "\n\n" + emotionalSupport, 2f, Config.gameWidth / 2 - 50f, 25f, Config.gameHeight / 2f + 150f);
        renderTextWithBorder(artists + "\n\n" + music + "\n\n" + libgdx, 2f, Config.gameWidth / 2 - 50f, Config.gameWidth / 2 + 25f, Config.gameHeight / 2f + 150f);
        renderTextWithBorder(thanks, 2f, Config.gameWidth, 0f,  100f);
        renderTextWithBorder(disclaimer, 1f, Config.gameWidth, 0f,  50f);



        batch.end();
    }

    private void renderTextWithBorder(String string, float scale, float width, float x, float y) {
        BitmapFont font = assets.font;
        GlyphLayout layout = assets.layout;
        font.getData().setScale(scale);

        layout.setText(font, string, textBorderColor, width, Align.center, true);
        font.draw(batch, layout, x +2, y + 2);
        layout.setText(font, string, textColor, width, Align.center, true);
        font.draw(batch, layout, x, y);
    }
}
