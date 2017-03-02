package com.acp.util;

public class Log {
    private static final String TAG = "Threads";

    public static void print(String msg) {
        int id = android.os.Process.myPid();
        Thread t = Thread.currentThread();
        StringBuffer sb = new StringBuffer();
        sb.append("Process id: ").append(id);
        sb.append("; Thread id:").append(t.getId());
        sb.append("; name:").append(t.getName());
        sb.append("; ").append(msg);
        android.util.Log.d(TAG, sb.toString());
    }
}
