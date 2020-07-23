package c246.groupapp;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class SpinAdapter extends ArrayAdapter<QueryDocumentSnapshot> {

    public SpinAdapter(Context context, int textViewResourceId, List<QueryDocumentSnapshot> objects) {
        super(context, textViewResourceId, objects);

    }
}
