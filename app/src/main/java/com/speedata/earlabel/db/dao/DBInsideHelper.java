package com.speedata.earlabel.db.dao;

import android.content.Context;

import com.elsw.base.db.orm.AbDBHelper;
import com.speedata.earlabel.db.bean.BaseInfor;


/**
 * Copyright (c) 2012 All rights reserved 名称：DBInsideHelper.java
 * 描述：手机data/data下面的数据库
 *
 * @author zhaoqp
 * @version v1.0
 * @date：2013-7-31 下午3:50:18
 */
public class DBInsideHelper extends AbDBHelper {
    // 数据库名
    private static final String DBNAME = "test.db";

    // 当前数据库的版本
    private static final int DBVERSION = 2;

    // 要初始化的表
    private static final Class<?>[] clazz = {BaseInfor.class};

    public DBInsideHelper(Context context) {
        super(context, DBNAME, null, DBVERSION, clazz);
    }
}
