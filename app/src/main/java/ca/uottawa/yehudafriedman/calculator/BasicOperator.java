package ca.uottawa.yehudafriedman.calculator;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/**The class BasicOperator defines the behaviour of simple arithmetic operators. In this case, a basic operator
    is any operator which takes two values and performs a computation on them. For example, Plus, Minus, exponent, etc.
    Since it is meant to be used in the MathExpression class, it implements the MathComponent interface.

    @author Yehuda Friedman

 */

public class BasicOperator implements MathComponent {

    private int rank;
    private OperatorTypes type;

    /**This constructor creates a new BasicOperator, depending on the OperatorType that is provided
     *
     * @param symbol the symbol representation of this operator
     */

    public BasicOperator(char symbol) {
        switch(symbol) {
            case '*':
                this.type = OperatorTypes.MULTIPLY;
                break;
            case '/':
                this.type = OperatorTypes.DIVIDE;
                break;
            case '^':
                this.type = OperatorTypes.EXPONENT;
                break;
            case '+':
                this.type = OperatorTypes.PLUS;
                break;
            case '-':
                this.type = OperatorTypes.MINUS;
                break;
        }

        this.rank = type.getRank();
    }

    /**A simple getter which returns the 'rank' of this operator, in terms of BEDMAS
     *
     * @return This operators rank
     */

    public int getRank() {
        return this.rank;
    }

    /**Calculate is a method which performs the appropriate calculation for this particular operator.
     * It then returns the resulting value.
     *
     * @param oper1 The lefthand operand
     * @param oper2 The righthand operand
     * @return The result of performing the operation on oper1 and oper2
     */

    public Apfloat calculate(Apfloat oper1, Apfloat oper2) {



        switch(this.type) {

            case PLUS:
                return oper1.add(oper2);


            case MINUS:
                return oper1.subtract(oper2);

            case MULTIPLY:
                return oper1.multiply(oper2);

            case DIVIDE:
                return oper1.divide(oper2);

            case EXPONENT:
                return ApfloatMath.pow(oper1, oper2);


            default:
                return null;
        }

    }

    /**A simple toString method for BasicOperators. Accesses the string representation stored in the enum OperatorTypes.
     *
     * @return The string representation of this operator
     */

    public String toString() {

        return this.type.getRepr();
    }

}

