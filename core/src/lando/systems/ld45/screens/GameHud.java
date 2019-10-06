package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameHud {
    private GameScreen screen;
    private double time;
    private double totalTime;
    private int score;

    public GameHud(GameScreen gameScreen) {
        screen = gameScreen;
        time = System.currentTimeMillis();
        score = 0;
    }

    public void update(float dt) {
        totalTime = System.currentTimeMillis() - time;
        score = (int) (totalTime / 100);
    }

    public void render(SpriteBatch batch) {
        float x = 6;
        float y = 26;

        drawString(batch, "Score:", x, y, screen.assets.font);
        drawString(batch, score+"", x + 130, y, screen.assets.font);

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
