package lando.systems.ld45;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld45.accessors.*;
import lando.systems.ld45.audio.AudioManager;
import lando.systems.ld45.particles.ParticleManager;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.screens.TitleScreen;
import lando.systems.ld45.state.PlayerState;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.AssetType;

public class Game extends ApplicationAdapter {

	public static Game game;

	public Pool<Vector2> vector2Pool = Pools.get(Vector2.class);
	public Pool<Color>   colorPool   = Pools.get(Color.class);

	public PlayerState player = new PlayerState();

	public ParticleManager particle;
	public AudioManager audio;
	public Assets assets;
	public TweenManager tween;

	private BaseScreen currentScreen;
	private BaseScreen nextScreen;
	private MutableFloat transitionPercent;
	private FrameBuffer transitionFBO;
	private FrameBuffer originalFBO;
	Texture originalTexture;
	Texture transitionTexture;
	ShaderProgram transitionShader;
	boolean transitioning;

	private float speedModifier = 1;

	public Game() {
		this.game = this;
	}

	@Override
	public void create () {

		transitionPercent = new MutableFloat(0);
		transitionFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Config.gameWidth, Config.gameHeight, false);
		transitionTexture = transitionFBO.getColorBufferTexture();


		originalFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Config.gameWidth, Config.gameHeight, false);
		originalTexture = originalFBO.getColorBufferTexture();

		transitioning = false;

		if (tween == null) {
			tween = new TweenManager();
			Tween.setWaypointsLimit(4);
			Tween.setCombinedAttributesLimit(4);
			Tween.registerAccessor(Color.class, new ColorAccessor());
			Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
			Tween.registerAccessor(Vector2.class, new Vector2Accessor());
			Tween.registerAccessor(Vector3.class, new Vector3Accessor());
			Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());
		}

		if (assets == null) {
			assets = new Assets();
		}

		if (audio == null) {
			audio = new AudioManager(false, this);
		}

		if (particle == null) {
			particle = new ParticleManager(assets);
		}

        Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("images/ui-cursor-hand.png")), 0, 0);
        Gdx.graphics.setCursor(customCursor);

		setScreen(new TitleScreen(this));
	}

	public void setSpeedModifier(float speedModifier) {
		this.speedModifier = speedModifier;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float dt = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f) * speedModifier;

		audio.update(dt);
		tween.update(dt);
		currentScreen.update(dt);

		if (nextScreen != null) {
			nextScreen.update(dt);
			transitionFBO.begin();
			nextScreen.render(assets.batch);
			transitionFBO.end();

			originalFBO.begin();
			currentScreen.render(assets.batch);
			originalFBO.end();

			assets.batch.setShader(transitionShader);
			assets.batch.begin();
			originalTexture.bind(1);
			transitionShader.setUniformi("u_texture1", 1);
			transitionTexture.bind(0);
			transitionShader.setUniformf("u_percent", transitionPercent.floatValue());
			assets.batch.setColor(Color.WHITE);
			assets.batch.draw(transitionTexture, 0,0, Config.gameWidth, Config.gameHeight);
			assets.batch.end();
			assets.batch.setShader(null);
		} else {
			currentScreen.render(assets.batch);
		}
	}

	@Override
	public void dispose () {
	}

	public void setScreen(BaseScreen screen) {
		setScreen(screen, null, 1f);
	}

	public void setScreen(final BaseScreen newScreen, ShaderProgram transitionType, float transitionSpeed) {
		if (nextScreen != null) return;
		if (transitioning) return; // only want one transition
		if (currentScreen == null) {
			currentScreen = newScreen;
		} else {
			transitioning = true;
			if (transitionType == null) {
				transitionShader = assets.randomTransitions.get(MathUtils.random(assets.randomTransitions.size - 1));
			} else {
				transitionShader = transitionType;
			}
			transitionPercent.setValue(0);
			Timeline.createSequence()
					.pushPause(.1f)
					.push(Tween.call(new TweenCallback() {
						@Override
						public void onEvent(int i, BaseTween<?> baseTween) {
							nextScreen = newScreen;
						}
					}))
					.push(Tween.to(transitionPercent, 1, transitionSpeed)
							.target(1))
					.push(Tween.call(new TweenCallback() {
						@Override
						public void onEvent(int i, BaseTween<?> baseTween) {
							currentScreen = nextScreen;
							nextScreen = null;
							transitioning = false;
						}
					}))
					.start(tween);
		}
	}

	public BaseScreen getScreen() {
		return currentScreen;
	}

	public static Assets getAssets() {
		return game.assets;
	}

	public static TextureRegion getAsset(AssetType assetType, float time) {
		return game.assets.assetMap.get(game.player.artPack).get(assetType).getKeyFrame(time);
	}

	public static BitmapFont getCurrentFont() {
		return game.assets.fontMap.get(game.player.artPack);
	}

	public static boolean isFancyPantsPack() {
		return game.player.artPack == ArtPack.d;
	}
}
