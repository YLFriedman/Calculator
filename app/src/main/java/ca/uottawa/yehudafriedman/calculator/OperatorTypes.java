package ca.uottawa.yehudafriedman.calculator;

/** OperatorTypes is an enum used by the BasicOperator and AdvancedOperator classes.
   It defines the BEDMAS ranking of each operator, as well as providing a string representation of
   the operator in question.

   @author Yehuda Friedman
 */


public enum OperatorTypes {

    PLUS(1, "+"), MINUS(1, "-"), MULTIPLY(2, "*"), DIVIDE(2, "/"), EXPONENT(3, "^"), SIN(4, "Sin"), COS(4, "Cos"),
    TAN(4, "Tan"), ARCSIN(4, "Asin"), ARCCOS(4, "Acos"), ARCTAN(4, "Atan"), ROOT(4, "Root"), LOG(4, "Log");

    private int rank;

    private String repr;


    private OperatorTypes(int rank, String repr) {
        this.rank = rank;
        this.repr = repr;
    }

    /**a getter which returns the rank of a particular operator
     *
     * @return the rank of a particular operator
     */
    public int getRank() {
        return this.rank;
    }

    /**A method to access the strings stored in each constant defined by this enum
     *
     * @return The string representation of a particular operator
     */

    public String getRepr() {
        return this.repr;
    }
}
