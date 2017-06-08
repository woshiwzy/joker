package com.wangzy.joker.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.view.BasePickerView;
import com.wangzy.joker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangzy on 2017/6/2.
 */

public abstract class JokeTypeDialog extends BasePickerView {


    @BindView(R.id.textViewTextJoke)
    TextView textViewTextJoke;
    @BindView(R.id.textViewImageJoke)
    TextView textViewImageJoke;
    @BindView(R.id.textViewMp4Joke)
    TextView textViewMp4Joke;

    public JokeTypeDialog(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.dialog_joke_type, contentContainer);
        ButterKnife.bind(this, contentContainer);
    }

    @OnClick({R.id.textViewTextJoke, R.id.textViewImageJoke, R.id.textViewMp4Joke})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewTextJoke:
                onClickType("text",textViewTextJoke.getText().toString());
                break;
            case R.id.textViewImageJoke:
                onClickType("text_img",textViewImageJoke.getText().toString());
                break;
            case R.id.textViewMp4Joke:
                onClickType("mp4",textViewMp4Joke.getText().toString());
                break;
        }
        dismiss();
    }

    public abstract void onClickType(String type,String label);

}
