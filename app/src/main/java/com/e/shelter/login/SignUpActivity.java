package com.e.shelter.login;


import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e.shelter.R;
import com.e.shelter.utilities.Emails;
import com.e.shelter.utilities.FavoriteCard;
import com.e.shelter.utilities.FavoriteShelter;
import com.e.shelter.utilities.User;
import com.e.shelter.validation.EmailValidator;
import com.e.shelter.validation.PasswordValidator;
import com.e.shelter.validation.TextInputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    public static String email;
    public static String password;
    public static String firstName;
    public static String lastName;
    public static String phone;
    public static String address;

    private TextInputEditText firstNameTextInputEditText;
    private TextInputEditText lastNameTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private TextInputEditText emailTextInputEditText;
    private TextInputEditText phoneTextInputEditText;
    private MaterialButton signUpMaterialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        firstNameTextInputEditText = findViewById(R.id.register_first_name);
        lastNameTextInputEditText = findViewById(R.id.register_last_name);
        passwordTextInputEditText = findViewById(R.id.register_password1);
        firstNameTextInputEditText = findViewById(R.id.register_first_name);
        emailTextInputEditText = findViewById(R.id.register_email);
        phoneTextInputEditText = findViewById(R.id.register_phone);
        signUpMaterialButton = findViewById(R.id.finalSignUpButton);
        signUpMaterialButton.setOnClickListener(this);
    }

    /**
     * Input all sign up details and adding to the database after checking is the user doesnt already exist or is blocked
     */
    public void signUp() {
        firstName = firstNameTextInputEditText.getText().toString();
        lastName = lastNameTextInputEditText.getText().toString();
        phone = phoneTextInputEditText.getText().toString();
        email = emailTextInputEditText.getText().toString();
        password = passwordTextInputEditText.getText().toString();
        if (EmailValidator.isValidEmailTextInputEditText(email, emailTextInputEditText) & PasswordValidator.isValidEmailTextInputEditText(password, passwordTextInputEditText)
                & TextInputValidator.isValidEditText(firstName, firstNameTextInputEditText) & TextInputValidator.isValidEditText(lastName, lastNameTextInputEditText)
                & TextInputValidator.isValidEditText(phone, phoneTextInputEditText)) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) { //There is no user with the same email address
                        FirebaseFirestore.getInstance().collection("Emails").add(new Emails(email,false));
                        FirebaseUser mAuthCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        User newUser = new User(firstName + " " + lastName, phone,"user", email,false);

                        final UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                                .setDisplayName(firstName + " " + lastName)
                                .build();
                        mAuthCurrentUser.updateProfile(update);
                        FirebaseFirestore.getInstance().collection("Users").document(mAuthCurrentUser.getUid()).set(newUser)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) { //No error on firebase side.
                                            updateUI();
                                            finish();
                                        }
                                            Log.d("Register", "db.collection: onComplete: ERROR!!! ");
                                    }
                                });

                        FavoriteShelter favoriteShelter = new FavoriteShelter(mAuthCurrentUser.getEmail(), new ArrayList<FavoriteCard>());
                        FirebaseFirestore.getInstance().collection("FavoriteShelters").document(mAuthCurrentUser.getUid()).set(favoriteShelter)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) { //No error on firebase side.
                                            updateUI();
                                            finish();
                                        } else
                                            Log.d("Register", "db.collection: onComplete: ERROR!!! ");
                                    }
                                });
                    } else {
                        Log.d("Register", "createUserWithEmailAndPassword: onComplete: ERROR!!! ");
                        Toast.makeText(SignUpActivity.this, "Email already exist", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void updateUI() {
        Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.finalSignUpButton) {
            signUp();
        }
    }
}
