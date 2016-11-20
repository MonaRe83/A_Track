package com.example.mona_.a_track;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Sign_UpActivity extends AppCompatActivity {
    // calling the database
    DatabaseHelper helper = new DatabaseHelper(this,null, null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
    }

    public void onSignUpClick(View v) {
    // Creating information and sending to database
        if(v.getId()==R.id.button_createprofile)
        {
            EditText name =(EditText)findViewById(R.id.eT_name);
            EditText email =(EditText)findViewById(R.id.eT_email);
            EditText username =(EditText)findViewById(R.id.et_username);
            EditText password =(EditText)findViewById(R.id.eT_pasword);
            EditText cpassword =(EditText)findViewById(R.id.eT_confirmpassword);

            String namestr = name.getText().toString();
            String emailstr = email.getText().toString();
            String usernamestr = username.getText().toString();
            String passwordstr = password.getText().toString();
            String cpasswordstr = cpassword.getText().toString();
            // If any fields are empty show toast message
            if (namestr.isEmpty()){

                Toast pass = Toast.makeText(Sign_UpActivity.this," enter a name",Toast.LENGTH_SHORT);
                pass.show();
            }

            if (emailstr.isEmpty()){

                Toast pass = Toast.makeText(Sign_UpActivity.this," enter a email",Toast.LENGTH_SHORT);
                pass.show();
            }

            if (usernamestr.isEmpty()){

                Toast pass = Toast.makeText(Sign_UpActivity.this," enter a username",Toast.LENGTH_SHORT);
                pass.show();
            }
            // If passwords do match can not complete sign up
            if (!passwordstr.equals(cpasswordstr)){

                Toast pass = Toast.makeText(Sign_UpActivity.this,"Passwords dont match",Toast.LENGTH_SHORT);
                pass.show();
            }
            else {

                // Using class user info
                UserInfo ui = new UserInfo();
                ui.setName(namestr);
                ui.setEmail(emailstr);
                ui.setUsername(usernamestr);
                ui.setPassword(passwordstr);

                helper.insertUserInfo(ui);
                Intent i = new Intent(Sign_UpActivity.this,MainActivity.class);
                startActivity(i);
            }


        }
    }
}
