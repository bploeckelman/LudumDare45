package lando.systems.ld45.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld45.Game;

public class UpgradeButton {

    enum State { disabled, normal, hovered, pressed }

    UpgradePanel panel;
    Rectangle bounds;
    String text;
    String description;
    State state;

    public UpgradeButton(UpgradePanel panel, String text, String description) {
        this.panel = panel;
        this.bounds = new Rectangle();
        this.state = State.disabled;
        this.text = text;
        this.description = description;
    }

    public void update(float dt) {
        // TODO: handle clicks and such?
    }

    public void render(SpriteBatch batch) {
        final float hpad = 10f;
        final float vpad = 20f;
        batch.draw(Game.getAssets().whitePixel, bounds.x + hpad, bounds.y + vpad,
                   bounds.width - 2f * hpad, bounds.height - 2f * vpad);

        GlyphLayout layout = Game.getAssets().layout;
        layout.setText(Game.getCurrentFont(), text, Color.BLACK, bounds.width, Align.center, false);
        Game.getCurrentFont().draw(batch, layout, bounds.x, bounds.y + bounds.height / 2f + layout.height / 2f);
        // TODO: cost underneath
    }

}
