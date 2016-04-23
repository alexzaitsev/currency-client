package pro.alex_zaitsev.currency.manager;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

import pro.alex_zaitsev.currency.Constants;
import pro.alex_zaitsev.currency.object.CachedValue;
import timber.log.Timber;

/**
 * Created by rocknow on 28.02.2015.
 */
public class SharedPrefManager {

    // ==============================================
    // SHARED PREFS NAMES
    // ==============================================
    private static final String DEFAULT_PREF_STORAGE = "DEFAULT_PREF_STORAGE";

    // ==============================================
    // SHARED PREFS VALUES NAMES
    // ==============================================

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "PREF_USER_LEARNED_DRAWER_NEW";
    private static final String PREF_CITY_ID = "PREF_CITY_ID";
    private static final String PREF_FILTER_CURRENCIES = "PREF_FILTER_CURRENCIES";
    private static final String PREF_FILTER_SORT = "PREF_FILTER_SORT";
    private static final String PREF_FILTER_BUY_SELL = "PREF_FILTER_BUY_SELL";

    // ==============================================
    // SHARED PREFS DEFAULT VALUES
    // ==============================================

    private static final boolean DEFAULT_USER_LEARNED_DRAWER = false;
    private static final int DEFAULT_CITY_ID = Constants.CITY_ID_ALL;
    private static String DEFAULT_FILTER_CURRENCIES;
    static {
        try {
            String currencies = Arrays.toString(new String[]{Constants.USD, Constants.EUR, Constants.RUB});
            DEFAULT_FILTER_CURRENCIES = new JSONArray(currencies).toString();
        } catch (JSONException e) {
            DEFAULT_FILTER_CURRENCIES = "";
            Timber.e(e, e.getMessage());
        }
    }

    /**
     * Set sort by time as default
     */
    private static final int DEFAULT_FILTER_SORT = 0;
    /**
     * Set sell as default
     */
    private static final int DEFAULT_FILTER_BUY_SELL = 1;

    // ==============================================
    // CACHED VALUES
    // ==============================================

    private CachedValue<Boolean> userLearnedDrawer;
    private CachedValue<Integer> cityId;
    private CachedValue<String> filterCurrencies;
    private CachedValue<Integer> filterSort;
    private CachedValue<Integer> filterBuySell;

    public SharedPrefManager(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DEFAULT_PREF_STORAGE, Context.MODE_PRIVATE);
        CachedValue.initialize(sp);
        userLearnedDrawer = new CachedValue<>(PREF_USER_LEARNED_DRAWER, DEFAULT_USER_LEARNED_DRAWER, Boolean.class);
        cityId = new CachedValue<>(PREF_CITY_ID, DEFAULT_CITY_ID, Integer.class);
        filterCurrencies = new CachedValue<>(PREF_FILTER_CURRENCIES, DEFAULT_FILTER_CURRENCIES, String.class);
        filterSort = new CachedValue<>(PREF_FILTER_SORT, DEFAULT_FILTER_SORT, Integer.class);
        filterBuySell = new CachedValue<>(PREF_FILTER_BUY_SELL, DEFAULT_FILTER_BUY_SELL, Integer.class);
    }

    public boolean isUserLearnedDrawer() {
        return userLearnedDrawer.getValue();
    }

    public void setUserLearnedDrawer(boolean userLearnedDrawer) {
        this.userLearnedDrawer.setValue(userLearnedDrawer);
    }

    public int getCityId() {
        return cityId.getValue();
    }

    public void setCityId(int cityId) {
        this.cityId.setValue(cityId);
    }

    public String[] getFilterCurrencies() {
        String currencies = filterCurrencies.getValue();
        try {
            JSONArray json = new JSONArray(currencies);
            String[] filterCurrencies = new String[json.length()];
            for (int i = 0; i < json.length(); i++) {
                filterCurrencies[i] = json.getString(i);
            }
            return filterCurrencies;
        } catch (JSONException e) {
            Timber.e(e, e.getMessage());
        }
        return new String[]{};
    }

    public void setFilterCurrencies(String[] currencies) {
        try {
            JSONArray json = new JSONArray(Arrays.toString(currencies));
            filterCurrencies.setValue(json.toString());
        } catch (JSONException e) {
            Timber.e(e, e.getMessage());
        }
    }

    public int getFilterSort() {
        return filterSort.getValue();
    }

    public void setFilterSort(int sort) {
        filterSort.setValue(sort);
    }

    public void setFilterBuySell(int buyOrSell) {
        filterBuySell.setValue(buyOrSell);
    }

    public int getFilterBuySell() {
        return filterBuySell.getValue();
    }
}
