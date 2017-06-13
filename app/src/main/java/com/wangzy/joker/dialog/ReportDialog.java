package com.wangzy.joker.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bigkoo.pickerview.view.BasePickerView;
import com.wangzy.joker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangzy on 2017/6/2.
 */

public abstract class ReportDialog extends BasePickerView {


    @BindView(R.id.textViewTextJoke)
    TextView textViewTextJoke;
    @BindView(R.id.textViewImageJoke)
    TextView textViewImageJoke;
    @BindView(R.id.textViewMp4Joke)
    TextView textViewMp4Joke;
    @BindView(R.id.textViewReportBad)
    TextView textViewReportBad;
    @BindView(R.id.textViewReportBreak)
    TextView textViewReportBreak;
    @BindView(R.id.editTextReportContent)
    EditText editTextReportContent;
    @BindView(R.id.buttonReport)
    Button buttonReport;

    private AVObject avObject;

    public ReportDialog(Context context, AVObject jokeObject) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.dialog_report, contentContainer);
        ButterKnife.bind(this, contentContainer);

        this.avObject = jokeObject;


    }


    @OnClick({R.id.textViewReportBad, R.id.textViewReportBreak, R.id.buttonReport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewReportBad:
                break;
            case R.id.textViewReportBreak:
                break;
            case R.id.buttonReport:
                break;
        }
    }

    private void report(AVObject joke) {
        if (null == joke) {

        }
    }


}
