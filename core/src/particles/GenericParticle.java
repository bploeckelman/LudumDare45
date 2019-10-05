package particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class GenericParticle implements Pool.Poolable{
    public enum OriginType {CENTER, CUSTOM}

    TextureRegion region;
    float startWidth;
    float endWidth;
    float startHeight;
    float endHeight;
    Vector2 startPos;
    Vector2 pos;
    Vector2 vel;
    Vector2 acc;
    Vector2 tar;
    float accDamping;
    OriginType originType;
    float originX;
    float originY;
    Color startColor;
    Color endColor;
    float startRotation;
    float endRotation;
    float ttl;
    float totalTtl;
    boolean drawDropShadow;

    public GenericParticle() {
        startPos = new Vector2();
        pos = new Vector2();
        vel = new Vector2();
        acc = new Vector2();
        tar = new Vector2();
        accDamping = 1;
        startColor = new Color();
        endColor = new Color();
    }

    @Override
    public void reset() {
        drawDropShadow = false;
    }

    public void init(TextureRegion region,
                     float startWidth, float endWidth,
                     float startHeight, float endHeight,
                     float x, float y,
                     float vx, float vy,
                     float ax, float ay,
                     float accDamping,
                     OriginType originType, float originX, float originY,
                     float sR, float sG, float sB, float sA,
                     float eR, float eG, float eB, float eA,
                     float startRotation, float endRotation,
                     float ttl) {
        this.region = region;
        this.startWidth = startWidth;
        this.endWidth = endWidth;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.startPos.set(x, y);
        this.pos.set(x, y);
        this.vel.set(vx, vy);
        this.acc.set(ax, ay);
        this.tar.set(0f, 0f);
        this.accDamping = accDamping;
        this.originType = originType;
        this.originX = originX;
        this.originY = originY;
        this.startColor.set(sR, sG, sB, sA);
        this.endColor.set(eR, eG, eB, eA);
        this.startRotation = startRotation;
        this.endRotation = endRotation;
        this.ttl = ttl;
        this.totalTtl = ttl;
    }

    public void init(TextureRegion region,
                     float startWidth, float endWidth,
                     float startHeight, float endHeight,
                     float x, float y,
                     float tx, float ty,
                     float vx, float vy,
                     float ax, float ay,
                     float accDamping,
                     OriginType originType, float originX, float originY,
                     float sR, float sG, float sB, float sA,
                     float eR, float eG, float eB, float eA,
                     float startRotation, float endRotation,
                     float ttl) {
        this.region = region;
        this.startWidth = startWidth;
        this.endWidth = endWidth;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.startPos.set(x, y);
        this.pos.set(x, y);
        this.vel.set(vx, vy);
        this.acc.set(ax, ay);
        this.tar.set(tx, ty);
        this.accDamping = accDamping;
        this.originType = originType;
        this.originX = originX;
        this.originY = originY;
        this.startColor.set(sR, sG, sB, sA);
        this.endColor.set(eR, eG, eB, eA);
        this.startRotation = startRotation;
        this.endRotation = endRotation;
        this.ttl = ttl;
        this.totalTtl = ttl;
    }

    public void setDrawDropShadow(boolean dropShadow) {
        drawDropShadow = dropShadow;
    }

    public void update(float dt){
        ttl -= dt;
        vel.add(acc.x * dt, acc.y * dt);
        pos.add(vel.x * dt, vel.y * dt);

        acc.scl(accDamping);
        if (acc.epsilonEquals(0.0f, 0.0f, 0.1f)) {
            acc.set(0f, 0f);
        }
    }

    public void render(SpriteBatch batch) {
        float t = MathUtils.clamp(1f - ttl/totalTtl, 0f, 1f);

        float r = MathUtils.lerp(startColor.r, endColor.r, t);
        float g = MathUtils.lerp(startColor.g, endColor.g, t);
        float b = MathUtils.lerp(startColor.b, endColor.b, t);
        float a = MathUtils.lerp(startColor.a, endColor.a, t);

        r = MathUtils.clamp(r, 0, 1f);
        g = MathUtils.clamp(g, 0, 1f);
        b = MathUtils.clamp(b, 0, 1f);
        a = MathUtils.clamp(a, 0, 1f);

        float rotation = MathUtils.lerp(startRotation, endRotation, t);
        float width = MathUtils.lerp(startWidth, endWidth, t);
        float height = MathUtils.lerp(startHeight, endHeight, t);

        float x = pos.x;
        float y = pos.y;

        if (originType == OriginType.CENTER) {
            originX = width/2f;
            originY = height/2f;
        }

        x -= originX;
        y -= originY;

        if (drawDropShadow){
            batch.setColor(0, 0, 0, a);
            batch.draw(region, x-1, y-1, originX, originY, width, height, 1f, 1f, rotation);
        }

        batch.setColor(r, g, b, a);
        batch.draw(region, x, y, originX, originY, width, height, 1f, 1f, rotation);
        batch.setColor(Color.WHITE);

    }
}
