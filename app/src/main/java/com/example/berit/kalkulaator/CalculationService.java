package com.example.berit.kalkulaator;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CalculationService extends Service {
    private final IBinder mBinder = new LocalBinder();

    public static String TAG = "CalculationService";

    public class LocalBinder extends Binder {
        CalculationService getService() {
            Log.d(TAG, "getService");
            return CalculationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    //Arvutamise tehte valik
    public String calculate(double n1, double n2, String op) {
        double calc = 0;

        if (op.equals("+")) {
            calc = n1 + n2;
        } else if (op.equals("-")) {
            calc = n1 - n2;
        } else if (op.equals("÷")) {
            if(n2==0){
                return "Nulliga ei saa jagada";
            } else {
                calc = n1 / n2;
            }
        } else if (op.equals("×")) {
            calc = n1 * n2;
        }
        return String.valueOf(calc);
    }
}