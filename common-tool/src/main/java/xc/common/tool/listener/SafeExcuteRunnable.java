package xc.common.tool.listener;

public abstract class SafeExcuteRunnable implements Runnable {
    @Override
    public final void run() {
        try{
            safeExcuteRun();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public abstract void safeExcuteRun();

}
