package com.safframework.app;

import android.os.Bundle;

/**
 * Created by Tony Shen on 2017/1/24.
 */

public class FragmentActivity extends android.support.v4.app.FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }
}
