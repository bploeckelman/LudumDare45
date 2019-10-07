package lando.systems.ld45.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.effects.BallPath;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.AssetType;
import lando.systems.ld45.utils.Utils;

public class Ball {

    public static float MAXSPEED = 500f;

    public Circle bounds = new Circle();
    public Vector2 vel = new Vector2();
    public Vector2 pos = new Vector2();
    public Color color = new Color();
    public TextureRegion keyframe;
    public BaseScreen screen;
    public float dtLeft;

    public BallPath path;
    private float accum;

    public float lastExposion =0;

    public Ball(BaseScreen screen, float radius) {
        this(screen, radius, Utils.getRandomHSVColor());
    }

    public Ball(BaseScreen screen, float radius, Color color) {
        // TODO: tweak Utils.getRandomColor() to exclude dumb colors
        if (color == Color.BLACK || color == Color.CLEAR) {
            color = Color.SALMON;
        }

        this.color.set(color);

        this.bounds.set(0, 0, radius);
        this.screen = screen;

        this.keyframe = screen.assets.assetMap.get(screen.game.player.artPack).get(AssetType.ball).getKeyFrames()[0];
        this.dtLeft = 0;

        this.path = new BallPath(screen.game, this.color);
        if (screen.game.player.artPack == ArtPack.a){
            this.color.set(Color.GRAY);
            this.path.setColor(Color.GRAY);
        }
        if (screen.game.player.artPack == ArtPack.d){
            this.color.set(Color.WHITE);
        }
    }

    public void initialize(float positionX, float positionY, float velocityX, float velocityY) {
        bounds.set(positionX, positionY, bounds.radius);
        pos.set(positionX,positionY);
        vel.set(velocityX, velocityY);
    }

    public void initialize(Vector2 position, Vector2 velocity) {
        initialize(position.x, position.y, velocity.x, velocity.y);
    }

    public void update(float dt) {
        lastExposion = Math.max(0, lastExposion-dt);
        accum += dt;
        vel.y -= Config.gravity * dt;
        vel.scl(.999f);

        if (vel.len() > MAXSPEED){
            vel.nor().scl(MAXSPEED);
        }
        if (vel.len() < 10){
            if (vel.epsilonEquals(0,0)) vel.set(0, 1);
            vel.nor().scl(10f);

        }

        pos.set(bounds.x, bounds.y);

        accum += dt;
        path.update(this, dt);

        keyframe = screen.assets.assetMap.get(screen.game.player.artPack).get(AssetType.ball).getKeyFrame(accum);
        // making trails appear less if the vel is smaller. probably better way to do this.
        if (vel.len2() > MathUtils.random(200000)) {
            screen.particle.addBallTrailingParticle(this, screen.game.player.artPack);
        }
    }

    public void renderTrailMesh() {
        path.renderMeshOnly();
    }

    public void render(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(keyframe, bounds.x - bounds.radius, bounds.y - bounds.radius, 2f * bounds.radius, 2f * bounds.radius);
        batch.setColor(Color.WHITE);
    }

    public boolean isOffscreen() {
        return bounds.y < -bounds.radius * 10;
    }

    public void causeExplosion(int size){
        if (lastExposion <= 0){
            GameScreen gameScreen = (GameScreen)screen;
            gameScreen.background.addCollision(bounds.x, bounds.y, size, .9f, path.color);
            lastExposion = .1f;
        }
    }
}
