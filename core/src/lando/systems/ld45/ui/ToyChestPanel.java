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
import lando.systems.ld45.utils.ArtPack;
import lando.systems.ld45.utils.AssetType;
import lando.systems.ld45.utils.UIAssetType;


public class ToyChestPanel extends Panel {

    private Rectangle pegRect;
    private Rectangle bumperRect;
    private Rectangle leftSpinnerRect;
    private Rectangle rightSpinnerRect;

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
        pegRect = new Rectangle(-100, -100, 1, 1);
        bumperRect = new Rectangle(-100, -100, 1, 1);
        leftSpinnerRect = new Rectangle(-100, -100, 1, 1);
        rightSpinnerRect = new Rectangle(-100, -100, 1, 1);
        projection = new Vector3();
    }


    @Override
    public void update(float dt) {
        super.update(dt);
        accum += dt;

        float hSpacing = bounds.height / 7f;
        pegRect.set(bounds.x + bounds.width/2 - 8, bounds.y + bounds.height - hSpacing*2f, 16, 16);
        bumperRect.set(bounds.x + bounds.width/2 - 16, bounds.y + bounds.height - hSpacing*3f, 32, 32);
        leftSpinnerRect.set(bounds.x + bounds.width/2 - 16, bounds.y + bounds.height - hSpacing*4f, 32, 32);
        rightSpinnerRect.set(bounds.x + bounds.width/2 - 16, bounds.y + bounds.height - hSpacing*5f, 32, 32);

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

        BitmapFont font = Game.getCurrentFont();

        // Pegs
        if (screen.game.player.canBuildPeg()) batch.setColor(Color.WHITE);
        else {
            batch.setColor(Color.GRAY);
            batch.draw(assets.whitePixel, pegRect.x-10, pegRect.y-10, pegRect.width+20, pegRect.height+20);
        }
        batch.draw(assets.assetMap.get(artPack).get(AssetType.peg).getKeyFrame(accum), pegRect.x, pegRect.y, pegRect.width, pegRect.height);
        assets.layout.setText(font, "Pegs: " + gameScreen.game.player.getCurrentPegs() +"/"+ gameScreen.game.player.pegs, Color.BLACK, bounds.width, Align.center, true);
        font.draw(batch, assets.layout, bounds.x, pegRect.y - 12);

        // Bumpers
        if (screen.game.player.canBuildBumper()) batch.setColor(Color.WHITE);
        else {
            batch.setColor(Color.GRAY);
            batch.draw(assets.whitePixel, bumperRect.x- 10, bumperRect.y-10, bumperRect.width+20, bumperRect.height+20);
        }
        batch.draw(assets.assetMap.get(artPack).get(AssetType.bumper).getKeyFrame(accum), bumperRect.x, bumperRect.y, bumperRect.width, bumperRect.height);
        assets.layout.setText(font, "Bumpers: " + gameScreen.game.player.getCurrentBumpers() +"/"+ gameScreen.game.player.bumpers, Color.BLACK, bounds.width, Align.center, true);
        font.draw(batch, assets.layout, bounds.x, bumperRect.y - 12);

        // Left Spinners
        if (screen.game.player.canBuildLeftSpinner()) batch.setColor(Color.WHITE);
        else {
            batch.setColor(Color.GRAY);
            batch.draw(assets.whitePixel, leftSpinnerRect.x-10, leftSpinnerRect.y-10, leftSpinnerRect.width+20, leftSpinnerRect.height+20);
        }
        batch.draw(assets.assetMap.get(artPack).get(AssetType.spinner).getKeyFrame(accum), leftSpinnerRect.x, leftSpinnerRect.y, leftSpinnerRect.width, leftSpinnerRect.height);
        assets.layout.setText(font, "Spinner(L): " + gameScreen.game.player.getCurrentLeftSpinner() +"/"+ gameScreen.game.player.leftSpinners, Color.BLACK, bounds.width, Align.center, true);
        font.draw(batch, assets.layout, bounds.x, leftSpinnerRect.y - 12);

        // Right Spinners
        if (screen.game.player.canBuildRightSpinner()) batch.setColor(Color.WHITE);
        else {
            batch.setColor(Color.GRAY);
            batch.draw(assets.whitePixel, rightSpinnerRect.x-10, rightSpinnerRect.y-10, rightSpinnerRect.width+20, rightSpinnerRect.height+20);
        }
        batch.draw(assets.assetMap.get(artPack).get(AssetType.spinner).getKeyFrame(accum), rightSpinnerRect.x + 32, rightSpinnerRect.y, -rightSpinnerRect.width, rightSpinnerRect.height);
        assets.layout.setText(font, "Spinner(R): " + gameScreen.game.player.getCurrentRightSpinner() +"/"+ gameScreen.game.player.rightSpinners, Color.BLACK, bounds.width, Align.center, true);
        font.draw(batch, assets.layout, bounds.x, rightSpinnerRect.y - 12);


        batch.setColor(Color.WHITE);

        if (selectedObject != null){
            selectedObject.render(batch);
        }
    }

}
