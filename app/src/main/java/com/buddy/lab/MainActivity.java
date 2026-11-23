package com.buddy.lab;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView infoView;
    private TextView logView;
    private boolean compact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logs.init(this);

        compact = getResources().getDisplayMetrics().widthPixels
                < TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 360,
                getResources().getDisplayMetrics());

        Logs.d("screen compact mode = " + compact);
        setContentView(buildUi());
        refresh();
    }

    private View buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#0F1216"));
        int pad = dp(compact ? 8 : 14);
        root.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("BuddyLab");
        title.setTextColor(Color.parseColor("#7FD1B9"));
        title.setTextSize(compact ? 15f : 18f);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setPadding(0, 0, 0, dp(6));
        root.addView(title);

        infoView = new TextView(this);
        infoView.setTextColor(Color.parseColor("#9FB0C0"));
        infoView.setTextSize(compact ? 9f : 11f);
        infoView.setTypeface(Typeface.MONOSPACE);
        infoView.setPadding(0, 0, 0, dp(8));
        root.addView(infoView);

        ScrollView scroll = new ScrollView(this);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        scroll.setLayoutParams(sp);
        scroll.setBackgroundColor(Color.parseColor("#161B21"));

        logView = new TextView(this);
        logView.setTextColor(Color.parseColor("#C7D3DE"));
        logView.setTextSize(compact ? 8f : 10f);
        logView.setTypeface(Typeface.MONOSPACE);
        logView.setPadding(dp(8), dp(8), dp(8), dp(8));
        logView.setTextIsSelectable(true);
        scroll.addView(logView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(scroll);

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER);
        bar.setPadding(0, dp(8), 0, 0);

        bar.addView(button("复制", v -> {
            copyToClipboard(Logs.report(this));
            toast("报告已复制");
        }));
        bar.addView(button("分享", v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "BuddyLab report");
            i.putExtra(Intent.EXTRA_TEXT, Logs.report(this));
            startActivity(Intent.createChooser(i, "分享诊断报告"));
        }));
        bar.addView(button("刷新", v -> refresh()));
        bar.addView(button("清空", v -> {
            Logs.clear();
            refresh();
        }));
        bar.addView(button("崩溃测试", v -> {
            Logs.d("intentional crash triggered");
            throw new RuntimeException("intentional crash for log verification");
        }));
        root.addView(bar);

        return root;
    }

    private Button button(String text, View.OnClickListener l) {
        Button b = new Button(this);
        b.setText(text);
        b.setAllCaps(false);
        b.setTextSize(compact ? 10f : 12f);
        b.setMinWidth(0);
        b.setMinimumWidth(0);
        b.setPadding(dp(6), 0, dp(6), 0);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        lp.setMargins(dp(2), 0, dp(2), 0);
        b.setLayoutParams(lp);
        b.setOnClickListener(l);
        return b;
    }

    private void refresh() {
        infoView.setText(Logs.deviceInfo(this));
        String t = Logs.text();
        logView.setText(t.isEmpty() ? "(无日志)" : t);
    }

    private void copyToClipboard(String text) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("BuddyLab", text));
        }
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private int dp(int v) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v,
                getResources().getDisplayMetrics()));
    }
}
