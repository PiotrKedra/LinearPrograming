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


    static Condition mc1 = new Condition(12000.0,new LinkedList<>(Arrays.asList(40.0,45.0,40.0,48.0)));
    static Condition mc2 = new Condition(6000.0,new LinkedList<>(Arrays.asList(30.0,15.0,36.0,12.0)));

    static Condition goalFunction = new Condition(Condition.whichWay.MAX, new LinkedList<>(Arrays.asList(12000.0,6000.0)));

    public static void main(String[] args) {
        c1.setNumber(0);
        c2.setNumber(1);
        c3.setNumber(2);
        c4.setNumber(3);
        c1.setWhichSide(Condition.whichWay.SMALLER);
        c4.setWhichSide(Condition.whichWay.SMALLER);
        c2.setWhichSide(Condition.whichWay.SMALLER);
        c3.setWhichSide(Condition.whichWay.SMALLER);
        System.out.println(getRestrictivePoints(new LinkedList<>(Arrays.asList(c1,c2,c3,c4)), Condition.whichWay.MAX));
        System.out.println(getBestSolutionForPP(getRestrictivePoints(new LinkedList<>(Arrays.asList(c1,c2,c3,c4)), Condition.whichWay.MAX), goalFunction));

        System.out.println(getSolutionPointFromPDToPP(getBestSolutionForPP(getRestrictivePoints(new LinkedList<>(Arrays.asList(c1,c2,c3,c4)), Condition.whichWay.MAX), goalFunction),new LinkedList<>(Arrays.asList(mc1,mc2))));

        System.out.println();
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

    static private Point getSolutionPointFromPDToPP(Pair<Point,Double> PDsolution, LinkedList<Condition> conditions){
        Integer numberOfProperCondition1 = PDsolution.getFirst().getCrossOfLines().getFirst();
        Integer numberOfProperCondition2 = PDsolution.getFirst().getCrossOfLines().getSecond();
        for(Condition condition:conditions) System.out.println(condition.getArguments());
        Condition newCondition1 = new Condition(conditions.getFirst().getEquals(),
                new LinkedList<>(Arrays.asList(conditions.getFirst().getArguments().get(numberOfProperCondition1),
                        conditions.getFirst().getArguments().get(numberOfProperCondition2))));
        Condition newCondition2 = new Condition(conditions.get(1).getEquals(),
                new LinkedList<>(Arrays.asList(conditions.get(1).getArguments().get(numberOfProperCondition1),
                        conditions.get(1).getArguments().get(numberOfProperCondition2))));
        return getCrossPoint(newCondition1,newCondition2);
    }

    //
    static private Pair<Point,Double> getBestSolutionForPP(LinkedList<Point> restrictivePoints, Condition goalFunction){ //PP - Primary Program
        Double solution = restrictivePoints.getFirst().getX()*goalFunction.getArguments().getFirst()+restrictivePoints.getFirst().getY()*goalFunction.getArguments().get(1);
        Point solutionPoint = restrictivePoints.getFirst();
        switch (goalFunction.getWhichSide()) {
            case MAX:
                for (Point point : restrictivePoints) {
                    if (solution < point.getX()*goalFunction.getArguments().getFirst()+point.getY()*goalFunction.getArguments().get(1)) {
                        solution = point.getX() * goalFunction.getArguments().getFirst() + point.getY() * goalFunction.getArguments().get(1);
                        solutionPoint = point;
                    }
                }
                break;
            case MIN:
                for (Point point : restrictivePoints) {
                    if (solution > point.getX()*goalFunction.getArguments().getFirst()+point.getY()*goalFunction.getArguments().get(1)) {
                        solution = point.getX() * goalFunction.getArguments().getFirst() + point.getY() * goalFunction.getArguments().get(1);
                        solutionPoint = point;
                    }
                }
                break;
        }
        return new Pair<>(solutionPoint,solution);
    }

    /*static private LinkedList<Point> getRestrictivePoints(LinkedList<Condition> conditions, final Condition.whichWay goalFunctionMinOrMax){
        LinkedList<Point> restrictivePoints = new LinkedList<>();
        LinkedList<Point> allPoints = getAllCross(conditions);
        Point restrictivePoint = allPoints.getFirst();
        Pair<Point,Point> restrictivePointsOnAxes = getCrossWithAxes(conditions.getFirst()) ;
        Pair<Point,Point> tmpPair;
        switch (goalFunctionMinOrMax){
            case MAX:
                for(Point point:allPoints){
                    if(restrictivePoint.getDistanceFromCenter() > point.getDistanceFromCenter()){
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
                    if(restrictivePoint.getDistanceFromCenter() < point.getDistanceFromCenter()){
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
    }*/

    static private LinkedList<Point> getRestrictivePoints(LinkedList<Condition> conditions, Condition.whichWay goalFunctionMinOrMax){
        LinkedList<Point> restrictivePoints = new LinkedList<>();
        LinkedList<Point> allPoints = getAllCross(conditions);

        for(Point point: allPoints){
            if(checkIfPointIsGoodWithCondition(point,conditions)){
                restrictivePoints.add(point);
            }
        }
        Pair<Point,Point> restrictivePointsOnAxes = getCrossWithAxes(conditions.getFirst()) ;
        Pair<Point,Point> tmpPair;
        switch (goalFunctionMinOrMax) {
            case MAX:
                for (Condition condition : conditions) {
                    tmpPair = getCrossWithAxes(condition);
                    if (restrictivePointsOnAxes.getFirst().getY() > tmpPair.getFirst().getY()) {

                        restrictivePointsOnAxes.setFirst(tmpPair.getFirst());
                    }
                    if (restrictivePointsOnAxes.getSecond().getX() > tmpPair.getSecond().getX()) {
                        restrictivePointsOnAxes.setSecond(tmpPair.getSecond());
                    }
                }
                break;
            case MIN:
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
        restrictivePoints.add(restrictivePointsOnAxes.getFirst());
        restrictivePoints.add(restrictivePointsOnAxes.getSecond());
        return restrictivePoints;
    }

    static private boolean checkIfPointIsGoodWithCondition(Point point, LinkedList<Condition> conditions){
        boolean result = true;
        for (Condition condition:conditions){
            switch (condition.getWhichSide()) {
                case SMALLER:
                    if (condition.getArguments().get(0) * point.getX() + condition.getArguments().get(1) * point.getY() > condition.getEquals()) {
                        result = false;
                    }
                    break;
                case GREATER:
                    if(condition.getArguments().get(0) * point.getX() + condition.getArguments().get(1) * point.getY() < condition.getEquals()){
                        result = false;
                    }
                    break;
            }

        }
        return result;
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
    static private Pair<Point,Point> getCrossWithAxes(Condition condition){
        Point pointOnY = new Point(0.0,condition.getEquals()/condition.getArguments().get(1),new Pair<>(condition.getNumber(),-1));
        Point pointOnX = new Point(condition.getEquals()/condition.getArguments().getFirst(),0.0,new Pair<>(-1,condition.getNumber()));
        return new Pair<>(pointOnY,pointOnX);
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
        return new Point(x,y,new Pair<>(conditon1.getNumber(),conditon2.getNumber()));
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
