package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld45.Config;
import lando.systems.ld45.ui.Button;
import lando.systems.ld45.ui.HudBox;

public class GameHud {

    private GameScreen screen;

    public float time = 0;
    public float scoreValue = 0;

    private HudBox scoreBox;
    private HudBox timeBox;

    private Button startButton;
    private Button editButton;
    private Button upgradeButton;

    public GameHud(GameScreen gameScreen) {

        this.screen = gameScreen;

        scoreValue = gameScreen.player.score;

        scoreBox = new HudBox(10, 10, 160, 40);
        timeBox = new HudBox(Config.gameWidth - 10 - 160, 10, 160, 40);

        startButton = new Button(screen, screen.hudCamera, screen.assets.whitePixel, 250, 105, 300f, 50f);
        startButton.setText("PLAY AGAIN");
        startButton.addClickHandler(() -> restart());
        screen.addUIElement(startButton);

        editButton = new Button(screen, screen.hudCamera, screen.assets.whitePixel, 450, 375, 300f, 50f);
        editButton.setText("EDIT");
        editButton.addClickHandler(() -> screen.editGame());
        screen.addUIElement(editButton);

        upgradeButton = new Button(screen, screen.hudCamera, screen.assets.whitePixel, 50, 375, 300f, 50f);
        upgradeButton.setText("UPGRADE");
        upgradeButton.addClickHandler(() -> { screen.game.setScreen(new UpgradeScreen(screen.game)); });
        screen.addUIElement(upgradeButton);
    }

    private void restart() {
        time = 0;
        screen.startGame();
    }

    public void update(float dt) {
        time += dt;

        if (!screen.gameOver) {
            timeBox.setText(toTimeString(time));
            timeBox.update(dt);
        }

        editButton.isVisible = upgradeButton.isVisible = screen.gameOver && !screen.editMode;
        startButton.isVisible = screen.gameOver || screen.editMode;

        scoreValue = MathUtils.lerp(scoreValue, screen.player.score, dt);

        scoreBox.setText("$" + (long)scoreValue);
        scoreBox.update(dt);
    }

    public void render(SpriteBatch batch) {
        if (!screen.editMode) {
            scoreBox.render(batch);
            timeBox.render(batch);
        }
        screen.renderUIElements(batch);
    }

    private StringBuilder sb = new StringBuilder(15);

    private String toTimeString(float time) {
        sb.setLength(0);
        int minutes = (int)time / 60;
        int seconds = (int)time % 60;
        sb.append(minutes);
        sb.append(" : ");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        return sb.toString();
    }
}
