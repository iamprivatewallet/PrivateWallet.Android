package com.fanrong.frwallet.dao.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class marketDataBean {

    public Integer code;
    public List<DataDTO> data;
    public String msg;

    public class DataDTO{
        public Integer amout;
        public Long createTime;
        public Integer id;
        public Double last;
        public Long lastVol;
        public Double rose;
        public Integer status;
        public String symbol;
        public Integer type;
        public Long updateTime;
        public boolean isLike;
    }
}
