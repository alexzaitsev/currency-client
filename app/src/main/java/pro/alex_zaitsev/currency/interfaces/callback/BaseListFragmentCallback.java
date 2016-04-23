package pro.alex_zaitsev.currency.interfaces.callback;

/**
 * Created by rocknow on 13.03.2015.
 */
public interface BaseListFragmentCallback extends Callback {
    
    boolean isOnline();

    void notifyNotOnline();
}
