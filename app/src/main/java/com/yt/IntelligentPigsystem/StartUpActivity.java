package com.yt.IntelligentPigsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_startup);

        // 使用 Handler 延迟三秒后跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 创建 Intent 实例，从 StartUpActivity 跳转到 MainActivity
                Intent intent = new Intent(StartUpActivity.this, LoginActivity.class);
                // 启动 MainActivity
                startActivity(intent);
                // 关闭当前的 StartUpActivity
                finish();
            }
        }, 3000); // 3000 毫秒即 3 秒
    }
}
