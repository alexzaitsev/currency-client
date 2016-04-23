package pro.alex_zaitsev.currency.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.event.AdvertGetEvent;
import pro.alex_zaitsev.currency.event.AdvertSavedEvent;
import pro.alex_zaitsev.currency.event.GetConfigEvent;
import pro.alex_zaitsev.currency.fragment.AdvertListFragment;
import pro.alex_zaitsev.currency.fragment.DrawerFragment;
import pro.alex_zaitsev.currency.interfaces.callback.BaseAdvertListFragmentCallback;
import pro.alex_zaitsev.currency.interfaces.callback.DrawerFragmentCallback;
import pro.alex_zaitsev.currency.interfaces.callback.FiltersFragmentCallback;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.presenter.MainActivityPresenter;
import pro.alex_zaitsev.currency.utils.IntentHelper;
import pro.alex_zaitsev.currency.utils.Toaster;


public class MainActivity extends BaseActivity<MainActivityPresenter>
        implements DrawerFragmentCallback, BaseAdvertListFragmentCallback,
        FiltersFragmentCallback {

    private static final int REQUEST_PROFILE = 333;
    private static final int REQUEST_DETAILS = 335;

    public DrawerFragment drawerFragment;
    public AdvertListFragment advertListFragment;

    @Override
    protected void setPresenter() {
        presenter = new MainActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter.updateTitle();

        presenter.trackAppOpened(getIntent());
        setFragments();
    }

    private void setFragments() {
        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        advertListFragment = (AdvertListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_advert_list);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        presenter.updateTitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.add, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            App.getAnalytics().onCreateNewAdvertFromMenu();
            startActivity(new Intent(this, CreateAdvertActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerFragment.isDrawerOpen()) {
            drawerFragment.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PROFILE && resultCode == RESULT_FIRST_USER) {
            advertListFragment.startLoading();
        }
        if (requestCode == REQUEST_DETAILS && resultCode == RESULT_FIRST_USER) {
            advertListFragment.startLoading();
        }
        if (requestCode == IntentHelper.REQUEST_MARKET) {
            presenter.checkAppVersion();
        }
    }

    @Override
    public void getAdverts() {
        presenter.getAdverts();
    }

    @Override
    public void onAdvertClick(Advert advert) {
        startActivityForResult(DetailsActivity.newIntent(this, advert), REQUEST_DETAILS);
    }

    @Override
    public void onTermsOfUsageClick() {
        startActivity(new Intent(this, TermsActivity.class));
    }

    @Override
    public void onProfileClick() {
        startActivityForResult(new Intent(this, ProfileActivity.class), REQUEST_PROFILE);
    }

    @Override
    public void onRateReviewClick() {
        startActivity(new Intent(this, RateReviewActivity.class));
    }

    @Override
    public boolean isOnline() {
        return presenter.isOnline();
    }

    @Override
    public void notifyNotOnline() {
        Toaster.makeToastShort(this, R.string.network_warn);
    }

    @Override
    public void onFilterCityClick(int i) {
        presenter.onCityClick(i);
        presenter.updateTitle();
    }

    @Override
    public int getFilterCityPosition() {
        return presenter.getCityPosition();
    }

    @Override
    public void onFilterCurrencyClick(String[] currencies) {
        presenter.onCurrencyClick(currencies);
    }

    @Override
    public String[] getFilterCurrencies() {
        return presenter.getFilterCurrencies();
    }

    @Override
    public int getFilterSort() {
        return presenter.getFilterSort();
    }

    @Override
    public void onFilterSortClick(int i) {
        presenter.onFilterSortClick(i);
    }

    @Override
    public void onFilterBuySellClick(int buyOrSell) {
        presenter.onFilterBuySellClick(buyOrSell);
    }

    @Override
    public int getFilterBuySell() {
        return presenter.getFilterBuySell();
    }

    public void onEvent(AdvertGetEvent event) {
        presenter.setAdverts(event.getAdvertList());
        presenter.filter();
    }

    public void onEvent(AdvertSavedEvent event) {
        presenter.onSavedEvent(event);
    }

    public void onEvent(GetConfigEvent event) {
        if (event.isSuccess()) {
            presenter.onGetConfig(event.getConfig());
        }
    }
}
