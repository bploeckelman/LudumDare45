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
import lando.systems.ld45.backgrounds.Background;
import lando.systems.ld45.backgrounds.FlatBackground;
import lando.systems.ld45.backgrounds.GraphPaperBackground;
import lando.systems.ld45.collision.CollisionManager;
import lando.systems.ld45.objects.*;
import lando.systems.ld45.state.PlayerState;
import lando.systems.ld45.ui.Panel;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.UIAssetType;
import lando.systems.ld45.utils.screenshake.ScreenShakeCameraController;

public class GameScreen extends BaseScreen {

    public Background background;
    public Array<GameObject> gameObjects = new Array<>();
    public Array<Ball> balls = new Array<>();


    public Hopper hopper;

    private CollisionManager collisionManager;
    public Boundary boundary;

    private GameHud hud = new GameHud(this);
    private float pathShaderTimer;

    public boolean editMode = false;
    public Vector3 projection = new Vector3();
    public Vector2 mousePosition = new Vector2();

    private Panel toyChestPanel;

    public GameScreen(Game game) {
        super(game);
        game.particle.clearAll();

        background = new GraphPaperBackground(this);
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

        this.toyChestPanel = new Panel(this, UIAssetType.toychest_panel, UIAssetType.toychest_panel_inset);
        this.toyChestPanel.setInitialBounds(worldCamera.viewportWidth, 0f,
                                            worldCamera.viewportWidth * (1f / 3f),
                                            worldCamera.viewportHeight);

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
        super.update(dt);

        shitCommands();

        background.update(dt);
        boundary.update(dt);
        pathShaderTimer += dt;

        if (!editMode) {
            hopper.update(dt);
        }

        collisionManager.solve(dt);
        for (int i = balls.size -1; i >= 0; i--){
            Ball b = balls.get(i);
            b.update(dt);
            if (b.isOffscreen()) {
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

        toyChestPanel.update(dt);
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            toyChestPanel.toggle(worldCamera);
        }

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

        batch.setProjectionMatrix(shaker.getCombinedMatrix());
        batch.begin();
        background.render(batch);
        batch.end();
        // NOTE: this has to be outside of batch begin/end because reasons
        renderBallTrails();

        batch.setProjectionMatrix(shaker.getCombinedMatrix());
        batch.begin();
        {
            if (editMode) boundary.renderEditMode(batch);
            boundary.render(batch);
            particle.renderBackgroundParticles(batch);
            balls.forEach(ball -> ball.render(batch));
            if (editMode) gameObjects.forEach(x -> x.renderEditModeRadius(batch));
            gameObjects.forEach(x -> x.render(batch));
            particle.renderForegroundParticles(batch);

            if (!editMode) hopper.render(batch);
            toyChestPanel.render(batch);

        }

        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            hud.render(batch);
        }
        batch.end();
    }

    private void renderBallTrails() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        // normal blendy
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        // vector-ish blendy
//        Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_COLOR, GL20.GL_ONE, GL20.GL_SRC_ALPHA, GL20.GL_ZERO);
        assets.ballTrailShader.begin();
        {
            switch(game.artPack){
                case a:
                    game.assets.crossHatchGradientTexture.bind(0);
                    break;
                case b:
                    game.assets.pixel.bind(0);
                    break;
                case c:
                    game.assets.pixel.bind(0);
                    break;
                case d:
                    Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_COLOR, GL20.GL_ONE, GL20.GL_SRC_ALPHA, GL20.GL_ZERO);
                    game.assets.ballTrailTexture.bind(0);
                    break;
            }


            assets.ballTrailShader.setUniformMatrix("u_projTrans", game.getScreen().shaker.getCombinedMatrix());
            assets.ballTrailShader.setUniformi("u_texture", 0);
            assets.ballTrailShader.setUniformf("u_time", pathShaderTimer);

            balls.forEach(Ball::renderTrailMesh);
        }
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        assets.ballTrailShader.end();
    }

    private void shitCommands() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            game.artPack = game.artPack.getNext();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            for (int i  = 0; i < 20; i++) {
                Ball ball = new Ball(this, 5f);
                ball.initialize(worldCamera.viewportWidth / 2f + MathUtils.random(-10f, 10f),
                                worldCamera.viewportHeight - 100f + MathUtils.random(-10f, 10f),
                                MathUtils.random(-100f, 100f), MathUtils.random(-5f, -50f));
                balls.add(ball);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            editMode = !editMode;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            balls.clear();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.setScreen(new WinnerScreen(game));
        }
    }
}
