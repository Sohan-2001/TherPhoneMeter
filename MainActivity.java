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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView temp,light,TEMP; //Reading Temperature Variable
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
        ImageButton POPUP=findViewById(R.id.imageButton);
        POPUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });
        buttonPress.setOnClickListener(v -> MainActivity.this.registerReceiver(broadcastreceiver,intentfilter));

        temp= findViewById(R.id.textViewtemp); //Accessing to Temperature output panel
        light= findViewById(R.id.textViewLight);
        TEMP=findViewById(R.id.textView4);


        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        SensorManager mySensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor LightSensor=mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        if(LightSensor!=null){
            mySensorManager.registerListener(LightSensorListener,LightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    //popup-message


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

            if(Light<50000){
                double val=batteryTemp-1;
                TEMP.setText((int)val+" ℃");
            }
            else{
                double val=batteryTemp+2;
                TEMP.setText((int)val+" ℃ 😎");
            }

        }
    };


}
