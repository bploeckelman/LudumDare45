package lando.systems.ld45.screens;

import aurelienribon.tweenengine.Tween;
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
        this.upgradePanel.setInitialBounds(0f, -worldCamera.viewportHeight, worldCamera.viewportWidth, worldCamera.viewportHeight);
        Tween.call((i, baseTween) -> upgradePanel.show(worldCamera)).delay(1f).start(game.tween);
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
            batch.setColor(Color.GRAY);
            batch.draw(assets.whitePixel, 0f, 0f, worldCamera.viewportWidth, worldCamera.viewportHeight);
            batch.setColor(Color.WHITE);
            upgradePanel.render(batch);
            renderUIElements(batch);
        }
        batch.end();
    }
}
