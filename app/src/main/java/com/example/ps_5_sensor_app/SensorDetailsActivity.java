package com.example.ps_5_sensor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorLightTextView;
    private TextView sensorName;
    private Boolean sensorLightClicked;
    private Boolean sensorTemperatureClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorLightTextView = findViewById(R.id.sensor_light_label);
        sensorName = findViewById(R.id.sensor_name);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLightClicked = getIntent().getBooleanExtra(SensorActivity.KEY_LIGHT_SENSOR_CLICKED, false);
        sensorTemperatureClicked = getIntent().getBooleanExtra(SensorActivity.KEY_TEMPERATURE_SENSOR_CLICKED, false);
        if(sensorLightClicked){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            sensorName.setText(R.string.name_sensor_light);
            if(sensor == null){
                sensorLightTextView.setText(R.string.missing_sensor);
            }
        }
        if(sensorTemperatureClicked){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            sensorName.setText(R.string.name_sensor_temperature);
            if(sensor == null){
                sensorLightTextView.setText(R.string.missing_sensor);
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(sensor != null){
            sensorManager.unregisterListener(this);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue= sensorEvent.values[0];

        switch (sensorType){
            case Sensor.TYPE_LIGHT:
                sensorLightTextView.setText(getResources().getString(R.string.light_sensor_lable, currentValue));
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sensorLightTextView.setText(getResources().getString(R.string.temperature_sensor_lable, currentValue));
                break;
            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d("Accuracy ", "Accuracy" + i);
    }
}