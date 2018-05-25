package com.abt.location;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.abt.location.bean.Location;
import com.abt.location.manager.LocationManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_latitude)
    TextView mLatitude;
    @BindView(R.id.tv_longitude)
    TextView mLongitude;
    @BindView(R.id.tv_accuracy)
    TextView mAccuracy;
    @BindView(R.id.tv_time)
    TextView mTime;
    @BindView(R.id.tv_data)
    TextView mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //google API返回的经纬度结果推荐去http://www.gpsspg.com/maps.htm查看具体位置，防止国内被混淆
        LocationManager.onCreateGPS(getApplication()); // 开启位置监听
        checkLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationManager.stopGPS();
    }

    /** 每隔2s更新一下经纬度结果 */
    private void checkLocation() {
        final Handler handler = new Handler();
        new Timer().scheduleAtFixedRate(new TimerTask() { // 每秒钟检查一下当前位置
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLatitude.setText(String.valueOf(Location.getInstance().latitude));
                        mLongitude.setText(String.valueOf(Location.getInstance().longitude));
                        mAccuracy.setText(String.valueOf(Location.getInstance().accuracy));
                        if (Location.getInstance().updateTime != 0)
                            mTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Location.getInstance().updateTime)));
                        if (LocationManager.getInstance() == null) return;
                        String json = LocationManager.getInstance().dataJson;
                        if (TextUtils.isEmpty(json)) return;
                        json = json.replaceAll("\\},", "},\n");
                        mData.setText(json);
                    }
                });
            }
        }, 0, 2000);
    }
}
