package ca.uottawa.yehudafriedman.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static StringBuilder displayExpression = new StringBuilder();
    private static Mode mode = Mode.STANDARD;
    private static TextView displayField;
    private int backspaceCounter = 0;

    private enum Mode{
        STANDARD, ON_ERROR, RESULT, EXTEND_SCIENTIFIC
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayField = findViewById(R.id.textView);

        ImageButton reset = findViewById(R.id.imageButton);
        View.OnLongClickListener listener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clearExpression();
                mode = Mode.STANDARD;
                return true;
            }
        };
        reset.setOnLongClickListener(listener);

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
                mode = Mode.STANDARD;
                clearExpression();
                displayExpression.append(bttnText);
                displayField.setText(displayExpression.toString());
                return;

            case EXTEND_SCIENTIFIC:
                if (backspaceCounter > 0) {
                    displayExpression.append(bttnText);
                    displayField.setText(displayExpression.toString());
                    backspaceCounter++;
                    return;
                }
                else {
                    mode = Mode.STANDARD;
                    clearExpression();
                    displayExpression.append(bttnText);
                    displayField.setText(displayExpression.toString());
                    return;
                }


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

            case RESULT:
                displayExpression.append(operator);
                displayField.setText(displayExpression.toString());
                mode = Mode.STANDARD;
                break;

            case EXTEND_SCIENTIFIC:
                displayExpression.append(operator);
                displayField.setText(displayExpression.toString());
                backspaceCounter++;
                break;

        }
    }

    public void onEqualsClick(View view) {
        String expression;
        expression = displayExpression.toString();
        clearExpression();

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
                displayExpression.append(result.toString());
                displayField.setText(displayExpression.toString());
                backspaceCounter = 0;
                if(displayExpression.toString().matches(ExpressionValidator.containsScientific)) {
                    mode = Mode.EXTEND_SCIENTIFIC;
                }
                else{
                    mode = Mode.RESULT;
                }
                return;
        }


    }

    public void onDeleteClick(View view) {

        switch(mode) {
            case EXTEND_SCIENTIFIC:
                if(backspaceCounter == 0) {
                    clearExpression();
                    mode = Mode.STANDARD;
                    return;
                }
                else if(displayExpression.toString().matches(ExpressionValidator.threeLetterSpecial)) {
                    backspaceCounter = backspaceCounter - 4;
                }
                else if(displayExpression.toString().matches(ExpressionValidator.fourLetterSpecial)) {
                    backspaceCounter = backspaceCounter - 5;
                }
                else{
                    backspaceCounter--;
                }

            case STANDARD:
                if(displayExpression.toString().matches(ExpressionValidator.threeLetterSpecial)) {
                    displayExpression.delete(displayExpression.length()-4, displayExpression.length());
                    displayField.setText(displayExpression.toString());
                    return;
                }
                else if(displayExpression.toString().matches(ExpressionValidator.fourLetterSpecial)) {
                    displayExpression.delete(displayExpression.length()-5, displayExpression.length());
                    displayField.setText(displayExpression.toString());
                    return;
                }
                else if(displayExpression.length() >= 1 ) {
                    displayExpression.deleteCharAt(displayExpression.length()-1);
                    displayField.setText(displayExpression.toString());
                }
                break;

            case ON_ERROR:
                clearExpression();
                mode = Mode.STANDARD;
                break;

            case RESULT:
                clearExpression();
                mode = Mode.STANDARD;
                break;

        }
    }

    public void onAdvancedOperatorClick(View view) {
        Button button = (Button) view;
        String operator = button.getText().toString();

        switch(mode) {
            case EXTEND_SCIENTIFIC:
                if(backspaceCounter == 0){
                    clearExpression();
                    displayExpression.append(operator + '(');
                    displayField.setText(displayExpression.toString());
                    mode = Mode.STANDARD;
                    break;
                }
                else {
                    displayExpression.append(operator + '(');
                    displayField.setText(displayExpression.toString());
                    if(operator.charAt(0) == 'A') {
                        backspaceCounter += 5;
                    }
                    else {
                        backspaceCounter += 4;
                    }
                    break;
                }

            case ON_ERROR:

            case RESULT:
                clearExpression();
                displayExpression.append(operator + '(');
                displayField.setText(displayExpression.toString());
                mode = Mode.STANDARD;
                break;

            case STANDARD:
                displayExpression.append(operator + '(');
                displayField.setText(displayExpression.toString());
                break;


        }
    }

    private static void clearExpression() {
        displayExpression.delete(0, displayExpression.length());
        displayField.setText("");
    }
}
