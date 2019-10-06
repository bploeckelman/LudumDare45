package lando.systems.ld45.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld45.Assets;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.AssetType;
import lando.systems.ld45.utils.Utils;


public class ParticleManager {

    public Assets assets;
    private final Array<GenericParticle> activeBackgroundParticles = new Array<>(false, 100);
    private final Array<GenericParticle> activeForegroundParticles = new Array<>(false, 256);
    private final Array<GenericParticle> activeTextParticles = new Array<>(false, 1000);
    private final Pool<GenericParticle> particlePool = Pools.get(GenericParticle.class, 3000);

    private Vector3 project = new Vector3();

    public ParticleManager(Assets assets) {
        this.assets = assets;
    }

    public void update(float dt) {
        for (int i = activeBackgroundParticles.size - 1; i >= 0; i--) {
            GenericParticle part = activeBackgroundParticles.get(i);
            part.update(dt);
            if (part.ttl <= 0) {
                activeBackgroundParticles.removeIndex(i);
                particlePool.free(part);
            }
        }

        for (int i = activeForegroundParticles.size -1; i >=0; i--){
            GenericParticle part = activeForegroundParticles.get(i);
            part.update(dt);
            if (part.tar == new Vector2(0f, 0f)) {
                if (part.pos.epsilonEquals(part.tar)) {
                    activeForegroundParticles.removeIndex(i);
                    particlePool.free(part);
                } else {
                    float interp = 1f - (part.ttl / part.totalTtl);
                    part.pos.set(MathUtils.lerp(part.startPos.x, part.tar.x, interp),
                            MathUtils.lerp(part.startPos.y, part.tar.y, interp));
                }
            }
            else if (part.ttl <= 0 && activeForegroundParticles.size > 0) {
                activeForegroundParticles.removeIndex(i);
                particlePool.free(part);
            }
        }

        for (int i = activeTextParticles.size - 1; i >= 0; i--) {
            GenericParticle part = activeTextParticles.get(i);
            part.update(dt);
            if (part.ttl <= 0) {
                activeTextParticles.removeIndex(i);
                particlePool.free(part);
            }
        }
    }

    public void renderBackgroundParticles(SpriteBatch batch) {
        activeBackgroundParticles.forEach(particle -> particle.render(batch));
    }

    public void renderForegroundParticles(SpriteBatch batch) {
        activeForegroundParticles.forEach(particle -> particle.render(batch));
        activeTextParticles.forEach(particle -> particle.render(batch));
    }

    public void addCloudParticles (float x, float y, float ttl) {
        int sparks = 5;
        for (int i = 0; i < sparks; i++){
            float vx = MathUtils.random(-1f, 1f);
            float vy = MathUtils.random(0, 1f);
            GenericParticle part = particlePool.obtain();
            part.init(assets.whitePixel, 10, 50, 10, 50,
                    x, y, vx, vy, 0, 0,
                    1f, GenericParticle.OriginType.CENTER, 0, 0,
                    1, 1, 1, 1,
                    1, 1, 1, 0f,
                    0, 0, ttl
            );
            activeForegroundParticles.add(part);
        }
    }

    public void addBallTrailingParticle(Ball ball, ArtPack artPack) {
        GenericParticle part = particlePool.obtain();
        float angle = MathUtils.random(360);
        float speed = MathUtils.random(20);
        float vx = MathUtils.cosDeg(angle) * speed;
        float vy = MathUtils.sinDeg(angle) * speed;
        float size = MathUtils.random(10f, 20f);
        float randomcolorFade = MathUtils.random(-.2f, .2f);
        int keyFrame = MathUtils.random(0, 2);
        TextureRegion particleStar = assets.assetMap.get(artPack).get(AssetType.particle_star).getKeyFrame(keyFrame);
        part.init(particleStar, size, size, size, size,
                ball.pos.x, ball.pos.y, vx, vy, 0, 0,
                0, GenericParticle.OriginType.CENTER, 0, 0,
                ball.color.r + randomcolorFade, ball.color.g+randomcolorFade, ball.color.b+randomcolorFade, 1,
                .5f, .5f, .5f, 0f,
                0, angle, 3f
        );
        activeBackgroundParticles.add(part);
    }

    Color tempColor = new Color();
    public void addFireworkExplosion(float x, float y) {
        int sparks = 1000;
        Utils.hsvToRgb(MathUtils.random(.5f, 1f), 1f, 1f, tempColor); // Pick a color between cyan (.5) and red (1f)
        for (int i = 0; i < sparks; i++) {
            GenericParticle part = particlePool.obtain();
            float angle = MathUtils.random(360);
            float speed = MathUtils.random(80);
            float vx = MathUtils.cosDeg(angle) * speed;
            float vy = MathUtils.sinDeg(angle) * speed;
            float startSize = MathUtils.random(1f, 5f);
            float endSize = 1f;
            float randomcolorFade = MathUtils.random(-.2f, .2f);
            part.init(assets.whiteCircle, startSize, endSize, startSize, endSize,
                    x, y, vx, vy, 0, -50,
                    1f, GenericParticle.OriginType.CENTER, 0, 0,
                    tempColor.r + randomcolorFade, tempColor.g+randomcolorFade, tempColor.b+randomcolorFade, 1,
                    .5f, .5f, .5f, .2f,
                    0, 0, 3f
            );
            activeForegroundParticles.add(part);
        }
    }

    public void addBallColisionParticle(Ball ball1, Ball ball2) {
        float ballVelocityScale = 10f;
        int numParticles = 30;
        for (int i = 0; i < numParticles / 2; i++){
            GenericParticle part;

            part = particlePool.obtain();
            part.init(assets.whitePixel,
                    1f, 5f,
                    1f, 5f,
                    ball1.bounds.x + MathUtils.random(-ball1.bounds.radius, ball1.bounds.radius),
                    ball1.bounds.y + MathUtils.random(-ball1.bounds.radius, ball1.bounds.radius),
                    MathUtils.random(-5f * ball1.bounds.radius, 5f * ball1.bounds.radius),
                    MathUtils.random(-5f * ball1.bounds.radius, 5f * ball1.bounds.radius),
                    ballVelocityScale * ball1.vel.x,
                    ballVelocityScale * ball1.vel.y, 0,
                    GenericParticle.OriginType.CENTER,
                    0, 0,
                    ball1.color.r, ball1.color.g, ball1.color.b, 0.5f,
                    ball1.color.r, ball1.color.g, ball1.color.b, 0.0f,
                    0, 1000f,
                    MathUtils.random(1f,2f));
            activeForegroundParticles.add(part);

            part = particlePool.obtain();
            part.init(assets.whitePixel,
                    1f, 5f,
                    1f, 5f,
                    ball2.bounds.x + MathUtils.random(-ball2.bounds.radius, ball2.bounds.radius),
                    ball2.bounds.y + MathUtils.random(-ball2.bounds.radius, ball2.bounds.radius),
                    MathUtils.random(-5f * ball2.bounds.radius, 5f * ball2.bounds.radius),
                    MathUtils.random(-5f * ball2.bounds.radius, 5f * ball2.bounds.radius),
                    ballVelocityScale * ball2.vel.x,
                    ballVelocityScale * ball2.vel.y, 0,
                    GenericParticle.OriginType.CENTER,
                    0, 0,
                    ball2.color.r, ball2.color.g, ball2.color.b, 0.5f,
                    ball2.color.r, ball2.color.g, ball2.color.b, 0.0f,
                    0, 1000f,
                    MathUtils.random(1f,2f));
            activeForegroundParticles.add(part);
        }
    }

    public void addPointsParticles(long points, float x, float y) {
        // create a particle for each number in 'points'
        Color startColor = Color.GOLD;
        Color endColor = Color.BROWN;
        float size = 15f;
        float yVel = 60f;
        String pointsStr = Long.toString(points, 10);
        for (int i = 0; i < pointsStr.length(); ++i) {
            GenericParticle part = particlePool.obtain();
            TextureRegion texture = assets.fontPoints.get(Character.digit(pointsStr.charAt(i), 10)).getKeyFrames()[0];
            part.init(texture,
                      size, 3f,
                      size, 3f,
                      x + i * (size-3f), y, 0f, yVel,
                      1, 1, 0.5f,
                      GenericParticle.OriginType.CENTER, 0, 0,
                      startColor.r, startColor.g, startColor.b, 1.0f,
                      endColor.r,   endColor.g,   endColor.b  , 0.25f,
                      0f, 0f, 2f);
            part.setDrawDropShadow(true);
            activeTextParticles.add(part);
        }
    }

}
