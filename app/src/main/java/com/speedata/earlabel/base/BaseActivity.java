package com.speedata.earlabel.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.speedata.earlabel.R;
import com.speedata.earlabel.widget.StatusBarUtil;


/**
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━
 *
 * @author Reginer on  2016/10/9 9:15.
 *         Description:Activity基类
 */
public class BaseActivity extends Activity {

    protected TextView mBarTitle;
    protected ImageView mBarLeft;
    protected ImageView mBarRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme));
        StatusBarUtil.setTranslucent(this);
    }


    /**
     * 这是导航
     *
     * @param left  左侧图标
     * @param title 标题
     */
    protected void setNavigation(int left, int title) {
        mBarTitle = (TextView) findViewById(R.id.tv_bar_title);
        if (title != 0) {
            mBarTitle.setText(title);
        }
    }

    /**
     * 初始化标题
     */
    public void initTitle(String title, boolean leftVis, View.OnClickListener lis) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvTitle.setText(title);
        ImageView imgExit = (ImageView) findViewById(R.id.iv_left);
        if (leftVis) {
            imgExit.setVisibility(View.VISIBLE);
            imgExit.setImageDrawable(getResources().getDrawable(R.drawable.icon_back));
        }
        else
            imgExit.setVisibility(View.INVISIBLE);
        imgExit.setOnClickListener(lis);

    }

    /**
     * 这是导航
     *
     * @param left  左侧图标
     * @param title 标题
     */
    protected void setNavigation(int left, String title) {
        mBarTitle = (TextView) findViewById(R.id.tv_bar_title);
        mBarLeft = (ImageView) findViewById(R.id.iv_left);
        if (!TextUtils.isEmpty(title)) {
            mBarTitle.setText(title);
        }
        mBarLeft.setVisibility(left == 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * 这是导航
     *
     * @param right  左侧图标
     */
    protected void setRightNavigation(int right,View.OnClickListener lis) {
        mBarRight = (ImageView) findViewById(R.id.iv_left);
        mBarRight.setVisibility(View.VISIBLE);

        mBarRight.setVisibility(right == 0 ? View.GONE : View.VISIBLE);
        mBarRight.setOnClickListener(lis);
    }


}