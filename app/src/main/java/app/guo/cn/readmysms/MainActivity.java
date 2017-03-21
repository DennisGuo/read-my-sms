package app.guo.cn.readmysms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * Created by Scylla on 2017/3/21.
 */

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mRefresh;
    private LinearLayout mList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        mRefresh = (Button) findViewById(R.id.refresh);
        mList = (LinearLayout) findViewById(R.id.list_log);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLogs();
                Toast.makeText(mContext,"短信日志刷新成功",Toast.LENGTH_LONG).show();
            }
        });

        loadLogs();

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadLogs();
                Toast.makeText(mContext,"ReadMySMS::解析新短信",Toast.LENGTH_LONG).show();
            }
        },new IntentFilter(SmsReceiver.BC_RECEIVE_SMS));

    }

    private void loadLogs() {

        try {
            List<String> arr = new ArrayList<>();
            String path = getFilesDir().getPath() + File.separator + "my-sms.json";
            File file = new File(path);

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String tmp = null;
                while ((tmp = reader.readLine()) != null) {
                    arr.add(tmp);
                }

            }
            renderLogs(arr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderLogs(List<String> arr) {
        Log.i(TAG,"共有日志记录"+arr.size()+"条");
        mList.removeAllViews();
        TextView view = (TextView) getLayoutInflater().inflate(R.layout.item_log, null);
        if(arr.size() > 0 ){
            for (String log:arr){
                MySms sms = JSON.parseObject(log,MySms.class);
                view = (TextView) getLayoutInflater().inflate(R.layout.item_log, null);
                view.setText(String.format("%s\n编号:%s总额:%s\n钱包:%s流水:%s",
                        sms.getDate().toString(),
                        sms.getNumber(),
                        sms.getTotal(),
                        sms.getWallet(),
                        sms.getSerial()));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,0,0,5);
                view.setLayoutParams(lp);
                mList.addView(view);
            }
        }else{
            view.setText("-_-暂无日志哦-_-");
            view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mList.addView(view);
        }

    }
}
