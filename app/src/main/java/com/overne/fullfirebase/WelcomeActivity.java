package com.overne.fullfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeActivity extends AppCompatActivity{
    private static final String TAG ="WelcomeActivity" ;
    //TODO 8. iniciando Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    //TODO 9. iniciando vistas
    private TextView tvUserDetail;
    private Button signOut;
    private CircleImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //TODO 10. instanciando
        tvUserDetail=(TextView)findViewById(R.id.tvUserDetail);
        signOut=(Button)findViewById(R.id.signOut);
        ivPhoto=(CircleImageView) findViewById(R.id.ivPhoto);
        //TODO 11. llamando al metodo inicialize y inicializeGoogleAccount()
        inicialize();
        //TODO 12. cerrar sesion
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }
    //TODO 11.1. inicialize
    public void inicialize(){
        //intanciando objetos
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser !=  null){
                    tvUserDetail.setText("IDUser: " + firebaseUser.getUid()+"\n"
                            +" Email: " + firebaseUser.getEmail());
                    Picasso.with(WelcomeActivity.this).load(firebaseUser.getPhotoUrl()).into(ivPhoto);
                }else {
                    Log.w(TAG, "onAuthStateChanged - signed_out");
                }
            }
        };
        //nota:implementamos en la clase
    }
    //TODO 12. signOut
    private void signOut(){
        if (LoginManager.getInstance() != null){
            Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            LoginManager.getInstance().logOut();
        }
    }
    //implementado
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
