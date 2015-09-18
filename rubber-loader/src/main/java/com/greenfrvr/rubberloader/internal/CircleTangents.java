package com.greenfrvr.rubberloader.internal;

/**
 * Created by greenfrvr
 */
public class CircleTangents {

    private float k1, l1;
    private float k2, l2;

    public CircleTangents() {
        k1 = k2 = l1 = l2 = 0;
    }

    public void setTangent1(float k1, float l1) {
        this.k1 = k1;
        this.l1 = l1;
    }

    public void setTangent2(float k2, float l2) {
        this.k2 = k2;
        this.l2 = l2;
    }

    public float getK1() {
        return k1;
    }

    public void setK1(float k1) {
        this.k1 = k1;
    }

    public float getL1() {
        return l1;
    }

    public void setL1(float l1) {
        this.l1 = l1;
    }

    public float getK2() {
        return k2;
    }

    public void setK2(float k2) {
        this.k2 = k2;
    }

    public float getL2() {
        return l2;
    }

    public void setL2(float l2) {
        this.l2 = l2;
    }
}
