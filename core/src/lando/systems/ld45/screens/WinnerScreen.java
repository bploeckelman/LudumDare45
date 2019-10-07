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
import lando.systems.ld45.ui.Button;
import lando.systems.ld45.ui.typinglabel.TypingLabel;
import lando.systems.ld45.utils.ArtPack;

public class WinnerScreen extends BaseScreen {

    private TypingLabel titleLabel;
    private TypingLabel themeLabel;
    private TypingLabel leftCreditLabel;
    private TypingLabel rightCreditLabel;
    private TypingLabel thanksLabel;
    private TypingLabel disclaimerLabel;


    static String title = "Ball of Duty: Special Drops";
    static String theme = "Made for Ludum Dare 45:\nTheme: Start with nothing";
    static String thanks = "Thanks for playing our game!";
    static String developers = "Developed by:\nDoug Graham\nBrian Ploeckelman\nBrian Rossman\nJeffrey Hwang\nJake Shropshire";
    static String artists = "Art by:\nMatt Neumann\nTroy Sullivan";
    static String otherDuties = "Other Duties as Assigned:\nLuke Bain";
    static String emotionalSupport = "Emotional Support:\nAsuka the Shiba\n" + "Romeo the Poodle";
    static String music = "Sound by:\nSomeone";
    static String libgdx = "Made with {COLOR=red}<3{COLOR=white} and LibGDX";
    static String disclaimer = "Disclaimer!!!\nNo balls were harmed in making of this game.";
    Color textColor = new Color(Color.WHITE);
    Color textBorderColor = new Color(Color.GRAY);

    private Button statButton;

    public WinnerScreen(Game game) {
        super(game);
        titleLabel = new TypingLabel(assets.fontMap.get(ArtPack.a), title, 0f, 0f);
        titleLabel.setWidth(Config.gameWidth);
        titleLabel.setFontScale(2f);
        titleLabel.setY(Config.gameHeight / 2f + 280f);

        themeLabel = new TypingLabel(assets.fontMap.get(ArtPack.d), theme, 25f, 0f);
        themeLabel.setWidth(Config.gameWidth / 2 - 50f);
        themeLabel.setFontScale(1);
        themeLabel.setY(Config.gameHeight / 2f + 220f);

        leftCreditLabel = new TypingLabel(assets.fontMap.get(ArtPack.c), developers + "\n\n" + emotionalSupport + "\n\n", 75f, Config.gameHeight / 2f + 130f);
        leftCreditLabel.setWidth(Config.gameWidth / 2 - 150f);
        leftCreditLabel.setLineAlign(Align.left);
        leftCreditLabel.setFontScale(1f);

        rightCreditLabel = new TypingLabel(assets.fontMap.get(ArtPack.c), artists + "\n\n" + music + "\n\n" + otherDuties + "\n\n" + libgdx, Config.gameWidth / 2 + 75f, Config.gameHeight / 2f + 130f);
        rightCreditLabel.setWidth(Config.gameWidth / 2 - 150f);
        rightCreditLabel.setLineAlign(Align.left);
        rightCreditLabel.setFontScale(1f);

        thanksLabel = new TypingLabel(assets.fontMap.get(ArtPack.d), thanks, 0f, 100f);
        thanksLabel.setWidth(Config.gameWidth);
        thanksLabel.setLineAlign(Align.center);
        thanksLabel.setFontScale(1f);

        disclaimerLabel = new TypingLabel(assets.fontMap.get(ArtPack.d), "{JUMP=.2}{WAVE=0.9;1.2;1.75}{RAINBOW}" + disclaimer + "{ENDRAINBOW}{ENDWAVE}{ENDJUMP}", 0f, 50f);
        disclaimerLabel.setWidth(Config.gameWidth);
        thanksLabel.setLineAlign(Align.center);
        disclaimerLabel.setFontScale(1f);

        statButton = new Button(this, worldCamera, Config.gameWidth / 2 + 25f, Config.gameHeight / 2f + 170f, Config.gameWidth / 2 - 50f, 40f);
        statButton.setText("Statistics");
        statButton.addClickHandler(() -> game.setScreen(new StatsScreen(game)));
        addUIElement(statButton);

    }

    @Override
    public void update(float dt) {
        super.update(dt);
        titleLabel.update(dt);
        themeLabel.update(dt);
        leftCreditLabel.update(dt);
        rightCreditLabel.update(dt);
        thanksLabel.update(dt);
        disclaimerLabel.update(dt);

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
        renderUIElements(batch);
        titleLabel.render(batch);
        themeLabel.render(batch);
        leftCreditLabel.render(batch);
        rightCreditLabel.render(batch);
        thanksLabel.render(batch);
        disclaimerLabel.render(batch);
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
