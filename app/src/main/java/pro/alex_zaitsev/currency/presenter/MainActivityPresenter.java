package pro.alex_zaitsev.currency.presenter;

import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.Constants;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.activity.MainActivity;
import pro.alex_zaitsev.currency.event.AdvertSavedEvent;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.object.model.Config;
import pro.alex_zaitsev.currency.presenter.core.ActivityPresenter;
import pro.alex_zaitsev.currency.utils.IntentHelper;
import pro.alex_zaitsev.currency.utils.ListLimiter;
import pro.alex_zaitsev.currency.utils.Network;
import pro.alex_zaitsev.currency.utils.PackageUtils;
import pro.alex_zaitsev.currency.utils.PlaceConverter;
import pro.alex_zaitsev.currency.utils.Toaster;
import pro.alex_zaitsev.currency.utils.provider.LocationProvider;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by rocknow on 01.03.2015.
 */
public class MainActivityPresenter extends ActivityPresenter<MainActivity> {

    private LocationProvider locationProvider = new LocationProvider();
    private boolean isConfigChecked = false;
    private List<Advert> adverts;
    private Subscription subscription;

    public MainActivityPresenter(MainActivity activity) {
        super(activity);
    }

    public void trackAppOpened(Intent intent) {
        App.getParseManager().trackAppOpened(intent);
        App.getParseManager().getConfigInBackground(activity);
    }

    public boolean isOnline() {
        return Network.isOnline(activity);
    }

    public void getAdverts() {
        App.getParseManager().getAdverts(0, ListLimiter.getLimit(), locationProvider.getCountryCode());
        Config config = App.getConfig();
        if (config == null) {
            App.getParseManager().getConfigInBackground(activity);
        } else {
            onGetConfig(config);
        }
    }

    public void saveCityId(int cityId) {
        App.getSharedPrefManager().setCityId(cityId);
    }

    public int getCityPosition() {
        int cityId = App.getSharedPrefManager().getCityId();
        return PlaceConverter.getListIndexAll(cityId);
    }

    public void onGetConfig(final Config config) {
        if (!isConfigChecked) {
            App.setConfig(config);
            checkAppVersion();
            isConfigChecked = true;
        }
    }

    public void checkAppVersion() {
        final Config config = App.getConfig();
        // check this app version
        if (config != null &&
                config.getLastVersionCode() > PackageUtils.getVersionCode(activity)) {
            int negativeText = config.isLastVersionCritical() ?
                    R.string.exit : R.string.cancel;
            new MaterialDialog.Builder(activity)
                    .title(R.string.warning)
                    .content(config.getLastVersionMessage())
                    .positiveText(R.string.update)
                    .negativeText(negativeText)
                    .cancelable(!config.isLastVersionCritical())
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                            IntentHelper.openMarket(activity);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            dialog.dismiss();
                            if (config.isLastVersionCritical()) {
                                activity.finish();
                            }
                        }
                    }).show();
        }
    }

    public void onCityClick(int position) {
        String[] cities = activity.getResources().getStringArray(R.array.cities_codes);
        App.getAnalytics().onFilterCity(cities[position]);
        int cityId = PlaceConverter.getCityIdAll(position);
        saveCityId(cityId);
        filter();
    }

    public void updateTitle() {
        String[] cities = activity.getResources().getStringArray(R.array.cities);
        List<String> cityList = new ArrayList<>(Arrays.asList(cities));
        cityList.add(0, activity.getString(R.string.all_cities));
        activity.getSupportActionBar().setTitle(cityList.get(getCityPosition()));
    }

    public String[] getFilterCurrencies() {
        return App.getSharedPrefManager().getFilterCurrencies();
    }

    public void onCurrencyClick(String[] currencies) {
        App.getSharedPrefManager().setFilterCurrencies(currencies);
        App.getAnalytics().onFilterCurrency();
        filter();
    }

    public int getFilterSort() {
        return App.getSharedPrefManager().getFilterSort();
    }

    public void onFilterSortClick(int i) {
        App.getSharedPrefManager().setFilterSort(i);
        String[] sort = activity.getResources().getStringArray(R.array.sort);
        App.getAnalytics().onFilterSort(sort[i]);
        activity.advertListFragment.onSortChanged();
    }

    public void onFilterBuySellClick(int buyOrSell) {
        App.getSharedPrefManager().setFilterBuySell(buyOrSell);
        if (buyOrSell == Constants.POSITION_BUY) {
            App.getAnalytics().onFilterBuy();
        } else {
            App.getAnalytics().onFilterSell();
        }
        filter();
    }

    public int getFilterBuySell() {
        return App.getSharedPrefManager().getFilterBuySell();
    }

    public void filter() {
        if (adverts == null || adverts.isEmpty()) {
            return;
        }
        if (subscription != null) {
            subscription.unsubscribe();
        }

        final int buySell = getFilterBuySell();
        final int cityId = App.getSharedPrefManager().getCityId();
        final List<String> filterCurrencies = new ArrayList<>();
        for (String currency : getFilterCurrencies()) {
            filterCurrencies.add(currency.toLowerCase(Locale.US));
        }

        subscription = AppObservable.bindActivity(activity, Observable.from(adverts)
                .filter(new Func1<Advert, Boolean>() {
                    @Override
                    public Boolean call(Advert advert) {
                        return advert.getBuySell() == buySell;
                    }
                })
                .filter(new Func1<Advert, Boolean>() {
                    @Override
                    public Boolean call(Advert advert) {
                        return cityId == Constants.CITY_ID_ALL || advert.getCityId() == cityId;
                    }
                })
                .filter(new Func1<Advert, Boolean>() {
                    @Override
                    public Boolean call(Advert advert) {
                        return filterCurrencies.contains(advert.getCurrencyFrom().toLowerCase(Locale.US));
                    }
                }))
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Advert>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, e.getMessage());
                    }

                    @Override
                    public void onNext(List<Advert> advertList) {
                        activity.advertListFragment.onAdvertGetEvent(advertList);
                        activity.drawerFragment.setFoundCount(advertList.size(), adverts.size());
                    }
                });
    }

    public void setAdverts(List<Advert> adverts) {
        this.adverts = adverts;
    }

    public void onSavedEvent(AdvertSavedEvent event) {
        if (event.isSuccess()) {
            adverts.add(event.getAdvert());
            filter();
        } else {
            Toaster.makeToastLong(activity, R.string.advert_not_posted);
        }
    }
}
