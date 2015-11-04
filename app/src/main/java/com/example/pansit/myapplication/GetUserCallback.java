package com.example.pansit.myapplication;

interface GetUserCallback {

    /**
     * Invoked when background task is completed
     */

    public abstract void done(DataKeeper returnedUser);
}
