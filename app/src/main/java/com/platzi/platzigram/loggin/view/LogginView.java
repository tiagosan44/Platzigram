package com.platzi.platzigram.loggin.view;

import android.view.View;

/**
 * Created by Santiago on 23/04/2018.
 */

public interface LogginView {

    void enableInputs();

    void disableInputs();

    void showProgressBar();

    void hideProgressBar();

    void logginError(String error);

    void goCreateAccount(View view);

    void goHome();
}
