package lando.systems.ld45;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class Game extends ApplicationAdapter {

	public Pool<Vector2> vector2Pool = Pools.get(Vector2.class);
	public Pool<Color>   colorPool   = Pools.get(Color.class);

	public ParticleManager particle;
	public AudioManager audio;
	public Assets assets;
	public TweenManager tween;

	BaseScreen currentScreen;
	
	@Override
	public void create () {
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

		setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float dt = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);
		audio.update(dt);
		tween.update(dt);
		currentScreen.update(dt);

		currentScreen.render(assets.batch);
	}
	
	@Override
	public void dispose () {
	}

	public void setScreen(BaseScreen screen) {
		this.currentScreen = screen;
	}

	public BaseScreen getScreen() {
		return currentScreen;
	}

}
