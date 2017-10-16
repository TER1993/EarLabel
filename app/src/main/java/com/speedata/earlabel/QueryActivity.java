package com.speedata.earlabel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.lflibs.DeviceControl;
import com.android.lflibs.serial_native;
import com.elsw.base.utils.ToastUtil;
import com.speedata.earlabel.base.BaseActivity;
import com.speedata.earlabel.db.bean.BaseInfor;
import com.speedata.earlabel.db.dao.BaseInforDao;
import com.speedata.earlabel.widget.TextEditView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private DeviceControl DevCtrl;
    private static final String SERIALPORT_PATH = "/dev/ttyMT2";
    private static final int BUFSIZE = 64;

    private serial_native NativeDev;
    private Handler handler;
    private ReadThread reader;

    private int size = 0;
    private byte xor_result = 0;
    private int count0 = 0;
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;
    private int count5 = 0;

    private TextEditView tevCardNumber; //卡号
    private ToggleButton powerBtn; //一个寻卡按钮，在卡号栏下面


    private TextEditView tevBreedingBoxNo; //饲养栏号
    private TextEditView tevVarieties; //品种
    private TextEditView tevPigAge; //猪龄
    private TextEditView tevWeight; //重量
    private TextEditView tevReproductiveNumber; //生育次数
    private TextEditView tevPigletQuantity; //仔猪数量
    private TextEditView tevEpidemicPreventionTime; //防疫时间
    private TextEditView tevDateOfFertilization; //受精日期

    private TextEditView tevFertilizationMode; //受精方式
    private TextEditView tevFertilizationCycle; //受精周期
    private TextEditView tevFeed; //饲料喂养


    private TextView tvShow; //显示查询结果

    private BaseInforDao baseInforDao;
    private Context context;
    private List<BaseInfor> baseInfors;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        initView();
        initTitle();
        //初始化
        NativeDev = new serial_native();
        if(NativeDev.OpenComPort(SERIALPORT_PATH) < 0)
        {
            //初始化失败
            ToastUtil.show(this, "初始化失败", ToastUtil.TOAST_SHOW_SHORT_TIME);
            return;
        }
        try {
            DevCtrl = new DeviceControl("/sys/class/misc/mtgpio/pin");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
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
                                // 查询扫到后再查
                                stopScan();
                                showMessage();
                                //  break;
                            }
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
                            // 查询扫到后再查
                            stopScan();
                            showMessage();
                        }
                    }
                }
            }
        };


    }

    private void initTitle() {
        initTitle("信息查询", true, v -> finish());
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        //初始化显示
        tevCardNumber = findViewById(R.id.card_number);
        tevCardNumber.setViewTitle(R.string._tevCardNumber);
        tevCardNumber.setViewEnable(false);

        tvShow = findViewById(R.id.tv_show);

        powerBtn = findViewById(R.id.toggleButton_power);
        powerBtn.setOnCheckedChangeListener(this);
        powerBtn.setText("扫码按钮");
        powerBtn.setTextOn("扫码");
        powerBtn.setTextOff("扫码");


        tevBreedingBoxNo = findViewById(R.id.breeding_box_no);
        tevBreedingBoxNo.setViewTitle(R.string._tevBreedingBoxNo);
        tevBreedingBoxNo.setViewEnable(false);

        tevVarieties = findViewById(R.id.varieties);
        tevVarieties.setViewTitle(R.string._tevVarieties);
        tevVarieties.setViewEnable(false);

        tevPigAge = findViewById(R.id.pig_age);
        tevPigAge.setViewTitle(R.string._tevPigAge);
        tevPigAge.setViewEnable(false);

        tevWeight = findViewById(R.id.weight);
        tevWeight.setViewTitle(R.string._tevWeight);
        tevWeight.setViewEnable(false);

        tevReproductiveNumber = findViewById(R.id.reproductive_number);
        tevReproductiveNumber.setViewTitle(R.string._tevReproductiveNumber);
        tevReproductiveNumber.setViewEnable(false);

        tevPigletQuantity = findViewById(R.id.piglet_quantity);
        tevPigletQuantity.setViewTitle(R.string._tevPigletQuantity);
        tevPigletQuantity.setViewEnable(false);

        tevEpidemicPreventionTime = findViewById(R.id.epidemic_prevention_time);
        tevEpidemicPreventionTime.setViewTitle(R.string._tevEpidemicPreventionTime); //防疫时间
        tevEpidemicPreventionTime.setViewEnable(false);

        tevDateOfFertilization = findViewById(R.id.date_of_fertilization);
        tevDateOfFertilization.setViewTitle(R.string._tevDateOfFertilization); //受精日期
        tevDateOfFertilization.setViewEnable(false);

        tevFertilizationMode = findViewById(R.id.fertilization_mode);
        tevFertilizationMode.setViewTitle(R.string._tevFertilizationMode);
        tevFertilizationMode.setViewEnable(false);

        tevFertilizationCycle = findViewById(R.id.fertilization_cycle);
        tevFertilizationCycle.setViewTitle(R.string._tevFertilizationCycle);
        tevFertilizationCycle.setViewEnable(false);

        tevFeed = findViewById(R.id.feed);
        tevFeed.setViewTitle(R.string._tevFeed);
        tevFeed.setViewEnable(false);


        context = QueryActivity.this;
        baseInforDao = new BaseInforDao(context);
        baseInfors = new ArrayList<>();
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

    private void showMessage() {

        String cardNumber = tevCardNumber.getViewContent();
        if ("".equals(cardNumber)){
            tvShow.setText("没有搜到标签");
            return;
        }

        baseInfors = baseInforDao.imQueryList("CardNumber=?", new String[]{cardNumber});
        if (baseInfors.size() == 0){
            tvShow.setText("数据库中没有当前标签对应的信息");

            //tevCardNumber.setViewContent("");
            tevBreedingBoxNo.setViewContent("");
            tevVarieties.setViewContent("");
            tevPigAge.setViewContent("");
            tevWeight.setViewContent("");
            tevReproductiveNumber.setViewContent("");
            tevPigletQuantity.setViewContent("");
            tevEpidemicPreventionTime.setViewContent("");
            tevDateOfFertilization.setViewContent("");
            tevFertilizationMode.setViewContent("");
            tevFertilizationCycle.setViewContent("");
            tevFeed.setViewContent("");

            return;
        }

        BaseInfor baseInfor = baseInfors.get(0);


//        String message = "卡号: " + baseInfor.getCardNumber() + "\n\n饲养栏号: " +baseInfor.getBreedingBoxNo()
//                + "\n\n品种: " + baseInfor.getVarieties() + "\n\n猪龄: " + baseInfor.getPigAge()
//                + "\n\n重量: " + baseInfor.getWeight() + "\n\n生育次数: " + baseInfor.getReproductiveNumber()
//                + "\n\n仔猪数量: " + baseInfor.getPigletQuantity() + "\n\n防疫时间: " + baseInfor.getEpidemicPreventionTime()
//                + "\n\n受精日期: " + baseInfor.getDateOfFertilization() + "\n\n受精方式: " + baseInfor.getFertilizationMode()
//                + "\n\n受精周期: " + baseInfor.getFertilizationCycle() + "\n\n饲料喂养: " + baseInfor.getFeed();

        String message = "显示标签对应信息";

        tvShow.setText(message);

        //tevCardNumber.setViewContent(baseInfor.getCardNumber());
        tevBreedingBoxNo.setViewContent(baseInfor.getBreedingBoxNo());
        tevVarieties.setViewContent(baseInfor.getVarieties());
        tevPigAge.setViewContent(baseInfor.getPigAge());
        tevWeight.setViewContent(baseInfor.getWeight());
        tevReproductiveNumber.setViewContent(baseInfor.getReproductiveNumber());
        tevPigletQuantity.setViewContent(baseInfor.getPigletQuantity());
        tevEpidemicPreventionTime.setViewContent(baseInfor.getEpidemicPreventionTime());
        tevDateOfFertilization.setViewContent(baseInfor.getDateOfFertilization());
        tevFertilizationMode.setViewContent(baseInfor.getFertilizationMode());
        tevFertilizationCycle.setViewContent(baseInfor.getFertilizationCycle());
        tevFeed.setViewContent(baseInfor.getFeed());



    }

    class ReadThread extends Thread {
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
