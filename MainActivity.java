package com.example.thermo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver; //listens to broadcasts
import android.content.Context; //provides access to android services like location, or broadcast receiving
import android.content.Intent; //start service, broadcast message
import android.content.IntentFilter; // fetches information about app activities
//android.content supplies data from one place to other
import android.hardware.Sensor; //lists to hardware sensors
import android.hardware.SensorEvent; //holds the sensor's type, collected data and more
import android.hardware.SensorEventListener; //checks new sensors data
import android.hardware.SensorManager; ///provides access it hardware sensors
import android.os.BatteryManager; //contains strings and values needed for battery and fetches battery properties
import android.os.Bundle; //pass data from one activity to another
//An activity is a single frame
import android.view.Gravity; //positioning the contents in the activity
import android.view.LayoutInflater; //to create layouts
import android.view.View; //basic building block of UI components like textView, editText, buttons...
import android.widget.Button; //for handling clickable contexts
import android.widget.ImageButton; //for handling image filled clickable contexts
import android.widget.LinearLayout; //layout that positions views vertically or horizontally
import android.widget.PopupWindow; //for pop-up screen
import android.widget.TextView; //to display texts
import java.time.LocalTime; //for fetching local time

import androidx.appcompat.app.AppCompatActivity; //to support new features on old devices

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
