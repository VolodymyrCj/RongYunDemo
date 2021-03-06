package com.vgaw.rongyundemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.vgaw.rongyundemo.message.MatchEngine;
import com.vgaw.rongyundemo.message.MatchMessage;
import com.vgaw.rongyundemo.message.SysMsgTem;
import com.vgaw.rongyundemo.message.SystemEngine;
import com.vgaw.rongyundemo.message.SystemMessage;
import com.vgaw.rongyundemo.util.DataFactory;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.notification.PushNotificationMessage;

/**
 * Created by caojin on 15-10-21.
 */
public class App extends Application {
    public static final String KEY = "0vnjpoadnwiyz";
    public static final String SECRET = "euxn7NM4jQCTEX";
    public static final String TOKEN = "token";
    private static final String SP_NAME = "sp";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    @Override
    public void onCreate() {
        super.onCreate();
        Preference.getInstance().init(getApplicationContext());
        DataFactory.getInstance().initial(getApplicationContext());
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            // 注册自定义消息
            RongIM.registerMessageType(MatchMessage.class);
            RongIM.registerMessageType(SystemMessage.class);
            // 注册自定义消息显示模板
            //RongIM.registerMessageTemplate(new SysMsgTem());
            // 事件监听
            MatchEngine.getInstance().initial(getApplicationContext());
            SystemEngine.getInstance().initial(getApplicationContext());
            RongIM.setOnReceivePushMessageListener(pushListener);
        }

    }

    private RongIMClient.OnReceivePushMessageListener pushListener = new RongIMClient.OnReceivePushMessageListener() {
        @Override
        public boolean onReceivePushMessage(PushNotificationMessage pushNotificationMessage) {
            return false;
        }
    };

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public SharedPreferences getSp() {
        return getSharedPreferences(SP_NAME, MODE_PRIVATE);
    }
}
