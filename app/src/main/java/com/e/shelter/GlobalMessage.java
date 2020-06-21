package com.e.shelter;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GlobalMessage extends AppCompatActivity {
    Button sendBtn;
    EditText txtMessage;
    EditText subjectTxt;
    String message;
    String subject;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_message);
        sendBtn = (Button) findViewById(R.id.sendSmsButton);
        subjectTxt =(EditText) findViewById(R.id.subject_text);
        txtMessage = (EditText) findViewById(R.id.messageText);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    sendEmail();
                } catch (FirebaseAuthException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * After entering collective notification from admin input sent to all user emails from database
     * @throws FirebaseAuthException
     */
    protected void sendEmail() throws FirebaseAuthException {


        subject =subjectTxt.getText().toString();
        message =txtMessage.getText().toString();
        Log.i("Send email", "");
        int i = 0;

        FirebaseFirestore.getInstance().collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                StringBuilder sb = new StringBuilder();
                if (task.isComplete()){
                    for (QueryDocumentSnapshot email : task.getResult()){
                        sb.append(email.get("email").toString());
                        sb.append(",");


                    }
                    String res = sb.toString();
                    String[] TO = res.split(",");
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT,message);
                    emailIntent.setType("message/rfc882");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("Finished sending email...", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(GlobalMessage.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }


}
