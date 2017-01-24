package com.safframework.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.injectview.annotations.OnClick;

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

        textView.setText("this is a textview!");
    }

    @OnClick(id={R.id.text})
    void clickText() {

        Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
    }
}
