package com.sonu.admitkartsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity {

    private EditText otpverificationedittext;
    private AppCompatButton otpvericationbutton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        otpverificationedittext=(EditText)findViewById(R.id.otpverificationedittext);
        otpvericationbutton=(AppCompatButton)findViewById(R.id.otpverificationbutton);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        String PhoneNumber=getIntent().getStringExtra("PhoneNumber");
        sendVerificationcode(PhoneNumber);

        otpvericationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String code=otpverificationedittext.getText().toString().trim();

                if(code.isEmpty() || code.length()<6)
                {
                    otpverificationedittext.setError("Enter code....");
                    otpverificationedittext.requestFocus();
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);
                verificationcode(code);
            }
        });

    }

    private void verificationcode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent=new Intent(VerifyActivity.this,StartedActivity.class);
                            startActivity(intent);
                        }
                        else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast toast = Toast.makeText(VerifyActivity.this, "Verification Code is wrong", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                });
    }



    private void sendVerificationcode(String phonenumber1)
    {
        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phonenumber1,60, TimeUnit.SECONDS,this,mCallBack);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
        {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                otpverificationedittext.setText(code);
                verificationcode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e)
        {
            Toast.makeText(VerifyActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }

    };

}

