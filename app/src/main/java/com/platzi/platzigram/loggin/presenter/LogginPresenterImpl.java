package com.platzi.platzigram.loggin.presenter;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.platzi.platzigram.loggin.interactor.LogginInteractor;
import com.platzi.platzigram.loggin.interactor.LogginInteractorImpl;
import com.platzi.platzigram.loggin.view.LogginView;

/**
 * Created by Santiago on 23/04/2018.
 */

public class LogginPresenterImpl implements LogginPresenter{

    private LogginView logginView;
    private LogginInteractor logginInteractor;


    public LogginPresenterImpl(LogginView logginView) {
        this.logginView = logginView;
        this.logginInteractor = new LogginInteractorImpl(this);
    }

    @Override
    public void signIn(String userName, String password, Activity activity, FirebaseAuth firebaseAuth) {
        logginView.disableInputs();
        logginView.showProgressBar();
        logginInteractor.sigIn(userName, password, activity, firebaseAuth);
    }

    @Override
    public void loginSuccess() {
        logginView.goHome();
        logginView.hideProgressBar();
    }

    @Override
    public void loginError(String error) {
        logginView.enableInputs();
        logginView.hideProgressBar();
        logginView.logginError(error);
    }
}
