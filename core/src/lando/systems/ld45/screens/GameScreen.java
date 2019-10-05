package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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

    public GameScreen(Game game) {
        super(game);

        int x = 150;
        for (int g = 0; g < 3; g++) {
            for (int l = 0; l < 4; l++) {
                GameObject item = addObject(Bumper.getBumper(this, g));
                item.pos.x = x;
                item.pos.y = 200;

                item = addObject(Peg.getPeg(this, l, g));
                item.pos.x = x;
                item.pos.y = 300;


                Spinner spinner = Spinner.getSpinner(this, l, g);
                spinner.left = (l % 2) == 0;
                item = addObject(spinner);
                item.pos.x = x;
                item.pos.y = 400;

                x += 50;
            }
        }

        for (int i  = 0; i < 10; i++) {
            balls.add(new Ball(this, MathUtils.random(10f, 15f)));
            balls.add(new Ball(this, MathUtils.random(3f, 5f)));
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
        gameObjects.forEach(x -> x.update(dt));
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

        batch.setProjectionMatrix(hudCamera.combined);
        hud.render(batch);

        batch.end();
    }
}
