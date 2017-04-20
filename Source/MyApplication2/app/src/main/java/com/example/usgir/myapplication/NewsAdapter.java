package com.example.usgir.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by girish on 4/19/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.RecyclerViewHolder> {
    Context context;
    ArrayList<String> newsitems,newstitle;

    public NewsAdapter(Context context, ArrayList<String> newsitems,ArrayList<String> newstitle) {
        this.newsitems = newsitems;
        this.context = context;
        this.newstitle=newstitle;

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listimageitem, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, context, newsitems);
        return recyclerViewHolder;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        try {
            Typeface fonttype = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirLTStd-Book.otf");
            holder.newstitle.setText(newstitle.get(position));
            holder.newstitle.setTypeface(fonttype);
            ScaleAnimation fade_in = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            fade_in.setDuration(500);     // animation duration in milliseconds
            fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
            holder.itemView.startAnimation(fade_in);
            holder.newstitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Web.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("uri", newsitems.get(position));
                    context.startActivity(intent);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return newsitems.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ArrayList<String> Newsitems;
        Context context;
        TextView newstitle;
        ImageView imageView;


        public RecyclerViewHolder(View itemView, Context context, ArrayList<String> Newsitems) {
            super(itemView);
            this.Newsitems = Newsitems;
            this.context = context;
            newstitle = (TextView) itemView.findViewById(R.id.newstitles);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
