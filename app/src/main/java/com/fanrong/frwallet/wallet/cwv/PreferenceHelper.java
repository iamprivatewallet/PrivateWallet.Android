//package com.fanrong.frwallet.wallet.cwv;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Base64;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.StreamCorruptedException;
//
///**
// * 本地存储工具类 可以存储基础数据类型及复杂对象
// * Created by terry on 7/21/16.
// */
//
//public class PreferenceHelper {
//
//    static final String SHARE_NAME = "app_share_preference";
//
//    private static Object syncObj = new Object();
//
//    private static PreferenceHelper mPH;
//
//    private SharedPreferences.Editor edit = null;
//
//    private Context mContext;
//
//    private PreferenceHelper(Context context) {
//        this.mContext = context;
//    }
//
//    private Context getMcontext() {
//        if (mContext == null) {
//            mContext = AppApplication.Companion.getInstance();
//        }
//        return mContext;
//    }
//
//    public static PreferenceHelper getInstance() {
//        try {
//            if (mPH == null) {
//                mPH = new PreferenceHelper(AppApplication.Companion.getInstance());
//            }
//            return mPH;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private SharedPreferences getPreference() {
//        return getMcontext().getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
//    }
//
//    public boolean getBooleanShareData(String key, boolean defaultBoolean) {
//        if (getMcontext() == null) {
//            return defaultBoolean;
//        }
//        return getPreference().getBoolean(key, defaultBoolean);
//    }
//
//    public int getIntShareData(String key, int defaultInt) {
//        if (getMcontext() == null) {
//            return defaultInt;
//        }
//        return getPreference().getInt(key, defaultInt);
//    }
//
//    public long getLongShareData(String paramString, long paramLong) {
//        if (getMcontext() == null) {
//            return paramLong;
//        }
//        return getPreference().getLong(paramString, paramLong);
//    }
//
//    public String getStringShareData(String key, String defaultString) {
//        if (getMcontext() == null) {
//            return defaultString;
//        }
//        return getPreference().getString(key, defaultString);
//    }
//
//    public boolean storeBooleanShareData(String key, boolean paramBoolean) {
//        if (getMcontext() != null) {
//            synchronized (syncObj) {
//                if (edit == null) {
//                    edit = getPreference().edit();
//                }
//                edit.putBoolean(key, paramBoolean).commit();
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean storeIntShareData(String key, int paramInt) {
//        if (getMcontext() != null) {
//            synchronized (syncObj) {
//                if (this.edit == null) {
//                    this.edit = getPreference().edit();
//                }
//                this.edit.putInt(key, paramInt).commit();
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void storeLongShareData(String key, long paramLong) {
//        if (getMcontext() != null) {
//            synchronized (syncObj) {
//                if (this.edit == null) {
//                    this.edit = getPreference().edit();
//                }
//                this.edit.putLong(key, paramLong).commit();
//            }
//        }
//    }
//
//    public void storeStringShareData(String key, String paramString) {
//        if (getMcontext() != null) {
//            synchronized (syncObj) {
//                if (this.edit == null) {
//                    this.edit = getPreference().edit();
//                }
//                this.edit.putString(key, paramString).commit();
//            }
//        }
//    }
//
//    /**
//     * load object
//     *
//     * @param key
//     * @param clazz
//     * @param <T>
//     * @return
//     */
//    public <T> T getObject(String key, Class<T> clazz) {
//        if (getMcontext() != null) {
//            if (getPreference().contains(key)) {
//                String objectVal = getPreference().getString(key, null);
//                byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
//                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
//                ObjectInputStream ois = null;
//                try {
//                    ois = new ObjectInputStream(bais);
//                    T t = (T) ois.readObject();
//                    return t;
//                } catch (StreamCorruptedException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (bais != null) {
//                            bais.close();
//                        }
//                        if (ois != null) {
//                            ois.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * save object
//     *
//     * @param key
//     * @param object
//     */
//    public void setObject(String key, Object object) {
//        if (getMcontext() != null) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream out = null;
//            try {
//                out = new ObjectOutputStream(baos);
//                out.writeObject(object);
//                String objectVal = new String(Base64.encode(baos.toByteArray(),
//                        Base64.DEFAULT));
//                SharedPreferences.Editor editor = getPreference().edit();
//                editor.putString(key, objectVal);
//                editor.commit();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (baos != null) {
//                        baos.close();
//                    }
//                    if (out != null) {
//                        out.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public void clearObjectForKey(String key) {
//        if (getMcontext() != null) {
//            synchronized (syncObj) {
//                if (this.edit == null) {
//                    this.edit = getPreference().edit();
//                }
//                this.edit.remove(key).commit();
//            }
//        }
//    }
//
//
//    public void clear() {
//        try {
//            if (getMcontext() != null) {
//                synchronized (syncObj) {
//                    if (edit == null) {
//                        edit = getPreference().edit();
//                    }
//                    edit.clear().commit();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public class PreferenceKey {
//
//        public static final String KEY_USER_INFO = "KEY_USER_INFO";
//
//        public static final String KEY_PARK_AREA = "KEY_PARK_AREA";
//
//        public static final String KEY_NEWS_SELECT_LIST = "KEY_NEWS_SELECT_LIST";
//
//        public static final String KEY_NEWS_HISTORY_LIST = "KEY_NEWS_HISTORY_LIST";
//
//        public static final String KEY_NEWS_TYPES = "KEY_NEWS_TYPES";
//
//        public static final String KEY_CURRENT_LOCATION = "KEY_CURRENT_LOCATION";
//
//        public static final String KEY_HAS_SHOW_GUIDE = "KEY_HAS_SHOW_GUIDE";
//
//        public static final String KEY_HAS_SHOW_REPORT_GUIDE = "KEY_HAS_SHOW_REPORT_GUIDE";
//
//        public static final String KEY_TOKEN_ID = "KEY_TOKEN_ID";
//
//        public static final String KEY_NEWS_FORUMS_BROWERS_HISTORY = "KEY_NEWS_FORUMS_BROWERS_HISTORY";
//
//        public static final String KEY_USER_USE_MESSAGE_PUSH = "KEY_USER_USE_MESSAGE_PUSH";
//
//        public static final String KEY_FORUM_DEFAULT_LIST = "KEY_FORUM_DEFAULT_LIST";
//
//        public static final String KEY_COIN_LIST = "KEY_COIN_LIST";
//
//        public static final String KEY_ADDRESS_LIST = "KEY_ADDRESS_LIST";
//
//        public static final String KEY_WALLET_LIST = "KEY_WALLET_LIST";
//
//        public static final String KEY_WALLET_CURRENT_PUBKEY = "KEY_WALLET_CURRENT_PUBKEY";
//
//        public static final String KEY_WALLET_TRANS = "KEY_WALLET_TRANS";
//
//        public static final String KEY_NODE_LIST = "KEY_NODE_LIST";
//
//        public static final String KEY_FBC_NODE_LIST = "KEY_FBC_NODE_LIST";
//        public static final String KEY_ETH_NODE_LIST = "KEY_ETH_NODE_LIST";
//        public static final String KEY_ALL_TOKENS = "KEY_ALL_TOKENS";
//        public static final String SPDT_WALLET = "SPDT_WALLET";
//        public static final String ETH_WALLET = "ETH_WALLET";
//        public static final String MNEMONIC_STR = "MNEMONIC_STR";
//        public static final String PASSWORD = "PASSWORD";
//        public static final String USERNAME = "USERNAME";
//
//        public static final String USER_ID = "USER_ID";
//        public static final String NICK_NAME = "NICK_NAME";
//        public static final String HEADER_PATH = "HEADER_PATH";
//        public static final String AUTH_NAME_NO = "AUTH_NAME_NO";
//        public static final String ADDRESS_BOOKS = "ADDRESS_BOOKS";
//        public static final String CHAIN_ID = "CHAIN_ID";
//
//
//        public PreferenceKey() {
//        }
//    }
//
//}
