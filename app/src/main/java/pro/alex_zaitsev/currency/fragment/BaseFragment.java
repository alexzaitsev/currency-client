package pro.alex_zaitsev.currency.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import pro.alex_zaitsev.currency.interfaces.callback.Callback;

/**
 * Created by rocknow on 28.02.2015.
 */
public abstract class BaseFragment<C extends Callback> extends Fragment {

    protected C callback;

    @Override
    public void onAttach(Activity activity) {
        try {
            callback = (C) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().getSimpleName() +
                " must implement " + callback.getClass().getSimpleName());
        }
        super.onAttach(activity);
    }
}
