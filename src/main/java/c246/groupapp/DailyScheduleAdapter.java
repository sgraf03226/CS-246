package c246.groupapp;

import android.content.Context;


import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class DailyScheduleAdapter extends RecyclerView.Adapter<DailyScheduleAdapter.ViewHolder> {

//    private ArrayList<String> employees = new ArrayList<>();
//    private ArrayList<String> schedule  = new ArrayList<>();
    //private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<ScheduleItem> s = new ArrayList<>();

    private Context context;


    public DailyScheduleAdapter(ArrayList<ScheduleItem> s, Context context) {
        Log.d("in adapter", "adapter created");
//        this.employees = employees;
//        this.schedule = schedule;
        this.s = s;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d("in adapter", "veiw holder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.d("in adapter", "on bind");
        viewHolder.title.setText(s.get(i).getE().getName());
        viewHolder.info.setText(s.get(i).getDate() + " from " + s.get(i).getStartTime() + " to " + s.get(i).getEndTime());


    }

    @Override
    public int getItemCount() {
        return s.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public TextView info;
            //ImageView image;
            LinearLayout rl;
            public ViewHolder(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.title);
                info = (TextView) itemView.findViewById(R.id.info);
                //image = itemView.findViewById(R.id.imageView);

                rl = itemView.findViewById(R.id.shedule_item);
            }
        }





}
