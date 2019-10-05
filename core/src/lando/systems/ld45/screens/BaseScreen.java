package lando.systems.ld45.screens;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.Assets;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.audio.AudioManager;

public abstract class BaseScreen extends InputAdapter {
    public final Game game;
    public final Assets assets;
    public final AudioManager audio;
    public final SpriteBatch batch;

    public OrthographicCamera worldCamera;
    public OrthographicCamera hudCamera;

    public BaseScreen(Game game) {
        this.game = game;
        this.assets = game.assets;
        this.audio = game.audio;
        this.batch = game.assets.batch;

        this.worldCamera = new OrthographicCamera();
        this.worldCamera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        this.worldCamera.update();

        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Config.gameWidth, Config.gameHeight);
        this.hudCamera.update();
    }

    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);

    public void dispose() {

    }
}
