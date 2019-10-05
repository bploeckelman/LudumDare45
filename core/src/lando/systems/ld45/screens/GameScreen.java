package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Game;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.objects.GameObject;

public class GameScreen extends BaseScreen {

    private Array<GameObject> gameObjects = new Array<>();
    private Array<Ball> balls = new Array<>();

    public GameScreen(Game game) {
        super(game);

        balls.add(new Ball(this, 10f));
    }

    public void addObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        balls.forEach(ball -> ball.update(dt));
        gameObjects.forEach(x -> x.update(dt));
    }

    @Override
    public void render(SpriteBatch batch) {
        balls.forEach(ball -> ball.trail.render(worldCamera));

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            balls.forEach(ball -> ball.render(batch));
            gameObjects.forEach(x -> x.render(batch));
        }
        batch.end();
    }
}
