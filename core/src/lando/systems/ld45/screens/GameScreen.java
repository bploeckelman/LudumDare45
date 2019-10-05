package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Game;
import lando.systems.ld45.collision.CollisionManager;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.objects.Bumper;
import lando.systems.ld45.objects.Boundary;
import lando.systems.ld45.objects.GameObject;

public class GameScreen extends BaseScreen {

    private Array<GameObject> gameObjects = new Array<>();
    public Array<Ball> balls = new Array<>();

    private CollisionManager collisionManager;
    public Boundary boundary;

    private GameHud hud = new GameHud();

    public GameScreen(Game game) {
        super(game);


        while (gameObjects.size < 10) {
            addObject(new Bumper(this, MathUtils.random(20, 40)));
        }

        for (int i  = 0; i < 10; i++) {
            balls.add(new Ball(this, 10f));
            balls.add(new Ball(this, 5f));
        }
        this.collisionManager = new CollisionManager(this);
        this.boundary = new Boundary(this);
    }

    public void addObject(GameObject gameObject) {
        gameObjects.add(gameObject);
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
