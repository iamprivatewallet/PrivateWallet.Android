package com.fanrong.frwallet.wallet.eth.eth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class test {

    private Integer code;
    private String msg;
    private DataDTO data;

    public static class DataDTO {
        private List<Dapp56DTO> dapp56;
        private List<Dapp128DTO> dapp128;
        private List<Banner11DTO> banner11;
        private List<Banner23DTO> banner23;
        private List<Banner22DTO> banner22;
        private List<DappTopDTO> dappTop;
        private List<Dapp1DTO> dapp1;

        public static class Dapp56DTO {
            private String id;
            private String appName;
            private String description;
            private String iconUrl;
            private String appUrl;
            private String firType;
            private String secType;
            private Integer chainId;
            private Integer isTop;
            private String languageCode;
        }

        public static class Dapp128DTO {
            private String id;
            private String appName;
            private String description;
            private String iconUrl;
            private String appUrl;
            private String firType;
            private String secType;
            private Integer chainId;
            private Integer isTop;
            private String languageCode;
        }

        public static class Banner11DTO {
            private Integer id;
            private Integer status;
            private Integer type;
            private Integer place;
            private Object imgWeb;
            private String imgH5;
            private Object videoUrl;
            private Object videoImg;
            private Object videoUrlH5;
            private Object videoImgH5;
            private Integer sortId;
            private String clickUrl;
            private String languageCode;
            private String title;
            private String createTime;
            private String updateTime;
        }

        public static class Banner23DTO {
            private Integer id;
            private Integer status;
            private Integer type;
            private Integer place;
            private Object imgWeb;
            private String imgH5;
            private Object videoUrl;
            private Object videoImg;
            private Object videoUrlH5;
            private Object videoImgH5;
            private Integer sortId;
            private String clickUrl;
            private String languageCode;
            private Object title;
            private String createTime;
            private String updateTime;
        }

        public class Banner22DTO {
            private Integer id;
            private Integer status;
            private Integer type;
            private Integer place;
            private Object imgWeb;
            private String imgH5;
            private Object videoUrl;
            private Object videoImg;
            private Object videoUrlH5;
            private Object videoImgH5;
            private Integer sortId;
            private Object clickUrl;
            private String languageCode;
            private Object title;
            private String createTime;
            private String updateTime;
        }

        public class DappTopDTO {
            private String id;
            private String appName;
            private String description;
            private String iconUrl;
            private String appUrl;
            private String firType;
            private String secType;
            private Integer chainId;
            private Integer isTop;
            private String languageCode;
        }

        public class Dapp1DTO {
            private String id;
            private String appName;
            private String description;
            private String iconUrl;
            private String appUrl;
            private String firType;
            private String secType;
            private Integer chainId;
            private Integer isTop;
            private String languageCode;
        }
    }
}

