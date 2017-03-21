package app.guo.cn.readmysms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 广播
 * Created by Scylla on 2017/3/21.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = SmsReceiver.class.getSimpleName();

    public static final String BC_RECEIVE_SMS = "broadcast_receive_sms";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"广播收到短息");
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
                Date date = new Date(msg.getTimestampMillis());//时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String number = msg.getOriginatingAddress();
                String receiveTime = format.format(date);
                String body = msg.getDisplayMessageBody();
                Log.i(TAG,"\nnumber:" + number
                        + "\nbody:" + body
                        + "\ntime:" + receiveTime);

                SmsRadarService.dealWithMsg(context,body);

                Intent bc = new Intent(BC_RECEIVE_SMS);
                context.sendBroadcast(bc);//传递过去

            }
        }
    }
}
