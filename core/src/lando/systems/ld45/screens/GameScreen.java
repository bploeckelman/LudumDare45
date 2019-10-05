package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Game;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.objects.GameObject;

public class GameScreen extends BaseScreen {

    private Array<GameObject> gameObjects = new Array<>();

    public GameScreen(Game game) {
        super(game);

        addObject(new Ball(assets.whiteCircle));
    }

    public void addObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    @Override
    public void update(float dt) {
        gameObjects.forEach(x -> x.update(dt));
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);

        gameObjects.forEach(x -> x.render(batch));

        batch.end();
    }
}
