package lando.systems.ld45.effects.trail;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TrailResolver {

    private Array<Vector2> tmp = new Array<>();

    public static int smoothIterations = 4;
    public static float simplifyTolerance = 10f;

    void resolve(Array<Vector2> input, Array<Vector2> output) {
        output.clear();

        // if there's nothing to simplify or smooth, just copy
        if (input.size <= 2) {
            output.addAll(input);
            return;
        }

        // simplify with squared tolerance
        if (simplifyTolerance > 0 && input.size > 3) {
            simplify(input, simplifyTolerance * simplifyTolerance, tmp);
            input = tmp;
        }

        //perform smooth operation(s)
        if (smoothIterations <= 0) {
            //no smooth, just copy input to output
            output.addAll(input);
        } else if (smoothIterations == 1) {
            // 1 iteration, smooth to output
            smooth(input, output);
        } else {
            //multiple iterations.. ping-pong between arrays
            int iters = smoothIterations;
            do {
                smooth(input, output);
                tmp.clear();
                tmp.addAll(output);
                Array<Vector2> old = output;
                input = tmp;
                output = old;
            } while (--iters > 0);
        }
    }

    //simple distance-based simplification
    //adapted from simplify.js
    private static void simplify(Array<Vector2> points, float squaredDistanceTolerance, Array<Vector2> out) {
        int len = points.size;

        Vector2 point = new Vector2();
        Vector2 prevPoint = points.get(0);

        out.clear();
        out.add(prevPoint);

        for (int i = 1; i < len; i++) {
            point = points.get(i);
            if (point.dst2(prevPoint) > squaredDistanceTolerance) {
                out.add(point);
                prevPoint = point;
            }
        }
        if (!prevPoint.equals(point)) {
            out.add(point);
        }
    }

    private static void smooth(Array<Vector2> input, Array<Vector2> output) {
        //expected size
        output.clear();
        output.ensureCapacity(input.size*2);

        //first element
        output.add(input.get(0));

        //average elements
        for (int i=0; i<input.size-1; i++) {
            Vector2 p0 = input.get(i);
            Vector2 p1 = input.get(i+1);

            Vector2 Q = new Vector2(0.75f * p0.x + 0.25f * p1.x, 0.75f * p0.y + 0.25f * p1.y);
            Vector2 R = new Vector2(0.25f * p0.x + 0.75f * p1.x, 0.25f * p0.y + 0.75f * p1.y);
            output.add(Q);
            output.add(R);
        }

        //last element
        output.add(input.get(input.size-1));
    }

}
