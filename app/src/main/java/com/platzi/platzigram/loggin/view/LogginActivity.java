package com.platzi.platzigram.loggin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.platzi.platzigram.R;
import com.platzi.platzigram.loggin.presenter.LogginPresenter;
import com.platzi.platzigram.loggin.presenter.LogginPresenterImpl;
import com.platzi.platzigram.view.ContainerActivity;

import java.util.Arrays;

public class LogginActivity extends AppCompatActivity implements LogginView{

    private TextInputEditText userName, password;
    private Button loggin;
    private LoginButton loginButtonFacebook;
    private ProgressBar progressBarLoggin;
    private LogginPresenter logginPresenter;

    private static final String TAG = "CreateAccountActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null){
                    Log.w(TAG, "Usuario loggeado " + firebaseUser.getEmail());
                    goHome();
                }else{
                    Log.w(TAG, "Usuario no loggeado");
                }
            }
        };

        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loggin = findViewById(R.id.login);
        loginButtonFacebook = findViewById(R.id.login_facebook);
        progressBarLoggin = findViewById(R.id.progressbarLoggin);
        hideProgressBar();

        logginPresenter = new LogginPresenterImpl(this);

        loggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Validaciones
                
                signIn(userName.getText().toString(), password.getText().toString());

            }
        });

        loginButtonFacebook.setReadPermissions(Arrays.asList("email"));
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.w(TAG, "Facebook Login Success Token: " + loginResult.getAccessToken().getApplicationId());
                signInFacebookFireBase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "Facebook Login cancelado");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG, "Facebook Login error: " + error.toString());
                error.printStackTrace();
                FirebaseCrash.report(error);
            }
        });
    }

    private void signInFacebookFireBase(AccessToken accessToken) {

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    SharedPreferences sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("email", firebaseUser.getEmail());
                    editor.commit();
                    goHome();
                    FirebaseCrash.logcat(Log.WARN, TAG,"Login Facebook exitoso");
                    Toast.makeText(LogginActivity.this, "Login Facebook exitoso", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseCrash.logcat(Log.WARN, TAG,"Login Facebook NO exitoso");
                    Toast.makeText(LogginActivity.this, "Login Facebook NO exitoso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(String userName, String password) {
        logginPresenter.signIn(userName, password, this, firebaseAuth);
    }


    @Override
    public void enableInputs() {
        userName.setEnabled(true);
        password.setEnabled(true);
        loggin.setEnabled(true);
    }

    @Override
    public void disableInputs() {
        userName.setEnabled(false);
        password.setEnabled(false);
        loggin.setEnabled(false);
    }

    @Override
    public void showProgressBar() {
        progressBarLoggin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLoggin.setVisibility(View.GONE);
    }

    @Override
    public void logginError(String error) {
        Toast.makeText(this, getString(R.string.loggin_error) + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void goHome() {
        Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
