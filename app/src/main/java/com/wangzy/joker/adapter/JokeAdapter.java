package com.wangzy.joker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.common.util.ListUtiles;
import com.common.util.Tool;
import com.common.view.CircleImageView;
import com.squareup.picasso.Picasso;
import com.wangzy.joker.R;
import com.wangzy.joker.constants.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzy on 2017/5/26.
 */

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.MyViewHolder> {

    public Context context;
    private ArrayList<AVObject> jokes;
    private LayoutInflater layoutInflater;
    private Point point;

    public JokeAdapter(Context context) {
        this.context = context;
        this.jokes = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
        this.point = Tool.getDisplayMetrics(context);
    }

    public void addMoreJokers(List<AVObject> jokes) {
        this.jokes.addAll(jokes);
        notifyDataSetChanged();
    }


    @Override
    public JokeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(layoutInflater.inflate(R.layout.item_joke, null));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final AVObject avObject = jokes.get(position);
        String type = avObject.getString("type");
        AVUser author = avObject.getAVUser("author");
        AVFile fileHeader = author.getAVFile("avatar");
        if (null != fileHeader) {
            String hader = fileHeader.getThumbnailUrl(true, 35, 35);
            Picasso.with(context).load(hader).
                    placeholder(R.drawable.bg_default_avatar).
                    resize(35, 35).
                    centerCrop().
                    into(holder.circleImageViewHeader);
        }


        holder.textViewAuthorName.setText(author.getUsername());
        holder.textViewJokerTitle.setText(avObject.getString("title"));


        holder.imageViewParise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                avObject.increment("nice");
                avObject.setFetchWhenSave(true);
                avObject.saveEventually();
                notifyDataSetChanged();

            }
        });
        holder.imageViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        holder.textViewNiceCount.setText(String.valueOf(avObject.getInt("nice")));

        switch (type) {
            case Constant.JOKETYPE.JOKE_TYPE_TEXT: {
                holder.textViewContent.setVisibility(View.VISIBLE);
                holder.textViewContent.setText(Html.fromHtml(avObject.getString("textContent")));
//                holder.imageViewTypeIcon.setImageResource(R.drawable.icon_type_text);

                holder.imageViewImg.setVisibility(View.GONE);
            }
            break;
            case Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG: {
                String url = avObject.getString("imageUrl");
                if (url.endsWith("gif")) {
                    holder.textViewJokerTitle.append("(gif)");
                } else {
                    holder.textViewJokerTitle.append("(图片)");
                }

                displayImage(url, holder.imageViewImg);
            }


            break;

            case Constant.JOKETYPE.JOKE_TYPE_TEXT_GIF: {

                holder.textViewJokerTitle.append("(视频)");
                String url = avObject.getString("imageUrl");
                if (url.endsWith("gif")) {
                    holder.textViewJokerTitle.append("(gif)");
                } else {
                    holder.textViewJokerTitle.append("(图片)");
                }

                holder.imageViewImg.setVisibility(View.VISIBLE);

                displayImage(url, holder.imageViewImg);
            }

            break;


            case Constant.JOKETYPE.JOKE_TYPE_TEXT_VIDEO: {
                String url = avObject.getString("imageUrl");
                holder.imageViewImg.setVisibility(View.VISIBLE);
                displayImage(url, holder.imageViewImg);

            }

            break;


        }

    }

    private void displayImage(String url, ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        Picasso.with(context).
                load(url).
                resize(point.x, point.y / 4).
                centerInside().
                config(Bitmap.Config.RGB_565).
                into(imageView);
    }


    @Override
    public int getItemCount() {
        return ListUtiles.getListSize(jokes);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.circleImageViewHeader)
        CircleImageView circleImageViewHeader;

        @BindView(R.id.textViewAuthorName)
        TextView textViewAuthorName;

        @BindView(R.id.textViewJokerTitle)
        TextView textViewJokerTitle;

        @BindView(R.id.textViewContent)
        TextView textViewContent;

        @BindView(R.id.imageViewParise)
        ImageView imageViewParise;

        @BindView(R.id.imageViewReport)
        ImageView imageViewReport;

        @BindView(R.id.textViewNiceCount)
        TextView textViewNiceCount;

        @BindView(R.id.imageViewImg)
        ImageView imageViewImg;


//        @BindView(R.id.imageViewTypeIcon)
//        ImageView imageViewTypeIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
