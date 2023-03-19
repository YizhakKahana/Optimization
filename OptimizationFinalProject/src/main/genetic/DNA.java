package main.genetic;

public class DNA {
    byte[] bits;
    public int fitness;

    public DNA(int bits) {
        this.bits = new byte[bits];
    }
}
