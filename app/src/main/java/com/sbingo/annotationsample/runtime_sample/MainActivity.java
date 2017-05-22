package com.sbingo.annotationsample.runtime_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sbingo.annotationsample.R;
import com.sbingo.runtime_annotation.AttachView;
import com.sbingo.runtime_annotation.ContentView;
import com.sbingo.runtime_annotation.RuntimeInjectUtils;

/**
 * Author: Sbingo
 * Date:   2017/5/22
 */

@ContentView(R.layout.layout_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @AttachView(R.id.msg)
    TextView msg;
    @AttachView(R.id.next)
    Button next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RuntimeInjectUtils.inject(this);
        msg.setText("运行时注解,反射注入");
        msg.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msg:
                Toast.makeText(this, "不要说话", Toast.LENGTH_SHORT).show();
                break;
            case R.id.next:
                Toast.makeText(this, "就要说话", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }
}
