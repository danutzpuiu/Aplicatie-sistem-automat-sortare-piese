package com.example.sistemautomatsortareculoare;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class DiscoverDevices extends MainActivity {
    public static final int REQUEST_DISCOVER_BT = 1;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    OutputStream trimitedata;
    InputStream primestedata;
    int rosu, galben, verde, albastru, maro, portocaliu;
    TextView mPairedTv;
    Button DiscoverBtn, PairedBtn, ConnectBtn, Stop, Start, Refresh;
    Switch SwitchBtn;
    BluetoothAdapter BAdapter;
    BluetoothDevice BTdevice;
    BluetoothSocket BTsocket = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_devices);
        getSupportActionBar().setTitle("Dispozitive & Conexiune");
        Refresh = findViewById(R.id.refreshBtn);
        DiscoverBtn = findViewById(R.id.discBtn);
        PairedBtn = findViewById(R.id.pairBtn);
        ConnectBtn = findViewById(R.id.connectBtn);
        mPairedTv = findViewById(R.id.pairTv);
        Start = findViewById(R.id.startBtn);
        Stop = findViewById(R.id.stopBtn);
        SwitchBtn = findViewById(R.id.switch1);
        Timer timer = new Timer();
        BAdapter = BluetoothAdapter.getDefaultAdapter();
        BTdevice = BAdapter.getRemoteDevice("00:19:10:08:E1:21");
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                if (SwitchBtn.isChecked()){
                    try {
                        trimitedata = BTsocket.getOutputStream();
                        trimitedata.write(48);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(t,500,6000);
        SwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    DisplayToast("Procesul automat de sortare va porni in cateva secunde!");
                }
            }
        });
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    trimitedata = BTsocket.getOutputStream();
                    trimitedata.write(48);
                    DisplayToast("Se sortează următoarea piesă...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayToast("Procesul automat de sortare este OPRIT.");
                if (SwitchBtn.isChecked()){
                    SwitchBtn.setChecked(!SwitchBtn.isChecked());
                }
                    try {
                        trimitedata = BTsocket.getOutputStream();
                        trimitedata.write(49);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });

        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    trimitedata = BTsocket.getOutputStream();
                    trimitedata.write(47);
                    primestedata = BTsocket.getInputStream();
                    primestedata.skip(primestedata.available());
                    int r = (int) primestedata.read();
                    rosu = r;
                    int a = (int) primestedata.read();
                    albastru = a;
                    int ver = (int) primestedata.read();
                    verde = ver;
                    int g = (int) primestedata.read();
                    galben = g;
                    int m = (int) primestedata.read();
                    maro = m;
                    int p = (int) primestedata.read();
                    portocaliu = p;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DisplayToast("Roşu: "+(char) rosu + " Albastru: " +(char) albastru+" Verde: " +(char) verde +" Galben: " +(char) galben + " Maro: " +(char) maro +" Portocaliu: " +(char) portocaliu);
                mPairedTv.setText("");
                mPairedTv.append("     Roşu: "+(char) rosu + "             Albastru: " +(char) albastru+"             Verde: " +(char) verde);
                mPairedTv.append("\n    Galben: " +(char) galben + "             Maro: " +(char) maro +"             Portocaliu: " +(char) portocaliu);
            }
        });

        DiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BAdapter.isDiscovering()){
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
                else{
                    DisplayToast("Dispozitivul Bluetooth este deja disponibil!");
                }
            }
        });

        PairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BAdapter.isEnabled()){
                    BAdapter.startDiscovery();
                    mPairedTv.setText("");
                    Set<BluetoothDevice> devices = BAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices){
                        mPairedTv.append("\nDispozitivul: " + device.getName() + ", Adresa MAC:" +device);
                    }
                }
            }
        });

        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BAdapter.isEnabled()){
                    try {
                        BTsocket = BTdevice.createRfcommSocketToServiceRecord(mUUID);
                        BTsocket.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (BTsocket.isConnected()){
                        DisplayToast("Conectat cu succes la dispozitivul " +BTdevice.getName());
                    }
                    else{
                        DisplayToast("Nu s-a putut conecta sau este deja conectat.");
                    }
                }
                else{
                    DisplayToast("Bluetooth trebuie pornit");
                }
            }
        });

    }

    void DisplayToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
}