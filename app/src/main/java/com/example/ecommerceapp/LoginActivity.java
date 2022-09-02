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

public class LoginActivity extends AppCompatActivity {
    TextView tvSignUp,tvForgetPass;
    FloatingActionButton floatingActionButton;
    EditText etEmail,etPass,etName,etPhone;

    private String parentDbName="Users";

    //Email pattern validation
    final String email_Pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        floatingActionButton=findViewById(R.id.LoginNow);
        tvSignUp=findViewById(R.id.tv_signUp);
        tvForgetPass=findViewById(R.id.tv_ForgetPassword);

        etEmail=findViewById(R.id.et_Email);
        etPass=findViewById(R.id.et_Password);
        etName=findViewById(R.id.et_Name);
        etPhone=findViewById(R.id.et_PhoneNum);

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginUser();
               // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //finish();

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                //finish();

            }
        });

    }
    private void LoginUser()
    {
       // String email=etEmail.getText().toString();
        String password=etPass.getText().toString();
       // String name=etName.getText().toString();
        String phone=etPhone.getText().toString();

        if(phone.isEmpty()){
            etPhone.setError("Please Enter Correct Phone ");
        }
        else if(password.isEmpty()||password.length()<6)
        {
            etPass.setError("Please enter Your Correct  Password");
        }
        else
        {
            progressDialog.setMessage("Wait for the Login");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AllowAccessToAccount(password,phone);


        }
    }

    private void AllowAccessToAccount( final String password,final String phone) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.child(parentDbName).child(phone).exists())
               {
                   Users userdata=snapshot.child(parentDbName).child(phone).getValue(Users.class);
                   if (userdata.getPhone().equals(phone))
                   {
                       if (userdata.getPassword().equals(password))
                       {
                           Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                           progressDialog.dismiss();

                           Intent intent =new Intent(LoginActivity.this, MainActivity.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                       }
                       else{
                           Toast.makeText(LoginActivity.this,"Password is Incorrect",Toast.LENGTH_LONG).show();
                           progressDialog.dismiss();
                       }
                   }

               }
               else
               {
                   Toast.makeText(LoginActivity.this,"Account"+phone+"does not exist",Toast.LENGTH_LONG).show();
                   progressDialog.dismiss();
                   Toast.makeText(LoginActivity.this,"You need to create a new Account",Toast.LENGTH_LONG).show();
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
// if authentication......
//    private void LoginUser() {
//
//        String email=etEmail.getText().toString();
//        String password=etPass.getText().toString();
//
//        if(!email.matches(email_Pattern)){
//            etEmail.setError("Please Enter Correct Email");
//        }
//        else if(password.isEmpty()||password.length()<6)
//        {
//            etPass.setError("Please enter Your Correct  Password");
//        }
//        else {
//            progressDialog.setMessage("Wait for the Login");
//            progressDialog.setTitle("Login");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
//
//
//            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful())
//                    {
//                        progressDialog.dismiss();
//                        senUserToNewActivity();
//                        Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(LoginActivity.this,"Login Fail",Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }
//
//
//
//    private void senUserToNewActivity() {
//        Intent intent =new Intent(LoginActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }'
}