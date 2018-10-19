package cis.gvsu.edu.calculatoapp;
//Snackbar makes a notification of some sort at the bottom.

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/*
 * @Authors: Matthew Kennedy, Tyler Bruder
 * @Version: Fall 2018
 * Android conversion app
 *
 */

/*
 *  Class that is going to take in and handle any input activity.
 */
public class InputActivity extends AppCompatActivity {
    public static final int VICE_SELECT = 1;

    /* Default mode */
    private String selectedMode = "length";
    /* Text box for input1 */
    private EditText input1;
    /* Text box for input2 */
    private EditText input2;
    /*  Unit view */
    private TextView inputUnit1;
    /*  Unit view */
    private TextView inputUnit2;
    /*  Calculate button */
    private Button calcBtn;
    /* Clear Button */
    private Button clearBtn;
    /* Toggle modes button */
    private Button modeBtn;

    /*
    *
    * Setting up the default 'home screen' for the app when launched
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        //Adding components to the app
        input1 = findViewById(R.id.textInput1);
        input2 = findViewById(R.id.textInput2);
        inputUnit1 = findViewById(R.id.inputUnit1);
        inputUnit2 = findViewById(R.id.inputUnit2);
        calcBtn = findViewById(R.id.calcBtn);
        clearBtn = findViewById(R.id.clearBtn);
        modeBtn = findViewById(R.id.modeBtn);
        changeHints();

        /* When focus changes, onFocusChange is called. */
        input1.setOnFocusChangeListener((View v, boolean b) -> onFocusChange(v, b));
        input2.setOnFocusChangeListener((View v, boolean b) -> onFocusChange(v, b));

        clearBtn.setOnClickListener(v -> clearFields());
        modeBtn.setOnClickListener(v -> swapMode());
        calcBtn.setOnClickListener(v -> calculate());
    }

    /* Clears the text fields when called */
    private void clearFields () {
        input1.setText("");
        input2.setText("");
    }

    /*
     * Updates the text hints when called
     *
     */
    private void changeHints() {
        input1.setHint("Enter " + selectedMode + " in " + inputUnit1.getText());
        input2.setHint("Enter " + selectedMode + " in " + inputUnit2.getText());
    }

    /*
    * Changes the units and conversions when the mode is changed
    *
    */
    private void swapMode() {
        //If the mode is length, use length units
        if(selectedMode.equals("length")){
            selectedMode = "volume";
            inputUnit1.setText("Gallons");
            inputUnit2.setText("Liters");
        } else {
            selectedMode = "length";
            inputUnit1.setText("Meters");
            inputUnit2.setText("Yards");
        }
        changeHints();
        clearFields();
        modeBtn.requestFocus();
    }

    /*
     * Calculates the conversion then sets the input.
     * Uses if statements to check if either input is empty, then gets the other as the "to" input.
     * If both empty, do nothing. If neither empty, start with input 1.
     */
    private void calculate() {
        //Have to use getText().toString, else the return is a CharSequence, not a String.
        double initVal = 0.0;
        EditText inputToChange = null;
        TextView fromView = null;
        TextView toView = null;

        //If both empty, do nothing.
        if ( input1.getText().toString().equals("") && input2.getText().toString().equals(""))
            return;

        //If input2 is empty or both are not empty, use input 1 as starting point.
        if (input2.getText().toString().equals("") ||(!input1.getText().toString().equals("") && !input2.getText().toString().equals(""))) {
            initVal = Double.parseDouble(input1.getText().toString());
            inputToChange = input2;
            toView = inputUnit2;
            fromView = inputUnit1;
        } else if (input1.getText().toString().equals("")) {
            initVal = Double.parseDouble(input2.getText().toString());
            inputToChange = input1;
            fromView = inputUnit2;
            toView = inputUnit1;
        }

        //Call the UnitsConverter.convert method with the init value and whatever units we have.
        if (selectedMode.equals("length")) {
            inputToChange.setText(Double.toString(UnitsConverter.convert(
                    initVal,
                    UnitsConverter.LengthUnits.valueOf( fromView.getText().toString()),
                    UnitsConverter.LengthUnits.valueOf(toView.getText().toString()))));
        } else {
            inputToChange.setText(Double.toString(UnitsConverter.convert(
                    initVal,
                    UnitsConverter.VolumeUnits.valueOf( fromView.getText().toString()),
                    UnitsConverter.VolumeUnits.valueOf(toView.getText().toString()))));
        }
        calcBtn.requestFocus();
    }

    /*
    * Found here https://stackoverflow.com/questions/15412943/hide-soft-keyboard-on-losing-focus
    * Interesting issue... it appears that, after selecting an EditText, then clicking a button, the focus is still on the button.
    * Not sure how to force the focus off of an edit text. However, the logic is still valid; if the view was either EditText,
    * and they have lost focus, then hide the keyboard. However, never really gets called.
     */
    public void onFocusChange(View v, boolean hasFocus) {
        //Doesn't quite work the way intended.
        if((v.getId() == input1.getId() || v.getId() == input2.getId()) && !hasFocus) {
            InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        if (v.getId() == input1.getId() && hasFocus)
            input2.setText("");
        else if (v.getId() == input2.getId() && hasFocus)
            input1.setText("");
    }

    /*
     * Creates the main menu and puts the settings menu resource file in.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
    /*
     * This will get called when an item is selected from the menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent i = new Intent(InputActivity.this, ViceSelectionActivity.class);
            i.putExtra("Mode", selectedMode);
            startActivityForResult(i, VICE_SELECT);
        }
        return true;
    }

    /*
     * When the intent is done, this is called when it comes.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        clearFields();
        if (resultCode == Activity.RESULT_OK) {
            inputUnit1.setText(data.getStringExtra("FromUnit"));
            inputUnit2.setText(data.getStringExtra("ToUnit"));
            changeHints();
        }
    }
}