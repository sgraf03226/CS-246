package c246.groupapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CreateSchedule  allows authorized users to create new schedules for other users
 *
 */
public class ViewSchedule extends AppCompatActivity implements
        ScheduleAdapter.OnScheduleSelectedListener{

    @BindView(R.id.recycler_employee_schedule)
    RecyclerView mEmployeeSchedulesRecycler;

    public String userId;

    private Query mQuery;
    private ScheduleAdapter mAdapter;
    String companyName;
    private FirebaseFirestore mFirestore;

    String employeeID;
    List<String> ListOfNames;
    Map<String, String> ListOfIds;
    DocumentReference doc;
    List<QueryDocumentSnapshot> names;

    /**
     * creates the activity necessary to create schedules
     * gets the data from firebase to fill the nameSpinner
     *
     * @param savedInstanceState is the state saved by last activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_display);
        ButterKnife.bind(this);

        companyName = "byui";

        mFirestore = FirebaseFirestore.getInstance();

        ListOfNames = new ArrayList<>();
        ListOfIds = new HashMap();
        names = new ArrayList<>();
        final Context here = this;

        userId = getIntent().getStringExtra("USER_ID");
        mQuery = mFirestore.collection("companies").document(companyName).collection("employees").document(
                userId).collection("schedules");
        Log.d("PASSED USER ID:", userId);
        mAdapter = new ScheduleAdapter(mQuery, this);
        mAdapter.setQuery(mQuery);

        mEmployeeSchedulesRecycler.setLayoutManager(new LinearLayoutManager(this));
        // mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        FirebaseFirestore.setLoggingEnabled(true);



        DocumentReference doc = mFirestore.collection("companies").document(companyName).collection("employees").document(userId);
        doc
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Employee e = documentSnapshot.toObject(Employee.class);
                            if (e.getisManager()) {

                            }
                        }

                    }
                });


        mEmployeeSchedulesRecycler.setAdapter(mAdapter);
        mEmployeeSchedulesRecycler.setVisibility(View.VISIBLE);



        mEmployeeSchedulesRecycler.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }
    /**
     *  Sends new schedule information to firebase under the name of the employee, the date
     *  and time selected in the spinners
     *
     * @param view enables button click
     */
    public void onClick(View view){

        }

    @Override
    public void onScheduleSelected(DocumentSnapshot schedule) {

    }
}
