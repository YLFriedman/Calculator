package ca.uottawa.yehudafriedman.calculator;

import org.apfloat.ApfloatMath;
import org.apfloat.Apfloat;

/**AdvancedOperator is a class that defines the behaviour of more complex mathematical operators.
  In this case, complex means trigonometric and logarithmic functions. Since it is meant to be used
 in the MathExpression class, it implements the MathComponent interface.

  @author Yehuda Friedman

 */

public class AdvancedOperator implements MathComponent {

    private Apfloat base; //provides a base for exponential/logarithmic functions
    private OperatorTypes type; //

    /**This first constructor is meant for advanced operators that do not need a base, i.e. trig
        functions.

        @param type The OperatorType that this AdvancedOperator will use

     */
    public AdvancedOperator(OperatorTypes type) {

        this.type = type;
        if(type == OperatorTypes.LOG) {
            base = new Apfloat(Math.E, Value.precision);
        }
        if(type == OperatorTypes.ROOT) {
            base = new Apfloat(2.0, Value.precision);
        }
    }

    public AdvancedOperator(String input) {
        input = input.toUpperCase();
        if(input.equals("ACOS")){
            this.type = OperatorTypes.ARCCOS;
        }
        if(input.equals("ASIN")) {
            this.type = OperatorTypes.ARCSIN;
        }

        else {
            this.type = OperatorTypes.ARCTAN;
        }

    }

    /**This secondary constructor is meant for advanced operators that do require a base, meaning
        the log and root functions.

         @param type The OperatorType that this AdvancedOperator will use

         @param base The desired base for this AdvancedOperator

     */

    public AdvancedOperator(OperatorTypes type, Apfloat base) {
        this.type = type; this.base = base;
    }


    public int getRank() {
        return type.getRank();
    }
    /**The calculate method takes in a double, and then applies the correct mathematical operation on it,
       depending on the OperatorType and base which belongs to the AdvancedOperator which called calculate()

       @param input The number which this operator will be applied to
       @return The result of applying this operator to the provided input

     */

    public Apfloat calculate(Apfloat input) {

        Apfloat result;

        switch(this.type) {

            case SIN:
                result = ApfloatMath.sin(input);
                break;

            case COS:
                result =  ApfloatMath.cos(input);
                break;

            case TAN:
                result = ApfloatMath.tan(input);
                break;

            case ARCSIN:
                result = ApfloatMath.asin(input);
                break;

            case ARCCOS:
                result =  ApfloatMath.acos(input);
                break;

            case ARCTAN:
                result =  ApfloatMath.atan(input);
                break;

            case ROOT:
                if (this.base.equals(2.0)) {
                    result = ApfloatMath.sqrt(input);
                }
                else {
                    result = ApfloatMath.pow(input, new Apfloat(1).divide(base));
                }
                break;

            case LOG:
                result = ApfloatMath.log(input, base);
                break;

            default:
                return null;
        }

        return result;



    }

    /**A simple toString method which accesses the string representation of the OperatorType belonging to
        this AdvancedOperator abd returning it. If the Operator has a base, then the base is added to that string.

        @return A string representation of this AdvancedOperator

     */

    public String toString() {

        if(!(base == null)) {//If there is a value for base, attach it to the string and return the result to the user.
            return this.type.getRepr() + "_" + this.base;
        }
        else {


            return this.type.getRepr();
        }


    }



}
