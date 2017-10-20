package com.speedata.earlabel.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.lflibs.DeviceControl;
import com.android.lflibs.serial_native;
import com.elsw.base.utils.ToastUtil;
import com.speedata.earlabel.R;
import com.speedata.earlabel.base.BaseActivity;
import com.speedata.earlabel.db.bean.BaseInfor;
import com.speedata.earlabel.db.dao.BaseInforDao;

import java.io.IOException;
import java.util.Calendar;

public class InforActivity extends BaseActivity implements IAddView, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private DeviceControl DevCtrl;
    private static final String SERIALPORT_PATH = "/dev/ttyMT2";
    private static final int BUFSIZE = 64;

    private serial_native NativeDev;
    private Handler handler;
    private ReadThread reader;

    private int size = 0;
    private byte xor_result;
    private int count0 = 0;
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;
    private int count5 = 0;

    private TextEditView tevCardNumber; //卡号
    private TextEditView tevBreedingBoxNo; //饲养栏号
    private TextEditView tevVarieties; //品种
    private TextEditView tevPigAge; //猪龄
    private TextEditView tevWeight; //重量
    private TextEditView tevReproductiveNumber; //生育次数
    private TextEditView tevPigletQuantity; //仔猪数量

    private TextView tevEpidemicPreventionTime; //防疫时间
    private TextView tevDateOfFertilization; //受精日期

    private TextEditView tevFertilizationMode; //受精方式
    private TextEditView tevFertilizationCycle; //受精周期
    private TextEditView tevFeed; //饲料喂养

    private ToggleButton powerBtn; //一个寻卡按钮，在卡号栏下面
    private Button btnInput; //录入内容
    private Button	clearBtn; //清空录入信息

    private BaseInforDao baseInforDao; //数据库
    private Context context;
    private long firstTime = 0;
    private AlertDialog mDialog;

    private TextView mAvAssetsStatus;
    private TextView mAvGyName;



    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);

        initView();
        initTitle();
        //初始化
        NativeDev = new serial_native();
        if(NativeDev.OpenComPort(SERIALPORT_PATH) < 0)
        {
            //初始化失败
            powerBtn.setEnabled(false);
            ToastUtil.show(this, "初始化失败", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return;
        }
        try {
            DevCtrl = new DeviceControl("/sys/class/misc/mtgpio/pin");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            powerBtn.setEnabled(false);
            new AlertDialog.Builder(this).setTitle(R.string.DIA_ALERT).setMessage(R.string.DEV_OPEN_ERR).setPositiveButton(R.string.DIA_CHECK, (dialog, which) -> finish()).show();
        }

        //接收返回的信息
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1)
                {

                    powerBtn.setText("扫到耳标");

                    byte[] buf = (byte[]) msg.obj;
                    if(buf.length==30)
                    {
                        for(int a=1; a<27;a++)
                        {
                            xor_result^= buf[a];
                        }
                        String cnt =  new String(buf);
                        String[] serial_number= new String[30];
                        serial_number[9] = cnt.substring(1,2);
                        serial_number[8] = cnt.substring(2,3);
                        serial_number[7] = cnt.substring(3,4);
                        serial_number[6] = cnt.substring(4,5);
                        serial_number[5] = cnt.substring(5,6);
                        serial_number[4] = cnt.substring(6,7);
                        serial_number[3] = cnt.substring(7,8);
                        serial_number[2] = cnt.substring(8,9);
                        serial_number[1] = cnt.substring(9,10);
                        serial_number[0] = cnt.substring(10,11);
                        String reverse = serial_number[0]+serial_number[1]+serial_number[2]+serial_number[3]+serial_number[4]+serial_number[5]+serial_number[6]+serial_number[7]+serial_number[8]+serial_number[9];
                        long dec_first= Long.parseLong(reverse,16);
                        String string=Long.toString(dec_first);
                        size = string.length();
                        switch (size)
                        {
                            case 1 :// if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + "0"+ "0" + "0" + "0" + "0" + "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //   break;
                            }
                            break;
                            case 2 : // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec + "0" + "0" + "0" + "0" + "0" + "0"+ "0" + "0" + "0" + "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //   break;
                            }
                            break;
                            case 3: // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec+ "0" + "0" + "0" + "0" + "0" + "0"+ "0" + "0" + "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                // break;
                            }
                            break;
                            case 4:   //if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec+ "0" + "0" + "0" + "0" + "0" + "0"+ "0" + "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //  break;
                            }
                            break;
                            case 5:  // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec+ "0" + "0" + "0" + "0" + "0" + "0"+ "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //  break;
                            }
                            break;
                            case 6:  // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec+ "0" + "0" + "0" + "0" + "0" + "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //  break;
                            }
                            break;
                            case 7:   // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec+ "0" + "0" + "0" + "0" + "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                // break;
                            }
                            break;
                            case 8:   // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec+ "0" + "0" + "0" + "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //  break;
                            }
                            break;
                            case 9:   // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec+ "0" + "0" + "0"+string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //  break;
                            }
                            break;
                            case 10:  // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec+ "0" + "0" +string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //	  break;
                            }
                            break;
                            case 11:   //if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec + "0" + string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //	  break;
                            }
                            break;
                            case 12:   // if (xor_result==buf[27])
                            {
                                serial_number[10] = cnt.substring(14,15);
                                serial_number[11] = cnt.substring(13,14);
                                serial_number[12] = cnt.substring(12,13);
                                serial_number[13] = cnt.substring(11,12);
                                String country_code = serial_number[11]+serial_number[12]+serial_number[13];
                                long dec_result=Long.parseLong(country_code,16);
                                String second_dec=Long.toString(dec_result);
                                String combine = second_dec + string ;
                                tevCardNumber.setViewContent(combine);
                                stopScan();
                                //  break;
                            }
                            break;

                            default:
                                break;
                        }
                    }

                    else{
                        String cnt =  new String(buf);
                        count0=Integer.parseInt(cnt.substring(1, 3),16);
                        count1=Integer.parseInt(cnt.substring(3, 5),16);
                        count2=Integer.parseInt(cnt.substring(5, 7),16);
                        count3=Integer.parseInt(cnt.substring(7, 9),16);
                        count4=Integer.parseInt(cnt.substring(9, 11),16);
                        count5=count0^count1^count2^count3^count4;
                        byte[] b= new byte[4];
                        b[0] = (byte) (count5 & 0xff );
                        if(b[0]==buf[11])
                        {
                            String jieguo = cnt.substring(1,cnt.length()-2);
                            tevCardNumber.setViewContent(jieguo);
                            stopScan();
                        }
                    }
                }
            }
        };


    }

    private void initTitle() {
        initTitle("信息录入", true, v -> finish());
    }

    private void initView() {
        //初始化显示
        tevCardNumber = findViewById(R.id.card_number);
        tevCardNumber.setViewTitle(R.string._tevCardNumber);
        tevCardNumber.setViewEnable(false);

        tevBreedingBoxNo = findViewById(R.id.breeding_box_no);
        tevBreedingBoxNo.setViewTitle(R.string._tevBreedingBoxNo);

        tevVarieties = findViewById(R.id.varieties);
        tevVarieties.setViewTitle(R.string._tevVarieties);
        tevVarieties.setVisibility(View.GONE);

        tevPigAge = findViewById(R.id.pig_age);
        tevPigAge.setViewTitle(R.string._tevPigAge);

        tevWeight = findViewById(R.id.weight);
        tevWeight.setViewTitle(R.string._tevWeight);

        tevReproductiveNumber = findViewById(R.id.reproductive_number);
        tevReproductiveNumber.setViewTitle(R.string._tevReproductiveNumber);

        tevPigletQuantity = findViewById(R.id.piglet_quantity);
        tevPigletQuantity.setViewTitle(R.string._tevPigletQuantity);

        tevEpidemicPreventionTime = findViewById(R.id.epidemic_prevention_time);
        tevEpidemicPreventionTime.setOnClickListener(this); //防疫时间

        tevDateOfFertilization = findViewById(R.id.date_of_fertilization);
        tevDateOfFertilization.setOnClickListener(this); //受精日期

        tevFertilizationMode = findViewById(R.id.fertilization_mode);
        tevFertilizationMode.setViewTitle(R.string._tevFertilizationMode);
        tevFertilizationMode.setVisibility(View.GONE);

        tevFertilizationCycle = findViewById(R.id.fertilization_cycle);
        tevFertilizationCycle.setViewTitle(R.string._tevFertilizationCycle);

        tevFeed = findViewById(R.id.feed);
        tevFeed.setViewTitle(R.string._tevFeed);

        powerBtn = findViewById(R.id.toggleButton_power);
        powerBtn.setOnCheckedChangeListener(this);
        powerBtn.setText("扫码按钮");
        powerBtn.setTextOn("扫码");
        powerBtn.setTextOff("扫码");

        btnInput = findViewById(R.id.btn_clear);
        btnInput.setOnClickListener(this);

        clearBtn = findViewById(R.id.btn_input);
        clearBtn.setOnClickListener(this);

        context = InforActivity.this;
        baseInforDao = new BaseInforDao(context);


        mAvAssetsStatus = findViewById(R.id.av_assets_status);
        mAvAssetsStatus.setOnClickListener(this);
        mAvGyName = findViewById(R.id.av_gy_name);
        mAvGyName.setOnClickListener(this);

        xor_result = 0;

        // 创建清空显示的确认对话框
        DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
        mDialog = new AlertDialog.Builder(this)
                .setTitle("清空输入内容")
                .setMessage("确定要清空页面内所有内容？")
                .setCancelable(false)
                .setNeutralButton("确定", dialogButtonOnClickListener)
                .setPositiveButton(R.string.out_miss, dialogButtonOnClickListener)
                .create();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        //切换就寻卡
            try {
                DevCtrl.PowerOnDevice();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
                NativeDev.ClearBuffer();
                reader = new ReadThread();
                reader.start();
                ToastUtil.show(this,"开启中......",ToastUtil.TOAST_SHOW_SHORT_TIME);
//				contView.setText(" status is " + powerBtn.isChecked());
            } catch (IOException e) {
                ToastUtil.show(this, "操作设备失败", ToastUtil.TOAST_SHOW_SHORT_TIME);
            }


    }

    @Override
    public void onClick(View v) {
        SetContentPopWindow popWindow;
        switch (v.getId()){

            case R.id.av_assets_status:
                popWindow = new SetContentPopWindow(this, this, R.id.av_assets_status);
                popWindow.showAtLocation(mAvAssetsStatus, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.av_gy_name:
                popWindow = new SetContentPopWindow(this, this, R.id.av_gy_name);
                popWindow.showAtLocation(mAvGyName, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.btn_clear:

                mDialog.show();

                break;

            case R.id.btn_input:

                commit(tevCardNumber.getViewContent(), tevBreedingBoxNo.getViewContent(),
                        //tevVarieties.getViewContent(),
                        mAvAssetsStatus.getText().toString(),
                        tevPigAge.getViewContent(), tevWeight.getViewContent(), tevReproductiveNumber.getViewContent(),
                        tevPigletQuantity.getViewContent(), tevEpidemicPreventionTime.getText().toString(),
                        tevDateOfFertilization.getText().toString(),
                        //tevFertilizationMode.getViewContent(),
                        mAvGyName.getText().toString(),
                        tevFertilizationCycle.getViewContent(), tevFeed.getViewContent());

                break;

            case R.id.epidemic_prevention_time:

                showDataDialog(R.id.epidemic_prevention_time);
                break;

            case R.id.date_of_fertilization:

                showDataDialog(R.id.date_of_fertilization);
                break;

            default:
                break;

        }
    }

    private void commit(String cardNumber, String breedingBoxNo, String varieties, String pigAge, String weight,
                        String reproductiveNumber, String pigletQuantity, String epidemicPreventionTime, String dateOfFertilization,
                        String fertilizationMode, String fertilizationCycle, String feed) {
        if (!submit(cardNumber, breedingBoxNo, varieties, pigAge, weight,
                reproductiveNumber, pigletQuantity, epidemicPreventionTime, dateOfFertilization,
                fertilizationMode, fertilizationCycle, feed)) {
            return;
        }
        BaseInfor baseInfor = new BaseInfor();
        baseInfor.setCardNumber(cardNumber);
        baseInfor.setBreedingBoxNo(breedingBoxNo);
        baseInfor.setVarieties(varieties);
        baseInfor.setPigAge(pigAge);
        baseInfor.setWeight(weight);
        baseInfor.setReproductiveNumber(reproductiveNumber);
        baseInfor.setPigletQuantity(pigletQuantity);
        baseInfor.setEpidemicPreventionTime(epidemicPreventionTime);
        baseInfor.setDateOfFertilization(dateOfFertilization);
        baseInfor.setFertilizationMode(fertilizationMode);
        baseInfor.setFertilizationCycle(fertilizationCycle);
        baseInfor.setFeed(feed);

        //此时 baseInfor是要保存的信息,如果标签已存在，则先删除再添加
        baseInforDao.imDelete("CardNumber=?", new String[]{cardNumber});
        baseInforDao.imInsert(baseInfor);
        ToastUtil.show(this, "数据保存成功", ToastUtil.TOAST_SHOW_SHORT_TIME);
    }

    @Override
    public void completeSelect(int id, SelectEntity content) {

        switch (id) {
            case R.id.av_assets_status:

                mAvAssetsStatus.setText(content.getShow());
                break;
            case R.id.av_gy_name:

                mAvGyName.setText(content.getShow());
                break;

            default:
                break;
        }


    }

    class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            Log.d("lfrfid", "thread start");
            while(!isInterrupted()) {
                byte[] buf = NativeDev.ReadPort(BUFSIZE);
                if(buf != null)
                {
                    Message msg = new Message();
/*					for(byte a: buf)
					{
						Log.d("lfrfid", String.format("%02x", a));
					}*/

                    if(buf.length >= 2)
                    {
                        size = 0;
                        msg.what = 1;
                        msg.obj = buf;
                        handler.sendMessage(msg);
                    }
                }
            }
            Log.d("lfrfid", "thread stop");
        }
    }

    @Override
    protected void onDestroy() {
       //去除判断，直接尝试关闭
        String text = powerBtn.getText().toString();
        if ("扫码".equals(text)){
            try {
                reader.interrupt();
                DevCtrl.PowerOffDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        NativeDev.CloseComPort();
        super.onDestroy();
    }

    /**
     * 显示日期选择器
     */
    private void showDataDialog(int id) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(InforActivity.this, AlertDialog.THEME_HOLO_LIGHT, (view, year, month, day) -> {
            switch (id) {
                case R.id.epidemic_prevention_time:
                    tevEpidemicPreventionTime.setText(String.valueOf(getString(R.string.year_month_day, year, (++month), day)));
                    break;
                case R.id.date_of_fertilization:
                    tevDateOfFertilization.setText(String.valueOf(getString(R.string.year_month_day, year, (++month), day)));
                    break;

                default:
                    break;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private boolean submit(String cardNumber, String breedingBoxNo, String varieties, String pigAge, String weight,
                           String reproductiveNumber, String pigletQuantity, String epidemicPreventionTime, String dateOfFertilization,
                           String fertilizationMode, String fertilizationCycle, String feed) {
        if (TextUtils.isEmpty(cardNumber)) {
            ToastUtil.show(this, "没有卡号，请扫描标签", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(breedingBoxNo)) {
            ToastUtil.show(this, "请填写饲养栏号", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(varieties)) {
            ToastUtil.show(this, "请填写品种", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(pigAge)) {
            ToastUtil.show(this, "请填写猪龄", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(weight)) {
            ToastUtil.show(this, "请填写重量", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(reproductiveNumber)) {
            ToastUtil.show(this, "请填写生育次数", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(pigletQuantity)) {
            ToastUtil.show(this, "请填写仔猪数量", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(epidemicPreventionTime)) {
            ToastUtil.show(this, "请填写防疫时间", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(dateOfFertilization)) {
            ToastUtil.show(this, "请填写受精日期", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(fertilizationMode)) {
            ToastUtil.show(this, "请填写受精方式", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(fertilizationCycle)) {
            ToastUtil.show(this, "请填写受精周期", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(feed)) {
            ToastUtil.show(this, "请填写饲料喂养", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return false;
        }

        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出当前页面", Toast.LENGTH_SHORT).show();
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

    /**
     * 退出时的对话框的按钮点击事件
     */
    private class DialogButtonOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: // 取消
                    // 取消显示对话框
                    mDialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEUTRAL: // 清空显示

                    tevCardNumber.setViewContent("");
                    tevBreedingBoxNo.setViewContent("");
                   // tevVarieties.setViewContent("");
                    tevPigAge.setViewContent("");
                    tevWeight.setViewContent("");
                    tevReproductiveNumber.setViewContent("");
                    tevPigletQuantity.setViewContent("");
                    tevEpidemicPreventionTime.setText("");
                    tevDateOfFertilization.setText("");
                  //  tevFertilizationMode.setViewContent("");
                    tevFertilizationCycle.setViewContent("");
                    tevFeed.setViewContent("");
                    mAvAssetsStatus.setText("");
                    mAvGyName.setText("");

                    break;

                default:
                    break;
            }
        }
    }



    private void stopScan(){
        try {
            reader.interrupt();
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            DevCtrl.PowerOffDevice();
//				contView.setText(" status is " + powerBtn.isChecked());
        } catch (IOException e) {
            ToastUtil.show(this, "操作设备失败", ToastUtil.TOAST_SHOW_SHORT_TIME);
        }
    }





}
