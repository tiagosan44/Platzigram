package com.platzi.platzigram.loggin.interactor;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Santiago on 23/04/2018.
 */

public interface LogginInteractor {

    void sigIn(String userName, String password, Activity activity, FirebaseAuth firebaseAuth);
}
