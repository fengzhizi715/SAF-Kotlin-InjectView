package com.safframework.app;

import android.app.Activity;
import android.os.Bundle;

import com.safframework.app.ui.TitleView;
import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;

/**
 * Created by Tony Shen on 2017/1/25.
 */

public class TestViewActivity extends Activity {

    @InjectView(R.id.title_view)
    TitleView titleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        Injector.injectInto(this);

        titleView.setTitleText("title");
    }
}
