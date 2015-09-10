package com.greenfrvr.rubberloader;

import android.graphics.RectF;

/**
 * Created by greenfrvr
 */
class Calculator {

    public static double[][] evaluateBezierEndpoints(RectF c1, RectF c2, float px, float py, boolean top) {
        return evaluateBezierEndpoints(c1.centerX(), c1.centerY(), c1.width() / 2, c2.centerX(), c2.centerY(), c2.width() / 2, px, py, top);
    }

    public static double[][] evaluateBezierEndpoints(float cx1, float cy1, float r1, float cx2, float cy2, float r2, float px, float py, boolean top) {
        double[] lines1 = tangentLines(cx1, cy1, r1, px, py);
        double[] lines2 = tangentLines(cx2, cy2, r2, px, py);

        double[][] pts = new double[4][2];

        pts[0] = circleLineIntersection(cx1, cy1, r1, lines1[0], lines1[1]);
        pts[1] = circleLineIntersection(cx1, cy1, r1, lines1[2], lines1[3]);

        pts[2] = circleLineIntersection(cx2, cy2, r2, lines2[0], lines2[1]);
        pts[3] = circleLineIntersection(cx2, cy2, r2, lines2[2], lines2[3]);

        return top ?
                new double[][]{pts[0][1] < pts[1][1] ? pts[0] : pts[1], pts[2][1] < pts[3][1] ? pts[2] : pts[3]} :
                new double[][]{pts[0][1] > pts[1][1] ? pts[0] : pts[1], pts[2][1] > pts[3][1] ? pts[2] : pts[3]};
    }


    public static double[][] circlesIntersection(RectF c1, RectF c2) {
        return circlesIntersection(c1.centerX(), c1.centerY(), c1.width() / 2, c2.centerX(), c2.centerY(), c2.width() / 2);
    }

    public static double[][] circlesIntersection(float cx1, float cy1, float r1, float cx2, float cy2, float r2) {
        double[] res = null;

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
            return res[1] < res[3] ? new double[][]{{res[0], res[1]}, {res[2], res[3]}} : new double[][]{{res[2], res[3]}, {res[0], res[1]}};
        }
        return null;
    }

    public static double[] tangentLines(float cx, float cy, float r, float px, float py) {
        float a = 4 * (r * r - px * px - cx * cx + 2 * px * cx);
        float b = 8 * (px * py + cx * cy - px * cy - py * cx);
        float c = 4 * (r * r - py * py - cy * cy + 2 * py * cy);

        double[] ks = quadraticRoots(a, b, c);
        if (ks.length == 1) {
            return new double[]{ks[0], py - ks[0] * px};
        } else {
            return new double[]{ks[0], py - ks[0] * px, ks[1], py - ks[1] * px};
        }
    }

    public static double[] circleLineIntersection(float cx, float cy, float r, double k, double l) {
        double a = k * k + 1;
        double b = 2 * (l * k - cx - cy * k);
        double c = l * l + cx * cx + cy * cy - r * r - 2 * l * cy;

        double[] rs = quadraticRoots(a, b, c);
        if (rs.length == 1) {
            return new double[]{rs[0], k * rs[0] + l};
        } else {
            return new double[]{rs[0], k * rs[0] + l, rs[1], k * rs[1] + l};
        }
    }

    private static double[] intersection(float x, float y, float r, float q, boolean order) {
        float a = x * x + y * y;
        float b = -2 * q * y;
        float c = q * q - r * r * x * x;

        double v1, v2;

        double[] ys = quadraticRoots(a, b, c);
        if (ys.length == 1) {
            v1 = (q - ys[0] * y) / x;
            return order ? new double[]{v1, ys[0]} : new double[]{ys[0], v1};
        } else {
            v1 = (q - ys[0] * y) / x;
            v2 = (q - ys[1] * y) / x;
            return order ? new double[]{v1, ys[0], v2, ys[1]} : new double[]{ys[0], v1, ys[1], v2};
        }
    }

    private static double[] quadraticRoots(double a, double b, double c) {
        double[] roots = new double[2];
        double d = b * b - 4.0 * a * c;
        double aa = a + a;

        if (d < 0.0) {
            roots[0] = -b / aa;
            roots[1] = -b / aa;
        } else if (b < 0.0) {
            double re = (-b + Math.sqrt(d)) / aa;
            roots[0] = re;
            roots[1] = c / (a * re);
        } else {
            double re = (-b - Math.sqrt(d)) / aa;
            roots[1] = re;
            roots[0] = c / (a * re);
        }
        return roots;
    }

}