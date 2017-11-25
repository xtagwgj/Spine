package com.xtagwgj.base.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.xtagwgj.base.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 应用启动图标未读消息数显示
 * Created by xtagwgj on 2017/11/19.
 */

public class BadgeManager {
    private Context mContext;

    private BadgeManager(Context mContext) {
        this.mContext = mContext;
    }

    public static BadgeManager from(Context context) {
        return new BadgeManager(context);
    }

    static {
        String manufacturer = Build.MANUFACTURER;
        Log.e("BadgeManager", "手机品牌: " + manufacturer);

        if (manufacturer.toLowerCase().contains("Xiaomi")) {
            IMPL = new ImplXiaoMi();
        } else if (manufacturer.toLowerCase().contains("sony")) {
            IMPL = new ImplSony();
        } else if (manufacturer.toLowerCase().contains("samsung") ||
                manufacturer.toLowerCase().contains("lg")) {
            IMPL = new ImplSamsumg();
        } else if (manufacturer.toLowerCase().contains("htc")) {
            IMPL = new ImplHtc();
        } else if (manufacturer.toLowerCase().contains("nova")) {
            IMPL = new ImplNova();
        } else if (manufacturer.toLowerCase().contains("huawei") ||
                manufacturer.toLowerCase().contains("honor")) {
            IMPL = new ImplHuawei();
        } else if (manufacturer.toLowerCase().contains("zuk") ||
                manufacturer.toLowerCase().contains("lenovo")) {
            IMPL = new ImplZuk();
        } else if (manufacturer.toLowerCase().contains("oppo")) {
            IMPL = new ImplOppo();
        } else if (manufacturer.toLowerCase().contains("vivo")) {
            IMPL = new ImplVivo();
        } else {
            IMPL = new ImplBase();
        }
    }

    interface Impl {
        void setBadgeNumber(Context context, int count);
    }

    private static final BadgeManager.Impl IMPL;

    /**
     * 设置角标
     */
    public void setBadgeCount(int count) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }

        IMPL.setBadgeNumber(mContext, count);
    }

    static class ImplBase implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            Log.e("BadgeManager", "不支持该手机设置角标 setBadgeNumber: " + count);
        }
    }

    static class ImplXiaoMi implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("title").setContentText("text").setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder.build();

            try {

                Field field = notification.getClass().getDeclaredField("extraNotification");
                Object extraNotification = field.get(notification);
                Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
                method.invoke(extraNotification, count);

                mNotificationManager.notify(0, notification);

            } catch (Exception e) {
                e.printStackTrace();
                // miui 6之前的版本
                Intent localIntent = new Intent(
                        "android.intent.action.APPLICATION_MESSAGE_UPDATE");
                localIntent.putExtra(
                        "android.intent.extra.update_application_component_name",
                        context.getPackageName() + "/." + getLauncherClassName(context));
                localIntent.putExtra(
                        "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
                context.sendBroadcast(localIntent);
            }
        }
    }


    static class ImplSony implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            boolean isShow = true;
            if (count == 0) {
                isShow = false;
            }
            Intent localIntent = new Intent();

            localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
            //是否显示
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);
            //启动页
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);
            //数字
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));
            //包名
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());

            context.sendBroadcast(localIntent);

        }
    }

    static class ImplSamsumg implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
        }
    }

    static class ImplHtc implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            Intent intentNotification = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
            ComponentName localComponentName = new ComponentName(context.getPackageName(),
                    getLauncherClassName(context));
            intentNotification.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
            intentNotification.putExtra("com.htc.launcher.extra.COUNT", count);
            context.sendBroadcast(intentNotification);

            Intent intentShortcut = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
            intentShortcut.putExtra("packagename", context.getPackageName());
            intentShortcut.putExtra("count", count);
            context.sendBroadcast(intentShortcut);
        }
    }

    static class ImplNova implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("tag", context.getPackageName() + "/" + getLauncherClassName(context));
            contentValues.put("count", count);
            context.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
                    contentValues);
        }
    }

    static class ImplHuawei implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            Intent localIntent1 = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            localIntent1.putExtra("packageName", context.getPackageName());
            localIntent1.putExtra("className", context.getPackageName() + "/" + getLauncherClassName(context));
            localIntent1.putExtra("notificationNum", count);
            context.sendBroadcast(localIntent1);
        }
    }

    static class ImplZuk implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            Uri CONTENT_URI = Uri.parse("content://" + "com.android.badge" + "/" + "badge");
            Bundle extra = new Bundle();
            extra.putInt("app_badge_count", count);
            Bundle b = null;
            b = context.getContentResolver().call(CONTENT_URI, "setAppBadgeCount", null, extra);
        }
    }

    static class ImplOppo implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {

            try {
                Bundle bundle = new Bundle();
                bundle.putInt("app_badge_count", count);
                context.getContentResolver().call(Uri.parse("content://com.android.badge/badge"),
                        "setAppBadgeCount", null, bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    static class ImplVivo implements Impl {

        @Override
        public void setBadgeNumber(Context context, int count) {
            try {

                String str = getLauncherClassName(context);
                if (str == null || str.trim().length() == 0) {
                    return;
                }

                Intent localIntent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
                localIntent.putExtra("packageName", context.getPackageName());
                localIntent.putExtra("className", str);
                localIntent.putExtra("notificationNum", count);
                context.sendBroadcast(localIntent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }

}
