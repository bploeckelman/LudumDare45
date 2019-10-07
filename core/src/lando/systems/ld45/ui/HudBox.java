package lando.systems.ld45.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.Assets;
import lando.systems.ld45.Config;
import lando.systems.ld45.Game;
import lando.systems.ld45.collision.Segment2D;
import lando.systems.ld45.ui.typinglabel.TypingLabel;
import lando.systems.ld45.utils.AssetType;

public class HudBox  {

    public float width = 10;

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

    public void update(float dt) {
        time += dt;
    }

    public void render(SpriteBatch batch) {
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);

        for (Segment2D segment : boxSegments) {
            batch.draw(Game.getAsset(AssetType.boundary_line, time), segment.start.x - width / 2f,
                    segment.start.y - width / 2f, width / 2f, width / 2f,
                    segment.delta.len() + width, width, 1, 1, segment.getRotation());
        }

        if (text != null) {
            font.setColor(textColor);
            font.draw(batch, text, bounds.x, textY, bounds.width - 10, Align.right, false);
            font.setColor(Color.WHITE);
        }
    }

    public void setText(String text) {
        font = Game.getCurrentFont();
        if (this.text != text) {
            this.text = text;
            textY = bounds.y + bounds.height - ((bounds.height - font.getCapHeight())/2);
        }
    }
}
