package com.safframework.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.safframework.app.R;
import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.injectview.annotations.OnClick;

/**
 * Created by Tony Shen on 2017/1/24.
 */

public class CustomerDialog extends Dialog {

    @InjectView(R.id.dialog_textView)
    TextView textView;

    Context context;

    public CustomerDialog(Context context) {
        super(context);
        this.context = context;
    }
    public CustomerDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.customer_dialog);
        Injector.injectInto(this);

        textView.setText("this textview is for customer dialog!");
    }


    @OnClick(id={R.id.dialog_button})
    void clickText1() {

        dismiss();
    }
}
