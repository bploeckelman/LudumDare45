package lando.systems.ld45.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld45.Config;
import lando.systems.ld45.objects.Ball;
import lando.systems.ld45.objects.Bumper;
import lando.systems.ld45.objects.GameObject;
import lando.systems.ld45.objects.Spinner;
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
    Vector2 frameVel1 = new Vector2();
    Vector2 frameVel2 = new Vector2();
    Vector2 nearest1 = new Vector2();
    Vector2 nearest2 = new Vector2();
    Vector2 normal = new Vector2();
    Vector2 incomingVector = new Vector2();
    public void solve(float dt){
        for (Ball b : screen.balls){
            b.dtLeft = dt;
        }
        boolean collisionHappened = true;
        int fuckInifiteLoops =0;
        collisionLoop:
        while (collisionHappened && fuckInifiteLoops < 100000){
            collisionHappened = false;
            fuckInifiteLoops++;
            for (Ball b : screen.balls){
                if (b.dtLeft <= 0) continue;
                boolean collided = false;
                frameVel1.set(b.vel.x * b.dtLeft, b.vel.y * b.dtLeft);
                tempStart1.set(b.bounds.x, b.bounds.y);
                tempEnd1.set(b.bounds.x + frameVel1.x, b.bounds.y + frameVel1.y);
                frameEndPos.set(tempEnd1);

                boolean overlapHappened = false;
                // first pass to move them all away
                for (int i = 0; i < screen.balls.size; i ++){
                    tempStart1.set(b.bounds.x, b.bounds.y);
                    Ball otherBall = screen.balls.get(i);
                    if (b == otherBall) continue;
                    tempStart2.set(otherBall.bounds.x, otherBall.bounds.y);
                    float overlapDist = tempStart1.dst(tempStart2) - (b.bounds.radius + otherBall.bounds.radius);
                    if (overlapDist <= .1f){
                        overlapDist -= 1.2f;
                        overlapHappened = true;
                        collisionHappened = true;

                        normal.set(tempStart2).sub(tempStart1).nor();
                        tempEnd1.set(tempStart1.x + (overlapDist/2f) * normal.x, tempStart1.y + (overlapDist/2f) * normal.y);
                        tempEnd2.set(tempStart2.x - (overlapDist/2f) * normal.x, tempStart2.y - (overlapDist/2f) * normal.y);

                        // keep in bounds
                        for (Segment2D segment : screen.boundary.segments) {
                            normal.set(segment.end).sub(segment.start).nor().rotate90(1);
                            float t = checkSegmentCollision(tempStart1, tempEnd1, segment.start, segment.end, nearest1, nearest2);
                            if (t != Float.MAX_VALUE && nearest1.dst(nearest2) < b.bounds.radius + 2f){
                                tempEnd1.set(tempStart1.sub(normal.x * b.bounds.radius, normal.y * b.bounds.radius));
                            }
                            t = checkSegmentCollision(tempStart2, tempEnd2, segment.start, segment.end, nearest1, nearest2);
                            if (t != Float.MAX_VALUE && nearest1.dst(nearest2) < otherBall.bounds.radius + 2f) {
                                tempEnd2.set(tempStart2.sub(normal.x * otherBall.bounds.radius, normal.y * otherBall.bounds.radius));
                            }
                        }
                        b.bounds.x = tempEnd1.x;
                        b.bounds.y = tempEnd1.y;

                        otherBall.bounds.x = tempEnd2.x;
                        otherBall.bounds.y = tempEnd2.y;

//                        Gdx.app.log("Collision", "RadiusCombined: " + (b.bounds.radius + otherBall.bounds.radius)+
//                                "  Overlap: " + overlapDist + " init: " + tempStart2.dst(tempStart1) +
//                                " final: " + tempStart1.set(b.bounds.x, b.bounds.y).dst(tempStart2.set(otherBall.bounds.x, otherBall.bounds.y)));
                    }
                }
                if (overlapHappened) continue collisionLoop;

                // Bounce balls
                for (int i = 0; i < screen.balls.size; i++){
                    Ball otherBall = screen.balls.get(i);
                    if (otherBall == b) continue;
                    tempStart1.set(b.bounds.x, b.bounds.y);
                    tempStart2.set(otherBall.bounds.x, otherBall.bounds.y);
                    tempEnd1.set(b.bounds.x + frameVel1.x, b.bounds.y + frameVel1.y);
                    frameVel1.set(b.vel.x * b.dtLeft, b.vel.y * b.dtLeft);
                    frameVel2.set(otherBall.vel.x * otherBall.dtLeft, otherBall.vel.y * otherBall.dtLeft);
                    Float time = Utils.intersectCircleCircle(tempStart1, tempStart2, frameVel1, frameVel2, b.bounds.radius, otherBall.bounds.radius);
                    if (time != null){
                        collisionHappened = true;
                        if (time <= 0f){
                            Gdx.app.log("collision", "ball was already inside a ball");
                            continue collisionLoop;
                        }
                        else if (time <= 1f){
                            collisionHappened = true;

                            frameEndPos.set(tempStart1.x + frameVel1.x * (time*.99f), tempStart1.y + frameVel1.y * (time*.99f));
                            tempStart2.set(tempStart2.x + frameVel2.x * (time*.99f), tempStart2.y + frameVel2.y * (time*.99f));

                            // keep in bounds
                            for (Segment2D segment : screen.boundary.segments) {
                                normal.set(segment.end).sub(segment.start).nor().rotate90(1);
                                float t = checkSegmentCollision(tempStart1, tempEnd1, segment.start, segment.end, nearest1, nearest2);
                                if (t != Float.MAX_VALUE && nearest1.dst(nearest2) < b.bounds.radius + 2f){
                                    tempEnd1.set(tempStart1.sub(normal.x * b.bounds.radius, normal.y * b.bounds.radius));
                                }
                                t = checkSegmentCollision(tempStart2, tempEnd2, segment.start, segment.end, nearest1, nearest2);
                                if (t != Float.MAX_VALUE && nearest1.dst(nearest2) < otherBall.bounds.radius + 2f) {
                                    tempEnd2.set(tempStart2.sub(normal.x * b.bounds.radius, normal.y * b.bounds.radius));
                                }
                            }

                            b.bounds.x = tempEnd1.x;
                            b.bounds.y = tempEnd1.y;
                            otherBall.bounds.x = tempStart2.x;
                            otherBall.bounds.y = tempStart2.y;

                            float mass1 = b.bounds.radius;
                            float mass2 = otherBall.bounds.radius;
                            float dist = frameEndPos.dst(tempStart2);
                            float nx = (tempStart2.x - frameEndPos.x) / dist;
                            float ny = (tempStart2.y - frameEndPos.y) / dist;
                            float p = 2 * (b.vel.x * nx + b.vel.y * ny - otherBall.vel.x * nx - otherBall.vel.y * ny) / (mass1 + mass2);

                            b.vel.set(b.vel.x - p * mass2 * nx, b.vel.y - p * mass2 * ny);
                            otherBall.vel.set(otherBall.vel.x + p * mass1 * nx, otherBall.vel.y + p * mass1 * ny);
                            b.dtLeft -= time * b.dtLeft;
                            otherBall.dtLeft -= Math.max(time * otherBall.dtLeft, 0f);
//                            continue collisionLoop;
                        }
                    }
                }

                // Bumpers
                for (GameObject obj : screen.gameObjects){
                    if (obj.circleBounds != null){
                        tempStart1.set(b.bounds.x, b.bounds.y);
                        frameVel1.set(b.vel.x * b.dtLeft, b.vel.y * b.dtLeft);
                        tempEnd1.set(b.bounds.x + frameVel1.x, b.bounds.y + frameVel1.y);
                        tempStart2.set(obj.circleBounds.x, obj.circleBounds.y);
                        frameVel2.set(0,0);
                        Float time = Utils.intersectCircleCircle(tempStart1, tempStart2, frameVel1, frameVel2, b.bounds.radius, obj.circleBounds.radius);
                        if (time != null) {
                            collisionHappened = true;
                            if (time == 0f) {
                                Gdx.app.log("collision", "ball was already inside a bumper");
                                float overlapDist = tempStart1.dst(tempStart2) - (b.bounds.radius + obj.circleBounds.radius);
                                overlapDist -= 1.2f;
                                normal.set(tempStart2).sub(tempStart1).nor();
                                tempEnd1.set(tempStart1.x + (overlapDist) * normal.x, tempStart1.y + (overlapDist) * normal.y);
                                b.bounds.x = tempEnd1.x;
                                b.bounds.y = tempEnd1.y;
                                continue collisionLoop;
                            } else if (time <= 1f) {
                                obj.hit();

                                frameEndPos.set(tempStart1.x + frameVel1.x * (time * .99f), tempStart1.y + frameVel1.y * (time * .99f));
                                tempStart2.set(tempStart2.x + frameVel2.x * (time * .99f), tempStart2.y + frameVel2.y * (time * .99f));
                                b.bounds.x = frameEndPos.x;
                                b.bounds.y = frameEndPos.y;

                                float mass1 = b.bounds.radius;
                                float mass2 = 10000;
                                float dist = frameEndPos.dst(tempStart2);
                                float nx = (tempStart2.x - frameEndPos.x) / dist;
                                float ny = (tempStart2.y - frameEndPos.y) / dist;
                                float p = 2 * (b.vel.x * nx + b.vel.y * ny - 0 * nx - 0 * ny) / (mass1 + mass2);


                                b.vel.set(b.vel.x - p * mass2 * nx, b.vel.y - p * mass2 * ny);
                                b.vel.scl(.8f);
                                normal.set(frameEndPos).sub(tempStart2).nor();
                                if (obj instanceof Bumper) {
                                    b.vel.add(normal.x * 4000 * dt, normal.y * 4000 * dt);
                                }
                                if (obj instanceof Spinner){
                                    Spinner spin = (Spinner) obj;
                                    normal.rotate90(spin.left ? 1 : -1);
                                    b.vel.add(normal.x * 10000 * dt, normal.y * 10000 * dt);

                                }
                                b.dtLeft -= time * b.dtLeft;
                            }
                        }

                    }
                }

                // Collide Boundary
                tempStart1.set(b.bounds.x, b.bounds.y);
                frameVel1.set(b.vel.x * b.dtLeft, b.vel.y * b.dtLeft);
                tempEnd1.set(b.bounds.x + frameVel1.x, b.bounds.y + frameVel1.y);
                for (Segment2D segment : screen.boundary.segments){
                    float t = checkSegmentCollision(tempStart1, tempEnd1, segment.start, segment.end, nearest1, nearest2);
                    if (t != Float.MAX_VALUE){
                        if (nearest1.dst(nearest2) < b.bounds.radius + 2f){
                            collided = true;
                            collisionHappened = true;
                            b.dtLeft -= dt;
                            frameEndPos.set(nearest1);
                            normal.set(segment.end).sub(segment.start).nor().rotate90(1);

                            float backupDist = (b.bounds.radius + 2.1f) - nearest1.dst(nearest2);
                            float x = frameEndPos.x - backupDist * (normal.x);
                            float y = frameEndPos.y - backupDist * (normal.y);
                            frameEndPos.set(x, y);
                            b.vel.scl(.8f);
                            if (nearest2.epsilonEquals(segment.start) || nearest2.epsilonEquals(segment.end)){
                                normal.set(nearest2).sub(frameEndPos).nor();
                                b.vel.set(Utils.reflectVector(incomingVector.set(b.vel), normal));
                            } else {
                                normal.set(segment.end).sub(segment.start).nor().rotate90(1);
                                b.vel.set(Utils.reflectVector(incomingVector.set(b.vel), normal));
                                b.vel.add(-normal.x * 400 * dt, -normal.y * 400 * dt);
                            }


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

