package com.pingqiu.hello;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Press me: ";
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.press_me);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentService();
            }
        });

        new StartIntentServiceAsyncTask().execute((Void[]) null);
    }


    private class StartIntentServiceAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // this might take a while ...
            try {
                Thread.sleep(1000 * 30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < 3; i++) {

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startIntentService();
                    }
                });
                try {
                    Thread.sleep(1000 * 15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            Log.d(TAG, "service sent");
        }
    }

    private static class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "Message received! : " + msg.getData().getString("str"));
        }
    }

    private void startIntentService() {
        Messenger messenger = new Messenger(new IncomingHandler());
        Intent intent = new Intent("com.example.service.ExampleService");
        intent.putExtra("com.example.service.Messenger", messenger);
        intent.setPackage("com.pingqiu.recyclerview");
        startService(intent);
    }
}
