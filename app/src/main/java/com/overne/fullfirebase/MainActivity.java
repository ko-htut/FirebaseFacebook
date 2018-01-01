package com.overne.fullfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
    private static final int SIGN_IN_FACEBOOK_CODE = 1;
    private CallbackManager callbackManager;
    //TODO 1. iniciando Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    //TODO 2. iniciando vistas
    private LoginButton btnSignInFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO 3. instanciando
        //iniciando facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        //iniciando vistas
        btnSignInFacebook=(LoginButton)findViewById(R.id.btnSignInFacebook);
        //TODO 4 llamando al metodo inicialize y inicializeGoogleAccount()
        initialize();
        //TODO 6. utilizamos el listener
        btnSignInFacebook.setReadPermissions("email","public_profile");
        btnSignInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.w(TAG, "Facebook Login Success Token:  " +
                        loginResult.getAccessToken().getToken());
                signInFacebookFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "Facebook Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG, "Facebook Error");
                error.printStackTrace();
            }
        });
    }
    //TODO 5.1. initialize
    public void initialize(){
        //intanciando objetos
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //definiendo el objeto firebaseUser
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                //validando la sesion
                if (firebaseUser !=  null){
                    Log.w(TAG, "onAuthStateChanged - signed_in" + firebaseUser.getUid());
                    Log.w(TAG, "onAuthStateChanged - signed_in" + firebaseUser.getEmail());
                }else {
                    Log.w(TAG, "onAuthStateChanged - signed_out");
                }
            }
        };
        //nota:implementamos en la clase
    }
    //onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //validando el request code
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //TODO 7. Inicio de Sesion con Facebook
    private void signInFacebookFirebase(AccessToken accessToken){
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Facebook Authentication Success", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Facebook Authentication Unsuccess", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //TODO adicionar
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    // TODO remover
    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
