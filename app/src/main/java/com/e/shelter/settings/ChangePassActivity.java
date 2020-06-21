package com.e.shelter.settings;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e.shelter.R;
import com.e.shelter.validation.PasswordValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    private TextInputEditText oldPasswordEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputEditText confirmNewPasswordEditText;
    private MaterialButton changePasswordMaterialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        oldPasswordEditText = findViewById(R.id.oldpass);
        newPasswordEditText = findViewById(R.id.newpass);
        confirmNewPasswordEditText = findViewById(R.id.newpass2);
        changePasswordMaterialButton = findViewById(R.id.changepass_button);
        changePasswordMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePass(oldPasswordEditText.getText().toString(), newPasswordEditText.getText().toString(), confirmNewPasswordEditText.getText().toString());
            }
        });


    }

    /**
     * changing password by confirming old password to new password and re-entering
     * @param oldPass
     * @param newPass
     * @param newPass2
     */
    public void ChangePass(String oldPass, final String newPass, final String newPass2) {
        if (!newPass.equals(newPass2)) {
            newPasswordEditText.setError("Passwords do not match");
            confirmNewPasswordEditText.setError("Passwords do not match");
        }
        else if (PasswordValidator.isValidEmailTextInputEditText(newPass, newPasswordEditText) & PasswordValidator.isValidEmailTextInputEditText(newPass2, confirmNewPasswordEditText)) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);
                // Prompt the user to re-provide their sign-in credentials
                assert user != null;
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePassActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Log.d("ChangePasswordActivity", "Error password not changed");
                                        newPasswordEditText.setError("Password must contain at least 6 characters");
                                        confirmNewPasswordEditText.setError("Password must contain at least 6 characters");
                                    }
                                }
                            });
                        } else {
                            Log.d("", "Error auth failed");
                            oldPasswordEditText.setError("Incorrect password");
                        }
                    }
                });
        }

    }


}
