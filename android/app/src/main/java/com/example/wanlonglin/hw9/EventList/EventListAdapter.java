package com.example.wanlonglin.hw9.EventList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wanlonglin.hw9.R;

import static android.content.Context.MODE_PRIVATE;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.LinerViewHolder> {

    private Context context;
    private EventList eventListArr;
    private OnItemClickListener listener;
    private int mode;

    public EventListAdapter(Context context, EventList eventListArr, OnItemClickListener listener, int mode) {
        this.context = context;
        this.eventListArr = eventListArr;
        this.listener = listener;
        this.mode = mode;
    }
    @NonNull
    @Override
    public EventListAdapter.LinerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LinerViewHolder(LayoutInflater.from(context).inflate(R.layout.event_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListAdapter.LinerViewHolder viewHolder, final int i) {
        viewHolder.eventName.setText(eventListArr.getEventName(i));
        viewHolder.venue.setText(eventListArr.getVenue(i));
        viewHolder.date.setText(eventListArr.getDate(i));
        Glide.with(context).load(eventListArr.getSegmentPic(i)).into(viewHolder.segmentPic);
        final String eventId = eventListArr.getEventId(i);
        check(eventId, viewHolder);
        viewHolder.clickArea.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onClick(i);
            }
        });
        viewHolder.heart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String choosenJsonString = eventListArr.getChoosenJsonObjectString(i);
                sharedPreference(eventId, choosenJsonString, viewHolder, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventListArr.length();
    }

    class LinerViewHolder extends RecyclerView.ViewHolder{
        private TextView eventName;
        private TextView venue;
        private TextView date;
        private ImageView segmentPic;
        private ImageView heart;
        private LinearLayout clickArea;

        public LinerViewHolder(View eventListItemView) {
            super(eventListItemView);
            eventName = (TextView) eventListItemView.findViewById(R.id.eventName);
            venue = (TextView) eventListItemView.findViewById(R.id.venue);
            date = (TextView) eventListItemView.findViewById(R.id.date);
            segmentPic = (ImageView) eventListItemView.findViewById(R.id.segmentPic);
            heart = (ImageView) eventListItemView.findViewById(R.id.heart);
            clickArea = (LinearLayout) eventListItemView.findViewById(R.id.clickArea);
        }
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }

    private void sharedPreference(String eventId, String choosenJsonString, EventListAdapter.LinerViewHolder viewHolder, int pos) {
        SharedPreferences prefs = context.getSharedPreferences("app", MODE_PRIVATE);
        if (!prefs.contains(eventId)) {
            String text = eventListArr.getEventName(pos) + "was added to favorites";
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = context.getSharedPreferences("app", MODE_PRIVATE).edit();
            editor.putString(eventId, choosenJsonString);
            editor.apply();
            viewHolder.heart.setImageResource(R.drawable.heart_fill_red);
            Log.d("xxxx",prefs.getString(eventId, null));

        } else {
            String text = eventListArr.getEventName(pos) + "was removed from favorites";
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = context.getSharedPreferences("app", MODE_PRIVATE).edit();
            editor.remove(eventId).commit();
            viewHolder.heart.setImageResource(R.drawable.heart_outline_black);
            if (mode == 1) {
                removeItem(pos);
            }
        }
    }

    private void check(String eventId, EventListAdapter.LinerViewHolder viewHolder) {
        SharedPreferences prefs = context.getSharedPreferences("app", MODE_PRIVATE);
        if (!prefs.contains(eventId)) {
            viewHolder.heart.setImageResource(R.drawable.heart_outline_black);
        } else {
            viewHolder.heart.setImageResource(R.drawable.heart_fill_red);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void removeItem(int position) {
        notifyItemRemoved(position);
        eventListArr.remove(position);
        notifyItemRangeChanged(position,eventListArr.length());
        if (eventListArr.length() == 0) {
            listener.onClick(-1);
        }
    }
}