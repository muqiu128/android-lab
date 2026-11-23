package com.buddy.lab;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Locale;

/**
 * 极简应用内日志 + 崩溃落盘。
 * 手机上没有 adb 时，靠这个把现场带回电脑看。
 */
public final class Logs {

    private static final String TAG = "BuddyLab";
    private static final int MAX_LINES = 400;
    private static final Deque<String> BUF = new ArrayDeque<>();
    private static final SimpleDateFormat TS =
            new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
    private static File dir;

    private Logs() {
    }

    public static void init(Context ctx) {
        Context app = ctx.getApplicationContext();
        dir = new File(app.getExternalFilesDir(null), "logs");
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            d("CRASH thread=" + t.getName() + "\n" + Log.getStackTraceString(e));
            dump("crash-" + System.currentTimeMillis() + ".txt", report(app));
            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
                // 无所谓
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        });

        d("app started, pid=" + android.os.Process.myPid());
    }

    public static File dir() {
        return dir;
    }

    public static synchronized void d(String msg) {
        String line = TS.format(new Date()) + "  " + msg;
        BUF.addLast(line);
        while (BUF.size() > MAX_LINES) {
            BUF.removeFirst();
        }
        Log.d(TAG, msg);
    }

    public static synchronized void clear() {
        BUF.clear();
    }

    public static synchronized String text() {
        StringBuilder sb = new StringBuilder();
        for (String s : BUF) {
            sb.append(s).append('\n');
        }
        return sb.toString();
    }

    public static String deviceInfo(Context ctx) {
        String version = "?";
        int code = 0;
        try {
            PackageInfo pi = ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            version = pi.versionName;
            code = pi.versionCode;
        } catch (Exception ignored) {
        }

        return "brand     : " + Build.BRAND + "\n"
                + "model     : " + Build.MODEL + "\n"
                + "device    : " + Build.DEVICE + " / " + Build.PRODUCT + "\n"
                + "android   : " + Build.VERSION.RELEASE + " (sdk " + Build.VERSION.SDK_INT + ")\n"
                + "abi       : " + join(Build.SUPPORTED_ABIS) + "\n"
                + "screen    : " + ctx.getResources().getDisplayMetrics().widthPixels
                + " x " + ctx.getResources().getDisplayMetrics().heightPixels
                + " @" + ctx.getResources().getDisplayMetrics().densityDpi + "dpi\n"
                + "app       : " + ctx.getPackageName() + " " + version + " (" + code + ")\n"
                + "watch     : " + ctx.getPackageManager()
                .hasSystemFeature("android.hardware.type.watch") + "\n"
                + "log dir   : " + (dir == null ? "-" : dir.getAbsolutePath()) + "\n";
    }

    public static String report(Context ctx) {
        return "==== device ====\n" + deviceInfo(ctx)
                + "\n==== log ====\n" + text();
    }

    public static String dump(String name, String content) {
        if (dir == null) {
            return null;
        }
        File f = new File(dir, name);
        try (FileWriter w = new FileWriter(f, false)) {
            w.write(content);
            return f.getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, "dump failed: " + e);
            return null;
        }
    }

    private static String join(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }
}
