package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld45.Config;
import lando.systems.ld45.ui.HudBox;

public class GameHud {

    private GameScreen screen;

    public float time = 0;
    public float scoreValue = 0;

    private HudBox scoreBox;
    private HudBox timeBox;

    public GameHud(GameScreen gameScreen) {

        this.screen = gameScreen;

        scoreBox = new HudBox(10, 10, 160, 40);
        timeBox = new HudBox(Config.gameWidth - 10 - 160, 10, 160, 40);
    }

    public void update(float dt) {
        time += dt;

        scoreValue = MathUtils.lerp(scoreValue, screen.player.score, dt);

        timeBox.setText(toTimeString(time));
        timeBox.update(dt);

        scoreBox.setText("$" + (long)scoreValue);
        scoreBox.update(dt);
    }

    public void render(SpriteBatch batch) {
        scoreBox.render(batch);
        timeBox.render(batch);
    }

    private static String toTimeString(float time) {
        int minutes = (int)time / 60;
        int seconds = (int)time % 60;
        return String.format("%d : %02d", minutes, seconds);
    }
}
