package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld45.ui.Panel;

public class GameHud {
    private GameScreen screen;
    private double time;
    private double totalTime;
    private float scoreValue;
    private Panel toyChestPanel;
    private Panel upgradePanel;

    private boolean firstView = true;

    public GameHud(GameScreen gameScreen) {
        this.screen = gameScreen;
        this.time = System.currentTimeMillis();
        this.scoreValue = 0f;
        this.toyChestPanel = new Panel(screen.assets, screen.game.tween);
        this.toyChestPanel.setInitialBounds(screen.hudCamera.viewportWidth, 0f,
                                            screen.hudCamera.viewportWidth * (1f / 3f),
                                            screen.hudCamera.viewportHeight);

        upgradePanel = new Panel(screen.assets, screen.game.tween);
        upgradePanel.setInitialBounds(0, -screen.hudCamera.viewportHeight, screen.hudCamera.viewportWidth, screen.hudCamera.viewportHeight);
        upgradePanel.horizontal = false;
    }

    public void update(float dt) {
        totalTime = System.currentTimeMillis() - time;
        scoreValue = MathUtils.lerp(scoreValue, screen.player.score, 0.15f);

        if (firstView) {
            firstView = false;
            upgradePanel.toggle(screen.hudCamera);
        }

        toyChestPanel.update(dt);
        upgradePanel.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            toyChestPanel.toggle(screen.hudCamera);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            upgradePanel.toggle(screen.hudCamera);
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

        toyChestPanel.render(batch);
        upgradePanel.render(batch);
//        if (toyChestPanel.isVisible()) {
//            TextureRegion cursor = screen.assets.uiCursorHand;
//            batch.draw(cursor, Gdx.input.getX(), screen.hudCamera.viewportHeight - Gdx.input.getY() - cursor.getRegionHeight());
//        }
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
