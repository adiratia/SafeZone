package com.e.shelter.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e.shelter.R;
import com.e.shelter.validation.EmailValidator;
import com.e.shelter.validation.NameValidator;
import com.e.shelter.validation.PasswordValidator;
import com.e.shelter.validation.PhoneValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class EditUserActivity extends AppCompatActivity {

    private TextInputEditText nameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;
    private MaterialButton updateMaterialButton;
    private MaterialButton updateEmailMaterialButton;
    private TextInputEditText passwordConfirmEditText;
    private String email;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        nameEditText = findViewById(R.id.nameEdit);
        emailEditText = findViewById(R.id.emailEdit);
        phoneEditText = findViewById(R.id.phoneEdit);
        InitEditText();

        //update Button
        updateMaterialButton = findViewById(R.id.updateButton);
        updateEmailMaterialButton = findViewById(R.id.updateEmailButton);
        //Click on update button
        updateMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUser();
            }
        });
        updateEmailMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });
    }


    public void InitEditText() {
        try {
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    nameEditText.setText(documentSnapshot.get("name").toString());
                                    userName = nameEditText.getText().toString();
                                    phoneEditText.setText(documentSnapshot.get("phoneNumber").toString());
                                    emailEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    email = emailEditText.getText().toString();
                                } else {
                                    Toast.makeText(EditUserActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
                                    updateMaterialButton.setClickable(false);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void EditUser() {
        if (NameValidator.isValidNameTextInputEditText(nameEditText.getText().toString(), nameEditText)
                & PhoneValidator.isValidNameTextInputEditText(phoneEditText.getText().toString(), phoneEditText)) {
            //user name update
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).update("name", nameEditText.getText().toString());
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nameEditText.getText().toString()).build();
            user.updateProfile(profileUpdates);
            //user phone number update
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).update("phoneNumber", phoneEditText.getText().toString());
            Toast.makeText(EditUserActivity.this, "Updated", Toast.LENGTH_LONG).show();

            //user name update in reviews
            FirebaseFirestore.getInstance().collection("UserReviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (queryDocumentSnapshot.getString("userName").equals(userName)) {
                                FirebaseFirestore.getInstance().collection("UserReviews").document(queryDocumentSnapshot.getId())
                                        .update("userName", nameEditText.getText().toString());
                            }
                        }
                    }
                }
            });
        }
    }

    public void updateEmail() {
        if (email.toLowerCase().equals(emailEditText.getText().toString().toLowerCase())) { // Same email (Do nothing)
        } else if (EmailValidator.isValidEmailTextInputEditText(emailEditText.getText().toString(), emailEditText)) { //different email
            // re-authenticate
            TextInputLayout emailTextInputLayout = new TextInputLayout(this, null, R.style.EditText_OutlinedBox_DayNight);
            final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(5,30); // Width , height
            emailTextInputLayout.setHint("Password");
            emailTextInputLayout.setLayoutParams(lparams);
            emailTextInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            passwordConfirmEditText = new TextInputEditText(emailTextInputLayout.getContext());
            passwordConfirmEditText.setTextColor(getColor(R.color.colorAccent));
            passwordConfirmEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            emailTextInputLayout.addView(passwordConfirmEditText);

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditUserActivity.this);
            builder.setTitle("Enter your password");
            builder.setView(emailTextInputLayout);
            builder.setMessage("Enter your password in order to change your email");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmPassword();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        }
    }

    public void confirmPassword() {
        if (PasswordValidator.isValidEmailTextInputEditText(passwordConfirmEditText.getText().toString(), passwordConfirmEditText)) {
            AuthCredential credential = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), passwordConfirmEditText.getText().toString());

            FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("EditUserActivity", "User re-authenticate.");
                        // check if email already exist in database
                        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(emailEditText.getText().toString().toLowerCase())
                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().getSignInMethods().size() >= 1) { // email exist
                                                Toast.makeText(EditUserActivity.this, "Email address is already in use", Toast.LENGTH_LONG).show();
                                                emailEditText.setError("Email address is already in use");
                                            } else {// email do not exist
                                                FirebaseAuth.getInstance().getCurrentUser().updateEmail(emailEditText.getText().toString().toLowerCase())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(EditUserActivity.this, "Email updated", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                                // update email in users
                                                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
                                                        .update("email", emailEditText.getText().toString().toLowerCase());

                                                // update email in user reviews
                                                FirebaseFirestore.getInstance().collection("UserReviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                if (queryDocumentSnapshot.getString("userEmail").equals(email)) {
                                                                    FirebaseFirestore.getInstance().collection("UserReviews").document(queryDocumentSnapshot.getId())
                                                                            .update("email", emailEditText.getText().toString().toLowerCase());
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });

                    } else {
                        Log.d("EditUserActivity", "re-authentication failed");
                        Toast.makeText(EditUserActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}