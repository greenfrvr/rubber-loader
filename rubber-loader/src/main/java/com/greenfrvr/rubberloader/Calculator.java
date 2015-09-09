package com.greenfrvr.rubberloader;

/**
 * Created by greenfrvr
 */
class Calculator {

    public static void circlesIntersection(float cx1, float cy1, float r1, float cx2, float cy2, float r2) {
        System.out.println("First circle - [(x,y):(" + cx1 + "," + cy1 + "), r: " + r1 + ", " +
                "Second circle - [(x,y):(" + cx2 + "," + cy2 + ", r: " + r2 + ")");

        float x = cx2 - cx1;
        float y = cy2 - cy1;
        float q = (r1 * r1 - r2 * r2 + x * x + y * y) / 2;

        float a, b, c;
        double v1, v2;

        if (x != 0) {
            a = 1 + (y * y) / (x * x);
            b = (2 * q * y) / (x * x);
            c = (q * q) / (x * x) - r1 * r1;

            try {
                double[] ys = roots(a, b, c);
                if (ys.length == 1) {
                    v1 = (c - ys[0] * y) / x;
                    System.out.println("Circles intersect in one point: [" + v1 + ", " + ys[0] + "]");
                } else {
                    v1 = (c - ys[0] * y) / x;
                    v2 = (c - ys[1] * y) / x;
                    System.out.println("Circles intersect in points: [" + v1 + ", " + ys[0] + "], [" + v2 + ", " + ys[1] + "]");
                }
            } catch (NoSolutionException e) {
                System.err.println("Circles don't intersect");
            } catch (NonEquationException e) {
                System.err.println(e.getMessage());
            }
        } else if (y != 0) {
            a = 1 + (x * x) / (y * y);
            b = (2 * q * x) / (y * y);
            c = (q * q) / (y * y) - r1 * r1;

            try {
                double[] xs = roots(a, b, c);
                if (xs.length == 1) {
                    v1 = (c - xs[0] * y) / x;
                    System.out.println("Circles intersect in points: [" + xs[0] + ", " + v1 + "]");
                } else {
                    v1 = (c - xs[0] * y) / x;
                    v2 = (c - xs[1] * y) / x;
                    System.out.println("Circles intersect in points: [" + xs[0] + ", " + v1 + "], [" + xs[1] + ", " + v2 + "]");
                }
            } catch (NoSolutionException e) {
                System.err.println("Circles don't intersect");
            } catch (NonEquationException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("ERROR");
        }
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