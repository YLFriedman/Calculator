package ca.uottawa.yehudafriedman.calculator;

import org.apfloat.Apfloat;
import java.util.ArrayList;

/**The class MathExpression describes objects that can process mathematical expressions, in the form of
 * ArrayLists of MathComponents. It also contains several static methods for formatting and checking validity of
 * said ArrayLists.
 *
 * @author Yehuda Friedman
 */


public class MathExpression{

    private int highestRank = -1; //int to track what the highest rank in the expression is
    private int rankIndex = -1; //int to track the index of the highest ranked operator
    private int pairedBracket = -1; // int to track the index of the lefthand bracket, if one exists
    public boolean zeroDivisionFlag = false;

    private ArrayList<MathComponent> storedExpression;

    /**Constructor for creating a new MathExpression object. Stores an ArrayList of MathComponents, and determines the
     * location of the highest ranked operator/bracket. Assumes that the stored expression is well formed.
     *
     * @param startingExpression an ArrayList<MathComponent></MathComponent> which contains the expression you want to solve
     */

    public MathExpression(ArrayList<MathComponent> startingExpression) {

        this.storedExpression = startingExpression;
        this.determineRanks();

    }

    /**Instance method for solving a stored expression. Uses recursive calls to reduce the size of the expression
     * and returns the final value of the expression at the end of the process. Assumes that the input expression
     * is well formed.
     *
     * @return a Value object which contains the double value of the evaluated expression
     */

    public Value solve() {

        if(storedExpression.size() == 1) {

            return (Value)storedExpression.get(0);
        }

        if(highestRank < 4 && highestRank > 0) {                                                    //if the next operation to perform is a BasicOperator
            Value operOne, operTwo;
            BasicOperator operator;
            operOne = (Value) storedExpression.get(rankIndex - 1);
            operTwo = (Value) storedExpression.get(rankIndex + 1);
            operator = (BasicOperator) storedExpression.get(rankIndex);
            if(operTwo.equals(Apfloat.ZERO) && operator.toString().equals("/") ){//if a zero division is detected, adjust the boolean and exit recursion
                zeroDivisionFlag = true;
                return new Value("0");
            }
            Apfloat result = operator.calculate(operOne.getValue(), operTwo.getValue());
            Value finalResult = new Value(result);
            storedExpression.remove(rankIndex - 1);
            storedExpression.remove(rankIndex);
            storedExpression.set(rankIndex - 1, finalResult);
            this.determineRanks();
            return this.solve();
        }

        if(highestRank == 5){                                                                       //if there are brackets present
            ArrayList<MathComponent> innerExpression = new ArrayList<MathComponent>();
            for(int i = pairedBracket +1; i < rankIndex; i ++) {
                innerExpression.add(storedExpression.get(i));
            }
            MathExpression inner = new MathExpression(innerExpression);
            Value result = inner.solve();                                                           //solve the interior of the brackets
            if(inner.zeroDivisionFlag == true){
                zeroDivisionFlag = true;
                return new Value("0");
            }
            storedExpression.subList(pairedBracket, rankIndex + 1).clear();
            storedExpression.add(pairedBracket, result);                                            //and insert the result back into the outer expression
            this.determineRanks();
            return this.solve();
        }

        if(highestRank == 4) {                                                                      //if the next operation to perform is an AdvancedOperator
            AdvancedOperator operator = (AdvancedOperator)storedExpression.get(rankIndex);
            Value operand = (Value)storedExpression.get(rankIndex + 1);
            Apfloat result = operator.calculate(operand.getValue());
            Value finalResult = new Value(result);
            storedExpression.remove(rankIndex + 1);
            storedExpression.remove(rankIndex);
            storedExpression.add(rankIndex, finalResult);
            this.determineRanks();
            return this.solve();

        }

        return null;



    }

    /**A toString method for the stored expression
     *
     * @return a string representation of the stored expression
     */

    public String toString() {
        StringBuilder val = new StringBuilder();
        for(int i = 0; i < storedExpression.size(); i ++) {
            val.append(storedExpression.get(i).toString());
            val.append(" ");
        }

        return val.toString();

    }

    /**This method determines the location and rank of the highest ranked operator in the expression,
     * and assigns those values to the instance variables highestRank, rankIndex, and pairedBracket.
     * Assumes the expression is well formed.
     */
    public void determineRanks() {

        highestRank = -1;
        rankIndex = 0;
        int tempRank;

        for(int i = 0; i < storedExpression.size(); i ++) {
            tempRank = storedExpression.get(i).getRank();
            if(tempRank == -2 && highestRank < 5) {
                pairedBracket = i;
            }
            if(tempRank > highestRank) {
                highestRank = tempRank;
                rankIndex = i;

            }
        }
    }

}
