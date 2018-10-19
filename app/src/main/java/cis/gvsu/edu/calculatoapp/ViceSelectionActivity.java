package cis.gvsu.edu.calculatoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

/*
 * @Authors: Matthew Kennedy, Tyler Bruder
 * @Version: Fall 2018
 * Android conversion app
 *
 */

/*
 * Main function of the class is to implements 'spinners', or choices for users to select
 * when attempting to use the conversion app
 */
public class ViceSelectionActivity extends AppCompatActivity {
    /* From the spinner */
    private Spinner inputSpinner1;
    /* To the spinner */
    private Spinner inputSpinner2;
    /* Saves changes from previous use of app */
    private FloatingActionButton doneBtn;


    /*
     * Checks for saved instances or creates a new instance for spinners
     * Logic and portions of this collabed with other groups.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When created, use saved instance first
        super.onCreate(savedInstanceState);
        //Set layout of saved instance
        setContentView(R.layout.activity_vice_selection);

        //Assigning spinners to the view ID's
        inputSpinner1 = findViewById(R.id.inputSpinner1);
        inputSpinner2 =  findViewById(R.id.inputSpinner2);
        doneBtn = findViewById(R.id.doneBtn);

        //Used to pass data between activities
        Bundle dataPass = getIntent().getExtras();
        String mode = dataPass.get("Mode").toString();
        List<String> choices = new ArrayList<String>(3);

        /* Chooses spinner based on the mode that is selected */
        if(mode.equals("length")){
            //Add length choices
            choices.add("Yards");
            choices.add("Meters");
            choices.add("Miles");
        } else {
            //Add volume choices
            choices.add("Gallons");
            choices.add("Liters");
            choices.add("Quarts");
        }

        //Array adapter used to store an array of objects as a datasource
        ArrayAdapter<String> objects = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
        objects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Adding the list to the spinner
        inputSpinner1.setAdapter(objects);
        inputSpinner2.setAdapter(objects);
        doneBtn.setOnClickListener((View v) -> returnChanges());
    }

    /*
     * Display the choice made by the user from the spinner
     */
    public void returnChanges(){
        //Intended selection by user
        Intent result = new Intent();
        //result + 'From Unit" displayed
        result.putExtra("FromUnit", inputSpinner1.getSelectedItem().toString());
        result.putExtra("ToUnit", inputSpinner2.getSelectedItem().toString());
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}

