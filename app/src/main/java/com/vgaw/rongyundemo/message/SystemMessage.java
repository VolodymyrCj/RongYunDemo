package com.vgaw.rongyundemo.message;

import android.os.Parcel;

import com.sea_monster.common.ParcelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;

/**
 * Created by caojin on 2016/2/29.
 */
@MessageTag(value = "app:system", flag = MessageTag.NONE)
public class SystemMessage extends BaseMessage {
    public static final int INVITE = 7;
    public static final int AGREE = 8;
    public static final int REFUSE = 9;
    public static final int IGNORE = 10;

    private int code;
    private String name;
    private String message;

    public SystemMessage(int code){
        super();
        this.code = code;
    }

    public SystemMessage(int code, String name){
        super();
        this.code = code;
        this.name = name;
    }

    public SystemMessage(int code, String name, String message){
        super();
        this.code = code;
        this.name = name;
        this.message = message;
    }

    public int getCode(){
        return this.code;
    }

    public String getName(){
        return this.name;
    }

    public String getMessage(){
        return this.message;
    }

    public SystemMessage(byte[] data){
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("code")){
                this.code = jsonObj.optInt("code");
            }
            if (jsonObj.has("name")){
                this.name = jsonObj.optString("name");
            }
            if (jsonObj.has("message")){
                this.message = jsonObj.optString("message");
            }

        } catch (JSONException e) {
        }
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", this.code);
            jsonObject.put("name", this.name);
            jsonObject.put("message", this.message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SystemMessage(Parcel in){
        this.code = ParcelUtils.readIntFromParcel(in);
        this.name = ParcelUtils.readFromParcel(in);
        this.message = ParcelUtils.readFromParcel(in);
    }

    public static final Creator<SystemMessage> CREATOR = new Creator<SystemMessage>() {
        @Override
        public SystemMessage createFromParcel(Parcel source) {
            return new SystemMessage(source);
        }

        @Override
        public SystemMessage[] newArray(int size) {
            return new SystemMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.code);
        ParcelUtils.writeToParcel(dest, this.name);
        ParcelUtils.writeToParcel(dest, this.message);
    }
}
