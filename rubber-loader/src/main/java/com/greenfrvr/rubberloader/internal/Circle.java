package com.greenfrvr.rubberloader.internal;

/**
 * Created by greenfrvr
 */
public class Circle {

    private float x;
    private float y;
    private float r;

    public Circle() {
    }

    public void set(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void offset(float dx, float dy, float dr) {
        this.x += dx;
        this.y += dy;
        this.r += dr;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }
}
