package com.sonu.admitkartsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText phonenumber;
    private AppCompatButton sendotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phonenumber=(TextInputEditText)findViewById(R.id.phonenumber);
        sendotp=(AppCompatButton)findViewById(R.id.signotp);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String PhoneNumber=phonenumber.getText().toString().trim();

                if(PhoneNumber.isEmpty() || PhoneNumber.length()<10)
                {
                    phonenumber.setError("enter a valid mobile number");
                    phonenumber.requestFocus();
                    return;
                }
                Intent intent=new Intent(MainActivity.this,VerifyActivity.class);
                intent.putExtra("PhoneNumber",PhoneNumber);
                startActivity(intent);
            }
        });
    }


}
