package com.e.shelter.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.e.shelter.R;
import com.e.shelter.validation.EmailValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private MaterialButton sendEmailMaterialButton;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.reset_password_text_input);
        sendEmailMaterialButton = findViewById(R.id.send_reset_password_email_button);

        sendEmailMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfResetPasswordIsPossible();
            }
        });

    }

    public void checkIfResetPasswordIsPossible() {
        final String email = emailEditText.getText().toString().toLowerCase();
        if (EmailValidator.isValidEmailTextInputEditText(email, emailEditText)) {
            FirebaseFirestore.getInstance().collection("Users").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isComplete()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    if (queryDocumentSnapshot.get("email").toString().equals(email)) {
                                        if (!queryDocumentSnapshot.getBoolean("blocked")) {
                                            resetPassword(email);
                                            return;
                                        }
                                        else {
                                            Toast.makeText(ResetPasswordActivity.this, "Reset Password Failed. Email address is blocked", Toast.LENGTH_LONG).show();
                                            break;
                                        }
                                    }
                                }
                                // email do not exist
                                Toast.makeText(ResetPasswordActivity.this, "Email address do not exist", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(ResetPasswordActivity.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Reset password link have been sent.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Error occurred. Try again", Toast.LENGTH_LONG).show();
                    Log.e("ResetPasswordActivity", "Sending reset password link failed", task.getException());
                }
            }
        });
    }
}
