package com.speedata.earlabel.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.speedata.earlabel.R;
import com.speedata.utils.ToolCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :Reginer in  2017/9/22 16:01.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
class SetContentPopWindow extends PopupWindow {
    SetContentPopWindow(InforActivity context, IAddView iAddView, int id) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View mView = inflater.inflate(R.layout.view_set_content_layout, new LinearLayout(context), false);
        RecyclerView mRecyclerView = mView.findViewById(R.id.rv_contracts_filter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        SelectAdapter mAdapter = new SelectAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        List<SelectEntity> mList = new ArrayList<>();
        switch (id) {

            case R.id.av_assets_status: {

                SelectEntity selectEntity = new SelectEntity();
                selectEntity.setShow("大白");
                mList.add(selectEntity);

                selectEntity = new SelectEntity();
                selectEntity.setShow("长白");
                mList.add(selectEntity);

                selectEntity = new SelectEntity();
                selectEntity.setShow("杜洛克");
                mList.add(selectEntity);
            }
                break;

            case R.id.av_gy_name: {
                SelectEntity selectEntity = new SelectEntity();
                selectEntity.setShow("人工受精");
                mList.add(selectEntity);

                selectEntity = new SelectEntity();
                selectEntity.setShow("自然受精");
                mList.add(selectEntity);
            }
                break;

            default:
                break;
        }
        mAdapter.replaceData(mList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            iAddView.completeSelect(id, mList.get(position));
            dismiss();
        });
        this.setContentView(mView);
        this.setWidth(ToolCommon.dip2px(context, 200));
        this.setHeight(ToolCommon.dip2px(context, 300));
        this.setFocusable(true);
        this.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_pop));
        mView.setOnTouchListener((v, event) -> {
            dismiss();
            v.performClick();
            return true;
        });

    }
}
