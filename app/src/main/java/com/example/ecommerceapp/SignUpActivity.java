package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    TextView tvSignIn;
    FloatingActionButton floatingActionButton;
    EditText etName,etPhone,etEmail,etPass;

    //Email pattern validation
    final String email_Pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private String parentDbName="Users";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        floatingActionButton=findViewById(R.id.RegisterNow);
        tvSignIn=findViewById(R.id.tv_signIn);
        etName=findViewById(R.id.et_Name);
        etPhone=findViewById(R.id.et_PhoneNum);
        etEmail=findViewById(R.id.et_Email);
        etPass=findViewById(R.id.et_Password);

        progressDialog=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                //finish();

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name=etName.getText().toString();
        String email=etEmail.getText().toString();
        String password=etPass.getText().toString();
        String phone=etPhone.getText().toString();

        if(!email.matches(email_Pattern)){
            etEmail.setError("Please Enter Correct Email");
        }
        else if(name.isEmpty())
        {
            etName.setError("Please Enter Your Name");
        }
        else if(password.isEmpty()||password.length()<6)
        {
            etPass.setError("Please enter Your Password");
        }
        else if(phone.isEmpty()||phone.length()<11)
        {
            etPhone.setError("Phone is not correct");
        }
        else{
            progressDialog.setMessage("Wait for the Registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            ValidatePhoneNumber(name,phone,password,email);
        }
    }
    private void ValidatePhoneNumber(String name, String phone, String password, String email) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userMap=new HashMap<>();
                    userMap.put("phone",phone);
                    userMap.put("name",name);
                    userMap.put("password",password);
                    userMap.put("email",email);

                    RootRef.child("Users").child(phone).updateChildren(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpActivity.this,"Congratulation! Your Account has been created Successfully",Toast.LENGTH_LONG).show();

                                        Intent intent =new Intent(SignUpActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this,"Network Error",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"This"+phone+"already exist",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,"Please try again with another Phone",Toast.LENGTH_LONG).show();
//                    Intent intent =new Intent(SignUpActivity.this, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // if we want to display user in authentication
//    private void createAccount()
//    {
//        String name=etName.getText().toString();
//        String email=etEmail.getText().toString();
//        String password=etPass.getText().toString();
//        String phone=etPhone.getText().toString();
//
//        if(!email.matches(email_Pattern)){
//            etEmail.setError("Please Enter Correct Email");
//        }
//        else if(name.isEmpty())
//        {
//            etName.setError("Please Enter Your Name");
//        }
//        else if(password.isEmpty()||password.length()<6)
//        {
//            etPass.setError("Please enter Your Password");
//        }
//        else if(phone.isEmpty()||phone.length()<11)
//        {
//            etPhone.setError("Phone does not Exist");
//        }
//        else
//        {
//            progressDialog.setMessage("Wait for the Registration");
//            progressDialog.setTitle("Registration");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
//
//            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful())
//                    {
//                        progressDialog.dismiss();
//                        sendUserToNextActivity();
//                        Toast.makeText(SignUpActivity.this,"Registration Successfull",Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(SignUpActivity.this,"Network Error",Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }
//
//    private void sendUserToNextActivity() {
//        Intent intent =new Intent(SignUpActivity.this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//
//    }
}