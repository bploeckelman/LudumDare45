package lando.systems.ld45.collision;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.screens.GameScreen;
import lando.systems.ld45.utils.Utils;

public class CollisionManager {

    GameScreen screen;

    public CollisionManager(GameScreen screen) {
        this.screen = screen;
    }


    Vector2 tempStart1 = new Vector2();
    Vector2 tempEnd1 = new Vector2();
    Vector2 frameEndPos = new Vector2();
    Vector2 tempStart2 = new Vector2();
    Vector2 tempEnd2 = new Vector2();
    Vector2 nearest1 = new Vector2();
    Vector2 nearest2 = new Vector2();
    Vector2 normal = new Vector2();
    Vector2 incomingVector = new Vector2();
    public void solve(float dt){
        for (Ball b : screen.balls){
            b.dtLeft = dt;
        }
        boolean collisionHappened = true;

        collisionLoop:
        while (collisionHappened){
            collisionHappened = false;
            for (Ball b : screen.balls){
                if (b.dtLeft <= 0) continue;
                boolean collided = false;
                tempStart1.set(b.bounds.x, b.bounds.y);
                tempEnd1.set(b.bounds.x + b.vel.x * dt, b.bounds.y + b.vel.y * dt);
                frameEndPos.set(tempEnd1);

                // first pass to move them all away
                for (int i = 0; i < screen.balls.size; i ++){
                    Ball otherBall = screen.balls.get(i);
                    if (b == otherBall) continue;
                    tempStart2.set(otherBall.bounds.x, otherBall.bounds.y);
                    float overlapDist = tempStart1.dst(tempStart2) - (b.bounds.radius + otherBall.bounds.radius);
                    if (overlapDist <= 0){
                        overlapDist -= .1f;
                        normal.set(tempStart2).sub(tempStart1).nor();
                        b.bounds.x = tempStart1.x + (overlapDist/2f) * normal.x;
                        b.bounds.y = tempStart1.y + (overlapDist/2f) * normal.y;

                        otherBall.bounds.x = tempStart2.x - (overlapDist/2f) * normal.x;
                        otherBall.bounds.y = tempStart2.y - (overlapDist/2f) * normal.y;
                        collisionHappened = true;
                        continue collisionLoop;
                    }
                }

                // Bounce balls
                for (int i = 0; i < screen.balls.size; i++){
                    Ball otherBall = screen.balls.get(i);
                    if (otherBall == b) continue;
                    tempStart2.set(otherBall.bounds.x, otherBall.bounds.y);
                    Float time = Utils.intersectCircleCircle(tempStart1, tempStart2, b.vel, otherBall.vel, b.bounds.radius, otherBall.bounds.radius, dt);
                    if (time != null){
                        if (time == 0f){
                            // no op? this shouldn't happen
                        } else if (time <= 1f){
                            collisionHappened = true;
                            frameEndPos.set(tempStart1.x + b.vel.x * dt * time, tempStart1.y + b.vel.y * dt * time);
                            tempStart2.set(tempStart2.x + otherBall.vel.x * dt * time, tempStart2.y + otherBall.vel.y * dt * time);
                            b.bounds.x = frameEndPos.x;
                            b.bounds.y = frameEndPos.y;
                            otherBall.bounds.x = tempStart2.x;
                            otherBall.bounds.y = tempStart2.y;

                            float mass1 = b.bounds.radius * b.bounds.radius;
                            float mass2 = otherBall.bounds.radius * otherBall.bounds.radius;
                            float dist = frameEndPos.dst(tempStart2);
                            float nx = (tempStart2.x - frameEndPos.x) / dist;
                            float ny = (tempStart2.y - frameEndPos.y) / dist;
                            float p = 2 * (b.vel.x * nx + b.vel.y * ny - otherBall.vel.x * nx - otherBall.vel.y * ny) / (mass1 + mass2);

                            b.vel.set(b.vel.x - p * mass2 * nx, b.vel.y - p * mass2 * ny);
                            otherBall.vel.set(otherBall.vel.x + p * mass1 * nx, otherBall.vel.y + p * mass1 * ny);
                            b.dtLeft -= time * dt;
                            otherBall.dtLeft -= time * dt;
                            continue collisionLoop;
                        }
                    }
                }

                // Collide Boundary
                for (Segment2D segment : screen.boundary.segments){
                    float t = checkSegmentCollision(tempStart1, tempEnd1, segment.start, segment.end, nearest1, nearest2);
                    if (t != Float.MAX_VALUE){
                        if (t > 0 && t*dt <= b.dtLeft && nearest1.dst(nearest2) < b.bounds.radius){
                            collided = true;
                            collisionHappened = true;
                            b.dtLeft -= t * dt;
                            frameEndPos.set(nearest1);
                            normal.set(segment.end).sub(segment.start).nor().rotate90(-1);

                            float backupDist = (b.bounds.radius+.1f) - nearest1.dst(nearest2);
                            float moveVectorLength = b.vel.len();
                            float x = frameEndPos.x - backupDist * (b.vel.x / moveVectorLength);
                            float y = frameEndPos.y - backupDist * (b.vel.y / moveVectorLength);
                            frameEndPos.set(x, y);
                            b.vel.set(Utils.reflectVector(incomingVector.set(b.vel), normal));
                        }
                    }
                }



                b.bounds.x = frameEndPos.x;
                b.bounds.y = frameEndPos.y;
                if (!collided) {
                    b.dtLeft = 0;
                }
            }
        }


    }



    Vector2 d1 = new Vector2();
    Vector2 d2 = new Vector2();
    Vector2 r = new Vector2();
    private float checkSegmentCollision(Vector2 seg1Start, Vector2 seg1End, Vector2 seg2Start, Vector2 seg2End, Vector2 nearestSeg1, Vector2 nearestSeg2){
        d1.set(seg1End).sub(seg1Start);
        d2.set(seg2End).sub(seg2Start);
        r.set(seg1Start).sub(seg2Start);

        float a = d1.dot(d1);
        float e = d2.dot(d2);
        float f = d2.dot(r);

        float b = d1.dot(d2);
        float c = d1.dot(r);

        float s = 0;
        float t = 0;

        float denom = a*e-b*b;
        if (denom != 0){
            s = MathUtils.clamp((b*f - c*e)/denom, 0f, 1f);
        } else {
            // Parallel
            return Float.MAX_VALUE;
        }

        t = (b*s + f) /e;
        if (t < 0) {
            t = 0;
            s = MathUtils.clamp(-c /a, 0, 1);
        } else if (t > 1) {
            t = 1;
            s = MathUtils.clamp((b-c)/a, 0, 1);
        }

        nearestSeg1.set(seg1Start).add(d1.scl(s));
        nearestSeg2.set(seg2Start).add(d2.scl(t));
        return s;
    }

}
