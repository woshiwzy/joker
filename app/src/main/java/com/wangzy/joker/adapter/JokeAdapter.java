package com.wangzy.joker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.common.util.DialogCallBackListener;
import com.common.util.DialogUtils;
import com.common.util.ListUtiles;
import com.common.util.LogUtil;
import com.common.util.StringUtils;
import com.common.util.Tool;
import com.common.view.BubbleView;
import com.common.view.CircleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wangzy.joker.App;
import com.wangzy.joker.R;
import com.wangzy.joker.activity.BaseJokeActivity;
import com.wangzy.joker.activity.JokeDetailActivity;
import com.wangzy.joker.constants.Constant;
import com.wangzy.joker.picasso.VideoRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzy on 2017/5/26.
 */

public abstract class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.MyViewHolder> {

    public Context context;
    private ArrayList<AVObject> jokes;
    private LayoutInflater layoutInflater;
    private Point point;

    private VideoRequestHandler videoRequestHandler;
    private Picasso picassoInstance;

    public JokeAdapter(Context context) {
        this.context = context;
        this.jokes = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
        this.point = Tool.getDisplayMetrics(context);

        this.videoRequestHandler = new VideoRequestHandler();
        picassoInstance = new Picasso.Builder(context.getApplicationContext())
                .addRequestHandler(videoRequestHandler)
                .build();

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


    long mLastTime = 0;
    long mCurTime = 0;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

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

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout.LayoutParams lpp = (RelativeLayout.LayoutParams) holder.imageViewImg.getLayoutParams();

                JokeDetailActivity.aspectGif = lpp.width * 1.0f / lpp.height;

                onItemCLick(avObject);

            }
        });

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (avObject.getAVUser("author").equals(AVUser.getCurrentUser())) {


                    DialogUtils.showConfirmDialog(((BaseJokeActivity) context), "", "确认删除", "确认", "取消", new DialogCallBackListener() {
                        @Override
                        public void onDone(boolean yesOrNo) {
                            if (yesOrNo) {


                                ((BaseJokeActivity) context).showProgressDialog(false);
                                avObject.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        ((BaseJokeActivity) context).hideProgressDialog();

                                        if (null == e) {

                                            jokes.remove(avObject);
                                            notifyDataSetChanged();

                                        } else {
                                            BaseJokeActivity.showAVException((Activity) context, e);
                                        }
                                    }
                                });

                            }
                        }
                    });

                    return true;
                } else {
                    return false;
                }


            }
        });


        String title = avObject.getString("title");
//        LogUtil.e(App.tag, "title:" + title + " type:" + type);

        holder.textViewAuthorName.setText(author.getUsername());
        holder.textViewJokerTitle.setText(avObject.getString("title"));

        int commentCount = avObject.getInt("commentCount");
        if (commentCount > 0) {
            holder.textViewCommentCounts.setText(" " + commentCount + " ");
        }

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

        holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((BaseJokeActivity) context).shareAvObject(avObject);
            }
        });

        holder.textViewNiceCount.setText(String.valueOf(avObject.getInt("nice") + avObject.getInt("initNiceCount")));

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
                holder.imageViewImg.setVisibility(View.VISIBLE);

                if (StringUtils.isEmpty(url)) {

                    String resultUrl = avObject.getAVFile("imageFile").getUrl();
                    if (resultUrl.endsWith("gif")) {
                        holder.textViewJokerTitle.append("(gif)");
                    } else {
                        holder.textViewJokerTitle.append("(图片)");
                    }
                    displayImage(resultUrl, holder.imageViewImg);

                } else {
                    if (url.endsWith("gif")) {
                        holder.textViewJokerTitle.append("(gif)");
                    } else {
                        holder.textViewJokerTitle.append("(图片)");
                    }
                    displayImage(url, holder.imageViewImg);
                }
            }


            break;

            case Constant.JOKETYPE.JOKE_TYPE_TEXT_GIF: {

                String url = avObject.getString("imageUrl");
                holder.imageViewImg.setVisibility(View.VISIBLE);

                if (StringUtils.isEmpty(url)) {

                    String resultUrl = avObject.getAVFile("imageFile").getUrl();
                    if (resultUrl.endsWith("gif")) {
                        holder.textViewJokerTitle.append("(gif)");
                    } else {
                        holder.textViewJokerTitle.append("(图片)");
                    }
                    displayImage(resultUrl, holder.imageViewImg);

                } else {
                    url = url.replace("\n", "");
                    if (url.endsWith("gif")) {
                        holder.textViewJokerTitle.append("(gif)");
                    } else {
                        holder.textViewJokerTitle.append("(图片)");
                    }
                    displayImage(url, holder.imageViewImg);
                }

            }

            break;


            case Constant.JOKETYPE.JOKE_TYPE_TEXT_VIDEO: {
                String url = avObject.getString("imageUrl");
                holder.imageViewImg.setVisibility(View.VISIBLE);
                displayImage(url, holder.imageViewImg);

            }

            break;

            case Constant.JOKETYPE.JOKE_TYPE_MP4: {

                holder.textViewJokerTitle.append("(mp4)");
                String url = avObject.getAVFile("thumbImg").getUrl();
                holder.imageViewImg.setVisibility(View.VISIBLE);
                displayImage(url, holder.imageViewImg);

//                Target target=new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//
//                        LogUtil.e(App.tag,"thumb load succes.....");
//                        int width = bitmap.getWidth();
//                        int height = bitmap.getHeight();
//
//                        int scw = point.x;
//                        int sch = point.y;
//
//                        int nh = (int) (height * (scw * 1.0f / width));
//
//                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(scw, nh);
//
//                        holder.imageViewImg.setImageBitmap(bitmap);
//                        holder.imageViewImg.setLayoutParams(layoutParams);
//
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                };
//                holder.imageViewImg.setTag(target);
//                picassoInstance.load(videoRequestHandler.SCHEME_VIDEO + ":" + url).into(target);

            }

            break;


        }

    }

    public void clearData() {
        jokes.clear();
    }

    public void clearAndRefrsh() {
        jokes.clear();
        notifyDataSetChanged();
    }


    public abstract void onItemCLick(AVObject joke);

    private void displayImage(String url, final ImageView imageView) {


        Target nTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                int scw = point.x;
                int sch = point.y;

                int nh = (int) (height * (scw * 1.0f / width));

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(scw, nh);

                imageView.setImageBitmap(bitmap);
                imageView.setLayoutParams(layoutParams);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        imageView.setTag(nTarget);
        imageView.setVisibility(View.VISIBLE);

        Picasso.with(context).
                load(url).
                config(Bitmap.Config.RGB_565).
                into(nTarget);
    }


    private final int DELAY = 500;//连续点击的临界点
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            delayTimer.cancel();
            super.handleMessage(msg);
        }
    };
    private Timer delayTimer;
    private TimerTask timeTask;

    private void delay() {
        if (timeTask != null)
            timeTask.cancel();

        timeTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                mHandler.sendMessage(message);
            }
        };
        delayTimer = new Timer();
        delayTimer.schedule(timeTask, DELAY);
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

        @BindView(R.id.rootView)
        View rootView;

        @BindView(R.id.bubbleView)
        BubbleView bubbleView;

        @BindView(R.id.imageViewShare)
        ImageView imageViewShare;


        @BindView(R.id.textViewCommentCounts)
        TextView textViewCommentCounts;

//        @BindView(R.id.imageViewTypeIcon)
//        ImageView imageViewTypeIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            bubbleView.setDefaultDrawableList();
        }
    }

}
