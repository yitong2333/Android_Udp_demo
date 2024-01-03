package com.yt.IntelligentPigsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yt.IntelligentPigsystem.db.MySqlHelper;
import com.yt.IntelligentPigsystem.net.UdpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvTem, tvHum, tvTime, tvName;
    //接收相关变量
    private TextView tvmPig,tvFmPig,tvWater,tvBean,tvFeed,tvProteins;
    UdpUtil udpUtil;
    String ip = "192.168.123.131";
    int port = 20001;
    //日期显示相关变量
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private final Handler handler = new Handler();
    //轻量级存储类初始化
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView ivAir;
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            WindowDecorActionBar.TabImpl tvCurrentTime;
            tvTime.setText(timeFormat.format(new Date()));
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tvTem = findViewById(R.id.tv_main_temper);
        tvHum = findViewById(R.id.tv_main_hum);
        tvTime = findViewById(R.id.tv_main_time);
        tvName = findViewById(R.id.tv_main_name);
        ivAir = findViewById(R.id.btn_main_air);
        tvmPig = findViewById(R.id.tv_main_mPig);
        tvFmPig = findViewById(R.id.tv_main_fmPig);
        tvBean = findViewById(R.id.tv_main_bean);
        tvFeed = findViewById(R.id.tv_main_feed);
        tvWater = findViewById(R.id.tv_main_water);
        tvProteins = findViewById(R.id.tv_main_proteins);

        handler.post(updateTimeRunnable);
        sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        getWeatherData();

        //UDP相关
        try {
            udpUtil = new UdpUtil();
            new Thread(runnable).start(); // 启动接收线程

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        String _tel = sharedPreferences.getString("Phone",null);
        tvName.setText(_tel);

        ivAir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AirActivity.class);
                startActivity(intent);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dialog,null);
                TextView tvForgetPass = view1.findViewById(R.id.tv_dia_forget);
                TextView tvDeleteAccount = view1.findViewById(R.id.tv_dia_delete);
                TextView tvSign = view1.findViewById(R.id.tv_dia_sign);
                TextView tvLogout = view1.findViewById(R.id.tv_dia_change);
                tvLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //登出
                        editor.clear();
                        editor.apply();
                        Toast.makeText(getApplicationContext(),"账户已登出！",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                tvSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //注册新用户
                        Intent intent = new Intent(getApplicationContext(),SigninActivity.class);
                        startActivity(intent);
                    }
                });
                tvForgetPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //忘记密码事件
                        Intent intent = new Intent(getApplicationContext(),ForgetActivity.class);
                        startActivity(intent);
                    }
                });
                tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //注销账户事件
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("确认注销账户么？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String _tel = sharedPreferences.getString("Phone",null);
                                MySqlHelper.SignHelper signHelper = new MySqlHelper.SignHelper(MainActivity.this);
                                signHelper.Delete(_tel);
                                Toast.makeText(getApplicationContext(),"已注销账户 "+ _tel,Toast.LENGTH_SHORT).show();
                                editor.clear();
                                editor.apply();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"什么也没做~",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                tvSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //注册新用户事件
                        Intent intent =new Intent(getApplicationContext(),SigninActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.setView(view1);
                alertDialog.show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
    }
    private void getWeatherData() {
        //获取天气
        String url = "https://devapi.qweather.com/v7/weather/now?location=101120601&key=eb2f5017665a4129b1e674c8a54b8aac";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject now = response.getJSONObject("now");
                            final String temperature = now.getString("temp");
                            final String humidity = now.getString("humidity");

                            // 更新 UI
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    tvTem.setText(temperature + "°C");
                                    tvHum.setText(humidity + "%");
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 处理错误
                    }
                });

        queue.add(jsonObjectRequest);
    }
    //udp接收
    Handler handler1 = new Handler(Looper.getMainLooper());
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String revStr = udpUtil.revMsg(12346);
                        Log.e("revStr", revStr);

                        // 将数据处理和UI更新回到主线程
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                // 确保revStr不为空且包含数据
                                if (revStr != null && !revStr.isEmpty()) {
                                    String[] data = revStr.split(",");
                                    // 确保data数组有足够的元素
                                    if (data.length >= 2) {
                                        tvmPig.setText(data[0] + " 头");
                                        tvFmPig.setText(data[1] + "头");
                                        tvBean.setText(data[2] + "KG  ");
                                        tvFeed.setText(data[3] + "KG");
                                        tvWater.setText(data[4] + "升");
                                        tvProteins.setText(data[5] + "KG");
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.e("UDP", "Error in receiving message", e);
                    }
                }
            }).start();

            // 安排下一次执行
            handler1.postDelayed(this, 1000);
        }

    };
}
