package com.greenfrvr.rubberloader;

import java.util.Arrays;

/**
 * Created by greenfrvr
 */
class Calculator {


    /**
     * Find two circles intersection points. Three situation are possible:
     * <br/>1. Circles intersect in 2 points
     * <br/>2. Circles intersect in 1 point
     * <br/>3. Circles has no intersection points
     *
     * @param cx1 first circle x coordinate
     * @param cy1 first circle y coordinate
     * @param r1  first circle radius
     * @param cx2 second circle x coordinate
     * @param cy2 second circle y coordinate
     * @param r2  second circle radius
     * @return array of intersection points in form of <i>[x0, y0, x1, y2]</i>, null if circles have equal center coordinates
     */
    public static double[] circlesIntersection(float cx1, float cy1, float r1, float cx2, float cy2, float r2) {
        System.out.println("First circle - [(x,y):(" + cx1 + "," + cy1 + "), r: " + r1 + ", " +
                "Second circle - [(x,y):(" + cx2 + "," + cy2 + ", r: " + r2 + ")");

        double[] res = null;

        float x = cx2 - cx1;
        float y = cy2 - cy1;
        float q = (r1 * r1 - r2 * r2 + x * x + y * y) / 2;

        if (x != 0) {
            res = intersection(x, y, r1, q, true);
        } else if (y != 0) {
            res = intersection(y, x, r1, q, false);
        } else {
            System.out.println("Centers have equal coordinates");
        }

        if (res != null) {
            for (int i = 0; i < res.length; i++) {
                res[i] += i % 2 == 0 ? cx1 : cy1;
            }
            System.out.println(Arrays.toString(res));
        }
        return res;
    }

    private static double[] intersection(float x, float y, float r, float q, boolean order) {

        float a = x * x + y * y;
        float b = -2 * q * y;
        float c = q * q - r * r * x * x;

        double v1, v2;

        try {
            double[] ys = roots(a, b, c);
            if (ys.length == 1) {
                v1 = (q - ys[0] * y) / x;
                return order ? new double[]{v1, ys[0]} : new double[]{ys[0], v1};
            } else {
                v1 = (q - ys[0] * y) / x;
                v2 = (q - ys[1] * y) / x;
                return order ? new double[]{v1, ys[0], v2, ys[1]} : new double[]{ys[0], v1, ys[1], v2};
            }
        } catch (NoSolutionException e) {
            System.out.println(e.getMessage());
        } catch (NonEquationException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static double[] roots(float a, float b, float c) throws NoSolutionException, NonEquationException {
        if (a == 0) {
            if (b != 0)
                return new double[]{-c / b};
            else
                throw new NonEquationException();
        }

        float D = b * b - 4 * a * c;

        if (D < 0) throw new NoSolutionException();

        if (D == 0)
            return new double[]{(-b + Math.sqrt(D)) / (2 * a)};
        else
            return new double[]{(-b + Math.sqrt(D)) / (2 * a), (-b - Math.sqrt(D)) / (2 * a)};
    }

    public static class NonEquationException extends Exception {

        @Override
        public String getMessage() {
            return "Given coefficients are not representing any equation";
        }
    }

    public static class NoSolutionException extends Exception {

        @Override
        public String getMessage() {
            return "Given equation has no solution in Real numbers";
        }
    }

}