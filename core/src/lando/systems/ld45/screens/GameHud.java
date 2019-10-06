package lando.systems.ld45.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Bounce;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld45.accessors.RectangleAccessor;

public class GameHud {
    private GameScreen screen;
    private double time;
    private double totalTime;
    private float scoreValue;
    private boolean toyChestPanelVisible;
    private boolean toyChestPanelAnimating;
    private Rectangle toyChestPanelBounds;

    public GameHud(GameScreen gameScreen) {
        this.screen = gameScreen;
        this.time = System.currentTimeMillis();
        this.scoreValue = 0f;
        this.toyChestPanelVisible = false;
        this.toyChestPanelAnimating = false;
        this.toyChestPanelBounds = new Rectangle(screen.hudCamera.viewportWidth, 0f,
                                                 screen.hudCamera.viewportWidth * (1f / 3f),
                                                 screen.hudCamera.viewportHeight);
    }

    public void update(float dt) {
        totalTime = System.currentTimeMillis() - time;
        scoreValue = MathUtils.lerp(scoreValue, screen.player.score, 0.15f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (!toyChestPanelAnimating) {
                toyChestPanelAnimating = true;

                if (toyChestPanelVisible) {
                    Tween.to(toyChestPanelBounds, RectangleAccessor.X, 0.05f)
                         .target(screen.hudCamera.viewportWidth)
                         .setCallback((i, baseTween) -> {
                             toyChestPanelVisible = false;
                             toyChestPanelAnimating = false;
                         })
                         .start(screen.game.tween);
                } else {
                    toyChestPanelVisible = true;
                    Tween.to(toyChestPanelBounds, RectangleAccessor.X, 0.33f)
                         .target(screen.hudCamera.viewportWidth - toyChestPanelBounds.width)
                         .ease(Bounce.OUT)
                         .setCallback((i, baseTween) -> toyChestPanelAnimating = false)
                         .start(screen.game.tween);
                }
            }
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

        if (toyChestPanelVisible) {
            screen.assets.uiPanelNinepatch.draw(batch, toyChestPanelBounds.x, toyChestPanelBounds.y, toyChestPanelBounds.width, toyChestPanelBounds.height);
            float insetMargin = 25f;
            screen.assets.uiPanelInsetNinepatch.draw(batch,
                toyChestPanelBounds.x + insetMargin, toyChestPanelBounds.y + insetMargin,
            toyChestPanelBounds.width - 2f * insetMargin, toyChestPanelBounds.height - 2f * insetMargin);
        }
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
