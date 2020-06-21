package com.e.shelter;

import android.os.Bundle;

import com.e.shelter.adapers.FavoriteListAdapter;
import com.e.shelter.utilities.FavoriteCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class FavoritesActivity extends AppCompatActivity {

    private ArrayList<FavoriteCard> list = new ArrayList<>();
    private ListView shelterCardListView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setTitle("Favorites");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.fav_loading_spinner);
        progressBar.setVisibility(View.VISIBLE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        shelterCardListView = findViewById(R.id.favListView);
        createFavoriteCardList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(2);
        finish();
    }

    /**
     * Adding to favorite list of selected shelter
     */
    public void createFavoriteCardList() {
        FirebaseFirestore.getInstance().collection("FavoriteShelters").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                @SuppressWarnings("unchecked")
                                ArrayList<Map> favShelters = (ArrayList<Map>) documentSnapshot.get("favoriteShelters");
                                for (int i = 0; i < favShelters.size(); i++) {
                                    FavoriteCard favoriteCard = new FavoriteCard(favShelters.get(i).get("name").toString(), favShelters.get(i).get("address").toString(),
                                            Double.parseDouble(favShelters.get(i).get("latitude").toString()), Double.parseDouble(favShelters.get(i).get("longitude").toString()));
                                    list.add(favoriteCard);
                                }
                            }
                        }
                        Collections.reverse(list);
                        FavoriteListAdapter adapter = new FavoriteListAdapter(getBaseContext(), R.layout.content_favorites, list, getIntent().getStringExtra("userEmail"));
                        shelterCardListView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }


}
