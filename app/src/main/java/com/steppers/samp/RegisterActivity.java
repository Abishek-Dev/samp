package com.steppers.samp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.steppers.samp.User;

import java.util.Date;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText firstname, lastname, mail, phone, rno, address, pwd, rpwd;
    Button mRegisterbtn;
    TextView mLoginPageBack;
    FirebaseAuth mAuth;
    DatabaseReference mdatabase;
    String fn,ln,Email,Password,rpassword,mobile,regno,add;
    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname = (EditText)findViewById(R.id.fn);
        lastname = (EditText)findViewById(R.id.ln);
        mail = (EditText)findViewById(R.id.em);
        phone = (EditText)findViewById(R.id.mn);
        rno = (EditText)findViewById(R.id.rno);
        address = (EditText)findViewById(R.id.add);
        pwd = (EditText)findViewById(R.id.pwd);
        rpwd = (EditText)findViewById(R.id.rpw);
        mRegisterbtn = (Button)findViewById(R.id.rger);
        //mLoginPageBack = (TextView)findViewById(R.id.buttonLogin);
        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mRegisterbtn.setOnClickListener(this);
        //mLoginPageBack.setOnClickListener(this);
        mDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    public void onClick(View v) {
        if (v==mRegisterbtn){
            UserRegister();
        }/*else if (v== mLoginPageBack){
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        }*/
    }

    private void UserRegister() {
        fn = firstname.getText().toString().trim();
        ln = lastname.getText().toString().trim();
        Email = mail.getText().toString().trim();
        Password = pwd.getText().toString().trim();
        rpassword=rpwd.getText().toString().trim();
        mobile=phone.getText().toString().trim();
        add=address.getText().toString().trim();
        regno=rno.getText().toString().trim();

        if (TextUtils.isEmpty(fn)){
            Toast.makeText(RegisterActivity.this, "Enter Your First Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(ln)){
            Toast.makeText(RegisterActivity.this, "Enter Your Last Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Email)){
            Toast.makeText(RegisterActivity.this, "Enter Your Email ID", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Password)){
            Toast.makeText(RegisterActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(rpassword)){
            Toast.makeText(RegisterActivity.this, "Kindly Re-Enter Your Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (!rpassword.equals(Password)){
            Toast.makeText(RegisterActivity.this, "Your Password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(mobile)){
            Toast.makeText(RegisterActivity.this, "Enter Your Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(add)){
            Toast.makeText(RegisterActivity.this, "Enter Your Residential Address", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(regno)){
            Toast.makeText(RegisterActivity.this, "Enter Your Register Number/ ID", Toast.LENGTH_SHORT).show();
            return;
        }else if (Password.length()<6 || rpassword.length()<6){
            Toast.makeText(RegisterActivity.this,"Passwor must be greater then 6 digit",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendEmailVerification();
                    mDialog.dismiss();
                    OnAuth(Objects.requireNonNull(task.getResult()).getUser());
                    mAuth.signOut();
                }else{
                    Toast.makeText(RegisterActivity.this,"error on creating user",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth(FirebaseUser user) {
        String uid=mdatabase.push().getKey();
        createAnewUser(uid);
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
    }


    private User BuildNewuser(){
        return new User(
                getDisplayName(),
                getDisplayLastName(),
                getUserEmail(),
                getUserPassword(),
                getUserMobile(),
                getUserAddress(),
                getUserReg(),
                new Date().getTime()
        );
    }

    private String getUserAddress() {
        return add;
    }

    private String getUserReg() {
        return regno;
    }

    private String getUserMobile() {
        return mobile;
    }

    private String getDisplayLastName() {
        return ln;
    }

    public String getDisplayName() {
        return fn;
    }

    public String getUserEmail() {
        return Email;
    }

    public String getUserPassword(){return Password;}

}