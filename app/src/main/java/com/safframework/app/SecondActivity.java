package com.safframework.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectExtra;

/**
 * Created by Tony Shen on 2017/1/24.
 */

public class SecondActivity extends Activity {

    @InjectExtra(key="param")
    String param;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Injector.injectInto(this);

        if (param!=null) {
            Toast.makeText(SecondActivity.this,"param1="+param,Toast.LENGTH_SHORT).show();
        }
    }
}
