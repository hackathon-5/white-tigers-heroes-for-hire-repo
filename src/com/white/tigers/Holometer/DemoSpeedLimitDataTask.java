package com.white.tigers.Holometer;

import android.os.AsyncTask;

/**
 * Created by mremi on 8/29/15.
 */
public class DemoSpeedLimitDataTask extends AsyncTask {

    RetrieveSpeedLimitDataCallback callback;

    public DemoSpeedLimitDataTask(RetrieveSpeedLimitDataCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Thread.sleep((long)(Math.random() * 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        callback.onSpeedRetrieved();
    }

}
