package com.example.areapartments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivitty extends AppCompatActivity {


    EditText oldpass, newpass, confirmpass;
    FirebaseAuth auth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_activitty);

        oldpass = findViewById(R.id.olPassword);
        newpass = findViewById(R.id.newPassword);
        confirmpass = findViewById(R.id.confirmNewPassword);

        dialog = new ProgressDialog(this);

    }

    public void changePasswordBtn(View v){

        String _password = oldpass.getText().toString();
        String _newpassword = newpass.getText().toString();
        String _confirmpassword = confirmpass.getText().toString();

        if ((_password.isEmpty())) {
            oldpass.setError("Password is required");
            oldpass.requestFocus();
            return;
        }

        if ((_newpassword.isEmpty())) {
            newpass.setError("Password is required");
            newpass.requestFocus();
            return;
        }
        else {
            if (!_newpassword.equals(_confirmpassword)) {
                confirmpass.setError("Password did not match");
                confirmpass.requestFocus();
                return;
            }
        }

        if (_password.length()<6){
            newpass.setError("Minimum length of password should be 6");
            newpass.requestFocus();
            return;
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!= null){
            dialog.setMessage("Changing password. Please wait. ");
            dialog.show();
            user.updatePassword(newpass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Your password has been changed.",Toast.LENGTH_LONG).show();
                                finish();
                                Intent i = new Intent(ChangePasswordActivitty.this, ProfileActivity.class);
                                startActivity(i);
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Your password could not be changed.",Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }

    }
}
