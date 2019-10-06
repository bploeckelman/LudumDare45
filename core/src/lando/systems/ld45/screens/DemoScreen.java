package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.ui.Button;

public class DemoScreen extends BaseScreen {

    private Button upgradeButton;

    private Ball ball;

    private boolean showUpgrade = false;

    public DemoScreen(Game game) {
        super(game);

        ball = new Ball(this, Config.ballRadius);
        ball.initialize(Config.gameWidth / 2, Config.initialBallY, 0, -99);

        upgradeButton = new Button(assets.whitePixel, Config.gameWidth - 200, 50, 180, 50);
        upgradeButton.setText("UPGRADE");
        upgradeButton.set(this);
        upgradeButton.addClickHandler(() -> game.setScreen(new UpgradeScreen(game)) );
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (ball != null) {
            ball.update(dt);
            ball.bounds.y += (ball.vel.y*dt);

            if (ball.isOffscreen()) {
                long points = 1;
                game.player.addScore(points);
                particle.addPointsParticles(points, ball.bounds.x, 10f);
                ball = null;
                showUpgrade = true;
            }
        }

        particle.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        particle.renderBackgroundParticles(batch);
        if (ball != null) {
            ball.render(batch);
        }

        if (showUpgrade) {
            renderUIElements(batch);
        }

        particle.renderForegroundParticles(batch);
        batch.end();
    }
}
