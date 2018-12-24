package ca.uottawa.yehudafriedman.calculator;

/**An extremely simple interface, used by all MathComponents which will be evaluated inside of the MathExpression class.
 * The only method which must be implemented by a class using this interface is getRank, which indicates the BEDMAS ranking of
 * a particular MathComponent.
 *
 * @author Yehuda Friedman
 */
public interface MathComponent {

        int getRank();


}
