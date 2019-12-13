package com.example.myapplication.model;

public class LoginModel {


    /**
     * error : 0
     * info : 登录成功
     * data : {"token":"c5849a09d6691f0e97755f13c34d00528532840a"}
     * a : login
     */

    public int error;
    public String info;
    public DataBean data;
    public String a;

    public static class DataBean {
        /**
         * token : c5849a09d6691f0e97755f13c34d00528532840a
         */

        public String token;
    }
}
