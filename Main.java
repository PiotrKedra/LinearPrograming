package com.company;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    static Condition c1 = new Condition(24.0,new LinkedList<>(Arrays.asList(40.0,30.0)));
    static Condition c2 = new Condition(27.4,new LinkedList<>(Arrays.asList(45.0,15.0)));
    static Condition c3 = new Condition(36.0,new LinkedList<>(Arrays.asList(40.0,36.0)));
    static Condition c4 = new Condition(14.4,new LinkedList<>(Arrays.asList(48.0,12.0)));

    public static void main(String[] args) {
        System.out.println(getListOfRestrictivePoints(new LinkedList<>(Arrays.asList(c1,c2,c3,c4)), Condition.whichWay.MAX));
    }

    private void mainMain(){
        Scanner line = new Scanner(System.in);
        boolean nextInput = true;
        System.out.println("Podaj warunki ograniczajace:");
        String s;
        while(nextInput) {
            System.out.print("warunek: ");
            s = line.nextLine();
            System.out.println("Jezeli chcesz podac kolejne ograniczenia nacisnij 1, jezeli nie to 2.");
            s = line.nextLine();
            if(s.equals("1")) nextInput = false;
        }
        System.out.println("");
    }

    static private LinkedList<Point> getListOfRestrictivePoints(LinkedList<Condition> conditions, final Condition.whichWay goalFunctionMinOrMax){
        LinkedList<Point> restrictivePoints = new LinkedList<>();
        LinkedList<Point> allPoints = getAllCross(conditions);
        Point restrictivePoint = allPoints.getFirst();
        Pair restrictivePointsOnAxes = getCrossWithAxes(conditions.getFirst()) ;
        Pair tmpPair;
        switch (goalFunctionMinOrMax){
            case MAX:
                for(Point point:allPoints){
                    if(restrictivePoint.getDistancFromCenter() > point.getDistancFromCenter()){
                        restrictivePoint = point;
                    }
                }
                for (Condition condition:conditions){
                    tmpPair = getCrossWithAxes(condition);
                    if(restrictivePointsOnAxes.getFirst().getY() > tmpPair.getFirst().getY()){

                        restrictivePointsOnAxes.setFirst(tmpPair.getFirst());
                    }
                    if(restrictivePointsOnAxes.getSecond().getX() > tmpPair.getSecond().getX()){
                        restrictivePointsOnAxes.setSecond(tmpPair.getSecond());
                    }
                }
                break;
            case MIN:
                for (Point point:allPoints){
                    if(restrictivePoint.getDistancFromCenter() < point.getDistancFromCenter()){
                        restrictivePoint = point;
                    }
                }
                for (Condition condition:conditions){
                    tmpPair = getCrossWithAxes(condition);
                    if(restrictivePointsOnAxes.getFirst().getY() < tmpPair.getFirst().getY()){
                        restrictivePointsOnAxes.setFirst(tmpPair.getFirst());
                    }
                    if(restrictivePointsOnAxes.getSecond().getX() < tmpPair.getSecond().getX()){
                        restrictivePointsOnAxes.setSecond(tmpPair.getSecond());
                    }
                }
                break;
        }
        restrictivePoints.add(restrictivePoint);
        restrictivePoints.add(restrictivePointsOnAxes.getFirst());
        restrictivePoints.add(restrictivePointsOnAxes.getSecond());
        return restrictivePoints;
    }

    //it return all cross point between all the condition (if x,y >=0)
    static private LinkedList<Point> getAllCross(LinkedList<Condition> conditons){
        LinkedList<Point> points = new LinkedList<>();
        for(int i=0;i<conditons.size()-1;++i){
            Condition c1 = conditons.get(i);
            for(int j=(i+1);j<conditons.size();++j){
                Condition c2 = conditons.get(j);
                Point point = getCrossPoint(c1,c2);
                if(point.getX()>=0 && point.getY()>=0) points.add(point);
            }
        }
        return points;
    }

    //returns crosses with axes
    static private Pair getCrossWithAxes(Condition condition){
        Point pointOnY = new Point(0.0,condition.getEquals()/condition.getArguments().get(1));
        Point pointOnX = new Point(condition.getEquals()/condition.getArguments().getFirst(),0.0);
        return new Pair(pointOnY,pointOnX);
    }

    //it gets cross point of two function: Ax + Bx = C
    static private Point getCrossPoint(Condition conditon1, Condition conditon2){
        Double a1 = conditon1.getArguments().getFirst();
        Double b1 = conditon1.getArguments().get(1);
        Double c1 = conditon1.getEquals();
        Double a2 = conditon2.getArguments().getFirst();
        Double b2 = conditon2.getArguments().get(1);
        Double c2 = conditon2.getEquals();
        Double mainDeterminant = a1*b2-b1*a2;
        Double xDeterminant = c1*b2-b1*c2;
        Double yDeterminant = a1*c2-c1*a2;
        Double x = xDeterminant/mainDeterminant;
        Double y = yDeterminant/mainDeterminant;
        return new Point(x,y);
    }

    static private LinkedList<Double> getArgumentsFromConditions(String conditon){
        LinkedList<Double> arguments = new LinkedList<>();
        Pattern pattern1 = Pattern.compile("<=");
        Pattern pattern2 = Pattern.compile("\\+");
        Pattern pattern3 = Pattern.compile("\\*");
        String[] equal = pattern1.split(conditon);
        String[] argWithXs = pattern2.split(equal[0]);
        for(String ele: argWithXs){
            String[] argXs = pattern3.split(ele);
            arguments.addLast(Double.parseDouble(argXs[0]));
        }

        return arguments;
    }

    private Double getNumberOfX(String xNumber){
        char[] number = xNumber.toCharArray();
        LinkedList<Character> rlyNubmer = new LinkedList<>();
        for(int i=1;i<number.length;++i){
            rlyNubmer.addLast(number[i]);
        }
        String resultString = "";
        for(Character ch:rlyNubmer) resultString += ch;
        return Double.parseDouble(resultString);
    }
}
