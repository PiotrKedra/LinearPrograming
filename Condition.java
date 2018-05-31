package com.company;

import java.util.LinkedList;

public class Condition {
    private boolean isItGoalFunction = false;
    private whichWay whichWay; //          <= i 'min' to 1, >= i 'max' to 2
    private Double equals;
    private LinkedList<Double> arguments;

    public Condition(Double equals, LinkedList<Double> arguments) {
        this.equals = equals;
        this.arguments = arguments;
    }

    public int getLenght(){
        return arguments.size();
    }

    public void setWhichSide(whichWay whichSide) {
        whichWay = whichSide;
    }

    public void setEquals(Double equals) {
        this.equals = equals;
    }

    public void setArguments(LinkedList<Double> arguments) {
        this.arguments = arguments;
    }

    public whichWay getWhichSide() {
        return whichWay;
    }

    public Double getEquals() {
        return equals;
    }

    public LinkedList<Double> getArguments() {
        return arguments;
    }

    public enum whichWay{
        MIN,MAX,SMALLER,GREATER
    }
}
