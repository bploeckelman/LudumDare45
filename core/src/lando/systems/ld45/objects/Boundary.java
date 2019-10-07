package lando.systems.ld45.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.collision.Segment2D;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.AssetType;


public class Boundary {
    public Array<Segment2D> segments;
    GameScreen screen;
    public Rectangle buildArea;
    float margin = 10f;
    float accum;

    public Boundary(GameScreen screen){
        this.screen = screen;
        segments = new Array<>();
        segments.add(new Segment2D(margin, 90, margin, screen.worldCamera.viewportHeight - margin));
        segments.add(new Segment2D(margin, screen.worldCamera.viewportHeight - margin, screen.worldCamera.viewportWidth-margin, screen.worldCamera.viewportHeight - margin));
        segments.add(new Segment2D(screen.worldCamera.viewportWidth - margin, screen.worldCamera.viewportHeight- margin, screen.worldCamera.viewportWidth - margin, 90));
        segments.add(new Segment2D(screen.worldCamera.viewportWidth- margin, 90, screen.worldCamera.viewportWidth/2f + 50, 20));
        segments.add(new Segment2D(screen.worldCamera.viewportWidth/2f - 50, 20, margin, 90));
        buildArea = new Rectangle(90, 180, (int)screen.worldCamera.viewportWidth-180, (int)screen.worldCamera.viewportHeight-270);
    }

    public void update(float dt){
        accum += dt;
    }

    public void render(SpriteBatch batch){
        float width = 20;
        for (Segment2D segment : segments){
            batch.draw(screen.assets.assetMap.get(screen.game.player.artPack).get(AssetType.boundary_line).getKeyFrame(accum), segment.start.x, segment.start.y - width/2f, 0, width/2f, segment.delta.len(), width, 1, 1, segment.getRotation());
        }
    }

    public void renderEditMode(SpriteBatch batch){
        batch.setColor(1f, 1f, 1f, .8f);
        screen.assets.buildArea.draw(batch, buildArea.x, buildArea.y, buildArea.width, buildArea.height);
        batch.setColor(Color.WHITE);
    }
}
