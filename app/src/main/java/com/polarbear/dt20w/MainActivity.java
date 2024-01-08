package com.polarbear.dt20w;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;

    private static final int MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT = 1;

    private EditText dataDisplay;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SerialPortService ID

    private static final String TAG = "MainActivity"; // 定义一个标签


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button connectButton = findViewById(R.id.connectButton);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //判断支不支持蓝牙
        if (bluetoothAdapter == null) {
            Toast.makeText(MainActivity.this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }


        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                //添加权限
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.BLUETOOTH_CONNECT},
                        MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT);

                Log.d(TAG, "onCreate: " + "添加权限");
                return;
            }

            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            selectBluetoothDevice();
        }

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                selectBluetoothDevice();
            } else {
                Toast.makeText(this, "为启动蓝牙", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String address = data.getStringExtra("address");
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    //add permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.BLUETOOTH_CONNECT},
                            MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT);
                    return;
                }
                BluetoothSocket mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket.connect();
                startListening(mmSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startListening(BluetoothSocket socket) {
        try {

            InputStream mmInputStream = socket.getInputStream();
            BufferedReader mmBufferedReader = new BufferedReader(new InputStreamReader(mmInputStream));
            Thread listenThread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        String receivedData = mmBufferedReader.readLine();
                        if (receivedData != null) {
                            int receivedValue = Integer.parseInt(receivedData);
                            if (receivedValue > 500) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 如果接收到的值大于300，执行相应的操作
                                        // 例如，显示一个提示消息或者触发某个事件
                                        dataDisplay.append("Received value is greater than 300: " + receivedData + "\n");
                                        // 这里可以添加其他操作
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 如果接收到的值不大于300，将其显示在界面上
                                        dataDisplay.append(receivedData + "\n");
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            listenThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectBluetoothDevice() {
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT) {
            // 检查请求结果
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，执行需要权限的操作
                // 例如，在这里启动 DeviceListActivity

                Log.d(TAG, "在这里启动 DeviceListActivity");
            } else {
                // 权限被拒绝，可能需要向用户解释为何需要该权限
                Log.d(TAG, "权限被拒绝，可能需要向用户解释为何需要该权限");
                Toast.makeText(this, "需要蓝牙连接权限才能执行该操作", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
