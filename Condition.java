package com.company;

import java.util.LinkedList;

public class Condition {
    private whichWay whichWay; //          <= i 'min' to 1, >= i 'max' to 2
    private Double equals;
    private LinkedList<Double> arguments;
    private Integer number;

    Condition(Condition.whichWay whichWay, LinkedList<Double> arguments) {
        this.whichWay = whichWay;
        this.arguments = arguments;
    }

    Condition(Double equals, LinkedList<Double> arguments) {
        this.equals = equals;
        this.arguments = arguments;
    }

    Integer getNumber() {
        return number;
    }

    void setNumber(Integer number) {
        this.number = number;
    }

    int getLenght(){
        return arguments.size();
    }

    void setWhichSide(whichWay whichSide) {
        whichWay = whichSide;
    }

    whichWay getWhichSide() {
        return whichWay;
    }

    Double getEquals() {
        return equals;
    }

    LinkedList<Double> getArguments() {
        return arguments;
    }

    public enum whichWay{
        MIN,MAX,SMALLER,GREATER
    }

    @Override
    public String toString() {
        return "Condition{" +
                "whichWay=" + whichWay +
                ", equals=" + equals +
                ", arguments=" + arguments +
                ", number=" + number +
                '}';
    }
}
