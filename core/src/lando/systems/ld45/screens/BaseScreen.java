package lando.systems.ld45.screens;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Assets;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.audio.AudioManager;
import lando.systems.ld45.particles.ParticleManager;
import lando.systems.ld45.state.PlayerState;
import lando.systems.ld45.ui.UIElement;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.screenshake.ScreenShakeCameraController;

public abstract class BaseScreen extends InputAdapter {
    public final Game game;
    public final Assets assets;
    public final AudioManager audio;
    public final SpriteBatch batch;
    public final ParticleManager particle;

    public OrthographicCamera worldCamera;
    public OrthographicCamera hudCamera;
    public ScreenShakeCameraController shaker;


//    public ArtPack artPack = ArtPack.a;

    public PlayerState player;
    protected float transitionDelay;

    public Array<UIElement> uiElements = new Array<>();

    public BaseScreen(Game game) {
        this.game = game;
        this.assets = game.assets;
        this.audio = game.audio;
        this.batch = game.assets.batch;
        this.particle = game.particle;
        this.player = game.player;

        this.worldCamera = new OrthographicCamera();
        this.worldCamera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        this.worldCamera.update();

        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        this.hudCamera.update();
        this.shaker = new ScreenShakeCameraController(worldCamera);
        transitionDelay = 1f;

    }

    public void addUIElement(UIElement element) {
        uiElements.add(element);
    }

    public void update(float dt) {
        transitionDelay -= dt;
        uiElements.forEach(x -> x.update(dt));
        shaker.update(dt);
    }

    public abstract void render(SpriteBatch batch);

    protected void renderUIElements(SpriteBatch batch) {
        uiElements.forEach(x -> x.render(batch));
    }

    public void dispose() {

    }
}
