package com.polarbear.dt20w;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
