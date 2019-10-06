package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld45.ui.Panel;
import lando.systems.ld45.utils.UIAssetType;

public class GameHud {
    private GameScreen screen;
    private double time;
    private double totalTime;
    private float scoreValue;
    private Panel toyChestPanel;

    private boolean firstView = true;

    public GameHud(GameScreen gameScreen) {
        this.screen = gameScreen;
        this.time = System.currentTimeMillis();
        this.scoreValue = 0f;
    }

    public void update(float dt) {
        totalTime = System.currentTimeMillis() - time;
        scoreValue = MathUtils.lerp(scoreValue, screen.player.score, 0.15f);

        if (firstView) {
            firstView = false;
        }
    }

    public void render(SpriteBatch batch) {
        float x = 6;
        float y = 26;

        String scoreText = Integer.toString(MathUtils.round(scoreValue), 10);
        drawString(batch, "Score:", x, y, screen.assets.font);
        drawString(batch, scoreText, x + 130, y, screen.assets.font);

        x += screen.hudCamera.viewportWidth - 166;

        drawString(batch, "Time:", x, y, screen.assets.font);
        drawString(batch, toTimeString((long)totalTime / 1000), x + 80, y, screen.assets.font);
    }

    private void drawString(SpriteBatch batch, String text, float x, float y, BitmapFont font) {
        font.setColor(Color.WHITE);
        font.draw(batch, text, x, y);
        font.getData().setScale(1.4f);
    }

    private static String toTimeString(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        String s = seconds < 10 ? "0" + seconds : "" + seconds;
        String m = minutes < 10 ? "0" + minutes : "" + minutes;
        return m + " : " + s;
    }
}
