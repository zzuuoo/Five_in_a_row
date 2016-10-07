package com.yezi.zuo.five_in_a_row;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button replace;
    private Button quit;
    private Button step;
    public static TextView tv;
    private wuzi wu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        replace =(Button)findViewById(R.id.replace);
        quit =(Button)findViewById(R.id.quit);
        tv =(TextView)findViewById(R.id.textView);
        step = (Button)findViewById(R.id.step);
        step.setOnClickListener(this);
        quit.setOnClickListener(this);
        replace.setOnClickListener(this);
        wu = (wuzi)findViewById(R.id.view);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.quit:
                finish();
                break;
            case R.id.replace:
                wu.reStart();
//                tv.setText("点击棋盘即更新布局并重新开始");
//                Toast.makeText(this,"点击棋盘即更新布局",Toast.LENGTH_SHORT).show();
                break;
            case R.id.step:
                wu.quit_step();
                break;
            default:
                break;

        }
    }
}
