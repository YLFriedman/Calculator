package ca.uottawa.yehudafriedman.calculator;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/**The Value class is used to represent numbers in a mathematical expression. It implements the MathComponent interface.
 *
 *@author Yehuda Friedman
 */

public class Value implements MathComponent{

    private static String excessiveSizeRegex = ".*\\d+\\.\\d{8,20}.*";
    private static String scientificNotationRegex = ".*\\d+e-*\\d{2,4}$";
    public static final int precision = 100;
    private Apfloat value;

    /**
     * Creates a new Value object with the provide double
     *
     * @param value the number this Value will store
     */
    public Value(String value) {
        if(value.equals("e")) {
            this.value = new Apfloat(Math.E, precision);
            return;
        }
        if(value.equals("π")){
            this.value = ApfloatMath.pi(precision);
            return;
        }

        this.value = new Apfloat(value, precision);
    }

    public Value(Apfloat value) {
        this.value = value;
    }

    /**Returns the number that belongs to this object
     *
     * @return the number stored in this Value
     */

    public Apfloat getValue() {
        return this.value;
    }

    /**If getRank is called on a Value, the object will return -1, since it has no BEDMAS rank
     *
     * @return the integer -1
     */

    public int getRank() {
        return -1;
    }

    /**
     * A simnle toString method which returns the string version of the double value
     *
     * @return the string representation of the value stored in this object
     */

    public String toString() {
        String rawString = value.toString();
        if(rawString.matches(scientificNotationRegex)) {
            rawString = rawString.replace('e', 'E');
            if(rawString.length() > 8) {
                return rawString.substring(0,4) + rawString.substring(rawString.length()-4);
            }
            else{
                return rawString;
            }
        }

        rawString = value.toString(true);
        if(rawString.matches(excessiveSizeRegex)) {
            int index = rawString.indexOf(".");
            return rawString.substring(0, index) + rawString.substring(index, index+9);
        }
        else{
            return rawString;
        }

    }

    @Override
    public boolean equals(Object other){
        return this.value.equals(other);
    }



}
