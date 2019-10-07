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

    private Button timeButton;
    private Button ballDropButton;

    // non edit
    private Button playAgainButton;
    private Button editButton;
    private Button upgradeButton;

    // edit buttons
    private Button playButton;
    private Button clearButton;
    private Button toyBoxButton;

    private String[] timeText = new String[] { "1X", "5X", "10X", "1/10X", "1/5X" };
    private int timeTextIndex = 0;

    public GameHud(GameScreen gameScreen) {

        this.screen = gameScreen;

        scoreValue = gameScreen.player.score;

        float viewWidth = screen.hudCamera.viewportWidth;

        scoreBox = new HudBox(10, 10, 110, 40);
        timeBox = new HudBox(viewWidth - 120, 10, 110, 40);
        timeButton = new Button(screen, screen.hudCamera,viewWidth - 230, 10, 100, 40);
        timeButton.addClickHandler(() -> setSpeed());
        screen.addUIElement(timeButton);
        resetSpeed();

        ballDropButton = new Button(screen, screen.hudCamera, 130, 10, 100, 40);
        ballDropButton.setText("DROP");
        ballDropButton.addClickHandler(() -> dropBalls());
        screen.addUIElement(ballDropButton);

        playAgainButton = new Button(screen, screen.hudCamera, 250, 210, 300f, 50f);
        playAgainButton.setText("PLAY AGAIN");
        playAgainButton.addClickHandler(() -> restart());
        screen.addUIElement(playAgainButton);

        editButton = new Button(screen, screen.hudCamera, 450, 340, 300f, 50f);
        editButton.setText("EDIT");
        editButton.addClickHandler(() -> screen.editGame());
        screen.addUIElement(editButton);

        upgradeButton = new Button(screen, screen.hudCamera, 50, 340, 300f, 50f);
        upgradeButton.setText("UPGRADE");
        upgradeButton.addClickHandler(() -> { screen.game.setScreen(new UpgradeScreen(screen.game)); });
        screen.addUIElement(upgradeButton);

        playButton = new Button(screen, screen.hudCamera, 50,110, 200f, 50f);
        playButton.setText("PLAY");
        playButton.addClickHandler(() -> restart());
        screen.addUIElement(playButton);

        clearButton = new Button(screen, screen.hudCamera, 300, 110, 200f, 50f);
        clearButton.setText("CLEAR");
        clearButton.addClickHandler(() -> screen.clearGameObjects());
        screen.addUIElement(clearButton);

        toyBoxButton = new Button(screen, screen.hudCamera, 550, 110, 200f, 50f);
        toyBoxButton.setText("OPEN");
        toyBoxButton.addClickHandler(() -> screen.toggleToyBox());
        screen.addUIElement(toyBoxButton);
    }

    private void resetSpeed() {
        timeTextIndex = -1;
        setSpeed();
    }

    private void setSpeed() {
        if (++timeTextIndex == timeText.length) {
            timeTextIndex = 0;
        }

        timeButton.setText(timeText[timeTextIndex]);

        // should probably use a fancy enum, but oh well
        float speedModifier = 1;
        switch (timeTextIndex) {
            case 1: speedModifier = 5; break;
            case 2: speedModifier = 10; break;
            case 3: speedModifier = 0.2f; break;
            case 4: speedModifier = 0.1f; break;
            default: speedModifier = 1; break;
        }
        screen.game.setSpeedModifier(speedModifier);
    }

    private void dropBalls() {
        ballDropButton.isDisabled = true;
        screen.hopper.dropBalls();
    }

    private void restart() {
        time = 0;
        ballDropButton.isDisabled = false;
        screen.startGame();
    }

    public void update(float dt) {
        time += dt;

        if (!screen.gameOver) {
            timeBox.setText(toTimeString(time));
            timeBox.update(dt);
        }

        if ((screen.gameOver || screen.editMode) && timeTextIndex != 0) {
            resetSpeed();
        }

        timeButton.isVisible = ballDropButton.isVisible = !(screen.gameOver || screen.editMode);
        editButton.isVisible = upgradeButton.isVisible = playAgainButton.isVisible = screen.gameOver && !screen.editMode;
        playButton.isVisible = clearButton.isVisible = screen.gameOver && screen.editMode;
        toyBoxButton.isVisible = playButton.isVisible && !(screen.toyChestPanel.isAnimating() || screen.toyChestPanel.isVisible());
        
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
        sb.append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        return sb.toString();
    }
}
