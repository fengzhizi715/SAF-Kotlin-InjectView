package com.safframework.app.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.safframework.app.R;
import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.injectview.annotations.OnClick;

/**
 * Created by Tony Shen on 2017/1/25.
 */

public class TitleView extends FrameLayout {

    @InjectView(R.id.button_left)
    Button leftButton;

    @InjectView(R.id.title_text)
    TextView titleText;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Injector.injectInto(this,LayoutInflater.from(context).inflate(R.layout.view_title, this));
    }

    @OnClick(id={R.id.button_left})
    void clickLeftButton() {

        ((Activity) getContext()).finish();
    }

    public void setTitleText(String text) {
        titleText.setText(text);
    }

    public void setLeftButtonText(String text) {
        leftButton.setText(text);
    }

    public void setLeftButtonListener(OnClickListener l) {
        leftButton.setOnClickListener(l);
    }

}