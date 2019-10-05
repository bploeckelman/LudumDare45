package lando.systems.ld45.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld45.Assets;
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
}
