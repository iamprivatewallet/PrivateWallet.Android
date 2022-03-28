package com.fanrong.frwallet.wallet.cwv.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取余额
 */
public class ToRMBResp implements Serializable {
    public String retcode;
    public String msg;
    public List<FloatNote> kline;

    public static class FloatNote implements Serializable {
        public String id;
        public String open;
        public String close;
        public String high;
        public String low;
        public String amount;
        public String vol;
        public String coin;
        public String tradecoin;
        public String period;
        public String updown;
        public String rmbclose;
        public String rmbopen;
        public String rmbhigh;
        public String rmblow;
        public String rmbvol;
    }
}
