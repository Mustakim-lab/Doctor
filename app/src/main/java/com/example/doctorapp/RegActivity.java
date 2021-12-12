package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegActivity extends AppCompatActivity {

    private EditText usernameText,gmailText,passwordText,nameText,ageText,genderText,phoneText,addressText,infoText;
    private Button subBtn;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        nameText=findViewById(R.id.name_editText);
        ageText=findViewById(R.id.ageEditText);
        genderText=findViewById(R.id.genderEditText);
        phoneText=findViewById(R.id.phoneEditText);
        addressText=findViewById(R.id.addressEditText);
        infoText=findViewById(R.id.informationEditText);

        usernameText=findViewById(R.id.userName_ID);
        gmailText=findViewById(R.id.user_gmail_ID);
        passwordText=findViewById(R.id.user_password_ID);
        subBtn=findViewById(R.id.regSubBtn_ID);
        progressBar=findViewById(R.id.progressBar_ID);

        firebaseAuth=FirebaseAuth.getInstance();

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameText.getText().toString().trim();
                String age=ageText.getText().toString().trim();
                String gender=genderText.getText().toString();
                String phone=phoneText.getText().toString().trim();
                String address=addressText.getText().toString();
                String info=infoText.getText().toString().trim();

                String username=usernameText.getText().toString().trim();
                String gmail=gmailText.getText().toString().trim();
                String password=passwordText.getText().toString().trim();

                if (name.isEmpty()){
                    nameText.setError("Enter name!!");
                    nameText.requestFocus();
                }else if (age.isEmpty()){
                    ageText.setError("Enter age!!");
                    ageText.requestFocus();
                }else if (gender.isEmpty()){
                    genderText.setError("Enter gender!!");
                    genderText.requestFocus();
                }else if (phone.isEmpty()) {
                    phoneText.setError("Enter phone!!");
                    phoneText.requestFocus();
                }else if (address.isEmpty()) {
                    addressText.setError("Enter address!!");
                    addressText.requestFocus();
                }else if (info.isEmpty()) {
                    infoText.setError("Enter information!!");
                    infoText.requestFocus();
                }else if (username.isEmpty()){
                    usernameText.setError("Enter User Name!!");
                    usernameText.requestFocus();
                }else if (gmail.isEmpty()){
                    gmailText.setError("Enter Gmail !!");
                    gmailText.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(gmail).matches()){
                    gmailText.setError("Enter Valid gmail !!");
                    gmailText.requestFocus();
                }else if (password.isEmpty()){
                    passwordText.setError("Enter Password !!");
                    passwordText.requestFocus();
                }else if (password.length()<6){
                    passwordText.setError("Enter 6 digit password !!");
                    passwordText.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(age,gender,phone,address,username,gmail,password);
                    sendinformation(name,info);
                }
            }
        });
    }

    private void registerUser(String age, String gender, String phone, String address, String username, String gmail, String password) {
        firebaseAuth.createUserWithEmailAndPassword(gmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                    String userID=firebaseUser.getUid();
                    reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(userID);
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("age",age);
                    hashMap.put("gender",gender);
                    hashMap.put("phone",phone);
                    hashMap.put("address",address);
                    hashMap.put("id",userID);
                    hashMap.put("username",username);
                    hashMap.put("gmail",gmail);

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(RegActivity.this,HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(RegActivity.this,"Registration successfully done",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void sendinformation(String name, String info) {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
       HashMap<String,Object> hashMap=new HashMap<>();
       hashMap.put("name",name);
       hashMap.put("information",info);

       reference.child("Information").push().setValue(hashMap);
    }
}