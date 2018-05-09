package com.platzi.platzigram.loggin.presenter;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Santiago on 23/04/2018.
 */

public interface LogginPresenter {

    void signIn(String userName, String password, Activity activity, FirebaseAuth firebaseAuth);  //Interactor

    void loginSuccess();

    void loginError(String error);
}
