package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.backgrounds.HexBackground;
import lando.systems.ld45.backgrounds.iBackground;
import lando.systems.ld45.collision.CollisionManager;
import lando.systems.ld45.objects.*;
import lando.systems.ld45.state.PlayerState;

public class GameScreen extends BaseScreen {

    public iBackground background;
    public Array<GameObject> gameObjects = new Array<>();
    public Array<Ball> balls = new Array<>();

    public PlayerState player = new PlayerState();
    public Hopper hopper;

    private CollisionManager collisionManager;
    public Boundary boundary;

    private GameHud hud = new GameHud(this);
    private float pathShaderTimer;

    public boolean editMode = false;
    public Vector3 projection = new Vector3();
    public Vector2 mousePosition = new Vector2();

    public GameScreen(Game game) {
        super(game);
        background = new HexBackground(this);
        hopper = new Hopper(this);

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

        startGame();
    }

    public void startGame() {
        editMode = false;
        hopper.reset();
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

//        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
//            for (int i  = 0; i < 20; i++) {
//                balls.add(new Ball(this, MathUtils.random(3f, 8f)));
//            }
//        }

        background.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            editMode = !editMode;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            balls.clear();
        }

        pathShaderTimer += dt;

        hopper.update(dt);

        collisionManager.solve(dt);
        for (int i = balls.size -1; i >= 0; i--){
            Ball b = balls.get(i);
            b.update(dt);
            if (b.bounds.y < - (b.bounds.radius * 10)){
                balls.removeIndex(i);

                long points = 1;
                player.addScore(points);
                particle.addPointsParticles(points, b.bounds.x, 10f);
            }
        }

        if (editMode) {
            projection.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            projection = worldCamera.unproject(projection);
            mousePosition.set(projection.x, projection.y);
        } else {
            mousePosition.set(0, 0);
        }

        gameObjects.forEach(x -> x.update(dt, mousePosition));
        particle.update(dt);

        if (isGameOver()) {
            endGame();
        }

        hud.update(dt);
    }

    private void endGame() {
        // player.balls += 3;
        startGame();
    }

    public boolean isGameOver() {
        return (balls.size == 0) && (hopper.availableBalls == 0);
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        background.render(batch);
        batch.end();
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
            hopper.render(batch);
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
        // normal blendy
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        // vector-ish blendy
        Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_COLOR, GL20.GL_ONE, GL20.GL_SRC_ALPHA, GL20.GL_ZERO);
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
