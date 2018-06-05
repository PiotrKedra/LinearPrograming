package com.company;

public class Point {
    private Double x;
    private Double y;
    private Pair<Integer,Integer> crossOfLines;

    public Point(Double x, Double y, Pair<Integer, Integer> crossOfLines) {
        this.x = x;
        this.y = y;
        this.crossOfLines = crossOfLines;
    }

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getDistanceFromCenter(){
        return Math.sqrt(this.getX()*this.getX()+this.getY()*this.getY());
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void setCrossOfLines(Pair<Integer, Integer> crossOfLines) {
        this.crossOfLines = crossOfLines;
    }

    public Pair<Integer, Integer> getCrossOfLines() {
        return crossOfLines;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
