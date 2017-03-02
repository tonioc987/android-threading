package com.acp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.acp.aidl.IMyClient;
import com.acp.aidl.IMyService;
import com.acp.util.Log;

public class MyService extends Service {
    private volatile IMyClient mClient = null;

    private IMyService.Stub mMyService = new IMyService.Stub() {
        // Instance initializer
        {
            Log.print("Instance initializer.");
        }

        @Override
        public void setCallback(IMyClient client) throws RemoteException {
            Log.print("setCallback.");
            mClient = client;
            mClient.callback(9876);
        }
    };

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.print("onBind.");
        return mMyService;
    }
}