package com.platzi.platzigram.loggin.interactor;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.platzi.platzigram.loggin.presenter.LogginPresenter;
import com.platzi.platzigram.loggin.repository.LogginRepository;
import com.platzi.platzigram.loggin.repository.LogginRepositoryImpl;

/**
 * Created by Santiago on 23/04/2018.
 */

public class LogginInteractorImpl implements LogginInteractor{

    private LogginPresenter logginPresenter;
    private LogginRepository logginRepository;

    public LogginInteractorImpl(LogginPresenter logginPresenter) {
        this.logginPresenter = logginPresenter;
        this.logginRepository = new LogginRepositoryImpl(logginPresenter);
    }

    @Override
    public void sigIn(String userName, String password, Activity activity, FirebaseAuth firebaseAuth) {
        logginRepository.signIn(userName, password, activity, firebaseAuth);
    }
}
