package pro.alex_zaitsev.currency.activity;

import android.content.Intent;
import android.os.Bundle;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.event.AdvertSavedEvent;
import pro.alex_zaitsev.currency.event.MyAdvertGetEvent;
import pro.alex_zaitsev.currency.fragment.MyAdvertsFragment;
import pro.alex_zaitsev.currency.interfaces.callback.MyAdvertsFragmentCallback;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.presenter.MyAdvertsActivityPresenter;
import pro.alex_zaitsev.currency.utils.Toaster;

/**
 * Created by rocknow on 10.03.2015.
 */
public class MyAdvertsActivity extends BaseUpActivity<MyAdvertsActivityPresenter>
        implements MyAdvertsFragmentCallback {

    private static final int REQUEST_DETAILS = 334;

    private MyAdvertsFragment myAdvertsFragment;

    @Override
    protected void setPresenter() {
        presenter = new MyAdvertsActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adverts);
        myAdvertsFragment = (MyAdvertsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_my_adverts);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAILS && resultCode == RESULT_FIRST_USER) {
            myAdvertsFragment.startLoading();
            setResult(RESULT_FIRST_USER);
        }
    }

    @Override
    public void onNewAdvertClick() {
        startActivity(new Intent(this, CreateAdvertActivity.class));
    }

    @Override
    public void onAdvertClick(Advert advert) {
        App.getAnalytics().onCreateNewAdvertFromMyAdverts();
        startActivityForResult(DetailsActivity.newIntent(this, advert), REQUEST_DETAILS);
    }

    @Override
    public void getAdverts() {
        presenter.getMyAdverts();
    }

    @Override
    public boolean isOnline() {
        return presenter.isOnline();
    }

    @Override
    public void notifyNotOnline() {
        Toaster.makeToastShort(this, R.string.network_warn);
    }

    public void onEvent(MyAdvertGetEvent event) {
        myAdvertsFragment.onMyAdvertGetEvent(event);
    }

    public void onEvent(AdvertSavedEvent event) {
        myAdvertsFragment.onAdvertSavedEvent(event);
        setResult(RESULT_FIRST_USER);
    }
}
