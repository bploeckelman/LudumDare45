package lando.systems.ld45.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld45.Game;
import lando.systems.ld45.ui.Panel;
import lando.systems.ld45.utils.UIAssetType;

public class UpgradeScreen extends  BaseScreen {

    private Panel upgradePanel;

    public UpgradeScreen(Game game) {
        super(game);

        this.upgradePanel = new Panel(this, UIAssetType.upgrade_panel, UIAssetType.upgrade_panel_inset);
        this.upgradePanel.horizontal = false;
        this.upgradePanel.setInitialBounds(0f, -worldCamera.viewportHeight,
                                            worldCamera.viewportWidth,
                                            worldCamera.viewportHeight);
        this.upgradePanel.show(worldCamera);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        upgradePanel.update(dt);
        // TODO: if (upgradePanel start game button was pressed...), run hide() and make callback set new screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)
         || (upgradePanel.isVisible() && !upgradePanel.isAnimating() && Gdx.input.justTouched())) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(worldCamera.combined);
        {
            renderUIElements(batch);

            batch.setColor(Color.WHITE);
            upgradePanel.render(batch);
        }
        batch.end();
    }
}
