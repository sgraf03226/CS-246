package c246.groupapp;


import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for a list of Schedules.
 */
public class EmployeeAdapter extends FirestoreAdapter<EmployeeAdapter.ViewHolder> {

    public interface OnScheduleSelectedListener {

        void onScheduleSelected(DocumentSnapshot schedule);

    }


    private OnScheduleSelectedListener mListener;

    public EmployeeAdapter(Query query, OnScheduleSelectedListener listener) {
        super(query);

        mListener = listener;
    }

    /**
     * Inflates a new schedule item
     * @param parent Helps get context of where to inflate
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 OnItemClickListener.onClick();

            }
                                         }

        );*/
    }

    /**
     * Custom class for the schedule items,grabs info from each document in
     * firebase to bind to each item_schedule
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        /*
        @BindView(R.id.user_item_image)
        ImageView imageView;
        */

        @BindView(R.id.user_item_name)
        TextView userView;

        @BindView(R.id.user_item_position)
        TextView positionView;

        @BindView(R.id.user_item_birthday)
        TextView birthdayView;

        /**
         * When passed an itemView, it binds it so the data in the schedule can be viewed
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Does the actual binding of values to specific xml fields.
         * @param snapshot snapshot of a document with all of its fields of info
         * @param listener
         */
        public void bind(final DocumentSnapshot snapshot,
                         final OnScheduleSelectedListener listener) {

            Employee employee = snapshot.toObject(Employee.class);
            Resources resources = itemView.getResources();

            /*/ Load image
            Glide.with(imageView.getContext())
                    .load(schedule.getPhoto())
                    .into(imageView);
            */
            userView.setText(employee.getName());
            positionView.setText(employee.getPosition());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        Log.d("EVENT CLICKss", "TRUE");
                        listener.onScheduleSelected(snapshot);
                    }
                }
            });
        }

    }
}
