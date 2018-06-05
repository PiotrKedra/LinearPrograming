package com.company;

public class Point {
    private Double x;
    private Double y;
    private Pair<Integer,Integer> crossOfLines;

    Point(Double x, Double y, Pair<Integer, Integer> crossOfLines) {
        this.x = x;
        this.y = y;
        this.crossOfLines = crossOfLines;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    Pair<Integer, Integer> getCrossOfLines() {
        return crossOfLines;
    }

    Double getX() {
        return x;
    }

    Double getY() {
        return y;
    }
}
