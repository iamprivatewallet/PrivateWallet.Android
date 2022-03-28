package com.fanrong.frwallet.wallet.cwv.http;

public class URLBuilder {

    /**
     * 生产测试开关
     * 生产true
     * 测试alse
     */
    private boolean IS_ON_LINE = false;

    /**
     * 短连接服务器地址
     * @return
     */
    public String getHostUrl() {
        if (IS_ON_LINE) {
            //生产
            return "http://52.220.97.222:1235";
        } else {
//            http://www.ruoqingzixun.com/appapi/login.php
//            return "http://www.ruoqingzixun.com/";
            return "http://52.220.97.222:1235";
        }
    }

    /**
     * 网宿云存储图片上传服务器
     * @return
     */
    public String getWCSUploadAddress() {
        return "http://joygo-qingchun.up9.v1.wcsapi.com";
    }

    /**
     * 登录
     * @return
     */
    public String getLoginUrl() {
        return String.format(getHostUrl(), "appapi/login.php");
    }




    private static URLBuilder instance;

    public static URLBuilder ins() {
        if (instance == null) {
            synchronized (URLBuilder.class) {
                if (instance == null) {
                    instance = new URLBuilder();
                }
            }
        }
        return instance;
    }

}
