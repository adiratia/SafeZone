package com.e.shelter.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.e.shelter.NewsActivity;
import com.e.shelter.ShowUsersActivity;
import com.e.shelter.TimerActivity;
import com.e.shelter.contactus.ContactPage;
import com.e.shelter.EditShelterDetails;
import com.e.shelter.FavoritesActivity;
import com.e.shelter.GlobalMessage;
import com.e.shelter.login.LoginActivity;
import com.e.shelter.R;
import com.e.shelter.search.AddressSuggestion;
import com.e.shelter.search.SearchHelper;
import com.e.shelter.settings.SettingsActivity;
import com.e.shelter.review.ShowReview;
import com.e.shelter.utilities.AddressWrapper;
import com.e.shelter.utilities.FavoriteCard;
import com.e.shelter.utilities.Review;
import com.e.shelter.utilities.Shelter;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, RatingDialogListener {

    private GoogleMap googleMap;
    private FloatingSearchView searchBar;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private LocationCallback locationCallback;
    private View mapView;
    private final float defaultZoom = 18;
    private Marker searchLocationMarker;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private TextView infoTitle;
    private TextView infoSnippet;
    private TextView statusTxt;
    private TextView capacityTxt;
    private TextView ratingTxt;
    private TextView ratingCountTxt;
    private String userEmail;
    private String userFullName;
    private String uid;
    private String permission = "user";
    private MaterialButton editShelterButton;
    private MaterialButton saveShelterButton;
    private Marker selectedMarker;
    private List<String> favoriteShelters;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private HashMap<String, Shelter> sheltersList;
    private Shelter selectedShelter;
    private String selectedShelterUID;
    private AppRatingDialog appRatingDialog;
    private AppCompatRatingBar ratingBarInfoDialog;
    TextView header_name;
    TextView header_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Init settings
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userFullName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        initializeUserPermission();

        //Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapViewActivity.this);

        //Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //Navigation drawer
        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Search bar
        searchBar = findViewById(R.id.searchBar);
        final SearchHelper searchHelper = new SearchHelper(MapViewActivity.this);
        searchBar.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                toggle.syncState();
                drawerLayout.openDrawer(GravityCompat.START);
                searchBar.closeMenu(true);
            }

            @Override
            public void onMenuClosed() {

            }
        });
        searchBar.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                if (searchLocationMarker != null) searchLocationMarker.remove();
            }
        });
        searchBar.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchBar.clearSuggestions();
                } else {
                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    searchBar.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    searchHelper.findSuggestions(MapViewActivity.this, newQuery, 5,
                            250, new SearchHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<AddressSuggestion> results) {
                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    searchBar.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    searchBar.hideProgress();

                                }
                            });

                }
            }
        });
        searchBar.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                searchBar.clearSuggestions();
                searchBar.setSearchText(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                if (searchLocationMarker != null) searchLocationMarker.remove();
                searchBar.showProgress();
                searchBar.clearSuggestions();
                hideSoftKeyboard();
                Log.i("Search Suggestion Click", currentQuery);
                searchHelper.findAddresses(MapViewActivity.this, currentQuery, 1, new SearchHelper.OnFindAddressListener() {
                    @Override
                    public void onResults(List<AddressWrapper> results) {
                        //this will swap the data and
                        //render the collapse/expand animations as necessary
                        LatLng latLng = new LatLng(Double.parseDouble(results.get(0).getLat()), Double.parseDouble(results.get(0).getLon()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
                        searchLocationMarker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(results.get(0).getAddressInHebrew())
                                .snippet(results.get(0).getAddressInEnglish())
                                .icon(BitmapDescriptorFactory.defaultMarker(150)));
                        searchBar.closeMenu(true);
                        searchBar.hideProgress();
                    }
                });
            }
        });

        //Header
        View header = navigationView.getHeaderView(0);
        header_email = header.findViewById(R.id.email_header);
        header_name = header.findViewById(R.id.name_header);
        if (userEmail != null) header_email.setText(userEmail);
        if (userFullName != null) header_name.setText(userFullName);

        //Night Mode Switch Initialization
        navigationView.getMenu().findItem(R.id.nav_night_mode_switch).setActionView(new SwitchCompat(this));
        if ((MapViewActivity.this.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_night_mode_switch).getActionView()).setChecked(true);
        } else {
            ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_night_mode_switch).getActionView()).setChecked(false);
        }

        //Bottom Information Window
        createBottomSheetDialog();

        //Rating Dialog Window
        createRatingDialog();
    }

    /**
     * Creates all the necessary settings for the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Google map current location button change
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (this.mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            //My Location Position
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 325);

            //Toolbar position
            View toolbar = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("4"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.addRule(RelativeLayout.ALIGN_RIGHT, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 0, 970);
        }

        // Check if GPS is enabled or not. If GPS is disabled request user to enable it.
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapViewActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(MapViewActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            }
        });
        task.addOnFailureListener(MapViewActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(MapViewActivity.this, 50);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Google map settings
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.2530, 34.7915), 12));
        if ((MapViewActivity.this.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            this.googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getBaseContext(), R.raw.night_map));
        } else {
            this.googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getBaseContext(), R.raw.day_map));
        }
        //Initialize night mode switch function
        initNightModeSwitch();

        // Navigation toolbar
        this.googleMap.getUiSettings().setMapToolbarEnabled(true);
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);

        // Add shelters to google map
        addSheltersIntoGoogleMap();

        // Get favorite shelters from DB
        retrieveFavoriteShelters();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50 && resultCode == RESULT_OK) {
            getDeviceLocation();
        }
        if (requestCode == 2) {
            retrieveFavoriteShelters();
            if (selectedMarker != null) {
                if (checkIfShelterInFavorite(selectedMarker.getTitle())) {
                    saveShelterButton.setText("SAVED");
                    saveShelterButton.setIconResource(R.drawable.savedbookmark_icon_white);
                } else {
                    saveShelterButton.setText("SAVE  ");
                    saveShelterButton.setIconResource(R.drawable.savebookmark_icon_white);
                }
            }
        }
        if (requestCode == 3) {
            Log.i("MapViewActivity", "From edit screen - finished");
            updateSelectedShelter();
        }
        if (requestCode == 4) {
            Log.i("MapViewActivity", "From settings screen - finished");
            header_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            header_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        selectedMarker = null;
        selectedShelter = null;
        selectedShelterUID = null;
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //if selected marker is a search location marker - do nothing
        if (searchLocationMarker != null && searchLocationMarker.getTitle().equals(marker.getTitle()))
            return false;

        //opens bottom dialog sheet when shelter marker is selected
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        selectedMarker = marker;

        //finds selected shelter & shelter uid from shelter list
        for (Map.Entry<String, Shelter> shelter : sheltersList.entrySet()) {
            if (shelter != null && shelter.getValue().getName().equals(selectedMarker.getTitle())) {
                selectedShelter = shelter.getValue();
                selectedShelterUID = shelter.getKey();
                break;
            }
        }
        //dialog init
        infoTitle.setText(selectedMarker.getTitle());
        infoSnippet.setText(selectedMarker.getSnippet());
        ratingCountTxt.setText(("(" + selectedShelter.getRateCount() + ")"));
        ratingTxt.setText(String.valueOf(Double.parseDouble(selectedShelter.getRating())));
        ratingBarInfoDialog.setRating(Float.parseFloat(selectedShelter.getRating()));
        capacityTxt.setText(selectedShelter.getCapacity());
        if (selectedShelter.getStatus().equals("open")) {
            statusTxt.setTextColor(getResources().getColor(R.color.quantum_googgreen));
            googleMap.getUiSettings().setMapToolbarEnabled(true);
        } else {
            statusTxt.setTextColor(getResources().getColor(R.color.quantum_googred));
            googleMap.getUiSettings().setMapToolbarEnabled(false);
        }
        statusTxt.setText(selectedShelter.getStatus());
        if (checkIfShelterInFavorite(marker.getTitle())) {
            saveShelterButton.setText("SAVED");
            saveShelterButton.setIconResource(R.drawable.savedbookmark_icon_white);
        } else {
            saveShelterButton.setText("SAVE  ");
            saveShelterButton.setIconResource(R.drawable.savebookmark_icon_white);
        }
        return false;
    }

    public void createBottomSheetDialog() {
        //init
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        saveShelterButton = findViewById(R.id.info_window_save_button);
        editShelterButton = findViewById(R.id.info_window_edit_button);
        MaterialButton rateShelterButton = findViewById(R.id.info_window_rate_button);
        infoTitle = findViewById(R.id.info_window_title);
        infoSnippet = findViewById(R.id.info_window_address);
        ratingTxt = findViewById(R.id.info_window_rating);
        ratingCountTxt = findViewById(R.id.info_window_rating_count);
        capacityTxt = findViewById(R.id.info_window_capacity);
        statusTxt = findViewById(R.id.info_window_status);
        ratingBarInfoDialog = findViewById(R.id.rating_bar_info_window);
        MaterialButton shareShelterButton = findViewById(R.id.info_window_share_button);

        //Save Button Function
        saveShelterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveShelterButton.getText().equals("SAVE  ")) {
                    addSelectedShelterToFavorites();
                    saveShelterButton.setText("SAVED");
                    saveShelterButton.setIconResource(R.drawable.savedbookmark_icon_white);

                } else {
                    removeSelectedShelterFromFavorites();
                    saveShelterButton.setText("SAVE  ");
                    saveShelterButton.setIconResource(R.drawable.savebookmark_icon_white);
                }
            }
        });

        //Edit Button Function
        editShelterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapViewActivity.this, EditShelterDetails.class);
                i.putExtra("name", selectedMarker.getTitle());
                i.putExtra("address", selectedMarker.getSnippet());
                i.putExtra("status", selectedShelter.getStatus());
                i.putExtra("capacity", selectedShelter.getCapacity());
                startActivityForResult(i, 3);
            }
        });

        //Rate Button Function
        rateShelterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appRatingDialog.show();
            }
        });

        //Share Button Function
        shareShelterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = "http://maps.google.com/maps?saddr=" + selectedMarker.getPosition().latitude + "," + selectedMarker.getPosition().longitude;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Shelter name: " + selectedMarker.getTitle() + "\n"
                        + "Address: " + selectedMarker.getSnippet());
                shareIntent.putExtra(Intent.EXTRA_TITLE, location);
                startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        });

        //bottom sheet onSlide animation
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bottomSheet.setAlpha(1 + slideOffset);
            }
        });
    }

    public void createRatingDialog() {
        appRatingDialog = new AppRatingDialog.Builder()
                .setPositiveButtonText("Send Feedback")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite OK", "Very Good", "Excellent"))
                .setDefaultRating(5)
                .setTitle("Rate this shelter")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setStarColor(R.color.quantum_googblue)
                .setNoteDescriptionTextColor(R.color.quantum_googblue)
                .setTitleTextColor(R.color.quantum_black_100)
                .setDescriptionTextColor(R.color.quantum_grey)
                .setHint("|")
                .setHintTextColor(R.color.quantum_googblue)
                .setCommentTextColor(R.color.quantum_googblue)
                .setCommentBackgroundColor(R.color.quantum_bluegrey50)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .create(MapViewActivity.this);
    }

    public boolean checkIfShelterInFavorite(String shelterName) {
        return favoriteShelters.contains(shelterName);
    }

    public void retrieveFavoriteShelters() {
        favoriteShelters = new ArrayList<>();
        database.collection("FavoriteShelters").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                @SuppressWarnings("unchecked")
                                ArrayList<Map> favShelters = (ArrayList<Map>) documentSnapshot.get("favoriteShelters");
                                for (int i = 0; i < favShelters.size(); i++) {
                                    favoriteShelters.add(favShelters.get(i).get("name").toString());
                                }
                            }
                        }
                    }
                });

    }

    /**
     * Finds device location, if it fails the function retrieves the last known location.
     */
    public void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    lastKnownLocation = task.getResult();
                    if (lastKnownLocation != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), defaultZoom));
                    } else {
                        final LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if (locationResult == null) {
                                    return;
                                }
                                lastKnownLocation = locationResult.getLastLocation();
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), defaultZoom));
                            }
                        };
                        if (ActivityCompat.checkSelfPermission(MapViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(MapViewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                } else {
                    Toast.makeText(MapViewActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Adding the shelters location from Firebase into the map.
     */
    public void addSheltersIntoGoogleMap() {
        sheltersList = new HashMap<>();
        database.collection("Shelters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Shelter shelter = document.toObject(Shelter.class);
                        sheltersList.put(document.getId(), shelter);
                        LatLng latLng = new LatLng(Double.parseDouble(shelter.getLat()), Double.parseDouble(shelter.getLon()));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng).snippet(shelter.getAddress()).title(shelter.getName());
                        googleMap.addMarker(markerOptions);
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * This function loads the json file from asset folder into a string.
     */
    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Starts specific function/intent from the navigation bar based on the item selected.
     *
     * @param item - selected item from side navigation bar.
     * @return true to keep item selected, false otherwise.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_info:
                Intent contactsIntent = new Intent(this, ContactPage.class);
                contactsIntent.putExtra("permission", permission);
                startActivity(contactsIntent);
                break;
            case R.id.nav_settings:
                Intent settingsActive = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsActive, 4);
                break;
            case R.id.nav_news:
                Intent newsIntent = new Intent(this, NewsActivity.class);
                startActivity(newsIntent);
                return false;
            case R.id.nav_night_mode_switch:
                nightModeSwitch();
                break;
            case R.id.nav_show_user:
                Intent userActive = new Intent(this, ShowUsersActivity.class);
                startActivity(userActive);
                break;
            case R.id.nav_show_reviews:
                Intent reviewActive = new Intent(this, ShowReview.class);
                startActivity(reviewActive);
                return false;
            case R.id.nav_timer:
                Intent timerActive = new Intent(this, TimerActivity.class);
                startActivity(timerActive);
                return false;
            case R.id.nav_global_message:
                Intent globalMessageActive = new Intent(this, GlobalMessage.class);
                //startActivity(globalMessageActive);
                startActivityForResult(globalMessageActive, 2);
                return false;
            case R.id.nav_favorite_shelters:
                Intent favIntent = new Intent(this, FavoritesActivity.class);
                favIntent.putExtra("uid", uid);
                startActivityForResult(favIntent, 2);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MapViewActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }

    /**
     * Back button press functionality
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Switches the application theme to [night mode/day mode].
     */
    public void nightModeSwitch() {
        if (((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_night_mode_switch).getActionView()).isChecked()) {
            ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_night_mode_switch).getActionView()).setChecked(false);
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getBaseContext(), R.raw.day_map));

        } else {
            ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_night_mode_switch).getActionView()).setChecked(true);
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getBaseContext(), R.raw.night_map));
        }
    }

    public void initNightModeSwitch() {
        ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_night_mode_switch).getActionView()).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getBaseContext(), R.raw.night_map));

                        } else
                            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getBaseContext(), R.raw.day_map));

                    }
                });
    }

    /**
     * Adds the shelters from mongoDB to firebase DB.
     * TODO: Use only once.
     */
    public void addSheltersToFireBaseDataBase() {
        MongoClient mongoClient = new MongoClient("10.0.2.2", 27017);
        DB db = mongoClient.getDB("SafeZone_DB");
        DBCollection dbCollection = db.getCollection("Shelters");
        DBCursor cursor = dbCollection.find();
        database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection("Shelters");
        while (cursor.hasNext()) {
            BasicDBObject object = (BasicDBObject) cursor.next();
            Shelter shelter = new Shelter(object.getString("name"),
                    object.getString("address"),
                    object.getString("lat"),
                    object.getString("lon"),
                    object.getString("status"),
                    ThreadLocalRandom.current().nextInt(50, 76) + " square meter",
                    "0",
                    "0");
            collectionReference.add(shelter);
        }
    }


    /**
     * Find shelters addresses.
     * @param latitude  - shelter latitude
     * @param longitude - shelter longitude
     * @return address
     * @throws IOException when gpc or internet is offline
     */
    public String findSheltersAddresses(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MapViewActivity.this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        return address;
    }

    /**
     * Adds selected shelter to favorite array list & database.
     * This function only used from bottom information dialog & only when the save button text equals to 'SAVE'.
     */
    public void addSelectedShelterToFavorites() {
        FavoriteCard favoriteCard = new FavoriteCard(selectedMarker.getTitle(), selectedMarker.getSnippet(),
                selectedMarker.getPosition().latitude, selectedMarker.getPosition().longitude);

        //add shelter name to favorite list
        favoriteShelters.add(selectedMarker.getTitle());

        //add shelter to database
        database.collection("FavoriteShelters").document(uid).update("favoriteShelters", FieldValue.arrayUnion(favoriteCard));

        Snackbar snackbar = Snackbar.make(bottomSheet, "Saved to favorites", Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    public void removeSelectedShelterFromFavorites() {
        FavoriteCard favoriteCard = new FavoriteCard(selectedMarker.getTitle(), selectedMarker.getSnippet(),
                selectedMarker.getPosition().latitude, selectedMarker.getPosition().longitude);

        //remove shelter name from favorite list
        favoriteShelters.remove(selectedMarker.getTitle());

        //remove shelter from databse
        database.collection("FavoriteShelters").document(uid).update("favoriteShelters", FieldValue.arrayRemove(favoriteCard));

        Snackbar snackbar = Snackbar.make(bottomSheet, "Removed from favorites", Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void onNegativeButtonClicked() {
        // Do Nothing
    }


    @Override
    public void onNeutralButtonClicked() {
        // Do Nothing
    }


    @Override
    public void onPositiveButtonClicked(int i, String s) {
        if (s.isEmpty()) {
            Toast.makeText(MapViewActivity.this, "Please write a feedback", Toast.LENGTH_LONG).show();
            appRatingDialog.show();
        } else {
            addReview(i, s);
        }
    }

    public void addReview(int stars, String review) {
        Date currentTime = Calendar.getInstance().getTime();
        Review newReview = new Review(selectedShelter.getName(), userFullName, userEmail, review, String.valueOf(stars), currentTime.toString());

        //add review for selected shelter
        database.collection("UserReviews").add(newReview);

        //update shelter total rating
        database.collection("UserReviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int amount = 0;
                    double averageRating, totalRating = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.get("shelterName").toString().equals(selectedMarker.getTitle())) {
                            amount++;
                            totalRating += Double.parseDouble(document.get("stars").toString());
                        }
                    }

                    averageRating = totalRating / amount;
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    database.collection("Shelters").document(selectedShelterUID).update("rating", decimalFormat.format(averageRating));
                    database.collection("Shelters").document(selectedShelterUID).update("rateCount", String.valueOf(amount));

                    //update marker window dialog
                    selectedShelter.setRating(decimalFormat.format(averageRating));
                    selectedShelter.setRateCount(String.valueOf(amount));
                    onMarkerClick(selectedMarker);
                } else {
                    Log.d("Show Review", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void updateSelectedShelter() {
        FirebaseFirestore.getInstance().collection("Shelters").document(selectedShelterUID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    selectedShelter = task.getResult().toObject(Shelter.class);
                    sheltersList.replace(selectedShelterUID, selectedShelter);
                    selectedMarker.setTitle(selectedShelter.getName());
                    selectedMarker.setSnippet(selectedShelter.getAddress());
                    onMarkerClick(selectedMarker);
                }
            }
        });
    }

    public void hideSoftKeyboard() {
        if (this.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void initializeUserPermission() {
        FirebaseFirestore.getInstance().collection("Users").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getString("permission").equals("user")) {
                        editShelterButton = findViewById(R.id.info_window_edit_button);
                        editShelterButton.setVisibility(View.INVISIBLE);
                        navigationView = findViewById(R.id.nav_view);
                        navigationView.getMenu().findItem(R.id.nav_global_message).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_show_user).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_show_reviews).setVisible(false);
                    }
                    else permission = "admin";
                }
            }
        });
    }
}