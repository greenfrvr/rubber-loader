package com.greenfrvr.rubberloader.calculation;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by greenfrvr
 */
public class Intersection {

    private PointF roots;

    public static Intersection newInstance() {
        return new Intersection();
    }

    private Intersection() {
        roots = new PointF();
    }

    public void circlesIntersection(RectF c1, RectF c2, PointF[] coors) {
        circlesIntersection(c1.centerX(), c1.centerY(), c1.width() / 2, c2.centerX(), c2.centerY(), c2.width() / 2, coors);
    }

    public void circlesIntersection(float cx1, float cy1, float r1, float cx2, float cy2, float r2, PointF[] coors) {
        float x = cx2 - cx1;
        float y = cy2 - cy1;
        float q = (r1 * r1 - r2 * r2 + x * x + y * y) / 2;

        if (x != 0) {
            intersection(x, y, r1, q, true, coors);
        } else if (y != 0) {
            intersection(y, x, r1, q, false, coors);
        }

        coors[0].offset(cx1, cy1);
        coors[1].offset(cx1, cy1);
    }

    private void intersection(float x, float y, float r, float q, boolean order, PointF[] coors) {
        float a = x * x + y * y;
        float b = -2 * q * y;
        float c = q * q - r * r * x * x;

        quadraticRoots(a, b, c);

        float v1 = (q - roots.x * y) / x;
        float v2 = (q - roots.y * y) / x;

        if (order) {
            if (roots.x < roots.y) {
                coors[0].x = v1;
                coors[0].y = roots.x;
                coors[1].x = v2;
                coors[1].y = roots.y;
            } else {
                coors[0].x = v2;
                coors[0].y = roots.y;
                coors[1].x = v1;
                coors[1].y = roots.x;
            }
        } else {
            if (roots.x < roots.y) {
                coors[0].x = roots.x;
                coors[0].y = v1;
                coors[1].x = roots.y;
                coors[1].y = v2;
            } else {
                coors[0].x = roots.y;
                coors[0].y = v2;
                coors[1].x = roots.x;
                coors[1].y = v1;
            }
        }
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
