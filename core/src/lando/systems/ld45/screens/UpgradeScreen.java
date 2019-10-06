package lando.systems.ld45.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.Game;
import lando.systems.ld45.ui.UpgradePanel;
import lando.systems.ld45.utils.UIAssetType;

public class UpgradeScreen extends  BaseScreen {

    private UpgradePanel upgradePanel;

    public UpgradeScreen(Game game) {
        super(game);

        this.upgradePanel = new UpgradePanel(this, UIAssetType.upgrade_panel, UIAssetType.upgrade_panel_inset);
        this.upgradePanel.setInitialBounds(0f, -worldCamera.viewportHeight,
                                            worldCamera.viewportWidth,
                                            worldCamera.viewportHeight);
        this.upgradePanel.show(worldCamera);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        upgradePanel.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);
        {
            batch.setColor(Color.WHITE);
            upgradePanel.render(batch);
        }
        batch.end();
    }
}
