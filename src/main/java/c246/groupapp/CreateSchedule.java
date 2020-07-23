package c246.groupapp;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;



/**
 * CreateSchedule  allows authorized users to create new schedules for other users
 *
 */
public class CreateSchedule extends AppCompatActivity {

    private static final String TAG = "Create Schedule";
    Spinner nameSpinner;
    Spinner daySpinner;

    Spinner startHour;
    Spinner startMin;
    Spinner startPM;

    EditText date;
    Spinner endHour;
    Spinner endMin;
    Spinner endPM;
    String uid;
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
        setContentView(R.layout.create_schedule);
        FirebaseFirestore.setLoggingEnabled(true);
        companyName = "byui";
        mFirestore = FirebaseFirestore.getInstance();
        uid = mFirestore.getApp().getName();
        ListOfNames = new ArrayList<>();
        ListOfIds = new HashMap();
        names = new ArrayList<>();
        final Context here = this;
        mFirestore.collection("companies").document(companyName).collection("employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        nameSpinner = findViewById(R.id.nameSpinner);

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                employeeID = document.getId();
                                String name = document.getData().toString();
                                if (name.contains(",")){
                                    name = name.substring(6,name.indexOf(','));
                                }
                                else {
                                    name = name.substring(6, name.length() - 1);
                                }
                                names.add(document);
                                ListOfNames.add(name);
                                ListOfIds.put(name, employeeID);
                            }

                            ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(here, android.R.layout.simple_spinner_dropdown_item, ListOfNames);
                            nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            nameSpinner.setAdapter(nameAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        date = findViewById(R.id.dateEdit);

        daySpinner = findViewById(R.id.daySpin);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.daysOfTheWeekSpinner, android.R.layout.simple_spinner_dropdown_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        startHour = findViewById(R.id.startHourSpinner3);
        ArrayAdapter<CharSequence> startHourAdapter = ArrayAdapter.createFromResource(this, R.array.SpinnerMonthAndHour, android.R.layout.simple_spinner_dropdown_item);
        startHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startHour.setAdapter(startHourAdapter);

        startMin = findViewById(R.id.startMinSpinner);
        ArrayAdapter<CharSequence> startMinAdapter = ArrayAdapter.createFromResource(this, R.array.SpinnerTimeMin, android.R.layout.simple_spinner_dropdown_item);
        startMinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMin.setAdapter(startMinAdapter);

        startPM = findViewById(R.id.startPMSpinner);
        ArrayAdapter<CharSequence> startPMAdapter = ArrayAdapter.createFromResource(this, R.array.SpinnerTimePM, android.R.layout.simple_spinner_dropdown_item);
        startPMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startPM.setAdapter(startPMAdapter);

        endHour = findViewById(R.id.endHourSpinner);
        ArrayAdapter<CharSequence> endHourAdapter = ArrayAdapter.createFromResource(this, R.array.SpinnerMonthAndHour, android.R.layout.simple_spinner_dropdown_item);
        endHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endHour.setAdapter(endHourAdapter);

        endMin = findViewById(R.id.endMinSpinner);
        ArrayAdapter<CharSequence> endMinAdapter = ArrayAdapter.createFromResource(this, R.array.SpinnerTimeMin, android.R.layout.simple_spinner_dropdown_item);
        endMinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endMin.setAdapter(endMinAdapter);

        endPM = findViewById(R.id.endPMSpinner);
        ArrayAdapter<CharSequence> endPMAdapter = ArrayAdapter.createFromResource(this, R.array.SpinnerTimePM, android.R.layout.simple_spinner_dropdown_item);
        endPMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endPM.setAdapter(endPMAdapter);


    }

    /**
     *  Sends new schedule information to firebase under the name of the employee, the date
     *  and time selected in the spinners
     *
     * @param view enables button click
     */
    public void onClick(View view){
        mFirestore = FirebaseFirestore.getInstance();
        String ID = "";
        String pic = "";

        if (nameSpinner.getSelectedItem().toString().equals("Tanner Harding")) {
            pic = "https://firebasestorage.googleapis.com/v0/b/cs246-fd74c.appspot.com/o/download.jpg?alt=media&token=04204b90-7685-48a0-b724-14bf5b6b9861";
        }


        for (QueryDocumentSnapshot q: names){
            String temp = nameSpinner.getSelectedItem().toString();
            String uID = ListOfIds.get(temp);
            String tempOther = q.toString();
            if (q.toString().contains(uID))
            {
                ID = q.getId();

            }
        }

        //set the right image for the day relevant day of the week. Ie if it is Monday it will show a M
        if(daySpinner.getSelectedItem().toString().equals("Monday")){
            pic = "https://firebasestorage.googleapis.com/v0/b/cs246-fd74c.appspot.com/o/m.jpg?alt=media&token=5b3ab9bb-b36d-4078-a428-13ed56a43163";
        }
        if (daySpinner.getSelectedItem().toString().equals("Tuesday")){
            pic = "https://firebasestorage.googleapis.com/v0/b/cs246-fd74c.appspot.com/o/tu.jpg?alt=media&token=9ab89921-8c29-47aa-96fa-ff0e2c40b565";
        }
        if (daySpinner.getSelectedItem().toString().equals("Wednesday")){
            pic = "https://firebasestorage.googleapis.com/v0/b/cs246-fd74c.appspot.com/o/w.jpg?alt=media&token=2af183be-c09d-47b3-8748-e0655aedc928";
        }
        if (daySpinner.getSelectedItem().toString().equals("Thursday")){
            pic = "https://firebasestorage.googleapis.com/v0/b/cs246-fd74c.appspot.com/o/th.jpg?alt=media&token=ebd7d901-fe03-419c-9f43-8dcbb8c1fb38";
        }
        if (daySpinner.getSelectedItem().toString().equals("Friday")){
            pic = "https://firebasestorage.googleapis.com/v0/b/cs246-fd74c.appspot.com/o/f.jpg?alt=media&token=f633098b-e9ae-42b4-8256-e6fd1a551797";
        }
        if (daySpinner.getSelectedItem().toString().equals("Saturday")){
            pic = "https://firebasestorage.googleapis.com/v0/b/cs246-fd74c.appspot.com/o/sa.jpg?alt=media&token=6cd8e3d2-3988-419d-836d-d81379bdb02d";
        }
        if (daySpinner.getSelectedItem().toString().equals("Sunday")){
            pic = "https://firebasestorage.googleapis.com/v0/b/cs246-fd74c.appspot.com/o/su.jpg?alt=media&token=fa86d335-c7a1-4972-9e1e-9b03d61f4bd9";
        }

        Schedule s = new Schedule(daySpinner.getSelectedItem().toString(), startHour.getSelectedItem().toString() + ':' + startMin.getSelectedItem().toString() + ' ' + startPM.getSelectedItem().toString()
                + '-' + endHour.getSelectedItem().toString() + ':' + endMin.getSelectedItem().toString() + ' ' + endPM.getSelectedItem().toString(), date.getText().toString(), pic);

        Log.d(TAG, "onClick:" + names.toString());

        Log.d(TAG, "onClick:" + ID + nameSpinner.getSelectedItem().toString());
        mFirestore.collection("companies").document(companyName).collection("employees").document(ID).collection("schedules")
                .add(s)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Schedule Created",Toast.LENGTH_LONG ).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document", e);
                    }
                });


    }
}