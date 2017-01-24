package com.safframework.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectViews;

/**
 * Created by Tony Shen on 2017/1/24.
 */

public class TestInjectViews extends Activity {

    @InjectViews(ids={R.id.text1,R.id.text2,R.id.text3,R.id.text4})
    TextView[] text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_injectviews);
        Injector.injectInto(this);

        text[0].setText("this is test1");
        text[1].setText("this is test2");
        text[2].setText("this is test3");
        text[3].setText("this is test4");
    }
}
