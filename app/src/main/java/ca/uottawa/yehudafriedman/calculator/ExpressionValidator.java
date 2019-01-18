package ca.uottawa.yehudafriedman.calculator;

import org.apfloat.Apfloat;

import java.util.ArrayList;


public class ExpressionValidator {

    private static String illegalPatternsRegex = ".*\\.\\d*\\..*|.*[^0-9]\\.[^0-9].*|.*[*/^+\\-][*/^+\\-].*|.*\\([*/^].*|.*[*/^+\\-]\\).*|.*\\(\\).*|.*^[(.)]$.*|.*[*/^+\\-]$|^[*/^].*";
    public static String containsScientific = ".*E\\-?[0-9]+.*";
    public static String threeLetterSpecial = ".*(Sin|Cos|Tan|Log)\\($";
    public static String fourLetterSpecial = ".*A(cos|sin|tan)\\($";
    public static String squareRootEnding = ".*√\\($";

    private static char[] specialValues = {'\u03C0','\u0065'};

    public enum Validity {
        VALID, BAD_SYNTAX, DIVISION_BY_ZERO
    }

    public static Validity isValid(String expression) {

        if(expression.matches(illegalPatternsRegex) || !validBrackets(expression)) {
            return Validity.BAD_SYNTAX;
        }
        String formatted = formatExpression(expression);
        MathExpression exp = parseExpression(formatted);
        exp.solve();
        if(exp.zeroDivisionFlag) {
            return Validity.DIVISION_BY_ZERO;
        }
        return Validity.VALID;
    }

    public static Value processAndSolve(String validExpression) {
        String formatted = formatExpression(validExpression);
        MathExpression exp = parseExpression(formatted);
        Value val = exp.solve();
        return val;
    }


    private static String formatExpression(String exp) {

        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < exp.length(); i ++ ) {
            char current = exp.charAt(i);
            builder.append(current);
            if(!(i == exp.length() -1)) {
                char next = exp.charAt(i+1);

                if(implicitMultiplication(current, next)) {
                    builder.append('*');
                }
            }
        }

        return builder.toString();

    }

    private static boolean validBrackets(String exp) {
        int leftBracketCount = 0;
        int rightBracketCount = 0;
        for(int i = 0; i < exp.length(); i ++ ) {
            if(exp.charAt(i) == '(') {
                leftBracketCount ++;
            }
            if(exp.charAt(i) == ')') {
                rightBracketCount ++;
            }
        }
        return leftBracketCount == rightBracketCount;
    }

    /**
     * Method to determine if an implicit multiplication sign should be inserted between two characters
     *
     * @param current the current character
     * @param next the next character
     * @return whether there should be a multiplication sign inserted
     */
    private static boolean implicitMultiplication(char current, char next) {

            if(Character.isDigit(current) || current == '.') {
                if(isSpecialValue(next) || next == '(' || (Character.isAlphabetic(next) && next != 'E')) {
                    return true;
                }
            }
            else if(isSpecialValue(current)) {
                if(Character.isDigit(next) || next == '(' || Character.isAlphabetic(next) || next == '.') {
                    return true;
                }
            }

            else if(current == ')') {
                if(Character.isDigit(next) || next == '(' || Character.isAlphabetic(next) || isSpecialValue(next) || next == '.') {
                    return true;
                }
            }

            return false;
    }

    private static MathExpression parseExpression(String exp) {
        int startingPoint = 0;
        ArrayList<MathComponent> expBuilder = new ArrayList<>();
        boolean onValue = false;
        StringBuilder valueBuilder = new StringBuilder();
        if(exp.matches(containsScientific)) {
            int end = findScientificEnd(exp);
            valueBuilder.append(exp.substring(0,end));
            if(end == exp.length()){
                appendValue(expBuilder, valueBuilder);
            }
            else{
                onValue = true;
            }
            startingPoint = end;
        }

        for(int i = startingPoint; i < exp.length(); i ++) {
            char current = exp.charAt(i);
            if(Character.isDigit(current) || current == '.') {
                valueBuilder.append(current);
                if(i == exp.length() -1) {
                    appendValue(expBuilder, valueBuilder);
                }
                onValue = true;
            }
            else if(isSpecialValue(current)) {
                String str = String.valueOf(current);
                Value specialValue = new Value(str);
                expBuilder.add(specialValue);
            }
            else if(isBracket(current)) {
                Bracket brack = new Bracket(current);
                if(onValue) {
                    appendValue(expBuilder, valueBuilder);
                    onValue = false;
                }
                expBuilder.add(brack);
            }
            else if(isOperator(current)) {
                if(onValue) {
                    appendValue(expBuilder, valueBuilder);
                    onValue = false;
                    BasicOperator operator = new BasicOperator(current);
                    expBuilder.add(operator);
                }
                else if(!isMinus(current) && !isPlus(current)){
                    BasicOperator operator = new BasicOperator(current);
                    expBuilder.add(operator);
                }
                else if(i == 0 || exp.charAt(i-1) == '('){
                    expBuilder.add(new Value(current + "1"));
                    expBuilder.add(new BasicOperator('*'));

                }
                else {
                    expBuilder.add(new BasicOperator(current));
                }
            }
            else if(Character.isAlphabetic(current)) {
                current = Character.toUpperCase(current);
                AdvancedOperator advanced;
                switch(current) {
                    case 'S':
                        advanced = new AdvancedOperator(OperatorTypes.SIN);
                        expBuilder.add(advanced);
                        expBuilder.add(new Bracket('('));
                        i += 3;
                        break;

                    case 'T' :
                        advanced = new AdvancedOperator(OperatorTypes.TAN);
                        expBuilder.add(advanced);
                        expBuilder.add(new Bracket('('));
                        i += 3;
                        break;

                    case 'C' :
                        advanced = new AdvancedOperator(OperatorTypes.COS);
                        expBuilder.add(advanced);
                        expBuilder.add(new Bracket('('));
                        i += 3;
                        break;

                    case 'L' :
                        advanced = new AdvancedOperator(OperatorTypes.LOG);
                        expBuilder.add(advanced);
                        expBuilder.add(new Bracket('('));
                        i += 3;
                        break;

                    case 'A' :
                        String operator = exp.substring(i, i+6);
                        advanced = new AdvancedOperator(operator);
                        expBuilder.add(advanced);
                        expBuilder.add(new Bracket('('));
                        i += 6;



                }

            }

            else if(current == '√') {
                expBuilder.add(new AdvancedOperator(OperatorTypes.ROOT));
                expBuilder.add(new Bracket('('));
                i ++;

            }

        }
        return new MathExpression(expBuilder);
    }

    private static boolean isOperator(char c) {
        if (c == '*' || c == '/' || c == '^' || c == '+' || c == '-') {
            return true;
        }

        return false;
    }

    private static boolean isBracket(char c) {
        return c == '(' || c== ')';
    }

    private static boolean isSpecialValue(char c) {
        for(int i = 0; i < specialValues.length; i ++) {
            if ( c == specialValues[i]) return true;
        }
        return false;
    }

    private static boolean isMinus(char c) {
        return c == '-';
    }

    private static boolean isPlus(char c) {
        return c == '+';
    }

    private static void appendValue(ArrayList<MathComponent> exp, StringBuilder builder) {
        Value value = new Value(builder.toString());
        exp.add(value);
        builder.setLength(0);
    }

    private static int findScientificEnd(String exp) {
        for(int i = 0; i < exp.length(); i ++) {
            char current = exp.charAt(i);
            if(isOperator(current)) {
                int check = i - 1;
                if(exp.charAt(check) != 'E') {
                    return i;
                }
            }
        }
        return exp.length();
    }


}
