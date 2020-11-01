package edu.cs.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Button bton;
    private EditText edtheight;
    private EditText edtweight;
    private EditText edtname;
    private TextView result;
    private CheckBox chk;
    private Spinner spinner;
    public static final String NAME = "NAME";
    public static final String HEIGHT = "HEIGHT";
    public static final String WEIGHT = "WEIGHT";
    public static final String GENDER = "GENDER";
    public static final String FLAG = "FLAG";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bton = findViewById(R.id.btn);
        edtheight = findViewById(R.id.heighid);
        edtweight = findViewById(R.id.weighid);
        edtname = findViewById(R.id.aa);
        result = findViewById(R.id.edtTxt);
        chk = findViewById(R.id.chk);
        setupViews();
        setupSharedPrefs();
        checkPrefs();

        Spinner spinner = findViewById(R.id.generspinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Male");
        arrayList.add("Female");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void btnonclick(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);

    }


    public void calculatebmi(View v) {
        String str1 = edtweight.getText().toString();
        String str2 = edtheight.getText().toString();
        closeKeyboard();
        if (TextUtils.isEmpty(str1)) {
            edtweight.setError("Please enter your weight");
            edtweight.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(str2)) {
            edtheight.setError("Please enter your height");
            edtheight.requestFocus();
            return;
        }

        float weight = Float.parseFloat(str1);
        float height = Float.parseFloat(str2) / 100;
        float bmiValue = calculateBMI(weight, height);
        String bmiInterpretation = interpretBMI(bmiValue);
        result.setText(String.valueOf(bmiValue + " --> " + bmiInterpretation));

    }

    private float calculateBMI(float weight, float height) {
        return (float) (weight / (height * height));
    }

    private String interpretBMI(float bmiValue) {
        if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue < 25) {

            return "Normal";
        } else if (bmiValue < 30) {

            return "Overweight";
        } else {
            return "Ooooh !!!";
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager i = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            i.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void setupViews() {
        edtname = findViewById(R.id.aa);
        edtheight = findViewById(R.id.heighid);
        edtweight = findViewById(R.id.weighid);
        spinner = findViewById(R.id.generspinner);
        chk = findViewById(R.id.chk);

    }

    private void checkPrefs() {
        boolean flag = prefs.getBoolean(FLAG, false);
        if (flag) {
            String name = prefs.getString(NAME, "");
            String height = prefs.getString(HEIGHT, "");
            String weight = prefs.getString(WEIGHT, "");
            String gender = prefs.getString(GENDER, "");
            edtname.setText(name);
            edtheight.setText(height);
            edtweight.setText(weight);
            //  spinner.setSelection(gender);
            chk.setChecked(true);

        }
    }

    public void btnsave(View v) {
        String name = edtname.getText().toString();
        String height = edtheight.getText().toString();
        String weight = edtweight.getText().toString();
        String gender = prefs.getString(GENDER, "");

        if (chk.isChecked()) {
            editor.putString(NAME, name);
            editor.putString(HEIGHT, height);
            editor.putString(WEIGHT, weight);
            editor.putString(GENDER, gender);
            editor.putBoolean(FLAG, true);
            editor.commit();
        }
    }
}