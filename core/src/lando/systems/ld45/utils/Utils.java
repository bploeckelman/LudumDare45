package lando.systems.ld45.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Utils {

    /**
     *
     * @param hue 0-1
     * @param saturation
     * @param value
     * @param outColor
     * @return
     */
    public static Color hsvToRgb(float hue, float saturation, float value, Color outColor) {
        if (outColor == null) outColor = new Color();
        hue = hue % 1f;
        int h = (int) (hue * 6);
        h = h % 6;
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: outColor.set(value, t, p, 1f); break;
            case 1: outColor.set(q, value, p, 1f); break;
            case 2: outColor.set(p, value, t, 1f); break;
            case 3: outColor.set(p, q, value, 1f); break;
            case 4: outColor.set(t, p, value, 1f); break;
            case 5: outColor.set(value, p, q, 1f); break;
            default: throw new GdxRuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
        return outColor;
    }

    static Array<String> colorKeysCache;
    public static Color getRandomColor() {
        if (colorKeysCache == null) {
            colorKeysCache = Colors.getColors().keys().toArray();
        }
        return Colors.getColors().get(colorKeysCache.get(MathUtils.random(0, colorKeysCache.size - 1)));
    }

    public static Color getRandomHSVColor(){
        return hsvToRgb(MathUtils.random(1f), 1f, 1f, null);
    }

    static Vector2 s = new Vector2();
    static Vector2 v = new Vector2();
    public static Float intersectCircleCircle(Vector2 pos1, Vector2 pos2, Vector2 vel1, Vector2 vel2, float rad1, float rad2) {
        return Utils.intersectCircleCircle(pos1.x, pos1.y, pos2.x, pos2.y, vel1.x, vel1.y, vel2.x, vel2.y, rad1, rad2);
    }

    public static Float intersectCircleCircle(float pos1x, float pos1y, float pos2x, float pos2y,
                                              float vel1x, float vel1y, float vel2x, float vel2y,
                                              float rad1, float rad2) {
        Float t;
        s.set(pos2x, pos2y).sub(pos1x, pos1y);
        v.set(vel2x, vel2y).sub(vel1x, vel1y);
        float r = rad1 + rad2;
        float c = s.dot(s) - r * r;
        if (c < 0){
            // Already overlap early out
            t = 0f;
            return t;
        }
        float a = v.dot(v);
        if (a < .1f) return null; // circles not moving relative to each other
        float b = v.dot(s);
        if (b >= 0f) return null; // circles moving away from each other
        float d = b * b - a * c;
        if (d < 0) return null; // No intersections
        t = (float)(-b - Math.sqrt(d)) / a;

        return t;
    }

    static Vector2 AB = new Vector2();
    static Vector2 AP = new Vector2();
    static Vector2 BC = new Vector2();
    static Vector2 BP = new Vector2();
    public static boolean isPointInRectangle(float pointX, float pointY, float Ax, float Ay, float Bx, float By, float Cx, float Cy) {
        AB.set(Ax, Ay).sub(Bx, By);
        AP.set(Ax, Ay).sub(pointX, pointY);
        BC.set(Bx, By).sub(Cx, Cy);
        BP.set(Bx, By).sub(pointX, pointY);

        float ABdotAP = AB.dot(AP);
        float ABdotAB = AB.dot(AB);
        float BCdotBP = BC.dot(BP);
        float BCdotBC = BC.dot(BC);
        return 0 <= ABdotAP && ABdotAP <= ABdotAB && 0 <= BCdotBP && BCdotBP <= BCdotBC;
    }

    public static boolean isPointInRectangle(Vector2 point, Vector2 A, Vector2 B, Vector2 C) {
        return isPointInRectangle(point.x, point.y, A.x, A.y, B.x, B.y, C.x, C.y);
    }

    public static Vector2 reflectVector(Vector2 incoming, Vector2 normal) {
        float initalSize = incoming.len();
        normal.nor();
        incoming.nor();
        float iDotN = incoming.dot(normal);
        incoming.set(incoming.x - 2f * normal.x * iDotN,
                incoming.y - 2f * normal.y * iDotN)
                .nor().scl(initalSize);
        return incoming;
    }

    private static Vector2 aspectSize = new Vector2();
    public static Vector2 calculateAspectRatioForFit(float srcWidth, float srcHeight, float maxWidth, float maxHeight) {
        float aspect = Math.min(maxWidth / srcWidth, maxHeight / srcHeight);
        return aspectSize.set(srcWidth * aspect, srcHeight * aspect);
    }

    public static boolean intersectSegmentBounds (float startX, float startY, float endX, float endY, Rectangle rectangle, Vector2 intersection) {
        float rectangleEndX = rectangle.x + rectangle.width;
        float rectangleEndY = rectangle.y + rectangle.height;

        if (Intersector.intersectSegments(startX, startY, endX, endY, rectangle.x, rectangle.y, rectangle.x, rectangleEndY, intersection))
            return true;

        if (Intersector.intersectSegments(startX, startY, endX, endY, rectangle.x, rectangle.y, rectangleEndX, rectangle.y, intersection))
            return true;

        if (Intersector.intersectSegments(startX, startY, endX, endY, rectangleEndX, rectangle.y, rectangleEndX, rectangleEndY, intersection))
            return true;

        if (Intersector.intersectSegments(startX, startY, endX, endY, rectangle.x, rectangleEndY, rectangleEndX, rectangleEndY, intersection))
            return true;

        return false;
    }

}
