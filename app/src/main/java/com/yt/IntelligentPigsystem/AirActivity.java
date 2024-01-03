package com.yt.IntelligentPigsystem;

import static android.system.Os.close;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yt.IntelligentPigsystem.net.UdpUtil;

import java.io.IOException;
import java.net.SocketException;

public class AirActivity extends AppCompatActivity {
    UdpUtil udpUtil;
    String ip = "192.168.123.238";
    int port = 20001;
    boolean isAirOn = false;
    boolean isWaterOn = false;
    boolean isFoodOn = false;
    boolean isFanOn = false;

    ImageView ivAir, ivWater, ivFood, ivFan;
    TextView tvAirStatus, tvWaterStatus, tvFoodStatus, tvFanStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_air_dialog);

        // 初始化控件
        ivAir = findViewById(R.id.iv_airDia_air);
        ivWater = findViewById(R.id.iv_airDia_water);
        ivFood = findViewById(R.id.iv_airDia_food);
        ivFan = findViewById(R.id.iv_airDia_fan);

        tvAirStatus = findViewById(R.id.tv_airDia_air);
        tvWaterStatus = findViewById(R.id.tv_airDia_water);
        tvFoodStatus = findViewById(R.id.tv_airDia_food);
        tvFanStatus = findViewById(R.id.tv_airDia_fans);

        ivAir.setOnClickListener(view -> toggleDeviceState("AC", ivAir, tvAirStatus));
        ivWater.setOnClickListener(view -> toggleDeviceState("WT", ivWater, tvWaterStatus));
        ivFood.setOnClickListener(view -> toggleDeviceState("FD", ivFood, tvFoodStatus));
        ivFan.setOnClickListener(view -> toggleDeviceState("FN", ivFan, tvFanStatus));

    }
    private void toggleDeviceState(String deviceCode, ImageView iv, TextView tv) {
        boolean isOn;
        switch (deviceCode) {
            case "AC":
                isAirOn = !isAirOn;
                isOn = isAirOn;
                break;
            case "WT":
                isWaterOn = !isWaterOn;
                isOn = isWaterOn;
                break;
            case "FD":
                isFoodOn = !isFoodOn;
                isOn = isFoodOn;
                break;
            case "FN":
                isFanOn = !isFanOn;
                isOn = isFanOn;
                break;
            default:
                return;
        }

        updateUI(iv, tv, isOn, deviceCode);
        sendUdpMessage(deviceCode + (isOn ? " ON!" : " OFF!"));
    }

    private void updateUI(ImageView iv, TextView tv, boolean isOn, String deviceCode) {
        iv.setBackgroundResource(isOn ? R.color.green2 : R.color.red);
        String statusText = getDeviceName(deviceCode) + (isOn ? " 已打开" : " 已关闭");
        tv.setText(statusText);
    }

    private String getDeviceName(String deviceCode) {
        switch (deviceCode) {
            case "AC":
                return "空调";
            case "WT":
                return "水系统";
            case "FD":
                return "进料系统";
            case "FN":
                return "排气扇";
            default:
                return "设备";
        }
    }

    private void sendUdpMessage(String message) {
        UdpUtil udpUtil;
        try {
            udpUtil = new UdpUtil();
            udpUtil.sendMsg(this, ip, port, message);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
