package edu.cs.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Button bton;
    private EditText height;
    private EditText weight;
    private EditText name;
    private TextView result;
   // public static final String mypreference = "mypref";
    private SharedPreferences myPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bton = findViewById(R.id.btn);
        height =  findViewById(R.id.heighid);
        weight =  findViewById(R.id.weighid);
        name =  findViewById(R.id.aa);
        result = findViewById(R.id.edtTxt);
        Spinner spinner = findViewById(R.id.generspinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Male");
        arrayList.add("Female");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
              //  Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }





    //explicit
    public void btnonclick(View view) {
        //  String msg = edttext.getText().toString();

        Intent intent = new Intent(this, MainActivity2.class);
        // intent.putExtra("data" , msg);
        startActivity(intent);

    }



    public void calculatebmi(View v) {

        String str1 = weight.getText().toString();
        String str2 = height.getText().toString();
        closeKeyboard();
        if (TextUtils.isEmpty(str1)) {
            weight.setError("Please enter your weight");
            weight.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(str2)) {
            height.setError("Please enter your height");
            height.requestFocus();
            return;
        }

//Get the user values from the widget reference
        float weight = Float.parseFloat(str1);
        float height = Float.parseFloat(str2) / 100;

        float bmiValue = calculateBMI(weight, height);


        String bmiInterpretation = interpretBMI(bmiValue);

        result.setText(String.valueOf(bmiValue + " --> " + bmiInterpretation));

    }

//Calculate BMI
private float calculateBMI(float weight,float height){
        return(float)(weight/(height*height));
}

// Interpret what BMI means
private String interpretBMI(float bmiValue){


         if(bmiValue< 18.5){
        return"Underweight";
        }else if(bmiValue< 25){

        return"Normal";
        }else if(bmiValue< 30){

        return"Overweight";
        }else{
        return"Obese";
        }
        }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager i = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            i.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void btnsave(View v){
        EditText label =  findViewById(R.id.aa);
        EditText nameEditText = (EditText) findViewById(R.id.heighid);
        EditText ageEditText = (EditText) findViewById(R.id.weighid);

        //set up SharedPreferences
        myPref = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("nameKey", nameEditText.getText().toString());
        editor.putInt("ageKey", Integer.parseInt(ageEditText.getText().toString()));
        editor.apply();
        label.setText("Saved");
    }
        }
