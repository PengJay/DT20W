package com.polarbear.dt20w;
import android.util.Log;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import android.view.View;
import android.widget.Toast;

import com.polarbear.dt20w.globaldata.MCUData;
import com.polarbear.dt20w.parser.MCUFrameDecoder;

public class MainActivity extends AppCompatActivity implements BLESPPUtils.OnBluetoothAction{

    // 蓝牙工具
    private BLESPPUtils mBLESPPUtils;
    // 保存搜索到的设备，避免重复
    private ArrayList<BluetoothDevice> mDevicesList = new ArrayList<>();

    // log 视图
    private TextView mLogTv;
    // 输入的 ET
    private EditText mInputET;
    private DeviceDialogCtrl mDeviceDialogCtrl;
    private static final String TAG = "MainActivity"; // 定义一个标签

    private MCUFrameDecoder mcuFrameDecoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermissions();

        mcuFrameDecoder = new MCUFrameDecoder();

        mBLESPPUtils = new BLESPPUtils(this, this);
        mBLESPPUtils.enableBluetooth();
        mBLESPPUtils.setStopString("01");
        if (!mBLESPPUtils.isBluetoothEnable()) mBLESPPUtils.enableBluetooth();
        mBLESPPUtils.onCreate();

        mDeviceDialogCtrl = new DeviceDialogCtrl(this);
        mDeviceDialogCtrl.show();

    }

    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, "android.permission-group.LOCATION") != 0) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            "android.permission.ACCESS_FINE_LOCATION",
                            "android.permission.ACCESS_COARSE_LOCATION",
                            "android.permission.ACCESS_WIFI_STATE"},
                    1
            );
        }
    }
    @Override
    public void onFoundDevice(BluetoothDevice device) {
        Log.d("BLE", "发现设备 " + device.getName() + device.getAddress());
        // 判断是不是重复的
        for (int i = 0; i < mDevicesList.size(); i++) {
            if (mDevicesList.get(i).getAddress().equals(device.getAddress())) return;
        }
        // 添加，下次有就不显示了
        mDevicesList.add(device);
        // 添加条目到 UI 并设置点击事件
        mDeviceDialogCtrl.addDevice(device, new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                BluetoothDevice clickDevice = (BluetoothDevice) v.getTag();
                postShowToast("开始连接:" + clickDevice.getName());
                mLogTv.setText(mLogTv.getText() + "\n" + "开始连接:" + clickDevice.getName());
                mBLESPPUtils.connect(clickDevice);
            }
        });
    }

    @Override
    public void onConnectSuccess(final BluetoothDevice device) {

    }
    @Override
    public void onConnectFailed(final String msg) {

    }
    @Override
    public void onReceiveBytes(final byte[] bytes) {
        MCUData mcuData = mcuFrameDecoder.decoder(bytes);
        showInfo(mcuData);
    }
    @Override
    public void onSendBytes(final byte[] bytes) {

    }
    @Override
    public void onFinishFoundDevice() { }

    void showInfo(MCUData mcuData){}

    private class TextActivity{
        public TextView laserData;
        public TextView driverData;
        public TextView tecData;

        public TextView limData;
        public TextView setData;
        public TextView powerInData;
        public TextView inputGreen;
        public TextView inputRed;
        public TextView inputBlue;
        public TextView voltageRedData;
        public TextView voltageGreenData;
        public TextView voltageBlueData;
        public TextView curRedData;
        public TextView curGreenData;
        public TextView curBlueData;
        public TextView offsetRedData;
        public TextView offsetGreenData;
        public TextView offsetBlueData;
        public TextView thRedData;
        public TextView thGreenData;
        public TextView thBlueData;
        public TextView fwData;

        TextActivity(){
            laserData = findViewById(R.id.laserData);
            driverData = findViewById(R.id.driverData);
            tecData = findViewById(R.id.tecData);
            limData = findViewById(R.id.limData);
            setData = findViewById(R.id.setData);
            powerInData = findViewById(R.id.powerInData);
            inputGreen = findViewById(R.id.inputGreen);
            inputRed = findViewById(R.id.inputRed);
            inputBlue = findViewById(R.id.inputBlue);
            voltageRedData = findViewById(R.id.voltageRedData);
            voltageGreenData = findViewById(R.id.voltageGreenData);
            voltageBlueData = findViewById(R.id.voltageBlueData);
            curRedData = findViewById(R.id.curRedData);
            curGreenData = findViewById(R.id.curGreenData);
            curBlueData = findViewById(R.id.curBlueData);
            offsetRedData = findViewById(R.id.offsetRedData);
            offsetGreenData= findViewById(R.id.offsetGreenData);
            offsetBlueData = findViewById(R.id.offsetBlueData);
            thRedData = findViewById(R.id.thRedData);
            thGreenData = findViewById(R.id.thGreenData);
            thBlueData = findViewById(R.id.thBlueData);
            fwData = findViewById(R.id.fwData);
        }
        void updateTextActivity(MCUData mcuData){

        }
    }

    private class DeviceDialogCtrl {
        private LinearLayout mDialogRootView;
        private ProgressBar mProgressBar;
        private AlertDialog mConnectDeviceDialog;

        DeviceDialogCtrl(Context context) {
            // 搜索进度条
            mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            mProgressBar.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            50
                    )
            );

            // 根布局
            mDialogRootView = new LinearLayout(context);
            mDialogRootView.setOrientation(LinearLayout.VERTICAL);
            mDialogRootView.addView(mProgressBar);
            mDialogRootView.setMinimumHeight(700);

            // 容器布局
            ScrollView scrollView = new ScrollView(context);
            scrollView.addView(mDialogRootView,
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            700
                    )
            );

            // 构建对话框
            mConnectDeviceDialog = new AlertDialog
                    .Builder(context)
                    .setNegativeButton("刷新", null)
                    .setPositiveButton("退出", null)
                    .create();
            mConnectDeviceDialog.setTitle("选择连接的蓝牙设备");
            mConnectDeviceDialog.setView(scrollView);
            mConnectDeviceDialog.setCancelable(false);
        }
        void show() {
            mBLESPPUtils.startDiscovery();
            mConnectDeviceDialog.show();
            mConnectDeviceDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mConnectDeviceDialog.dismiss();
                    return false;
                }
            });
            mConnectDeviceDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mConnectDeviceDialog.dismiss();
                    finish();
                }
            });
            mConnectDeviceDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogRootView.removeAllViews();
                    mDialogRootView.addView(mProgressBar);
                    mDevicesList.clear();
                    mBLESPPUtils.startDiscovery();
                }
            });
        }

        /**
         * 取消对话框
         */
        void dismiss() {
            mConnectDeviceDialog.dismiss();
        }
        private void addDevice(final BluetoothDevice device, final View.OnClickListener onClickListener) {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    TextView devTag = new TextView(MainActivity.this);
                    devTag.setClickable(true);
                    devTag.setPadding(20,20,20,20);
                    //devTag.setBackgroundResource(R.drawable.rect_round_button_ripple);
                    devTag.setText(device.getName() + "\nMAC:" + device.getAddress());
                    //devTag.setTextColor(Color.WHITE);
                    devTag.setOnClickListener(onClickListener);
                    devTag.setTag(device);
                    devTag.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                    );
                    ((LinearLayout.LayoutParams) devTag.getLayoutParams()).setMargins(
                            20, 20, 20, 20);
                    mDialogRootView.addView(devTag);
                }
            });
        }
    }
    private void postShowToast(final String msg) {
        postShowToast(msg, null);
    }
    private void postShowToast(final String msg, final DoSthAfterPost doSthAfterPost) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                if (doSthAfterPost != null) doSthAfterPost.doIt();
            }
        });
    }

    private interface DoSthAfterPost {
        void doIt();
    }

}
