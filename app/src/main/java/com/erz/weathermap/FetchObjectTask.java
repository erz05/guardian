package com.erz.weathermap;

/**
 * Created by edgarramirez on 12/21/14.
 */
public class FetchObjectTask<ItemObject> extends BackgroundTask<Object, ItemObject> {

    public FetchObjectTask(OnBackgroundTaskListener<ItemObject> listener) {
        super(listener);
    }

    @Override
    protected ItemObject doExecute(Object... params) {
        return null;
    }
}
