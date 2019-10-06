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
import lando.systems.ld45.state.PlayerState;

public class GameScreen extends BaseScreen {

    public Array<GameObject> gameObjects = new Array<>();
    public Array<Ball> balls = new Array<>();

    public PlayerState player = new PlayerState();

    private CollisionManager collisionManager;
    public Boundary boundary;

    private GameHud hud = new GameHud(this);
    private float pathShaderTimer;

    public boolean editMode = false;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            for (int i  = 0; i < 20; i++) {
                balls.add(new Ball(this, MathUtils.random(3f, 8f)));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            balls.clear();
        }

        pathShaderTimer += dt;

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
        // NOTE: this has to be outside of batch begin/end because reasons
        renderBallTrails();

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            boundary.render(batch);
            particle.renderBackgroundParticles(batch);
            balls.forEach(ball -> ball.render(batch));
            gameObjects.forEach(x -> x.render(batch));
            particle.renderForegroundParticles(batch);
        }
        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            hud.render(batch);
            batch.draw(assets.whiteCircle, mousePosition.x - 2, mousePosition.y - 2, 2, 2, 4, 4, 1, 1, 1);
        }
        batch.end();
    }

    private void renderBallTrails() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        assets.ballTrailShader.begin();
        {
            game.assets.ballTrailTexture.bind(0);

            assets.ballTrailShader.setUniformMatrix("u_projTrans", game.getScreen().worldCamera.combined);
            assets.ballTrailShader.setUniformi("u_texture", 0);
            assets.ballTrailShader.setUniformf("u_time", pathShaderTimer);

            balls.forEach(Ball::renderTrailMesh);
        }
        assets.ballTrailShader.end();
    }

}
