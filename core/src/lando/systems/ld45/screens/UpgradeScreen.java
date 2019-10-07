package lando.systems.ld45.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
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
        Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                upgradePanel.show(worldCamera);
            }
        }).delay(1f).start(game.tween);

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
            batch.setColor(Color.BLACK);
            batch.draw(assets.whitePixel, 0,0, worldCamera.viewportWidth, worldCamera.viewportHeight);
            batch.setColor(Color.WHITE);
            upgradePanel.render(batch);
        }
        batch.end();
    }
}
