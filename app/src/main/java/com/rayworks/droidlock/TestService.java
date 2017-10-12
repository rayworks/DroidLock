package com.rayworks.droidlock;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

public class TestService extends Service {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler();

    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d(">>> worker activated pid : %d", Process.myPid());
        showToast("Service is working now");

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Timber.d("try locking in the background...");
                try {
                    if (!FileUtils.applyFileLocking()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showToast("Unable to acquire the lock, exiting now.");
                            }
                        });
                    } else {
                        Timber.d("locking operation complete");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TestService.this.stopSelf();

            }
        };
        executorService.submit(task);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Timber.d(">>> worker destroyed");
        showToast("Service is terminated");
    }

    private void showToast(String msg) {
        Toast.makeText(this, "[TestService] " + msg, Toast.LENGTH_SHORT).show();
    }
}
