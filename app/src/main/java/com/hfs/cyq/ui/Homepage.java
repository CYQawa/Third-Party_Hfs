package com.hfs.cyq.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.hfs.cyq.R;
public class Homepage extends LinearLayout {
    private Context context;

    public Homepage(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.home_layout, this, true);
    }
}

