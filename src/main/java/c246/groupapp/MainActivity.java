package c246.groupapp;

import android.widget.ArrayAdapter;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.text.SimpleDateFormat;
import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This will launch the login activity from firebase if the user is not
 * logged in, if the user is logged in it will display
 * their schedule;
 */
public class MainActivity extends AppCompatActivity implements
        ScheduleAdapter.OnScheduleSelectedListener, EmployeeAdapter.OnScheduleSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.text_current_search)
    TextView mCurrentSearchView;

    @BindView(R.id.recycler_restaurants)
    RecyclerView mSchedulesRecycler;

    @BindView(R.id.recycler_users)
    RecyclerView mUserRecycler;

    @BindView(R.id.view_empty)
    ViewGroup mEmptyView;

    private static final int LIMIT = 50;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private Query eQuery;
    private MainActivityViewModel mViewModel;
    private ScheduleAdapter mAdapter;
    private EmployeeAdapter eAdapter;
    private String companyName;
    private String userId;
    boolean isEmpty;
    Integer viewNum;
    boolean manager;
    String name;
    String employeeID;
    Map<String, String> ListOfIds;
    FirebaseUser user;
    DocumentSnapshot exists;


    /**
     * Sets up the users recycler view of schedules, has solutions if the user is not logged in
     * @param savedInstanceState Creates the recycle view that shows the users schedule
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        companyName = "byui";
        isEmpty = true;
        viewNum = 10;
        manager = false;
        ListOfIds = new HashMap();

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //set up the query for the current user
        String date = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());

        if(user != null) {
            //getting the user id from firebase
            userId = user.getUid();
            mQuery = mFirestore.collection("companies").document(companyName).collection("employees").document(
                    userId).collection("schedules");//.orderBy("date");//.whereGreaterThanOrEqualTo("date", date).orderBy("date").limit(viewNum);

            eQuery = mFirestore.collection("companies").document(companyName).collection("employees");
            DocumentReference doc = mFirestore.collection("companies").document(companyName).collection("employees").document(userId);
            doc
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Employee e = documentSnapshot.toObject(Employee.class);
                                if (e.getisManager()) {
                                    manager = true;
                                    Log.d("Manager set ", "TRUE");
                                    invalidateOptionsMenu();
                                } else {
                                    manager = false;
                                    Log.d("Manager set", "FALSE");
                                    invalidateOptionsMenu();
                                }
                            } else {
                                manager = false;
                            }

                            if (manager)
                            {
                                mUserRecycler.setAdapter(eAdapter);
                                mSchedulesRecycler.setVisibility(View.GONE);
                                mUserRecycler.setVisibility(View.VISIBLE);

                            } else{
                                mSchedulesRecycler.setAdapter(mAdapter);
                                mSchedulesRecycler.setVisibility(View.VISIBLE);
                                mUserRecycler.setVisibility(View.GONE);
                            }

                        }
                    });

        }

        mFirestore.collection("companies").document(companyName).collection("employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                employeeID = document.getId();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.getData().toString();
                                if (name.contains(",")){
                                    name = name.substring(6,name.indexOf(','));
                                }
                                else {
                                    name = name.substring(6, name.length() - 1);
                                }
                                ListOfIds.put(name, employeeID);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // RecyclerView of schedule items

        mAdapter = new ScheduleAdapter(mQuery, this);
        eAdapter = new EmployeeAdapter(eQuery, this);


        mSchedulesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mUserRecycler.setLayoutManager(new LinearLayoutManager(this));


    }

    /**
     * Takes a menu xml file and loads it into the three dots
     * Will not inflate make schedule button if user is not a manager
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_sign_out);
        item.setVisible(true);
        MenuItem scheduleItem = menu.findItem(R.id.makeSchedule);
        MenuItem addUserItem = menu.findItem(R.id.addEmployee);
        MenuItem timeOffItem = menu.findItem(R.id.requestTimeOff);
        // Hide admin controls from non-managers
        if(manager){
            scheduleItem.setVisible(true);
            addUserItem.setVisible(true);
            timeOffItem.setVisible(false);
        } else {
            scheduleItem.setVisible(false);
            addUserItem.setVisible(false);
            timeOffItem.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Functions for when an menu item is clicked.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                AuthUI.getInstance().signOut(this);
                userId = "";
                startSignIn(true);
                break;
            case R.id.addEmployee:
                //startActivity()
                startSignIn(false);
                break;
            case R.id.makeSchedule:
                startActivity(new Intent(this, CreateSchedule.class));
                return true;
            case R.id.requestTimeOff:
                // TODO replace with request time off signal
                startActivity(new Intent(this, CreateSchedule.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    This is an example of how to commit something to our firebase database on an item click.
    It is currently not being used.
     */
    private void onAddItemsClicked() {
        // Add a bunch of random restaurants
        WriteBatch batch = mFirestore.batch();
        for (int i = 0; i < 10; i++) {
            DocumentReference restRef = mFirestore.collection("restaurants").document();

            // Create random restaurant / ratings
            //Schedule randomSchedule = ScheduleUtil.getRandom(this);


            // Add restaurant
            //batch.set(restRef, randomSchedule);

        }
    }

    /**
     * This code is run when a user attempts to log in.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mViewModel.setIsSigningIn(false);

            if (resultCode != RESULT_OK && shouldStartSignIn()) {
                startSignIn(true);
            }

            //Need to check that the user id has changed or not
            user = FirebaseAuth.getInstance().getCurrentUser();

            userId = user.getUid();
            if (user != null) {
                name = user.getDisplayName();
            }
            Log.d("Debugging", "UserID: " + userId);

            DocumentReference docref = mFirestore.collection("companies").document(companyName).collection("employees").document(userId);
            docref
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "if doc exists " + documentSnapshot.exists());

                        if (!documentSnapshot.exists()) {
                            exists = documentSnapshot;

                            Employee newE = new Employee(name);
                            newE.setIsManager(false);
                            newE.setPosition(null);

                            Log.d(TAG, "Get name of user " + user.getDisplayName());

                            mFirestore.collection("companies").document(companyName).collection("employees").document(userId)
                                    .set(newE)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });


                    }
                }

            });


            //update query to show the right person's info
            String date = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());
            if(userId != null) {
                mQuery = mFirestore.collection("companies").document(companyName).collection("employees").document(
                        userId).collection("schedules").orderBy("date");//.whereGreaterThanOrEqualTo("date", date).orderBy("date").limit(10);


                eQuery = mFirestore.collection("companies").document(companyName).collection("employees");

                mAdapter.setQuery(mQuery);
                eAdapter.setQuery(eQuery);
            }

            //check for manager status to update menu
            DocumentReference doc = mFirestore.collection("companies").document(companyName).collection("employees").document(userId);
                    doc
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                Employee e = documentSnapshot.toObject(Employee.class);

                                if (e.getisManager()) {
                                    manager = true;
                                    Log.d("Manager set ", "TRUE");
                                    invalidateOptionsMenu();
                                } else {
                                    manager = false;
                                    Log.d("Manager set", "FALSE");
                                    invalidateOptionsMenu();
                                }
                            }
                            else{
                                manager = false;
                            }

                            if (manager)
                            {
                                mUserRecycler.setAdapter(eAdapter);
                                mSchedulesRecycler.setVisibility(View.GONE);
                                mUserRecycler.setVisibility(View.VISIBLE);

                            } else{
                                mSchedulesRecycler.setAdapter(mAdapter);
                                mSchedulesRecycler.setVisibility(View.VISIBLE);
                                mUserRecycler.setVisibility(View.GONE);
                            }
                        }
                    });

        }
    }


    /**
     * Checks for authentication and starts listening to firebase events
     */
    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn(true);
            return;
        }

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
        if (eAdapter != null) {
            eAdapter.startListening();
        }

    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    private void startSignIn(boolean signin) {
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(signin);
    }

    @Override
    public void onScheduleSelected(DocumentSnapshot schedule) {
        Log.d("OVERRIDE CLICKER", "TRUE");
        Employee e = schedule.toObject(Employee.class);
        String name = e.getName();
        String Id = ListOfIds.get(name);
        Intent intent = new Intent(this, ViewSchedule.class);
        intent.putExtra("USER_ID", Id);
        startActivity(intent);
    }
}
