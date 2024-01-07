package com.polarbear.dt20w;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.polarbear.dt20w.communication.BlueToothConnect;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private BluetoothManager bluetoothmanger;
    private BluetoothAdapter bluetoothadapter;

    private static final String TAG = "MainActivity"; // 定义一个标签


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothmanger = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothadapter = bluetoothmanger.getAdapter();
        //判断支不支持蓝牙
        if (bluetoothadapter == null) {
            Toast.makeText(MainActivity.this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        }

        //如果没有打开， 弹窗请求打开蓝牙
        if (!bluetoothadapter.isEnabled()) {
            Log.d(TAG, "蓝牙没有打开， 请求打开蓝牙");
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "检查权限，申请权限");


                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                bluetoothadapter.enable();
                return;
            }
            bluetoothadapter.enable();
        } else {
            Log.d(TAG,"蓝牙已打开了");
        }

        Log.d(TAG,"1111111111111111111");



        Log.d(TAG,"222222222222222222");

        // 在布局文件中定义了一个按钮（id 为 myButton）
        Button myButton = findViewById(R.id.myButton);
        // 设置按钮的点击事件监听器
        myButton.setOnClickListener(view -> {

            // 点击按钮时显示一个 Toast 消息
            showToast("Button Clicked!");
        });

    }

    // 显示 Toast 消息的辅助方法
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
