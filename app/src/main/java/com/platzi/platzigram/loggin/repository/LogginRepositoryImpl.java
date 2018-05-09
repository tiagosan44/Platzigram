package com.platzi.platzigram.loggin.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.platzi.platzigram.loggin.presenter.LogginPresenter;

/**
 * Created by Santiago on 23/04/2018.
 */

public class LogginRepositoryImpl implements LogginRepository{

    LogginPresenter logginPresenter;

    private static final String TAG = "LogginRepositoryImpl";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public LogginRepositoryImpl(LogginPresenter logginPresenter) {
        this.logginPresenter = logginPresenter;
    }

    @Override
    public void signIn(String userName, String passwrod, final Activity activity, FirebaseAuth firebaseAuth) {
        boolean success = true;

        firebaseAuth.signInWithEmailAndPassword(userName, passwrod).
                addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            SharedPreferences sharedPreferences = activity.getSharedPreferences("USER", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("email", firebaseUser.getEmail());
                            editor.commit();
                            logginPresenter.loginSuccess();
                        }else{
                            logginPresenter.loginError("Ocurrió un error");
                        }
                    }
                });
    }
}
