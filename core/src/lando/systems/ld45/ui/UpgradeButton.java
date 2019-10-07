package lando.systems.ld45.ui;

import com.badlogic.gdx.math.Rectangle;

public class UpgradeButton extends Button {

    UpgradePanel panel;
    Rectangle collisionBounds;
    UpgradeProps props;

    public UpgradeButton(UpgradePanel panel, UpgradeProps upgradeProps) {
        super(panel.screen, panel.screen.worldCamera, 0f, 0f, 0f, 0f);
        this.panel = panel;
        this.collisionBounds = new Rectangle();
        this.props = upgradeProps;
        this.setText(props.text);
    }

    public String getDescription() {
        return props.getCurrentDescription();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setText(props.text);
    }

    public void setHudBox(float x, float y, float width, float height) {
        float hpad = 10f;
        float vpad = 20f;
        box.reset(x  + hpad, y + vpad, width - 2f * hpad, height - 2f * vpad);
        bounds.set(x + hpad, y + vpad, width - 2f * hpad, height - 2f * vpad);
        collisionBounds.set(x + hpad, y + vpad, width - 2f * hpad, height - 2f * vpad);
    }

}
