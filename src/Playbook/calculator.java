package Playbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
 

public class calculator {
    private static final ArrayList<Character> DIVIDERS = new ArrayList<Character>
            (Arrays.asList('*', '/', '-', '+'));
    private static final int RIGHT_DIRECTION = 1;
    private static final int LEFT_DIRECTION = -1;
 
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String expression = "";
 
        System.out.print("Entrer l'expression mathematiques: ");
        try {
            expression = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Expression: " + expression);
 
        System.out.println("La reponse est : " + calc(expression));
    }
    //Recursive function with the state machine
     private static String calc(String expression) {
        int pos = 0;
        //Extracting expression from braces, doing recursive call
        //replace braced expression on result of it solving
        if (-1 != (pos = expression.indexOf("("))) {
 
            String subexp = extractExpressionFromBraces(expression,pos);
            expression = expression.replace("("+subexp+")", calc(subexp));
 
            return calc(expression);
 
        //Three states for calculating sin cos exp
        //input must be like sin0.7
        }         
        else if (expression.indexOf("*") > 0 | expression.indexOf("/") > 0) {
 
            int multPos = expression.indexOf("*");
            int divPos = expression.indexOf("/");
 
            pos = Math.min(multPos, divPos);
            if (multPos < 0) pos = divPos; else if (divPos < 0) pos = multPos; 
                //*Pos will be -1 result of min will be incorrect.
 
            char divider = expression.charAt(pos);
 
            String leftNum = extractNumber(expression, pos, LEFT_DIRECTION);
            String rightNum = extractNumber(expression, pos, RIGHT_DIRECTION);
 
            expression = expression.replace(leftNum + divider + rightNum, 
                calcShortExpr(leftNum, rightNum, divider));
 
            return calc(expression);
 
        
        } else if (expression.indexOf("+") > 0 | expression.indexOf("-") > 0) {
 
            int summPos = expression.indexOf("+");
            int minusPos = expression.indexOf("-");
 
            pos = Math.min(summPos, minusPos);
 
            if (summPos < 0) pos = minusPos; else if (minusPos < 0) pos = summPos;
 
            char divider = expression.charAt(pos);
 
            String leftNum = extractNumber(expression, pos, LEFT_DIRECTION);
            String rightNum = extractNumber(expression, pos, RIGHT_DIRECTION);
 
            expression = expression.replace(leftNum + divider + rightNum, 
                calcShortExpr(leftNum, rightNum, divider));
 
            return calc(expression);
 
        } else return expression;
    }
 
    private static String extractExpressionFromBraces(String expression, int pos) {
        int braceDepth = 1;
        String subexp="";
 
        for (int i = pos+1; i < expression.length(); i++) {
            switch (expression.charAt(i)) {
                case '(':
                    braceDepth++;
                    subexp += "(";
                    break;
                case ')':
                    braceDepth--;
                    if (braceDepth != 0) subexp += ")";
                    break;
                default:
                    if (braceDepth > 0) subexp += expression.charAt(i);
 
            }
            if (braceDepth == 0 && !subexp.equals("")) return subexp;
        }
        return "Echec !!!";
    }
 
    private static String extractNumber(String expression, int pos, int direction) {
 
        String resultNumber = "";
        int currPos = pos + direction;
        //shift pos on next symbol from divider
 
        //Pour les nombres negatives
        if (expression.charAt(currPos) == '-') {
            resultNumber+=expression.charAt(currPos);
            currPos+=direction;
        }
 
        for (; currPos >= 0 &&
               currPos < expression.length() &&
               !DIVIDERS.contains(expression.charAt(currPos));
               currPos += direction) {
            resultNumber += expression.charAt(currPos);
        }
 
        if (direction==LEFT_DIRECTION) resultNumber = new 
                StringBuilder(resultNumber).reverse().toString();
 
        return resultNumber;
    }
 
    private static String calcShortExpr(String leftNum, String rightNum, char divider) {
        switch (divider) {
            case '*':
                return Double.toString(Double.parseDouble(leftNum) * 
                        Double.parseDouble(rightNum));
            case '/':
                return Double.toString(Double.parseDouble(leftNum) / 
                        Double.parseDouble(rightNum));
            case '+':
                return Double.toString(Double.parseDouble(leftNum) + 
                        Double.parseDouble(rightNum));
            case '-':
                return Double.toString(Double.parseDouble(leftNum) - 
                        Double.parseDouble(rightNum));
            default:
                return "0";
        }
    }
}
