package app.guo.cn.readmysms;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * 消息体存储
 * Created by Scylla on 2017/3/21.
 */

public class MySms implements Parcelable{

    private String number; //Код
    private String total;  //сумма
    private String wallet; //кошелек
    private String serial; //Cессия

    private Date date = new Date();
    private boolean send = false;


    public MySms() {
    }

    public MySms(String number, String total, String wallet, String serial) {
        this.number = number;
        this.total = total;
        this.wallet = wallet;
        this.serial = serial;
    }

    protected MySms(Parcel in) {
        number = in.readString();
        total = in.readString();
        wallet = in.readString();
        serial = in.readString();
        send = in.readByte() != 0;
    }

    public static final Creator<MySms> CREATOR = new Creator<MySms>() {
        @Override
        public MySms createFromParcel(Parcel in) {
            return new MySms(in);
        }

        @Override
        public MySms[] newArray(int size) {
            return new MySms[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(number);
        parcel.writeString(total);
        parcel.writeString(wallet);
        parcel.writeString(serial);
        parcel.writeByte((byte) (send ? 1 : 0));
    }
}
