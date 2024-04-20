package com.example.wanlonglin.hw9.Tab2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanlonglin.hw9.R;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.LinerViewHolder> {

    private Context context;
    private PicList picList;
    private MusicList musicList;

    public ArtistsAdapter(Context context, PicList picList, MusicList musicList) {
        this.context = context;
        this.picList = picList;
        this.musicList = musicList;
    }
    @NonNull
    @Override
    public ArtistsAdapter.LinerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LinerViewHolder(LayoutInflater.from(context).inflate(R.layout.artists_list_item, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ArtistsAdapter.LinerViewHolder viewHolder, final int i) {

        if (!picList.getName(i).equals("")) {
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.titleValue.setText(picList.getName(i));
        }

        if (musicList != null) {
            if (!musicList.getNmae(i).equals("")) {
                viewHolder.name.setVisibility(View.VISIBLE);
                viewHolder.nameValue.setText(musicList.getNmae(i));
            }
            if (!musicList.getFollowers(i).equals("")) {
                viewHolder.follower.setVisibility(View.VISIBLE);
                viewHolder.followerValue.setText(musicList.getFollowers(i));
            }
            if (!musicList.getPopularity(i).equals("")) {
                viewHolder.popularity.setVisibility(View.VISIBLE);
                viewHolder.popularityValue.setText(musicList.getPopularity(i));
            }
            if (!musicList.getCheckAt(i).equals("")) {
                viewHolder.checkAt.setVisibility(View.VISIBLE);
                String checkAtUrl = "<a href=\"" +  musicList.getCheckAt(i) + "\">Spotify</a>";
                viewHolder.checkAtValue.setText(Html.fromHtml(checkAtUrl));
                viewHolder.checkAtValue.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        String[] urlArr = picList.getUrlArr(i);
        Glide.with(context).load(urlArr[0]).into(viewHolder.img1);
        Glide.with(context).load(urlArr[1]).into(viewHolder.img2);
        Glide.with(context).load(urlArr[2]).into(viewHolder.img3);
        Glide.with(context).load(urlArr[3]).into(viewHolder.img4);
        Glide.with(context).load(urlArr[4]).into(viewHolder.img5);
        Glide.with(context).load(urlArr[5]).into(viewHolder.img6);
        Glide.with(context).load(urlArr[6]).into(viewHolder.img7);
        Glide.with(context).load(urlArr[7]).into(viewHolder.img8);
    }

    @Override
    public int getItemCount() {
        return picList == null ? 0 : picList.getLength() > 2 ? 2 : picList.getLength();
    }

    class LinerViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout title;
        private LinearLayout name;
        private LinearLayout follower;
        private LinearLayout popularity;
        private LinearLayout checkAt;
        private TextView titleValue;
        private TextView nameValue;
        private TextView followerValue;
        private TextView popularityValue;
        private TextView checkAtValue;
        private ImageView img1;
        private ImageView img2;
        private ImageView img3;
        private ImageView img4;
        private ImageView img5;
        private ImageView img6;
        private ImageView img7;
        private ImageView img8;


        public LinerViewHolder(View artistItemView) {
            super(artistItemView);
            name = (LinearLayout) artistItemView.findViewById(R.id.name);
            follower = (LinearLayout) artistItemView.findViewById(R.id.follower);
            popularity = (LinearLayout) artistItemView.findViewById(R.id.popularity);
            checkAt = (LinearLayout) artistItemView.findViewById(R.id.checkAt);
            title = (LinearLayout) artistItemView.findViewById(R.id.title);


            nameValue = (TextView) artistItemView.findViewById(R.id.nameValue);
            followerValue = (TextView) artistItemView.findViewById(R.id.followerValue);
            popularityValue = (TextView) artistItemView.findViewById(R.id.popularityValue);
            checkAtValue = (TextView) artistItemView.findViewById(R.id.checkAtValue);

            titleValue = (TextView) artistItemView.findViewById(R.id.titleValue);
            img1 = (ImageView) artistItemView.findViewById(R.id.img1);
            img2 = (ImageView) artistItemView.findViewById(R.id.img2);
            img3 = (ImageView) artistItemView.findViewById(R.id.img3);
            img4 = (ImageView) artistItemView.findViewById(R.id.img4);
            img5 = (ImageView) artistItemView.findViewById(R.id.img5);
            img6 = (ImageView) artistItemView.findViewById(R.id.img6);
            img7 = (ImageView) artistItemView.findViewById(R.id.img7);
            img8 = (ImageView) artistItemView.findViewById(R.id.img8);

        }
    }
}
