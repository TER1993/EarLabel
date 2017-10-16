package com.speedata.earlabel;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.speedata.earlabel.base.BaseActivity;
import com.speedata.earlabel.widget.InforActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private long firstTime = 0; //退出时做判断的时间对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTitle("动物耳标管理",false,null);
    }

    private void initView() {

        findViewById(R.id.iv_write).setOnClickListener(this);
        findViewById(R.id.iv_check).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_write:
                Intent intent1 = new Intent(MainActivity.this, InforActivity.class);
                startActivity(intent1);
                break;

            case R.id.iv_check:
                Intent intent2 = new Intent(MainActivity.this, QueryActivity.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime; //更新firstTime
                    return true;
                } else {                    //两次按键小于2秒时，退出应用
                    finish();
                }
                break;

            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}
