package me.duke.eth.browser.control;

import java.util.ArrayList;
import java.util.List;

import me.duke.eth.browser.dto.PushConfig;

public class AdapterWallet {

    private static AdapterWallet singleton = null;

    private AbstractRpc mRpc;

    private PushConfig mConfigStore;

    private final List<OnConfigStateChangeListener> onChangeListeners = new ArrayList<>();

    private AdapterWallet() {
    }

    public static AdapterWallet getInstance() {
        if (singleton == null) {
            singleton = new AdapterWallet();
        }
        return singleton;
    }

    public void initAdapterWallet(PushConfig configStore,AbstractRpc rpc){
        if(configStore == null || rpc == null){
            throw new ClassCastException("初始化失败，参数不能为null");
        }
        mConfigStore = configStore;
        mRpc = rpc;
    }

    protected AbstractRpc getRpc() {
        return mRpc;
    }

    protected PushConfig getConfigStore() {
        return mConfigStore;
    }

    public void changeState(PushConfig pushConfig) {
        mConfigStore = pushConfig;
        for (OnConfigStateChangeListener configStateChangeListener:onChangeListeners){
            configStateChangeListener.onChange();
        }
    }

    public void addOnChangeListener(OnConfigStateChangeListener onConfigStateChangeListener) {
        if (!onChangeListeners.contains(onConfigStateChangeListener))
            onChangeListeners.add(onConfigStateChangeListener);
    }

    public void removeOnchangeListener(OnConfigStateChangeListener onConfigStateChangeListener) {
        onChangeListeners.remove(onConfigStateChangeListener);
    }
}
