package com.example.myapplication.model;

import java.io.Serializable;

public class InformationModel implements Serializable {


    /**
     * error : 0
     * info : 查询成功
     * data : {"name":"张三","shop":"湖南省长沙市啊啊啊啊经销商","phone":"18655554444","drivingID":"604444333322221111444"}
     * a : putPrzCode
     */

    public int error;
    public String info;
    public DataBean data;
    public String a;

    public static class DataBean implements Serializable{
        /**
         * name : 张三
         * shop : 湖南省长沙市啊啊啊啊经销商
         * phone : 18655554444
         * drivingID : 604444333322221111444
         */

        public String name;
        public String shop;
        public String phone;
        public String drivingID;
    }
}
