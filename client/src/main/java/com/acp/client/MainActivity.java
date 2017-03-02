package com.acp.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;

import com.acp.aidl.IMyClient;
import com.acp.aidl.IMyService;
import com.acp.util.Log;

public class MainActivity extends Activity {
    private IMyService mService;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text_view);
        Log.print("onCreate.");

        Intent i = new Intent();
        i.setClassName("com.acp.service","com.acp.service.MyService");
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder
                iBinder) {
            mService = IMyService.Stub.asInterface(iBinder);
            try {
                mService.setCallback(mClient);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.print("onServiceConnected.");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    private IMyClient.Stub mClient = new IMyClient.Stub() {
        @Override
        public void callback(int i) throws RemoteException {
            Log.print("callback." + i);
        }
    };
}
