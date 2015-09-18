package com.greenfrvr.rubberloader.calculation;

import android.graphics.PointF;
import android.graphics.RectF;

import com.greenfrvr.rubberloader.internal.BezierQ;
import com.greenfrvr.rubberloader.internal.CircleTangents;

/**
 * Created by greenfrvr
 */
public class BezierEndpoints {

    private CircleTangents tangents;
    private PointF roots;
    float[][] pts;
    boolean top;

    public static BezierEndpoints top() {
        return new BezierEndpoints(true);
    }

    public static BezierEndpoints bot() {
        return new BezierEndpoints(false);
    }

    private BezierEndpoints(boolean top) {
        this.roots = new PointF();
        this.tangents = new CircleTangents();
        this.pts = new float[4][2];
        this.top = top;
    }

    public void evaluateBezierEndpoints(RectF c1, RectF c2, BezierQ bezierQ) {
        evaluateBezierEndpoints(c1.centerX(), c1.centerY(), c1.width() / 2, c2.centerX(), c2.centerY(), c2.width() / 2,
                bezierQ.getMiddle(), top ? bezierQ.getStart() : bezierQ.getEnd(), top ? bezierQ.getEnd() : bezierQ.getStart());
    }

    private void evaluateBezierEndpoints(float cx1, float cy1, float r1, float cx2, float cy2, float r2, PointF p, PointF p1, PointF p2) {

        tangentLines(cx1, cy1, r1, p.x, p.y);
        circleLineIntersection(cx1, cy1, r1, tangents.getK1(), tangents.getL1(), pts[0]);
        circleLineIntersection(cx1, cy1, r1, tangents.getK2(), tangents.getL2(), pts[1]);

        tangentLines(cx2, cy2, r2, p.x, p.y);
        circleLineIntersection(cx2, cy2, r2, tangents.getK1(), tangents.getL1(), pts[2]);
        circleLineIntersection(cx2, cy2, r2, tangents.getK2(), tangents.getL2(), pts[3]);

        if (top) {
            if (pts[0][1] < pts[1][1]) {
                p1.set(pts[0][0], pts[0][1]);
            } else {
                p1.set(pts[1][0], pts[1][1]);
            }

            if (pts[2][1] < pts[3][1]) {
                p2.set(pts[2][0], pts[2][1]);
            } else {
                p2.set(pts[3][0], pts[3][1]);
            }
        } else {
            if (pts[0][1] > pts[1][1]) {
                p1.set(pts[0][0], pts[0][1]);
            } else {
                p1.set(pts[1][0], pts[1][1]);
            }

            if (pts[2][1] > pts[3][1]) {
                p2.set(pts[2][0], pts[2][1]);
            } else {
                p2.set(pts[3][0], pts[3][1]);
            }
        }
    }

    private void circleLineIntersection(float cx, float cy, float r, float k, float l, float[] pts) {
        float a = k * k + 1;
        float b = 2 * (l * k - cx - cy * k);
        float c = l * l + cx * cx + cy * cy - r * r - 2 * l * cy;

        quadraticRoots(a, b, c);
        pts[0] = roots.x;
        pts[1] = k * roots.x + l;
    }

    private void tangentLines(float cx, float cy, float r, float px, float py) {
        float a = 4 * (r * r - px * px - cx * cx + 2 * px * cx);
        float b = 8 * (px * py + cx * cy - px * cy - py * cx);
        float c = 4 * (r * r - py * py - cy * cy + 2 * py * cy);

        quadraticRoots(a, b, c);
        tangents.setTangent1(roots.x, py - roots.x * px);
        tangents.setTangent2(roots.y, py - roots.y * px);
    }

    private void quadraticRoots(float a, float b, float c) {
        float d = b * b - 4.0f * a * c;
        float aa = a + a;

        if (d < 0.0) {
            roots.x = -b / aa;
            roots.y = -b / aa;
        } else if (b < 0.0) {
            float re = (float) ((-b + Math.sqrt(d)) / aa);
            roots.x = re;
            roots.y = c / (a * re);
        } else {
            float re = (float) ((-b - Math.sqrt(d)) / aa);
            roots.y = re;
            roots.x = c / (a * re);
        }
    }

}
