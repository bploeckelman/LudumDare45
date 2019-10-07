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
import lando.systems.ld45.audio.AudioManager;
import lando.systems.ld45.backgrounds.*;
import lando.systems.ld45.collision.CollisionManager;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.objects.Boundary;
import lando.systems.ld45.objects.GameObject;
import lando.systems.ld45.objects.Hopper;
import lando.systems.ld45.ui.Panel;
import lando.systems.ld45.ui.ToyChestPanel;
import lando.systems.ld45.utils.UIAssetType;

public class GameScreen extends BaseScreen {

    public Background background;
    public Array<GameObject> gameObjects;
    public Array<Ball> balls = new Array<>();


    public Hopper hopper;

    private CollisionManager collisionManager;
    public Boundary boundary;

    private GameHud hud = new GameHud(this);
    private float pathShaderTimer;

    public boolean editMode = false;
    public Vector3 projection = new Vector3();
    public Vector2 mousePosition = new Vector2();

    public Panel toyChestPanel;

    public boolean gameOver = false;


    public GameScreen(Game game, boolean editMode) {
        super(game);
        this.editMode = editMode;
        game.particle.clearAll();
        gameObjects = game.player.gameObjects;

        switch(game.player.artPack){
            case a:
                background = new GraphPaperBackground(this);
                break;
            case b:
                background = new FlatBackground(this);
                break;
            case c:
                background = new PixelBackground(this);
                break;
            case d:
                background = new HexBackground(this);
                break;
        }
        hopper = new Hopper(this);



        this.collisionManager = new CollisionManager(this);
        this.boundary = new Boundary(this);

        this.toyChestPanel = new ToyChestPanel(this, UIAssetType.toychest_panel, UIAssetType.toychest_panel_inset);
        this.toyChestPanel.setInitialBounds(worldCamera.viewportWidth, 0f,
                                            worldCamera.viewportWidth * (1f / 3f),
                                            worldCamera.viewportHeight);

        if (editMode){
            toyChestPanel.show(worldCamera);
            gameOver = true;
        }
//        startGame();
        hopper.reset();
    }

    public void startGame() {
        gameOver = editMode = false;
        toyChestPanel.hide(worldCamera);
        hopper.reset();
    }

    public void editGame() {
        gameOver = false;
        editMode = true;
    }

    public GameObject addObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        return gameObject;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        hud.update(dt);

        if (transitionDelay>0) return;

        // NOTE: shit commands are for debug only
//        shitCommands();

        background.update(dt);
        boundary.update(dt);
        pathShaderTimer += dt;
        projection.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        projection = worldCamera.unproject(projection);

        if (!editMode) {
            hopper.update(dt);
        } else if (!toyChestPanel.isVisible() && !toyChestPanel.isAnimating()){
            boolean isSelected = false;
            for (GameObject gameObject : gameObjects){
                if (gameObject.isSelected) isSelected = true;
            }

            if (projection.x > worldCamera.viewportWidth * .9f && !isSelected){
                toyChestPanel.show(worldCamera);
            }
        }

        if (editMode && toyChestPanel.isVisible() && projection.x < worldCamera.viewportWidth*.5f){
            toyChestPanel.hide(worldCamera);
        }

        collisionManager.solve(dt);
        for (int i = balls.size -1; i >= 0; i--){
            Ball b = balls.get(i);
            b.update(dt);
            if (b.isOffscreen()) {
                balls.removeIndex(i);

                long points = player.cashMultiplier;
                player.addScore(points);
                particle.addPointsParticles(points, b.bounds.x, 10f);
            }
        }

        if (editMode && (!toyChestPanel.isVisible() || toyChestPanel.isAnimating())) {
            projection.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            projection = worldCamera.unproject(projection);
            mousePosition.set(projection.x, projection.y);
        } else {
            mousePosition.set(0, 0);
        }

        gameObjects.forEach(x -> x.update(dt, mousePosition));
        particle.update(dt);


        if (toyChestPanel.isVisible()) {
            toyChestPanel.update(dt);
        }

        if (isGameOver()) {
            endGame();
        }

    }

    public void toggleToyBox() {
        toyChestPanel.toggle(worldCamera);
    }

    public void clearGameObjects() {
        gameObjects.clear();
    }

    private void endGame() {
        gameOver = true;
    }

    public boolean isGameOver() {
        return (balls.size == 0) && (hopper.availableBalls == 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(shaker.getCombinedMatrix());
        batch.begin();
        {
            background.render(batch);
        }
        batch.end();

        // NOTE: this has to be outside of batch begin/end because reasons
        boundary.renderMeshOnly();

        if (player.hasEffectTrails) {
            renderBallTrails();
        }

        batch.setProjectionMatrix(shaker.getCombinedMatrix());
        batch.begin();
        {
            if (editMode) boundary.renderEditMode(batch);

            if (player.hasEffectParticles) {
                particle.renderBackgroundParticles(batch);
            }

            balls.forEach(ball -> ball.render(batch));

            if (editMode) gameObjects.forEach(x -> x.renderEditModeRadius(batch));

            gameObjects.forEach(x -> x.render(batch));

            if (player.hasEffectParticles) {
                particle.renderForegroundParticles(batch);
            }

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
            switch(game.player.artPack){
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
            game.player.artPack = game.player.artPack.getNext();
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

    public void addShake(float amount){
        if (player.hasEffectScreenshake){
            shaker.addDamage(amount);
        }
    }
}
