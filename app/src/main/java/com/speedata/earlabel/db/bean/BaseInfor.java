package com.speedata.earlabel.db.bean;

import com.elsw.base.db.orm.annotation.Column;
import com.elsw.base.db.orm.annotation.Table;

/**
 * Created by Administrator on 2017/8/22.
 */
@Table(name = "BaseInfor")
public  class BaseInfor { //保存的信息

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
    private String CardNumber; //卡号


    @Column(name = "BreedingBoxNo")
    private String BreedingBoxNo; //饲养栏号


    @Column(name = "Varieties")
    private String Varieties; //品种


    @Column(name = "PigAge")
    private String PigAge; //猪龄


    @Column(name = "Weight")
    private String Weight; //重量


    @Column(name = "ReproductiveNumber")
    private String ReproductiveNumber; //生育次数


    @Column(name = "PigletQuantity")
    private String PigletQuantity; //仔猪数量


    @Column(name = "EpidemicPreventionTime")
    private String EpidemicPreventionTime; //防疫时间


    @Column(name = "DateOfFertilization")
    private String DateOfFertilization; //受精日期


    @Column(name = "FertilizationCycle")
    private String FertilizationCycle; //受精方式


    @Column(name = "FertilizationMode")
    private String FertilizationMode; //受精周期


    @Column(name = "Feed")
    private String Feed; //饲料喂养


    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getBreedingBoxNo() {
        return BreedingBoxNo;
    }

    public void setBreedingBoxNo(String breedingBoxNo) {
        BreedingBoxNo = breedingBoxNo;
    }

    public String getVarieties() {
        return Varieties;
    }

    public void setVarieties(String varieties) {
        Varieties = varieties;
    }

    public String getPigAge() {
        return PigAge;
    }

    public void setPigAge(String pigAge) {
        PigAge = pigAge;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getReproductiveNumber() {
        return ReproductiveNumber;
    }

    public void setReproductiveNumber(String reproductiveNumber) {
        ReproductiveNumber = reproductiveNumber;
    }

    public String getPigletQuantity() {
        return PigletQuantity;
    }

    public void setPigletQuantity(String pigletQuantity) {
        PigletQuantity = pigletQuantity;
    }

    public String getEpidemicPreventionTime() {
        return EpidemicPreventionTime;
    }

    public void setEpidemicPreventionTime(String epidemicPreventionTime) {
        EpidemicPreventionTime = epidemicPreventionTime;
    }

    public String getDateOfFertilization() {
        return DateOfFertilization;
    }

    public void setDateOfFertilization(String dateOfFertilization) {
        DateOfFertilization = dateOfFertilization;
    }

    public String getFertilizationCycle() {
        return FertilizationCycle;
    }

    public void setFertilizationCycle(String fertilizationCycle) {
        FertilizationCycle = fertilizationCycle;
    }

    public String getFertilizationMode() {
        return FertilizationMode;
    }

    public void setFertilizationMode(String fertilizationMode) {
        FertilizationMode = fertilizationMode;
    }

    public String getFeed() {
        return Feed;
    }

    public void setFeed(String feed) {
        Feed = feed;
    }



    @Override
    public String toString() {
        return "BaseInfor{" +
                "CardNumber='" + CardNumber + '\'' +
                ", BreedingBoxNo='" + BreedingBoxNo + '\'' +
                ", Varieties='" + Varieties + '\'' +
                ", PigAge='" + PigAge + '\'' +
                ", Weight='" + Weight + '\'' +
                ", ReproductiveNumber='" + ReproductiveNumber + '\'' +
                ", PigletQuantity='" + PigletQuantity + '\'' +
                ", EpidemicPreventionTime='" + EpidemicPreventionTime + '\'' +
                ", DateOfFertilization='" + DateOfFertilization + '\'' +
                ", FertilizationCycle='" + FertilizationCycle + '\'' +
                ", FertilizationMode='" + FertilizationMode + '\'' +
                ", Feed='" + Feed + '\'' +
                '}';
    }


}
