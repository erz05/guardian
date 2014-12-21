package com.erz.weathermap;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.RejectedExecutionException;

/**
 * Created by edgarramirez on 12/18/14.
 */
public abstract class BackgroundTask<ParamType, ResultType> extends AsyncTask<ParamType, BackgroundTask.BackgroundStatus, ResultType> {

    private final int ERROR_TYPE = -111;
    private final int PROGRESS_TYPE = 222;
    private OnBackgroundTaskListener<ResultType> listener;

    public BackgroundTask(OnBackgroundTaskListener<ResultType> listener){
        this.listener = listener;
    }

    protected abstract ResultType doExecute(ParamType... params);

    @Override
    protected final ResultType doInBackground(ParamType... params) {
        return doExecute(params);
    }

    public void run(ParamType... params){
        try{
            if (Build.VERSION.SDK_INT >= 11){
                this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            }else{
                this.execute(params);
            }
        }catch(RejectedExecutionException e){
            listener.onError("Rejected Execution");
        }
    }

    @Override
    protected final void onPreExecute(){
        if(listener != null)
            listener.onBegin();
    }

    @Override
    protected final void onPostExecute(ResultType result) {
        if(listener != null)
            listener.onComplete(result);
        listener = null;
    }

    @Override
    protected final void onProgressUpdate(BackgroundStatus... values){
        if(listener != null) {
            for (BackgroundStatus value : values) {
                if(value.type == ERROR_TYPE){
                    listener.onError(value.message);
                }else {
                    listener.onProgress(value.message);
                }
            }
        }
    }

    public void error(String message){
        publishProgress(new BackgroundStatus(message, ERROR_TYPE));
    }

    public void progress(String message){
        publishProgress(new BackgroundStatus(message, PROGRESS_TYPE));
    }

    public static final class BackgroundStatus {
        public String message;
        public int type;

        public BackgroundStatus(String message, int type){
            this.message = message;
            this.type = type;
        }
    }
}

