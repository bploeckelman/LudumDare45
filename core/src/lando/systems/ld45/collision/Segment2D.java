package lando.systems.ld45.collision;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Segment2D {
    public Vector2 start;
    public Vector2 end;
    public Vector2 delta;


    public Segment2D (Vector2 start, Vector2 end){
        this(start.x, start.y, end.x, end.y);
    }

    public Segment2D (float x1, float y1, float x2, float y2){
        this.start = new Vector2(x1, y1);
        this.end = new Vector2(x2, y2);
        this.delta = new Vector2(end).sub(start);
    }

    public float getRotation(){
        return delta.angle();
    }


}
