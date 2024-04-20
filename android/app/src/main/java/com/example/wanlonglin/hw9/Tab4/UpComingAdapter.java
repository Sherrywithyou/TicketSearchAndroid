package com.example.wanlonglin.hw9.Tab4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wanlonglin.hw9.R;

public class UpComingAdapter extends RecyclerView.Adapter<UpComingAdapter.LinerViewHolder>{
    private Context context;
    private UpComingList upComingList;

    public UpComingAdapter(Context context, UpComingList upComingList) {
        this.context = context;
        this.upComingList = upComingList;
    }


    @NonNull
    @Override
    public UpComingAdapter.LinerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LinerViewHolder(LayoutInflater.from(context).inflate(R.layout.upcoming_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UpComingAdapter.LinerViewHolder viewHolder, final int i) {
        viewHolder.title.setText(upComingList.getTitle(i));
        viewHolder.artist.setText(upComingList.getArtist(i));
        viewHolder.showTime.setText(upComingList.getShowTime(i));
        viewHolder.type.setText("Type " + upComingList.getType(i));

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = upComingList.getUrl(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return upComingList.getLength();
    }

    class LinerViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView artist;
        private TextView showTime;
        private TextView type;
        private CardView card_view;


        public LinerViewHolder(View upcomingItemView) {
            super(upcomingItemView);
            title = (TextView) upcomingItemView.findViewById(R.id.title);
            artist = (TextView) upcomingItemView.findViewById(R.id.artist);
            showTime = (TextView) upcomingItemView.findViewById(R.id.showTime);
            type = (TextView) upcomingItemView.findViewById(R.id.type);
            card_view = (CardView) upcomingItemView.findViewById(R.id.card_view);
        }
    }
}
