package com.e.shelter;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.e.shelter.adapers.UserListAdapter;
import com.e.shelter.utilities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowUsersActivity extends AppCompatActivity {
    private ListView userListView;
    private ArrayList<User> userArrayList = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_user_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Accounts");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        userListView = findViewById(R.id.user_list_view);
        retrieveAllAccounts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_show_users, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_users);

        SearchManager searchManager = (SearchManager) ShowUsersActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ShowUsersActivity.this.getComponentName()));
        }
        //Search action
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<User> searchArrayList = new ArrayList<>();
                for (int i = 0; i < userArrayList.size(); i++) {
                    if (userArrayList.get(i).getName().startsWith(query.toLowerCase()) || userArrayList.get(i).getEmail().startsWith((query.toLowerCase()))) {
                        searchArrayList.add(userArrayList.get(i));
                    }
                    UserListAdapter adapter = new UserListAdapter(getBaseContext(), R.layout.content_users, searchArrayList);
                    userListView.setAdapter(adapter);
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
                UserListAdapter adapter = new UserListAdapter(getBaseContext(), R.layout.content_users, userArrayList);
                userListView.setAdapter(adapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_show_all_accounts:
                showAllUsers();
                break;
            case R.id.action_show_admins:
                showAccounts("admin");
                break;
            case R.id.action_show_regular_users:
                showAccounts("user");
                break;
            case R.id.action_show_blocked_users:
                showBlockedAccounts(true);
                break;
            case R.id.action_show_unblocked_users:
                showBlockedAccounts(false);
                break;
        }
        return false;
    }

    /**
     * @return
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
     * Retrieving all users from the database
     */
    public void retrieveAllAccounts() {
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User user = documentSnapshot.toObject(User.class);
                    userArrayList.add(user);
                }
            }
        });
        FirebaseFirestore.getInstance().collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if (!user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                                    userArrayList.add(user);
                            }
                            showAllUsers();
                        } else {
                            Log.d("Show Users Class", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void showAllUsers() {
        UserListAdapter adapter = new UserListAdapter(getBaseContext(), R.layout.content_users, userArrayList);
        userListView.setAdapter(adapter);
    }

    private void showAccounts(String type) {
        ArrayList<User> accountsList = new ArrayList<>();
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getPermission().equals(type)) {
                accountsList.add(userArrayList.get(i));
            }
        }
        UserListAdapter adapter = new UserListAdapter(getBaseContext(), R.layout.content_users, accountsList);
        userListView.setAdapter(adapter);
        userListView.refreshDrawableState();
    }

    private void showBlockedAccounts(Boolean status) {
        ArrayList<User> accountsList = new ArrayList<>();
        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).getBlocked() == status) {
                accountsList.add(userArrayList.get(i));
            }
        }
        UserListAdapter adapter = new UserListAdapter(getBaseContext(), R.layout.content_users, accountsList);
        userListView.setAdapter(adapter);
    }

}
