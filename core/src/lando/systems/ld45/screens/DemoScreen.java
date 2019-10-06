package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;

public class DemoScreen extends BaseScreen {

    private Vector2 position;

    public DemoScreen(Game game) {
        super(game);

        position = new Vector2(Config.gameWidth / 2, Config.initialBallY);
    }

    @Override
    public void update(float dt) {
        if (position.y > -30) {
            position.y -= Config.gravity * dt;
        } else {
            // show button and transition
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(assets.whiteCircle, position.x - Config.ballRadius, position.y - Config.ballRadius,
                Config.ballRadius * 2, Config.ballRadius * 2);

        batch.end();
    }
}
