package c246.groupapp;

import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for a list of Schedules.
 */
public class ScheduleAdapter extends FirestoreAdapter<ScheduleAdapter.ViewHolder> {

    public interface OnScheduleSelectedListener {

        void onScheduleSelected(DocumentSnapshot schedule);

    }

    private OnScheduleSelectedListener mListener;

    public ScheduleAdapter(Query query, OnScheduleSelectedListener listener) {
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
        return new ViewHolder(inflater.inflate(R.layout.item_schedule, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    /**
     * Custom class for the schedule items,grabs info from each document in
     * firebase to bind to each item_schedule
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.schedule_item_image)
        ImageView imageView;

        @BindView(R.id.schedule_item_day)
        TextView dayView;

        @BindView(R.id.schedule_item_date)
        TextView dateView;

        @BindView(R.id.schedule_item_time)
        TextView timeView;

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

            Schedule schedule = snapshot.toObject(Schedule.class);
            Resources resources = itemView.getResources();

            // Load image
            Glide.with(imageView.getContext())
                    .load(schedule.getPhoto())
                    .into(imageView);

            dayView.setText(schedule.getDay());
            timeView.setText(schedule.getTime());
            dateView.setText(schedule.getDate());


            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onScheduleSelected(snapshot);
                    }
                }
            });
        }

    }
}
