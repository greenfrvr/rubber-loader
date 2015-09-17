package com.greenfrvr.rubberloader.calculation;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by greenfrvr
 */
@Deprecated
public class Calculator {

    public static void evaluateBezierEndpoints(RectF c1, RectF c2, float px, float py, PointF[] coors, RectF[] tangents, boolean top) {
        evaluateBezierEndpoints(c1.centerX(), c1.centerY(), c1.width() / 2, c2.centerX(), c2.centerY(), c2.width() / 2, px, py, coors, tangents, top);
    }

    public static void evaluateBezierEndpoints(float cx1, float cy1, float r1, float cx2, float cy2, float r2, float px, float py, PointF[] coors, RectF[] tangents, boolean top) {
        tangentLines(cx1, cy1, r1, px, py, tangents[0]);
        tangentLines(cx2, cy2, r2, px, py, tangents[1]);

        float[][] pts = new float[4][2];

        pts[0] = circleLineIntersection(cx1, cy1, r1, tangents[0].left, tangents[0].top);
        pts[1] = circleLineIntersection(cx1, cy1, r1, tangents[0].right, tangents[0].bottom);

        pts[2] = circleLineIntersection(cx2, cy2, r2, tangents[1].left, tangents[1].top);
        pts[3] = circleLineIntersection(cx2, cy2, r2, tangents[1].right, tangents[1].bottom);

        if (top) {
            if (pts[0][1] < pts[1][1]) {
                coors[0].x = pts[0][0];
                coors[0].y = pts[0][1];
            } else {
                coors[0].x = pts[1][0];
                coors[0].y = pts[1][1];
            }

            if (pts[2][1] < pts[3][1]) {
                coors[1].x = pts[2][0];
                coors[1].y = pts[2][1];
            } else {
                coors[1].x = pts[3][0];
                coors[1].y = pts[3][1];
            }
        } else {
            if (pts[0][1] > pts[1][1]) {
                coors[0].x = pts[0][0];
                coors[0].y = pts[0][1];
            } else {
                coors[0].x = pts[1][0];
                coors[0].y = pts[1][1];
            }

            if (pts[2][1] > pts[3][1]) {
                coors[1].x = pts[2][0];
                coors[1].y = pts[2][1];
            } else {
                coors[1].x = pts[3][0];
                coors[1].y = pts[3][1];
            }
        }
    }

    public static void circlesIntersection(RectF c1, RectF c2, PointF[] coors) {
        circlesIntersection(c1.centerX(), c1.centerY(), c1.width() / 2, c2.centerX(), c2.centerY(), c2.width() / 2, coors);
    }

    public static void circlesIntersection(float cx1, float cy1, float r1, float cx2, float cy2, float r2, PointF[] coors) {
        float[] res = null;

        float x = cx2 - cx1;
        float y = cy2 - cy1;
        float q = (r1 * r1 - r2 * r2 + x * x + y * y) / 2;

        if (x != 0) {
            res = intersection(x, y, r1, q, true);
        } else if (y != 0) {
            res = intersection(y, x, r1, q, false);
        }

        if (res != null) {
            for (int i = 0; i < res.length; i++) {
                res[i] += i % 2 == 0 ? cx1 : cy1;
            }

            if (res[1] < res[3]) {
                coors[0].x = res[0];
                coors[0].y = res[1];
                coors[1].x = res[2];
                coors[1].y = res[3];
            } else {
                coors[0].x = res[2];
                coors[0].y = res[3];
                coors[1].x = res[0];
                coors[1].y = res[1];
            }
        }
    }

    public static void tangentLines(float cx, float cy, float r, float px, float py, RectF tangent) {
        float a = 4 * (r * r - px * px - cx * cx + 2 * px * cx);
        float b = 8 * (px * py + cx * cy - px * cy - py * cx);
        float c = 4 * (r * r - py * py - cy * cy + 2 * py * cy);

        float[] ks = quadraticRoots(a, b, c);
        tangent.left = ks[0];
        tangent.top = (py - ks[0] * px);
        tangent.right = ks[1];
        tangent.bottom = (py - ks[1] * px);
    }

    public static float[] circleLineIntersection(float cx, float cy, float r, float k, float l) {
        float a = k * k + 1;
        float b = 2 * (l * k - cx - cy * k);
        float c = l * l + cx * cx + cy * cy - r * r - 2 * l * cy;

        float[] rs = quadraticRoots(a, b, c);
        if (rs.length == 1) {
            return new float[]{rs[0], k * rs[0] + l};
        } else {
            return new float[]{rs[0], k * rs[0] + l, rs[1], k * rs[1] + l};
        }
    }

    private static float[] intersection(float x, float y, float r, float q, boolean order) {
        float a = x * x + y * y;
        float b = -2 * q * y;
        float c = q * q - r * r * x * x;

        float v1, v2;

        float[] ys = quadraticRoots(a, b, c);
        if (ys.length == 1) {
            v1 = (q - ys[0] * y) / x;
            return order ? new float[]{v1, ys[0]} : new float[]{ys[0], v1};
        } else {
            v1 = (q - ys[0] * y) / x;
            v2 = (q - ys[1] * y) / x;
            return order ? new float[]{v1, ys[0], v2, ys[1]} : new float[]{ys[0], v1, ys[1], v2};
        }
    }

    private static float[] quadraticRoots(float a, float b, float c) {
        float[] roots = new float[2];
        float d = b * b - 4.0f * a * c;
        float aa = a + a;

        if (d < 0.0) {
            roots[0] = -b / aa;
            roots[1] = -b / aa;
        } else if (b < 0.0) {
            float re = (float) ((-b + Math.sqrt(d)) / aa);
            roots[0] = re;
            roots[1] = c / (a * re);
        } else {
            float re = (float) ((-b - Math.sqrt(d)) / aa);
            roots[1] = re;
            roots[0] = c / (a * re);
        }
        return roots;
    }

}