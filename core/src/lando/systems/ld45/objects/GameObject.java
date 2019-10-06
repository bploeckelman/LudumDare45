package lando.systems.ld45.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld45.collision.Segment2D;
import lando.systems.ld45.screens.GameScreen;

public abstract class GameObject {

    public long value = 1;

    // don't set directly!
    protected Vector2 pos = new Vector2();
    public Rectangle bounds = new Rectangle();

    protected Vector2 size;
    protected TextureRegion image;

    protected float hitTime = 1;
    protected float currentHitTime = 0;

    protected GameScreen screen;
    public Circle circleBounds;
    public Array<Segment2D> segmentBounds;

    private boolean isSelected = false;
    private Vector2 selectionOffset = new Vector2();

    public Circle placementBounds;

    public GameObject(GameScreen screen, TextureRegion image) {
        this(screen, image, image.getRegionWidth(), image.getRegionHeight());
    }

    public GameObject(GameScreen screen, TextureRegion image, float width, float height) {
        this.screen = screen;

        this.image = image;
        size = new Vector2(width, height);
        placementBounds = new Circle(0,0, size.x/2f);
    }

    public void setCircleBounds(float x, float y, float radius) {
        if (circleBounds == null){
            circleBounds = new Circle(x, y, radius);
        } else {
            circleBounds.set(x, y, radius);
        }

        placementBounds.set(x, y, radius + 10);
    }

    public void setPosition(Vector2 position){
        this.setPosition(position.x, position.y);
    }

    public void setPosition(float x, float y) {
        pos.x = x;
        pos.y = y;
        bounds.set(x - size.x / 2, y - size.y / 2, size.x, size.y);
    }

    public void update(float dt, Vector2 mousePosition) {

        if (checkSelected(mousePosition)) {
            adjustPosition(mousePosition);
        };

        currentHitTime = Math.max(currentHitTime - dt, 0);
    }

    public boolean checkSelected(Vector2 mousePosition) {
        if (Gdx.input.justTouched() || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (bounds.contains(mousePosition)) {
                selectionOffset.set(mousePosition.x - pos.x, mousePosition.y - pos.y);
                isSelected = true;
            }
        } else if (isSelected && !(Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isTouched())) {
            isSelected = false;
        }

        return isSelected;
    }

    Vector2 tempPosition = new Vector2();
    Vector2 tempVector = new Vector2();
    private void adjustPosition(Vector2 mousePosition) {
        if (isSelected) {
            tempPosition.set(mousePosition).sub(selectionOffset);
            boolean overlaping = true;
            while(overlaping){
                overlaping = false;
                for (int i = 0; i < screen.gameObjects.size; i++){
                    GameObject obj = screen.gameObjects.get(i);
                    if (obj == this) continue;
                    placementBounds.setPosition(tempPosition);
                    if (obj.placementBounds.overlaps(placementBounds)){
                        overlaping = true;
                        tempVector.set(tempPosition).sub(obj.placementBounds.x, obj.placementBounds.y);
                        if (tempVector.epsilonEquals(Vector2.Zero)) tempVector.set(0,1);
                        tempVector.nor().rotate(MathUtils.random(-1f, 1f)).scl(placementBounds.radius + obj.placementBounds.radius + 1f);
                        tempPosition.set(obj.placementBounds.x, obj.placementBounds.y).add(tempVector);
                    }
                }
            }

            setPosition(tempPosition);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
    }

    public void hit() {
        currentHitTime = hitTime;
    }
}
