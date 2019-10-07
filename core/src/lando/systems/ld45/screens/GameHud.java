package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Config;
import lando.systems.ld45.collision.Segment2D;
import lando.systems.ld45.ui.HudBox;
import lando.systems.ld45.utils.AssetType;

public class GameHud {

    private GameScreen screen;
    private double time;
    private double totalTime;
    private float scoreValue;

    private HudBox scoreBox;
    private HudBox timeBox;


    public GameHud(GameScreen gameScreen) {

        this.screen = gameScreen;
        this.time = System.currentTimeMillis();
        this.scoreValue = 0f;

        scoreBox = new HudBox(10, 10, 160, 40);
        timeBox = new HudBox(Config.gameWidth - 10 - 160, 10, 160, 40);
    }

    public void update(float dt) {
        scoreBox.update(dt);
        timeBox.update(dt);

        totalTime = System.currentTimeMillis() - time;
        scoreValue = MathUtils.lerp(scoreValue, screen.player.score, dt);
    }

    public void render(SpriteBatch batch) {
        scoreBox.render(batch);
        timeBox.render(batch);

//
//        float x = 6;
//        float y = 26;
//
//        // screen.game.assets.assetMap.get(screen.game.artPack).get(AssetType.boundary_line).
//
//        String scoreText = Integer.toString(MathUtils.round(scoreValue), 10);
//        drawString(batch, "¥", x, y, screen.assets.font);
//        drawString(batch, scoreText, x + 130, y, screen.assets.font);
//
//        x += screen.hudCamera.viewportWidth - 166;
//
//        drawString(batch, "Time:", x, y, screen.assets.font);
//        drawString(batch, toTimeString((long)totalTime / 1000), x + 80, y, screen.assets.font);
    }

    private void drawString(SpriteBatch batch, String text, float x, float y, BitmapFont font) {
        font.setColor(Color.BLUE);
        font.draw(batch, text, x, y);
    }

    private static String toTimeString(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        String s = seconds < 10 ? "0" + seconds : "" + seconds;
        String m = minutes < 10 ? "0" + minutes : "" + minutes;
        return m + " : " + s;
    }
}
