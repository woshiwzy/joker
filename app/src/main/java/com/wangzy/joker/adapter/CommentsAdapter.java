package com.wangzy.joker.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.common.util.ListUtiles;
import com.common.util.Tool;
import com.common.view.CircleImageView;
import com.common.view.RelativeTimeTextView;
import com.squareup.picasso.Picasso;
import com.wangzy.joker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzy on 2017/5/26.
 */

public abstract class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    public Context context;
    private ArrayList<AVObject> commentsAdapter;
    private LayoutInflater layoutInflater;
    private Point point;


    public CommentsAdapter(Context context) {
        this.context = context;
        this.commentsAdapter = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
        this.point = Tool.getDisplayMetrics(context);

    }

    public void addComments(List<AVObject> jokes) {
        this.commentsAdapter.addAll(jokes);
        notifyDataSetChanged();
    }


    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        CommentViewHolder myViewHolder = new CommentViewHolder(layoutInflater.inflate(R.layout.item_comments, parent,false));
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {

        final AVObject avObject = commentsAdapter.get(position);
        AVUser author = avObject.getAVUser("author");

        AVFile fileHeader = author.getAVFile("avatar");


        if (null != fileHeader) {
            String hader = fileHeader.getThumbnailUrl(true, 35, 35);
            Picasso.with(context).load(hader).

                    placeholder(R.drawable.bg_default_avatar).
                    into(holder.circleImageViewHeader);
        }


        String commentContent = avObject.getString("commentContent");
        AVUser user = avObject.getAVUser("author");
        holder.textViewAuthorName.setText(user.getUsername());
        holder.textViewContent.setText(commentContent);
        holder.textViewTime.setReferenceTime(avObject.getDate("createdAt").getTime());


    }

    public void clearData() {
        commentsAdapter.clear();
    }

    public void clearAndRefrsh() {
        commentsAdapter.clear();
        notifyDataSetChanged();
    }


    public abstract void onItemCLick(AVObject joke);


    @Override
    public int getItemCount() {
        return ListUtiles.getListSize(commentsAdapter);
    }


     class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.circleImageViewHeader)
        CircleImageView circleImageViewHeader;
        @BindView(R.id.textViewAuthorName)
        TextView textViewAuthorName;

        @BindView(R.id.timestamp)
        RelativeTimeTextView textViewTime;

        @BindView(R.id.textViewContent)
        TextView textViewContent;

        CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
