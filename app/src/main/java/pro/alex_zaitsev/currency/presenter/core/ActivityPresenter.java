package pro.alex_zaitsev.currency.presenter.core;

import pro.alex_zaitsev.currency.activity.BaseActivity;

/**
 * Created by rocknow on 01.03.2015.
 */
public abstract class ActivityPresenter<A extends BaseActivity> extends Presenter {

    protected A activity;

    public ActivityPresenter(A activity) {
        this.activity = activity;
    }
}
