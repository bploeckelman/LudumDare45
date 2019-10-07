package lando.systems.ld45.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld45.Assets;
import lando.systems.ld45.Game;
import lando.systems.ld45.objects.Bumper;
import lando.systems.ld45.objects.GameObject;
import lando.systems.ld45.objects.Peg;
import lando.systems.ld45.objects.Spinner;
import lando.systems.ld45.screens.BaseScreen;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.state.PlayerState;
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.AssetType;
import lando.systems.ld45.utils.UIAssetType;


public class ToyChestPanel extends Panel {

    private HudBox pegRect;
    private HudBox bumperRect;
    private HudBox leftSpinnerRect;
    private HudBox rightSpinnerRect;

    private ArtPack artPack;
    private Assets assets;
    private float accum;

    private Vector3 projection;
    private GameObject selectedObject;
    private GameScreen gameScreen;

    public ToyChestPanel(BaseScreen screen, UIAssetType uiAssetTypePanel, UIAssetType uiAssetTypePanelInset) {
        super(screen, uiAssetTypePanel, uiAssetTypePanelInset);
        this.assets = screen.assets;
        this.artPack = screen.game.player.artPack;

        this.gameScreen = (GameScreen)screen;
        pegRect = new HudBox(-100, 1, 1, 1);
        bumperRect = new HudBox(-100, 1, 1, 1);
        leftSpinnerRect = new HudBox(-100, 1, 1, 1);
        rightSpinnerRect = new HudBox(-100, 1, 1, 1);
        projection = new Vector3();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        accum += dt;

        float gap = 40;
        float width = bounds.width - (gap*2);
        float height = (bounds.height - (gap*5)) / 4;
        float x = bounds.x + gap;
        float y = bounds.y + bounds.height - height - 28;

        pegRect.reset(x, y, width, height);
        pegRect.update(dt);
        y -= height + gap;
        bumperRect.reset(x, y, width, height);
        bumperRect.update(dt);
        y -= height + gap;
        leftSpinnerRect.reset(x, y, width, height);
        leftSpinnerRect.update(dt);
        y -= height + gap;
        rightSpinnerRect.reset(x, y, width, height);
        rightSpinnerRect.update(dt);

        projection.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        projection = screen.worldCamera.unproject(projection);

        if (Gdx.input.justTouched()){

            if (pegRect.contains(projection.x, projection.y) && gameScreen.game.player.canBuildPeg()){
                selectedObject = Peg.getPeg(gameScreen);
            }
            if (bumperRect.contains(projection.x, projection.y) && gameScreen.game.player.canBuildBumper()){
                selectedObject = Bumper.getBumper(gameScreen);
            }
            if (leftSpinnerRect.contains(projection.x, projection.y) && gameScreen.game.player.canBuildLeftSpinner()){
                selectedObject = Spinner.getSpinner(gameScreen);
                ((Spinner)selectedObject).left = true;
            }
            if (rightSpinnerRect.contains(projection.x, projection.y) && gameScreen.game.player.canBuildRightSpinner()){
                selectedObject = Spinner.getSpinner(gameScreen);
                ((Spinner)selectedObject).left = false;

            }
        }

        if (Gdx.input.isTouched() && selectedObject != null){
            selectedObject.setPosition(projection.x, projection.y);
            if (!bounds.contains(projection.x, projection.y)){
                screen.player.gameObjects.add(selectedObject);
                selectedObject.isSelected = true;
                selectedObject.selectionOffset.set(0,0);
                selectedObject = null;
                hide(screen.worldCamera);
            }
        } else {
            selectedObject = null;
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        pegRect.render(batch);
        bumperRect.render(batch);
        leftSpinnerRect.render(batch);
        rightSpinnerRect.render(batch);

        PlayerState state = gameScreen.game.player;

        draw(batch, AssetType.peg, pegRect.bounds, 16, 1, "Pegs",state.getCurrentPegs(), state.pegs);
        draw(batch, AssetType.bumper, bumperRect.bounds, 32, 1,"Bumpers", state.getCurrentBumpers(), state.bumpers);
        draw(batch, AssetType.spinner, leftSpinnerRect.bounds, 32, 1, "Spinner(L)", state.getCurrentLeftSpinner(), state.leftSpinners);
        draw(batch, AssetType.spinner, rightSpinnerRect.bounds, 32, -1, "Spinner(R)", state.getCurrentRightSpinner(), state.rightSpinners);

        batch.setColor(Color.WHITE);

        if (selectedObject != null){
            selectedObject.render(batch);
        }
    }

    private void draw(SpriteBatch batch, AssetType assetType, Rectangle boxBounds, float size, float scale, String type, int count, int total) {
        String text = type + ": " + (total - count) + "/" + total;

        batch.draw(Game.getAsset(assetType, accum), boxBounds.x + (boxBounds.width - size)/2,
                boxBounds.y + (boxBounds.height - size)/2, size, size);
        assets.layout.setText(Game.getCurrentFont(), text, Color.BLACK, bounds.width, Align.center, true);
        Game.getCurrentFont().draw(batch, assets.layout, bounds.x, boxBounds.y - 7);

        if (count == total) {
            batch.setColor(0.4f, 0.4f, 0.4f, 0.4f);
            batch.draw(gameScreen.assets.whitePixel, boxBounds.x, boxBounds.y, boxBounds.width / 2, boxBounds.height / 2,
                    boxBounds.width, boxBounds.height, scale, 1, 0);
        }
        batch.setColor(Color.WHITE);
    }

}
