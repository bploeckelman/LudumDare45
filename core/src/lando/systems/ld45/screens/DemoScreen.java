package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.ui.Button;


public class DemoScreen extends BaseScreen {

    private Button upgradeButton;

    private Ball ball;

    private float waitTime = 0;

    public DemoScreen(Game game) {
        super(game);

        ball = new Ball(this, Config.ballRadius);
        ball.color.set(Color.WHITE);
        ball.initialize(Config.gameWidth / 2, 620, 0, -99);

        float width = 500f;
        float height = 50f;

        upgradeButton = new Button(this, hudCamera,
                (hudCamera.viewportWidth - width) / 2f, (hudCamera.viewportHeight - height) / 2, width, height);
        upgradeButton.setText("Buy Upgrade Menu: $1");
        upgradeButton.addClickHandler(() -> game.setScreen(new UpgradeScreen(game), game.assets.pizelizeShader, 1f) );
        upgradeButton.isVisible = false;
        addUIElement(upgradeButton);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (transitionDelay > 0) return;

        if (ball != null) {
            ball.update(dt);
            ball.bounds.y += (ball.vel.y*dt);

            if (ball.isOffscreen()) {
                long points = 1;
                game.player.addScore(points);
                particle.addPointsParticles(points, ball.bounds.x, 10f, 75f);
                ball = null;
                waitTime = 2;
            }
        } else {
            waitTime -= dt;
            upgradeButton.isVisible = (waitTime < 0);
        }

        particle.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.setColor(Color.DARK_GRAY);
        batch.draw(assets.whitePixel, 0, 0, worldCamera.viewportWidth, worldCamera.viewportHeight);

        batch.setColor(Color.WHITE);

        particle.renderBackgroundParticles(batch);
        if (ball != null) {
            ball.keyframe = game.assets.whiteCircle;
            ball.render(batch);
        }

        renderUIElements(batch);
        
        particle.renderForegroundParticles(batch);
        batch.end();
    }
}
