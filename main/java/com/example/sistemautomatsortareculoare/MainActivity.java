package com.example.sistemautomatsortareculoare;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    BluetoothAdapter BAdapter;
    ImageView mBlueIv;
    ToggleButton BTstatus;
    Button Devices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Devices = (Button) findViewById(R.id.devicesBtn);
        Devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openDiscover();
            }
        });
        mBlueIv = findViewById(R.id.bluetoothIv);
        mBlueIv.setImageResource(R.drawable.ic_action_bluetoothoff);
        BTstatus = (ToggleButton) findViewById(R.id.onBtn);
        BTstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
                    mBlueIv.setImageResource(R.drawable.ic_action_bluetoothon);
                }
                else{
                    BAdapter.disable();
                    mBlueIv.setImageResource(R.drawable.ic_action_bluetoothoff);
                }
            }
        });
        //Adaptor bluetooth
        BAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    void DisplayToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public void openDiscover(){
        Intent intent = new Intent(this, DiscoverDevices.class);
        startActivity(intent);
    }
}
