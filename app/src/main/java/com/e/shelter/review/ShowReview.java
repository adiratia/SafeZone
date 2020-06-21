package com.e.shelter.review;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.e.shelter.R;
import com.e.shelter.adapers.ReviewListAdapter;
import com.e.shelter.utilities.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class ShowReview extends AppCompatActivity {
    /**
     * class ShowReview fields
     */
    private ListView reviewListView;
    private ArrayList<Review> reviewArrayList = new ArrayList<>();
    private SearchView searchView;
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_review_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("User Reviews");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        reviewListView = findViewById(R.id.review_list_view);
        retrieveUserReviews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_show_reviews, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_reviews);

        SearchManager searchManager = (SearchManager) ShowReview.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ShowReview.this.getComponentName()));
        }
        //Search action
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Review> searchArrayList = new ArrayList<>();
                for (int i = 0; i < reviewArrayList.size(); i++) {
                    if (reviewArrayList.get(i).getShelterName().contains(query) || reviewArrayList.get(i).getUserEmail().startsWith(query)
                    || reviewArrayList.get(i).getUserName().startsWith(query)) {
                        searchArrayList.add(reviewArrayList.get(i));
                    }
                    ReviewListAdapter adapter = new ReviewListAdapter(getBaseContext(), R.layout.content_reviews, searchArrayList);
                    reviewListView.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //Close Search action
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ReviewListAdapter adapter = new ReviewListAdapter(getBaseContext(), R.layout.content_reviews, reviewArrayList);
                reviewListView.setAdapter(adapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

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
     * Getting all reviews from database
     */
    public void retrieveUserReviews() {
        FirebaseFirestore.getInstance().collection("UserReviews").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review review = document.toObject(Review.class);
                                reviewArrayList.add(review);
                            }
                        } else {
                            Log.d("Show Review Class", "Error getting documents: ", task.getException());
                        }
                        ReviewListAdapter adapter = new ReviewListAdapter(getBaseContext(), R.layout.content_reviews, reviewArrayList);
                        reviewListView.setAdapter(adapter);
                    }
                });
    }
}




