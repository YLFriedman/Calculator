package ca.uottawa.yehudafriedman.calculator;

/**The Bracket class defines the behaviour of brackets inside of mathematical expressions. Since this class is intended
 * to be used with the MathExpression class, it implements the MathComponent interface.
 *
 * @author Yehuda Friedman
 */


public class Bracket implements MathComponent {


    private char symbol;

    /**A constructor which creates a new bracket, and gives it a char 'symbol' to store. The character is intended to be either '(' or ')',
     * depending on which type of bracket is desired
     *
     * @param symbol one of two characters, '(' or ')'
     */

    public Bracket(char symbol) {

        this.symbol = symbol;

    }

    /**
     * A simple getter for the symbol stored in this Bracket object.
     *
     * @return the symbol belonging to this Bracket
     */

    public char getSymbol(){
        return this.symbol;
    }

    /**Returns the rank of a particular bracket. I've defined the rank of right brackets to be 5, while the left brackets have a rank of -2.
     * This is because the righthand bracket indicates the completion of a mathematical expression which can be evaluated.
     *
     * @return the rank of this Bracket
     */
    public int getRank() {
        if(this.symbol == ')') {//If it's a right bracket, return 5, else return -2
            return 5;
        }
        else {
            return -2;
        }
    }

    /**A toString method for Bracket objects. Returns a string created by the character symbol stored in the object
     *
     * @return A string verion of this Bracket's symbol
     */

    public String toString() {

        return Character.toString(symbol);
    }



}
