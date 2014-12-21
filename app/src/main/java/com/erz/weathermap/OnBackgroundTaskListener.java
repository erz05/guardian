package com.erz.weathermap;

/**
 * Created by edgarramirez on 12/18/14.
 */
public interface OnBackgroundTaskListener<ResultType> {
    void onBegin();
    void onProgress(String message);
    void onComplete(ResultType result);
    void onError(String message);
}
