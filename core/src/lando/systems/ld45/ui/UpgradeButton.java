package lando.systems.ld45.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld45.Game;

public class UpgradeButton extends Button {

    enum State { disabled, normal, hovered, pressed }

    UpgradePanel panel;
    Rectangle bounds;
    String text;
    String description;
    State state;

    public UpgradeButton(UpgradePanel panel, String text, String description) {
        super(panel.screen, panel.screen.worldCamera, 0f, 0f, 0f, 0f);
        this.panel = panel;
        this.bounds = new Rectangle();
        this.state = State.disabled;
        this.text = text;
        this.description = description;
        this.setText(text);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setText(text);
    }

    public void setHudBox(float x, float y, float width, float height) {
        float hpad = 10f;
        float vpad = 20f;
        box.reset(x  + hpad, y + vpad, width - 2f * hpad, height - 2f * vpad);
        bounds.set(x + hpad, y + vpad, width - 2f * hpad, height - 2f * vpad);
    }


}
