package ca.uottawa.yehudafriedman.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static StringBuilder storedExpression = new StringBuilder();
    private static StringBuilder displayExpression = new StringBuilder();
    private static Value storedValue = null;
    private static Mode mode = Mode.STANDARD;
    private static TextView displayField;
    private int backspaceCounter = 0;

    private enum Mode{
        STANDARD, ON_ERROR, RESULT, EXTEND_RESULT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayField = findViewById(R.id.textView);

    }

    public void onNumberClick(View view) {
        Button current = (Button) view;
        String bttnText = current.getText().toString();

        switch(mode) {
            case STANDARD:
                displayExpression.append(bttnText);
                displayField.setText(displayExpression.toString());
                return;

            case ON_ERROR:
                mode = Mode.STANDARD;
                displayExpression.append(bttnText);
                displayField.setText(displayExpression.toString());
                return;

            case RESULT:
                storedValue = null;
                mode = Mode.STANDARD;
                clearExpressions();
                displayExpression.append(bttnText);
                displayField.setText(displayExpression.toString());
                return;

            case EXTEND_RESULT:
                storedExpression.append(bttnText);
                displayExpression.append(bttnText);
                displayField.setText(displayExpression.toString());
                backspaceCounter++;
                return;

        }

    }

    public void onBasicOperatorClick(View view) {
        Button button = (Button) view;
        Character operator = button.getText().charAt(0);

        switch(mode) {
            case STANDARD:
                displayExpression.append(operator);
                displayField.setText(displayExpression.toString());
                break;

            case ON_ERROR:
                displayExpression.append(operator);
                displayField.setText(displayExpression.toString());
                mode = Mode.STANDARD;
                break;

            case EXTEND_RESULT:
                storedExpression.append(operator);

            case RESULT:
                displayExpression.append(operator);
                displayField.setText(displayExpression.toString());
                backspaceCounter++;
                mode = Mode.EXTEND_RESULT;
                break;

        }
    }

    public void onEqualsClick(View view) {
        String expression;
        if(mode == Mode.EXTEND_RESULT) {
            expression = storedExpression.toString();
        }
        else{
            expression = displayExpression.toString();
        }

        clearExpressions();

        switch(ExpressionValidator.isValid(expression)) {
            case BAD_SYNTAX:
                mode = Mode.ON_ERROR;
                displayField.setText(getString(R.string.badExpressionMessage));
                return;

            case DIVISION_BY_ZERO:
                mode = Mode.ON_ERROR;
                displayField.setText(getString(R.string.zeroDivisionError));
                return;

            case VALID:
                Value result = ExpressionValidator.processAndSolve(expression);
                storedValue = result;
                displayExpression.append(result.toString());
                storedExpression.append(result.getValue().toString(true));
                displayField.setText(displayExpression.toString());
                mode = Mode.RESULT;
                return;
        }


    }

    public void onDeleteClick(View view) {

        switch(mode) {
            case STANDARD:
                displayExpression.deleteCharAt(displayExpression.length()-1);
                displayField.setText(displayExpression.toString());
                break;

            case ON_ERROR:
                displayField.setText("");
                mode = Mode.STANDARD;
                break;

            case RESULT:
                displayField.setText("");
                clearExpressions();
                mode = Mode.STANDARD;
                break;

            case EXTEND_RESULT:
                displayExpression.deleteCharAt(displayExpression.length()-1);
                storedExpression.deleteCharAt(displayExpression.length()-1);
                backspaceCounter--;
                break;
        }
    }

    public static void clearExpressions() {
        displayExpression.delete(0, displayExpression.length());
        storedExpression.delete(0, storedExpression.length());
    }
}
