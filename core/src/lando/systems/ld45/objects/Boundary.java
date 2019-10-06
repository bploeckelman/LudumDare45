package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.collision.Segment2D;
import lando.systems.ld45.screens.GameScreen;

public class Boundary {
    public Array<Segment2D> segments;
    GameScreen screen;

    public Boundary(GameScreen screen){
        this.screen = screen;
        segments = new Array<>();
        segments.add(new Segment2D(0, 90, 0, screen.worldCamera.viewportHeight));
        segments.add(new Segment2D(0, screen.worldCamera.viewportHeight, screen.worldCamera.viewportWidth, screen.worldCamera.viewportHeight));
        segments.add(new Segment2D(screen.worldCamera.viewportWidth, screen.worldCamera.viewportHeight, screen.worldCamera.viewportWidth, 90));
        segments.add(new Segment2D(screen.worldCamera.viewportWidth, 90, screen.worldCamera.viewportWidth/2f + 50, 20));
        segments.add(new Segment2D(screen.worldCamera.viewportWidth/2f - 50, 20, 0, 90));
    }

    public void render(SpriteBatch batch){
        float width = 2;
        for (Segment2D segment : segments){
            batch.draw(screen.assets.whitePixel, segment.start.x - width/2f, segment.start.y - width/2f, width/2f, width/2f, segment.delta.len() + width, width, 1, 1, segment.getRotation());
        }
    }
}
