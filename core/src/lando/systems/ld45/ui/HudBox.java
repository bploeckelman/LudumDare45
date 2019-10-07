package lando.systems.ld45.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Assets;
import lando.systems.ld45.Game;
import lando.systems.ld45.collision.Segment2D;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.AssetType;

public class HudBox  {

    public float width = 10;
    public int align = Align.right;
    public boolean wrap = false;

    private Assets assets = Game.getAssets();

    private Rectangle bounds;
    private Array<Segment2D> boxSegments = new Array<>();
    private float time = 0;

    public Color textColor = Color.BLACK;
    private BitmapFont font;
    private String text;
    private float textY;

    public HudBox(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);

        float left = x;
        float right = left + width;
        float bottom = y;
        float top = bottom + height;

        boxSegments.add(new Segment2D(left, bottom, right, bottom));
        boxSegments.add(new Segment2D(right, bottom, right, top));
        boxSegments.add(new Segment2D(right, top, left, top));
        boxSegments.add(new Segment2D(left, top, left, bottom));
    }

    public void reset(float x, float y, float width, float height) {
        bounds.set(x, y, width, height);

        float left = x;
        float right = left + width;
        float bottom = y;
        float top = bottom + height;

        Segment2D segment = boxSegments.get(0);
        segment.start.set(left, bottom);
        segment.end.set(right, bottom);
        segment.delta.set(segment.end).sub(segment.start);

        segment = boxSegments.get(1);
        segment.start.set(right, bottom);
        segment.end.set(right, top);
        segment.delta.set(segment.end).sub(segment.start);

        segment = boxSegments.get(2);
        segment.start.set(right, top);
        segment.end.set(left, top);
        segment.delta.set(segment.end).sub(segment.start);

        segment = boxSegments.get(3);
        segment.start.set(left, top);
        segment.end.set(left, bottom);
        segment.delta.set(segment.end).sub(segment.start);
    }

    public void update(float dt) {
        time += dt;
    }

    public void render(SpriteBatch batch) {
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);

        if (Game.game.player.artPack == ArtPack.a) {
            renderSimple(batch, AssetType.boundary_line);
        }
        else {
            renderComplex(batch, width * 0.7f);
        }

        if (text != null) {
            font.setColor(textColor);
            GlyphLayout layout = Game.getAssets().layout;
            layout.setText(font, text, Color.BLACK, bounds.width - 10f, align, true);
            font.draw(batch, layout, bounds.x, bounds.y + layout.height / 2f + bounds.height / 2f);
            font.setColor(Color.WHITE);
        }
    }

    private void renderSimple(SpriteBatch batch, AssetType assetType) {
        for (Segment2D segment : boxSegments) {
            batch.draw(Game.getAsset(assetType, time), segment.start.x - width / 2f,
                    segment.start.y - width / 2f, width / 2f, width / 2f,
                    segment.delta.len() + width, width, 1, 1, segment.getRotation());
        }
    }

    private void renderComplex(SpriteBatch batch, float width) {

        float half = width/2;

        AssetType[] types = new AssetType[] {
                AssetType.boundary_line_short_bottom,
                AssetType.boundary_line_short_left,
                AssetType.boundary_line_short_top,
                AssetType.boundary_line_short_right
        };

        if (bounds.width > 400) {
            types[0] = AssetType.boundary_line_long_bottom;
            types[2] = AssetType.boundary_line_long_top;
        }

        if (bounds.height > 200) {
            types[1] = AssetType.boundary_line_long_left;
            types[3] = AssetType.boundary_line_long_right;
        }

        batch.draw(Game.getAsset(types[0], time), bounds.x + half, bounds.y - half, bounds.width - width, width);
        batch.draw(Game.getAsset(types[1], time), bounds.x - half, bounds.y + half, width, bounds.height - width);
        batch.draw(Game.getAsset(types[2], time), bounds.x + half, bounds.y + bounds.height - half, bounds.width - width, width);
        batch.draw(Game.getAsset(types[3], time), bounds.x + bounds.width - half, bounds.y + half, width, bounds.height - width);

        float capSize = width * 3 / 2.5f;
        half = capSize / 2;

        // corners
        batch.draw(Game.getAsset(AssetType.boundary_line_corner, time), bounds.x - half, bounds.y - half, capSize, capSize);
        batch.draw(Game.getAsset(AssetType.boundary_line_corner, time), bounds.x - half, bounds.y + bounds.height - half, capSize, capSize);
        batch.draw(Game.getAsset(AssetType.boundary_line_corner, time), bounds.x + bounds.width - half, bounds.y - half, capSize, capSize);
        batch.draw(Game.getAsset(AssetType.boundary_line_corner, time), bounds.x + bounds.width - half, bounds.y + bounds.height - half, capSize, capSize);
    }

    public void setText(String text) {
        font = Game.getCurrentFont();
        if (this.text != text) {
            this.text = text;
            textY = bounds.y + bounds.height - ((bounds.height - font.getCapHeight())/2);
        }
    }
}
