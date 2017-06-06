package com.junhee.android.loopertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int mainValue = 0;
    TextView mainText, backText;
    EditText numEdit;
    CalThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainText = (TextView) findViewById(R.id.mainvalue);
        backText = (TextView) findViewById(R.id.backvalue);
        numEdit = (EditText) findViewById(R.id.number);
        findViewById(R.id.increase).setOnClickListener(this);
        findViewById(R.id.square).setOnClickListener(this);
        findViewById(R.id.root).setOnClickListener(this);


        thread = new CalThread(handler);
        thread.setDaemon(true);
        thread.start();

    }

    Handler handler = new Handler() {
                public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    backText.setText("square result : " + msg.arg1);
                    break;

                case 1:
                    backText.setText("root result : " + ((Double) msg.obj).doubleValue());
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Message msg;
        switch (v.getId()) {
            case R.id.increase:
                mainValue++;
                mainText.setText("MainValue : " + mainValue);
                break;

            case R.id.square:
                msg = new Message();
                msg.what = 0;
                msg.arg1 = Integer.parseInt(numEdit.getText().toString());
                thread.backHandler.sendMessage(msg);
                break;

            case R.id.root:
                msg = new Message();
                msg.what = 1;
                msg.arg1 = Integer.parseInt(numEdit.getText().toString());
                thread.backHandler.sendMessage(msg);
                break;
        }
    }
}


class CalThread extends Thread {
    Handler mainHandler;
    Handler backHandler;

    CalThread(Handler handler) {
        mainHandler = handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        backHandler = new Handler() {
            public void handleMessage(Message msg) {
                Message retmsg = new Message();
                switch (msg.what) {
                    case 0:
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        retmsg.what = 0;
                        retmsg.arg1 = msg.arg1 * msg.arg1;
                        break;

                    case 1:
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        retmsg.what = 1;
                        retmsg.obj = new Double(Math.sqrt((double) msg.arg1));
                        break;
                }
                mainHandler.sendMessage(retmsg);
            }
        };
        Looper.loop();
    }
}


