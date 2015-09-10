package com.greenfrvr.rubberloader;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CircleIntersectionsUnitTest {

    @Test
    public void intersectionTest1() throws Exception {
        System.out.println("\nTest 1 (Equal y coors, 2 intersection points)");
        Calculator.circlesIntersection(-40, 0, 40, 32, 0, 48);
    }

    @Test
    public void intersectionTest2() throws Exception {
        System.out.println("\nTest 2 (Equal x coors, 2 intersection points)");
        Calculator.circlesIntersection(5, 32, 40, 5, 6, 48);
    }

    @Test
    public void intersectionTest3() throws Exception {
        System.out.println("\nTest 3 (Different coors, 2 intersection points)");
        Calculator.circlesIntersection(-10, 32, 40, 13, -8, 48);
    }

    @Test
    public void intersectionTest4() throws Exception {
        System.out.println("\nTest 6 (Different coors, 2 intersection points)");
        Calculator.circlesIntersection(-35.31088913245535f, -8.839745962155614f, 38, 38.97114317029974f, 22.5f, 45);
    }

    @Test
    public void intersectionTest5() throws Exception {
        System.out.println("\nTest 4 (One intersection point)");
        Calculator.circlesIntersection(-40, 0, 40, 40, 0, 40);
    }

    @Test
    public void intersectionTest6() throws Exception {
        System.out.println("\nTest 5 (No intersection)");
        Calculator.circlesIntersection(-35.31088913245535f, -8.839745962155614f, 40, 38.97114317029974f, 22.5f, 40);
    }

    @Test
    public void intersectionTest7() throws Exception {
        System.out.println("\nTest 7 (Equal centers)");
        Calculator.circlesIntersection(45, 0, 40, 45, 0, 40);
    }
}