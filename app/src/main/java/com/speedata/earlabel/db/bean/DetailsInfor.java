package com.speedata.earlabel.db.bean;

import com.elsw.base.db.orm.annotation.Column;
import com.elsw.base.db.orm.annotation.Table;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Table(name = "DetailsInfor")
public  class DetailsInfor { //保存的详细仔猪数量信息及多次的防疫时间信息

//    CardNumber; //卡号
//    BreedingBoxNo; //饲养栏号
//    Varieties; //品种
//    PigAge; //猪龄
//    Weight; //重量
//    ReproductiveNumber; //生育次数
//    PigletQuantity; //仔猪数量
//    EpidemicPreventionTime; //防疫时间
//    DateOfFertilization; //受精日期
//    FertilizationMode; //受精方式
//    FertilizationCycle; //受精周期
//    Feed; //饲料喂养



    @Column(name = "CardNumber")
    private String CardNumber; //卡号,唯一键值用于关联相关信息


    @Column(name = "ReproductiveNumber")
    private String ReproductiveNumber; //生育次数


    @Column(name = "PigletQuantity")
    private List<String> PigletQuantity; //仔猪数量


    @Column(name = "EpidemicPreventionTime")
    private List<String> EpidemicPreventionTime; //防疫时间

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getReproductiveNumber() {
        return ReproductiveNumber;
    }

    public void setReproductiveNumber(String reproductiveNumber) {
        ReproductiveNumber = reproductiveNumber;
    }

    public List<String> getPigletQuantity() {
        return PigletQuantity;
    }

    public void setPigletQuantity(List<String> pigletQuantity) {
        PigletQuantity = pigletQuantity;
    }

    public List<String> getEpidemicPreventionTime() {
        return EpidemicPreventionTime;
    }

    public void setEpidemicPreventionTime(List<String> epidemicPreventionTime) {
        EpidemicPreventionTime = epidemicPreventionTime;
    }

    @Override
    public String toString() {
        return "DetailsInfor{" +
                "CardNumber='" + CardNumber + '\'' +
                ", ReproductiveNumber='" + ReproductiveNumber + '\'' +
                ", PigletQuantity=" + PigletQuantity +
                ", EpidemicPreventionTime=" + EpidemicPreventionTime +
                '}';
    }
}
