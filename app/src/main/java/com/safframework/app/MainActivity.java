package com.safframework.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;

/**
 * Created by Tony Shen on 2017/1/24.
 */

public class MainActivity extends Activity {

    @InjectView(R.id.text)
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Injector.injectInto(this);

//        textView.setText("this is a textview!");
    }
}
