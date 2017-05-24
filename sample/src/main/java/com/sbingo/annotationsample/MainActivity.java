package com.sbingo.annotationsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sbingo.BindView;
import com.sbingo.api.ViewInjector;
import com.sbingo.runtime_annotation.RuntimeInjectUtils;
import com.sbingo.runtime_annotation.annotation.AttachView;
import com.sbingo.runtime_annotation.annotation.ContentView;
import com.sbingo.runtime_annotation.annotation.OnClick;
import com.sbingo.runtime_annotation.annotation.OnTouch;

/**
 * Author: Sbingo
 * Date:   2017/5/22
 */

@ContentView(R.layout.layout_main)
public class MainActivity extends AppCompatActivity {

    @AttachView(R.id.msg)
    private TextView msg;
    @AttachView(R.id.next)
    private Button next;
    @BindView(R.id.first)
    TextView first;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RuntimeInjectUtils.inject(this);
        msg.setText("运行时注解,反射注入");
        ViewInjector.injectView(this);
        first.setText("本View来自于编译时注解");
    }

    @OnClick({R.id.msg, R.id.next})
    public void onViewClicked(View v) {
        Log.d("main_activity", "onViewClicked");
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

    @OnTouch({R.id.msg, R.id.next})
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("main_activity", "onTouch");
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        switch (v.getId()) {
            case R.id.msg:
                Toast.makeText(this, "不要摸我", Toast.LENGTH_SHORT).show();
                break;
            case R.id.next:
                Toast.makeText(this, "摸你咋滴", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return false;
    }
}
