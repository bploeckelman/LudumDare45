package lando.systems.ld45.backgrounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;


public class Collision {

    public Vector2 pos;
    public float intensity;
    public boolean finished;

    public float explosionSize;
    Interpolation interpolation = Interpolation.exp5Out;
    float ttl;
    public float maxTTL;
    public Color color;

    public Collision(float x, float y, float size, float maxTTL, Color color) {
        this.explosionSize = size;
        this.finished = false;
        this.pos = new Vector2(x, y);
        this.ttl = 0;
        this.maxTTL = maxTTL;
        this.color = new Color(color.r * .5f, color.g * .5f, color.b * .5f, color.a);
    }

    public void update(float dt) {
        ttl += dt;
        this.intensity = interpolation.apply(1, 0, ttl/maxTTL);
        if (ttl > maxTTL) finished = true;
    }


}
