package com.platzi.platzigram.loggin.repository;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Santiago on 23/04/2018.
 */

public interface LogginRepository {

    void signIn(String userName, String passwrod, Activity activity, FirebaseAuth firebaseAuth);
}
