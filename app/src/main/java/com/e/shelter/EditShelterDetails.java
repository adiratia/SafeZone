package com.e.shelter;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e.shelter.validation.TextInputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EditShelterDetails extends AppCompatActivity {
    public TextInputEditText nameEditText;
    public TextInputEditText statusEditText;
    public TextInputEditText addressEditText;
    public TextInputEditText capacityEditText;
    public MaterialButton update;
    public String oldShelterName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_shelter_details);

        nameEditText = findViewById(R.id.shelter_name_text_input_edit_shelter);
        statusEditText = findViewById(R.id.shelter_status_text_input_edit_shelter);
        addressEditText = findViewById(R.id.shelter_address_text_input_edit_shelter);
        capacityEditText = findViewById(R.id.shelter_capacity_text_input_edit_shelter);
        nameEditText.setText(getIntent().getStringExtra("name"));
        addressEditText.setText(getIntent().getStringExtra("address"));
        capacityEditText.setText(getIntent().getStringExtra("capacity"));
        statusEditText.setText(getIntent().getStringExtra("status"));
        oldShelterName = getIntent().getStringExtra("name");

        update= findViewById(R.id.updateShelterInfoButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_details();
            }
        });
    }

    /**
     * updating details of shelter after the admin inputs new details and is updating to the database
     */
    public void update_details() {
        if (TextInputValidator.isValidEditText(nameEditText.getText().toString(), nameEditText)
                & TextInputValidator.isValidEditText(addressEditText.getText().toString(), addressEditText)
                & TextInputValidator.isValidEditText(capacityEditText.getText().toString(), capacityEditText)
                & TextInputValidator.isValidEditText(statusEditText.getText().toString(), statusEditText)) {

            FirebaseFirestore.getInstance().collection("Shelters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.get("name").toString().equals(nameEditText.getText().toString()) && !oldShelterName.equals(nameEditText.getText().toString())) {
                                nameEditText.setError("Shelter name already exist");
                                return;
                            }
                        }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.get("name").toString().equals(oldShelterName)) {
                                Map<Object, String> map = new HashMap<>();
                                map.put("name", nameEditText.getText().toString());
                                map.put("address", addressEditText.getText().toString());
                                map.put("status", statusEditText.getText().toString());
                                map.put("capacity", capacityEditText.getText().toString());
                                FirebaseFirestore.getInstance().collection("Shelters").document(document.getId()).set(map, SetOptions.merge());
                                setResult(3);
                                finish();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
