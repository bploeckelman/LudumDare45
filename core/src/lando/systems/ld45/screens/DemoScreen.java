package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.ui.Button;

public class DemoScreen extends BaseScreen {

    private Vector2 position;
    private Button upgradeButton;

    private boolean showUpgrade = false;

    public DemoScreen(Game game) {
        super(game);

        position = new Vector2(Config.gameWidth / 2, Config.initialBallY);

        upgradeButton = new Button(assets.whitePixel, Config.gameWidth - 200, 50, 180, 50);
        upgradeButton.setText("UPGRADE");
        upgradeButton.set(this);
        upgradeButton.addClickHandler(() -> game.setScreen(new GameScreen(game)) );
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (position.y > -30) {
            position.y -= Config.gravity * dt;
        } else {
            showUpgrade = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(assets.whiteCircle, position.x - Config.ballRadius, position.y - Config.ballRadius,
                Config.ballRadius * 2, Config.ballRadius * 2);

        if (showUpgrade) {
            renderUIElements(batch);
        }
        batch.end();
    }
}
