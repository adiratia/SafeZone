package com.e.shelter.contactus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.e.shelter.R;
import com.e.shelter.adapers.ContactListAdapter;
import com.e.shelter.utilities.Contact;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContactPage extends AppCompatActivity {
    /**
     *
     * class ContactPage fields
     */
    private ListView contactsListView;
    private ArrayList<Contact> contactsArrayList = new ArrayList<>();
    private ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_page);

        retrieveContacts();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Contacts");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        contactsListView = findViewById(R.id.contacts_list_view);

        FloatingActionButton addButton = findViewById(R.id.add_contacts_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactPage.this, AddNewContactActivity.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    /**
     *
     * @return boolean
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Log.i("TAG", "From Add/Edit Contact Screen");
            retrieveContacts();
        }
    }

    /**
     * get contacts from database
     */
    public void retrieveContacts() {
        contactsArrayList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("ContactUsInformation").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contact contact = document.toObject(Contact.class);
                                contactsArrayList.add(contact);
                            }
                        } else {
                            Log.d("Show Review Class", "Error getting documents: ", task.getException());
                        }
                        ContactListAdapter adapter = new ContactListAdapter(ContactPage.this, R.layout.content_contacts,
                                contactsArrayList, getIntent().getStringExtra("permission"));
                        contactsListView.setAdapter(adapter);
                    }
                });
    }

    /**
     * add contact information to database
     */
    public void addContactsToFireBase() {
        MongoClient mongoClient = new MongoClient("10.0.2.2", 27017);
        DB db = mongoClient.getDB("SafeZone_DB");
        DBCollection dbCollection = db.getCollection("contactPage");
        DBCursor cursor = dbCollection.find();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("ContactUsInformation");
        while (cursor.hasNext()) {
            BasicDBObject object = (BasicDBObject) cursor.next();
            Contact contact = new Contact(object.getString("name"), object.getString("nameInEnglish"), object.getString("phoneNumber"));
            collectionReference.document(contact.getNameInEnglish()).set(contact);
        }
    }


}

