package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Game;
import lando.systems.ld45.collision.CollisionManager;
import lando.systems.ld45.objects.*;

public class GameScreen extends BaseScreen {

    private Array<GameObject> gameObjects = new Array<>();
    public Array<Ball> balls = new Array<>();

    private CollisionManager collisionManager;
    public Boundary boundary;

    private GameHud hud = new GameHud();

    public boolean editMode = true;
    public Vector3 projection = new Vector3();
    public Vector2 mousePosition = new Vector2();

    public GameScreen(Game game) {
        super(game);

        int x = 150;
        for (int g = 0; g < 3; g++) {
            for (int l = 0; l < 4; l++) {
                GameObject item = addObject(Bumper.getBumper(this, g));
                item.setPosition(x, 200);

                item = addObject(Peg.getPeg(this, l, g));
                item.setPosition(x, 300);

                Spinner spinner = Spinner.getSpinner(this, l, g);
                spinner.left = (l % 2) == 0;
                item = addObject(spinner);
                item.setPosition(x, 400);

                x += 50;
            }
        }

        for (int i  = 0; i < 100; i++) {
            balls.add(new Ball(this, MathUtils.random(3f, 8f)));
        }
        this.collisionManager = new CollisionManager(this);
        this.boundary = new Boundary(this);
    }

    public GameObject addObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        return gameObject;
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        collisionManager.solve(dt);
        balls.forEach(ball -> ball.update(dt));
        for (int i = balls.size -1; i >= 0; i--){
            Ball b = balls.get(i);
            b.update(dt);
            if (b.bounds.y < - b.bounds.radius){
                balls.removeIndex(i);
            }
        }

        projection.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        projection = worldCamera.unproject(projection);
        mousePosition.set(projection.x, projection.y);
        gameObjects.forEach(x -> x.update(dt, mousePosition));
        particle.update(dt);

        hud.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        assets.ballTrailTexture.bind();
        balls.forEach(Ball::renderTrail);

        balls.forEach(ball -> ball.trail.render(worldCamera));
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            boundary.render(batch);
            particle.renderBackgroundParticles(batch);
            balls.forEach(ball -> ball.render(batch));
            gameObjects.forEach(x -> x.render(batch));
            particle.renderForegroundParticles(batch);
        }

        batch.draw(assets.whiteCircle, mousePosition.x - 2, mousePosition.y - 2, 2, 2, 4, 4, 1, 1, 1);

        batch.setProjectionMatrix(hudCamera.combined);
        hud.render(batch);

        batch.end();
    }
}
