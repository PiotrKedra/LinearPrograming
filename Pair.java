package com.company;

public class Pair {
    private Point first;
    private Point second;

    public Pair(Point first, Point second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(Point first) {
        this.first = first;
    }

    public void setSecond(Point second) {
        this.second = second;
    }

    public Point getFirst() {
        return first;
    }

    public Point getSecond() {
        return second;
    }
}
