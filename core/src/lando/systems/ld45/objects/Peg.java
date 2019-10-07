package lando.systems.ld45.objects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.AssetType;

public class Peg extends GameObject {

    public static Peg getPeg(GameScreen screen, int level, int gfxPack) {
        return new Peg(screen, level, gfxPack, 16);
    }

    public Peg(GameScreen screen, int level, int gfxPack, float size) {
        super(screen, screen.assets.assetMap.get(screen.game.player.artPack).get(AssetType.peg).getKeyFrames()[0], size, size);
        setCircleBounds(-10, 10, size/2f);
    }

    @Override
    public void update(float dt, Vector2 mousePosition) {
        super.update(dt, mousePosition);
        setCircleBounds(pos.x, pos.y, size.x/2f);
        image = screen.assets.assetMap.get(screen.game.player.artPack).get(AssetType.peg).getKeyFrame(artAccum);
    }
}
