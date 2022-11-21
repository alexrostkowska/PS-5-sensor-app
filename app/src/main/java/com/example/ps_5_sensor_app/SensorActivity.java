package com.example.ps_5_sensor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private SensorAdapter adapter;
    private Boolean sensorLightClicked = false;
    private Boolean sensorTemperatureClicked = false;
    public static final String KEY_LIGHT_SENSOR_CLICKED = "LIGHT_SENSOR";
    public static final String KEY_TEMPERATURE_SENSOR_CLICKED = "TEMPERATURE_SENSOR";
    public static final String KEY_LOCATION_SENSOR_CLICKED = "LOCATION_SENSOR";
    private boolean subtitleVisible;
    public static final String SUBTITLE_KEY = "SUBTITLE_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_KEY);
        }

        setContentView(R.layout.sensor_activity);
        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        if(adapter == null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else
            adapter.notifyDataSetChanged();

        updateSubtitle();
    }

    private class SensorHolder extends RecyclerView.ViewHolder {
        private TextView sensorTextView;
        private ImageView iconImageView;
        private Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            //itemView.setOnClickListener(this);
            sensorTextView = itemView.findViewById(R.id.sensor_name);
            iconImageView = itemView.findViewById(R.id.sensor_item_view);
        }

        public void bind(Sensor sensor){
            this.sensor = sensor;
            sensorTextView.setText(sensor.getName());
            iconImageView.setImageResource(R.drawable.ic_action_name);
            Log.d("Sensors", "Sensor name: "+ sensor.getName() + " Producent:"+sensor.getVendor() +
                    " Max return value:" + sensor.getMaximumRange());

            if(sensor.getType() == Sensor.TYPE_LIGHT){
                sensorTextView.setTypeface(null, Typeface.BOLD_ITALIC);
                sensorLightClicked = true;
                sensorTextView.setOnClickListener((v)->{
                    Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    intent.putExtra(KEY_LIGHT_SENSOR_CLICKED, sensorLightClicked);
                    startActivity(intent);
                });
            }
            if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
                sensorTextView.setTypeface(null, Typeface.BOLD_ITALIC);
                sensorTemperatureClicked = true;
                sensorTextView.setOnClickListener((v)->{
                    Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    intent.putExtra(KEY_TEMPERATURE_SENSOR_CLICKED, sensorTemperatureClicked);
                    startActivity(intent);
                });
            }
            if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                sensorTextView.setTextColor(Color.GREEN);
                sensorTextView.setOnClickListener((v)->{
                    Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                    intent.putExtra(KEY_LOCATION_SENSOR_CLICKED, true);
                    startActivity(intent);
                });
            }
        }
    }
    private class SensorAdapter extends  RecyclerView.Adapter<SensorHolder>{
        private List<Sensor> sensors;
        SensorAdapter(List<Sensor> sensors){
            this.sensors = sensors;
        }
        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensor_activity_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        String subtitle = getString(R.string.sensor_count, sensorList.size());
        if(!subtitleVisible){
            subtitle = null;
        }
        getSupportActionBar().setSubtitle(subtitle);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putBoolean(SUBTITLE_KEY, subtitleVisible);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
