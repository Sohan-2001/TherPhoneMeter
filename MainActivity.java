package com.example.thermo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView temp,light; //Reading Temperature Variable
    float batteryTemp,Light;
    IntentFilter intentfilter;
    int count=0;
    String currentBatterytemp="Battery Temperature : ";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPress = findViewById(R.id.button);
        buttonPress.setOnClickListener(v -> MainActivity.this.registerReceiver(broadcastreceiver,intentfilter));

        temp= findViewById(R.id.textViewtemp); //Accessing to Temperature output panel
        light= findViewById(R.id.textViewLight);


        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        SensorManager mySensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor LightSensor=mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        if(LightSensor!=null){
            mySensorManager.registerListener(LightSensorListener,LightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    private final SensorEventListener LightSensorListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_LIGHT){
                Light=event.values[0];

                if(count>0){
                    light.setText("Surrounding Light : "+Light+" lx");
                }

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            count=1;



            batteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;

            temp.setText(currentBatterytemp +" "+batteryTemp +" "+ (char) 0x00B0 +"C");

        }
    };


}
