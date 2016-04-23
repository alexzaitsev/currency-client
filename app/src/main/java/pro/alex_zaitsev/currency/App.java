package pro.alex_zaitsev.currency;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import de.greenrobot.event.EventBus;
import pro.alex_zaitsev.currency.manager.DataManager;
import pro.alex_zaitsev.currency.manager.ParseManager;
import pro.alex_zaitsev.currency.manager.SharedPrefManager;
import pro.alex_zaitsev.currency.object.model.Config;
import pro.alex_zaitsev.currency.utils.Analytics;
import pro.alex_zaitsev.currency.utils.LogReleaseTree;
import pro.alex_zaitsev.currency.utils.provider.CurrencyProvider;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by rocknow on 28.02.2015.
 */
public class App extends Application {

    private static SharedPrefManager sharedPrefManager;
    private static DataManager dataManager;
    private static ParseManager parseManager;
    private static CurrencyProvider currencyProvider;
    private static EventBus eventBus;
    private static Config config;
    private static Analytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics ganalytics = GoogleAnalytics.getInstance(this);
        Tracker tracker = ganalytics.newTracker(R.xml.analytics_app_tracker);
        analytics = new Analytics(tracker);
        ganalytics.enableAutoActivityReports(this);
        ganalytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.font_default))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        sharedPrefManager = new SharedPrefManager(this);
        dataManager = new DataManager();
        parseManager = new ParseManager(this);
        currencyProvider = new CurrencyProvider();
        eventBus = EventBus.getDefault();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new LogReleaseTree());
        }
    }

    public static SharedPrefManager getSharedPrefManager() {
        return sharedPrefManager;
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    public static CurrencyProvider getCurrencyProvider() {
        return currencyProvider;
    }

    public static ParseManager getParseManager() {
        return parseManager;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        App.config = config;
    }

    public static Analytics getAnalytics() {
        return analytics;
    }
}
