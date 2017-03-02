package com.acp.testhandler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acp.util.Log;

public class MainActivity extends Activity {
    private static final int M1 = 1;
    private static final int M2 = 2;
    private static final int M3 = 3;
    private static final int IDLE = 4;
    private Button mButton;
    private LooperThread mLooperThread;
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextViewIdle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView1 = (TextView) findViewById(R.id.text_view1);
        mTextView2 = (TextView) findViewById(R.id.text_view2);
        mTextView3 = (TextView) findViewById(R.id.text_view3);
        mTextViewIdle = (TextView) findViewById(R.id.text_view_idle);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLooperThread.incrementTextView();
            }
        });
        mLooperThread = new LooperThread(mHandler);
        mLooperThread.start();
    }

    @Override
    protected void onDestroy() {
        mLooperThread.finish();
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.print("Activity.handleMessage " + String.valueOf(msg.what));
            switch (msg.what) {
                case M1:
                    mTextView1.setText(String.valueOf(msg.arg1));
                    break;
                case M2:
                    mTextView2.setText(String.valueOf(msg.arg1));
                    break;
                case M3:
                    mTextView3.setText(String.valueOf(msg.arg1));
                    break;
                case IDLE:
                    mTextViewIdle.setText(String.valueOf(msg.arg1));
                    break;
            }
        }
    };

    private static class LooperThread extends Thread
            implements MessageQueue.IdleHandler {
        private Handler mHandler1;
        private Handler mHandler2;
        private Handler mUiHandler;
        int i = 0;

        public LooperThread(Handler uiHandler) {
            mUiHandler = uiHandler;
        }

        @Override
        public void run() {
            Log.print("LooperThread.run");
            Looper.prepare();
            mHandler1 = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Log.print("LooperThread.handleMessage1");
                    Message m = mUiHandler.obtainMessage(msg.what, msg.arg1,
                            msg.arg2);
                    mUiHandler.sendMessage(m);
                }
            };
            mHandler2 = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Log.print("LooperThread.handleMessage2");
                    Message m = mUiHandler.obtainMessage(msg.what, msg.arg1 * 2,
                            msg.arg2);
                    mUiHandler.sendMessage(m);
                }
            };
            MessageQueue queue = Looper.myQueue();
            queue.addIdleHandler(this);
            Looper.loop();
        }

        @Override
        public boolean queueIdle() {
            Log.print("LooperThread.queueIdle");
            Message m = mUiHandler.obtainMessage(IDLE, ++i, 0);
            mUiHandler.sendMessage(m);
            return true;
        }

        public void incrementTextView() {
            Log.print("incrementTextView start");
            for (int i = 1; i < 5; ++i) {
                Message m1 = mHandler1.obtainMessage(M1, i, 0);
                mHandler1.sendMessageDelayed(m1, i * 1000);
                Message m2 = mHandler2.obtainMessage(M2, i, 0);
                mHandler2.sendMessageDelayed(m2, i * 1000 + 300);
                final int j = i * 3;
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Log.print("LooperThread.Runnable");
                        Message m = mUiHandler.obtainMessage(M3, j, 0);
                        mUiHandler.sendMessage(m);
                    }
                };
                mHandler1.postDelayed(r, i * 1000 + 600);
            }
            Log.print("incrementTextView end");
        }

        public void finish() {
            mHandler1.getLooper().quit();
        }
    }
}
