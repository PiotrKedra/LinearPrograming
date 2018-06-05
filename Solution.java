package com.company;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*this example work just for all kind of PP, but if we have to change for PD its juz work for input like:
40*x1 + 45*x2 + 40*x3 + 48*x4 >= 12000
30*x1 + 15*x2 + 30*x3 + 12*x4 >= 6000
G(x1,x2,x3,x4) = 24*x1 + 27*x2 + 36*x3 + 14.4*x4 -> min
input has to look like above example
 */ 

class Solution {
    void handleInput(){
        Scanner line = new Scanner(System.in);
        boolean nextInput = true;
        System.out.println("Podaj warunki ograniczajace:");
        String s;
        LinkedList<String> conditionsConditions = new LinkedList<>();
        while(nextInput) {
            System.out.print("warunek: ");
            s = line.nextLine().replaceAll("\\s","");
            conditionsConditions.addLast(s);
            System.out.println("Jezeli chcesz podac kolejne ograniczenia nacisnij 1, jezeli funkcje celu to 2.");
            s = line.nextLine();
            if(!s.equals("1")) nextInput = false;
        }
        System.out.println("Podaj funkcje celu: ");
        String goalFunctionString = line.nextLine().replaceAll("\\s","");
        LinkedList<Condition> conditions = new LinkedList<>();
        int i=0;
        for(String c:conditionsConditions) {
            Condition condition = changeStringToConditions(c);
            condition.setNumber(i);
            ++i;
            conditions.addLast(condition);
        }
        Condition goalFunction = changeStringToGoalFunction(goalFunctionString);
        if(conditions.getFirst().getLenght()>2){ //if it is greater then 2 we have to change PP To DP
            Pair<LinkedList<Condition>,Condition> PD = changePPToPD(conditions,goalFunction);
            LinkedList<Point> restrictivePoints = getRestrictivePoints(PD.getFirst(),PD.getSecond().getWhichSide());
            Pair<Point,Double> solutionForPD = getBestSolutionForChangedPP(restrictivePoints,PD.getSecond());
            Point solutionPoint = getSolutionPointFromPDToPP(solutionForPD,conditions);
            System.out.println("Lista punktów ograniczających zbiór rozwiązań dopuszczalnych dla PD: ");
            for(Point point: restrictivePoints) System.out.println(point);
            System.out.println("Punkt realizujacy optimum PP: " + solutionPoint);
            System.out.println("Wartość w tym punkcie: " + solutionForPD.getSecond());
        }else {
            LinkedList<Point> restrictivePoints = getRestrictivePoints(conditions,goalFunction.getWhichSide());
            Pair<Point,Double> solutionForPD = getBestSolutionForChangedPP(restrictivePoints,goalFunction);
            System.out.println("Lista punktów ograniczających zbiór rozwiązań dopuszczalnych: ");
            for(Point point: restrictivePoints) System.out.println(point);
            System.out.println("Punkt realizujacy optimum PP: " + solutionForPD.getFirst());
            System.out.println("Wartość w tym punkcie: " + solutionForPD.getSecond());
        }
    }

    private Point getSolutionPointFromPDToPP(Pair<Point,Double> PDsolution, LinkedList<Condition> conditions){
        Integer numberOfProperCondition1 = PDsolution.getFirst().getCrossOfLines().getFirst();
        Integer numberOfProperCondition2 = PDsolution.getFirst().getCrossOfLines().getSecond();
        Condition newCondition1 = new Condition(conditions.getFirst().getEquals(),
                new LinkedList<>(Arrays.asList(conditions.getFirst().getArguments().get(numberOfProperCondition1),
                        conditions.getFirst().getArguments().get(numberOfProperCondition2))));
        Condition newCondition2 = new Condition(conditions.get(1).getEquals(),
                new LinkedList<>(Arrays.asList(conditions.get(1).getArguments().get(numberOfProperCondition1),
                        conditions.get(1).getArguments().get(numberOfProperCondition2))));
        return getCrossPoint(newCondition1,newCondition2);
    }

    //
    private Pair<Point,Double> getBestSolutionForChangedPP(LinkedList<Point> restrictivePoints, Condition goalFunction){ //PP - Primary Program
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

    private LinkedList<Point> getRestrictivePoints(LinkedList<Condition> conditions, Condition.whichWay goalFunctionMinOrMax){
        LinkedList<Point> restrictivePoints = new LinkedList<>();
        LinkedList<Point> allPoints = getAllCross(conditions);

        for(Point point: allPoints){
            //System.out.println(point);
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

    private boolean checkIfPointIsGoodWithCondition(Point point, LinkedList<Condition> conditions){
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
    private LinkedList<Point> getAllCross(LinkedList<Condition> conditons){
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
    private Pair<Point,Point> getCrossWithAxes(Condition condition){
        Point pointOnY = new Point(0.0,condition.getEquals()/condition.getArguments().get(1),new Pair<>(condition.getNumber(),-1));
        Point pointOnX = new Point(condition.getEquals()/condition.getArguments().getFirst(),0.0,new Pair<>(-1,condition.getNumber()));
        return new Pair<>(pointOnY,pointOnX);
    }

    //it gets cross point of two function: Ax + Bx = C
    private Point getCrossPoint(Condition conditon1, Condition conditon2){
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
        return new Point(x,y,new Pair<>(conditon1.getNumber(),conditon2.getNumber())); //add this... +0.000000000000004
    }

    private Pair<LinkedList<Condition>, Condition> changePPToPD(LinkedList<Condition> conditions, Condition goalFunction){
        LinkedList<Condition> conditionsPD = new LinkedList<>();
        Condition.whichWay whichWayConditions = conditions.getFirst().getWhichSide();
        if(whichWayConditions.equals(Condition.whichWay.SMALLER)) whichWayConditions = Condition.whichWay.GREATER;
        else whichWayConditions = Condition.whichWay.SMALLER;
        int numberOfPDConditions = conditions.getFirst().getLenght();
        for(int i=0;i<numberOfPDConditions;++i){
            Condition condition = new Condition(goalFunction.getArguments().get(i),new LinkedList<>(Arrays.asList(conditions.get(0).getArguments().get(i),conditions.get(1).getArguments().get(i))));
            condition.setWhichSide(whichWayConditions);
            condition.setNumber(i);
            conditionsPD.addLast(condition);
        }

        Condition.whichWay whichWayGoalFunction = goalFunction.getWhichSide();
        if(whichWayGoalFunction.equals(Condition.whichWay.MAX)) whichWayGoalFunction = Condition.whichWay.MIN;
        else whichWayGoalFunction = Condition.whichWay.MAX;
        Condition goalFunctionPD = new Condition(whichWayGoalFunction,new LinkedList<>(Arrays.asList(conditions.get(0).getEquals(),conditions.get(1).getEquals())));
        return new Pair<>(conditionsPD,goalFunctionPD);
    }

    ////those function change input form users to my classes, so it is pointless to read ;o

    private Condition changeStringToGoalFunction(String goalFunction){
        LinkedList<Double> arguments;
        Pattern pattern1 = Pattern.compile("->");
        Pattern pattern2 = Pattern.compile("=");
        Pattern pattern3 = Pattern.compile("\\+");
        String[] equalSplitTab = pattern1.split(goalFunction);
        String[] argumentsTab = pattern2.split(equalSplitTab[0]);
        Condition.whichWay whichWay;
        if(equalSplitTab[1].equals("max")) whichWay = Condition.whichWay.MAX;
        else whichWay = Condition.whichWay.MIN;

        String[] argWithXs = pattern3.split(argumentsTab[1]);
        arguments = changeStringArgumentsToDoubles(argWithXs);
        return new Condition(whichWay,arguments);
    }

    private Condition changeStringToConditions(String conditon){
        LinkedList<Double> arguments;
        Pattern pattern1 = Pattern.compile("(<=)|(>=)");
        String[] equalSplitTab = pattern1.split(conditon);
        Matcher matcher = pattern1.matcher(conditon);
        Condition.whichWay whichWay = Condition.whichWay.SMALLER;
        while (matcher.find()){
            if(matcher.group(0).equals("<=")) whichWay = Condition.whichWay.SMALLER;
            else whichWay = Condition.whichWay.GREATER;
        }
        Pattern pattern2 = Pattern.compile("\\+");
        String[] argWithXs = pattern2.split(equalSplitTab[0]);
        arguments = changeStringArgumentsToDoubles(argWithXs);
        Condition result = new Condition(Double.parseDouble(equalSplitTab[1]),arguments);
        result.setWhichSide(whichWay);
        return result;
    }

    private LinkedList<Double> changeStringArgumentsToDoubles(String[] argWithXs){
        LinkedList<Double> arguments = new LinkedList<>();
        Pattern pattern3 = Pattern.compile("\\*");
        Integer numberOfX = 1;
        for(String ele: argWithXs){
            String[] argXs = pattern3.split(ele);
            if(getNumberOfX(argXs[1]).equals(numberOfX)) arguments.addLast(Double.parseDouble(argXs[0]));
            else {
                arguments.addLast(0.0);
                arguments.addLast(Double.parseDouble(argXs[0]));
                ++numberOfX;
            }
            ++numberOfX;
        }
        return arguments;
    }

    private Integer getNumberOfX(String xNumber){
        char[] number = xNumber.toCharArray();
        LinkedList<Character> rlyNubmer = new LinkedList<>();
        for(int i=1;i<number.length;++i){
            rlyNubmer.addLast(number[i]);
        }
        StringBuilder myString = new StringBuilder();
        for(Character ch:rlyNubmer) myString.append(ch);
        return Integer.parseInt(myString.toString());
    }
}
