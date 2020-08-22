package com.example.cidianl;

import android.content.Context;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
    }

    public void text() {
        setText("123");
    }
}
