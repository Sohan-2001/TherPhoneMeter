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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.time.LocalTime;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView temp,light,TEMP,showMessage,showEmoji; //Reading Temperature Variable
    float batteryTemp;
    static float Light;
    IntentFilter intentfilter;
    int count=0;
    String currentBatterytemp="Battery Temperature : ";

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPress = findViewById(R.id.button);
        ImageButton POPUP=findViewById(R.id.imageButton);
        POPUP.setOnClickListener(v -> {
            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_window, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

            // dismiss the popup window when touched
            popupView.setOnTouchListener((v1, event) -> {
                popupWindow.dismiss();
                return true;
            });
        });
        buttonPress.setOnClickListener(v -> MainActivity.this.registerReceiver(broadcastreceiver,intentfilter));

        temp= findViewById(R.id.textViewtemp); //Accessing to Temperature output panel
        light= findViewById(R.id.textViewLight);
        TEMP=findViewById(R.id.textView4);
        showMessage=findViewById(R.id.textView);
        showEmoji=findViewById(R.id.textView5);
        LocalTime now=LocalTime.now();
        String greet="Good";
        if(now.isBefore(LocalTime.parse("11:00:00.0"))&& now.isAfter(LocalTime.parse("06:00:00.0"))){
            greet+=" Morning";
        }
        else if(now.isBefore(LocalTime.parse("16:00:00.0"))&& now.isAfter(LocalTime.parse("11:00:00.1"))){
            greet+=" Noon";
        }
        else if(now.isBefore(LocalTime.parse("19:00:00.0"))&& now.isAfter(LocalTime.parse("16:00:00.1"))){
            greet+=" Afternoon";
        }
        else if(now.isBefore(LocalTime.parse("21:00:00.0"))&& now.isAfter(LocalTime.parse("19:00:00.1"))){
            greet+=" Evening";
        }
        else if(now.isAfter(LocalTime.parse("21:00:00.1"))){
            greet+=" Night";
        }
        else if(now.isBefore(LocalTime.parse("04:00:00.0"))&& now.isAfter(LocalTime.parse("00:00:00.0"))){
            greet+=" Night";
        }
        else{
            greet+=" Dawn";
        }
        showMessage.setText(greet);


        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        SensorManager mySensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor LightSensor=mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        if(LightSensor!=null){
            mySensorManager.registerListener(LightSensorListener,LightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    //popup-message


    private final SensorEventListener LightSensorListener=new SensorEventListener() {
        @SuppressLint("SetTextI18n")
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

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {





            batteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
            count=1;

            temp.setText(currentBatterytemp +" "+batteryTemp +" "+ (char) 0x00B0 +"C");

            if(Light<=400){
                double val=batteryTemp;
                TEMP.setText((int)val+" ℃");
                showMessage.setText("Chilling inside room, right!");
                showEmoji.setText("📖🍷😪");
            } else if (Light>400&&Light<=1000) {
                double val=batteryTemp;
                TEMP.setText((int)val+" ℃");
                showMessage.setText("Reading!");
                showEmoji.setText("📖");

            }

             else if(Light>=60000){
                double val=batteryTemp;
                TEMP.setText((int)val+" ℃");
                showMessage.setText("Taking Sun-Bath! LOL");
                showEmoji.setText("😎😎");
            }
            else{
                double val=batteryTemp;
                TEMP.setText((int)val+" ℃");
                showMessage.setText("Enjoying the summer!");
                showEmoji.setText("🥵♨️🛎️");
            }

        }
    };


}
