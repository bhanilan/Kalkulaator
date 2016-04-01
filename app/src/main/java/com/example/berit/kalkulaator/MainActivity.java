package com.example.berit.kalkulaator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean mBound = false;
    private CalculationService calcService;
    private String display = "";
    private String op = "";
    private String no1 = "";
    private String no2 = "";
    private double n1, n2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate called");
        }

        setContentView(R.layout.activity_main);

        Resources res = getResources();
        float fontSize = res.getDimension(R.dimen.font_size);
    }

    //Saving and displaying first and second number
    public void onClick(View view){
        Button button = (Button) view;
        String str = button.getText().toString();
        str = validation(str);

        if (op.isEmpty()){
            no1 += str;
        } else {
            if(no2.isEmpty()){
                display = "";
            }
            no2 += str;
        }
        display += str;
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(display);
    }

    //Saving operation sign
    public void selectOperation(View view){
        if (!no1.isEmpty() && !no2.isEmpty()) {
            Button button = (Button) view;
            doCalculation(view);
            String str = button.getText().toString();
            n1 = Double.parseDouble(no1);
            op = str;
            return;
        }
        n1 = Double.parseDouble(no1);
        Button button = (Button) view;
        op = button.getText().toString();
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(no1);
    }

    //Use Calculation service
    public void doCalculation(View view){
        if (no1.isEmpty() || no2.isEmpty()) {
            return;
        }
        EditText editText = (EditText) findViewById(R.id.editText);

        n2 = Double.parseDouble(no2);

        String calc = calcService.calculate(n1, n2, op);
        editText.setText(calc);
        display = calc;
        no1 = calc;
        no2 ="";
        op = "";
    }

    //Delete previous
    public void doDelete(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        display = "";
        no1 = "";
        op = "";
        no2 = "";
        editText.setText(display);
    }

    //Input validation
    public String validation(String str) {
        //if first input is coma
        if (display.isEmpty() && ".".equals(str)) {
            str = "0.";
        }
        //if second number starts with coma
        if (!display.isEmpty() && !op.isEmpty() && no2.isEmpty() && ".".equals(str)) {
            str = "0.";
        }
        //if number starts with 0 coma should be clicked
        if (no1.equals("0") && !".".equals(str)) {
            str = "";
        }
        //number can't contain more than one coma
        if (display.contains(".") && ".".equals(str)) {
            str = "";
        }
        return str;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStart called");
        }
        Intent intent = new Intent(this, CalculationService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onResume called");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPause called");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStop called");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDestroy called");
        }
    }

    //Yhenduse loomine arvutamise teenusega
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d(TAG, "onServiceConnected");
            CalculationService.LocalBinder binder = (CalculationService.LocalBinder) service;
            calcService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "onServiceDisconnected");
            mBound = false;
        }
    };
}