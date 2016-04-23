package pro.alex_zaitsev.currency.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import pro.alex_zaitsev.currency.BuildConfig;


/**
 * Created by rocknow on 03.04.2015.
 */
public class Analytics {

    private static final String CATEGORY_UI = "ui";

    private static final String ACTION_FILTER = "filter";
    private static final String LABEL_BUY_CLICK = "buy";
    private static final String LABEL_SELL_CLICK = "sell";
    private static final String LABEL_CITY_ = "city_";
    private static final String LABEL_CURRENCY_CLICK = "currency";
    private static final String LABEL_SORT_CLICK_ = "sort_";

    private static final String ACTION_ADVERT = "advert";
    private static final String LABEL_NEW_MENU = "new_menu";
    private static final String LABEL_NEW_MY_ADVERTS = "new_my_adverts";
    private static final String LABEL_ACTIONS_MENU = "actions_menu";
    private static final String LABEL_DONE_MENU_SUCCESS = "done_menu_success";
    private static final String LABEL_DONE_MENU_DATA_NOT_VALID = "done_menu_data_not_valid";
    private static final String LABEL_EDIT = "edit";
    private static final String LABEL_ENABLE = "enable";
    private static final String LABEL_DISABLE = "disable";
    private static final String LABEL_DELETE = "delete";
    private static final String LABEL_DELETE_OK = "delete_ok";
    private static final String LABEL_DELETE_CANCEL = "delete_cancel";

    private static final String ACTION_CREATE = "create";
    private static final String LABEL_CREATE_EXIT = "create_exit";
    private static final String LABEL_CREATE_EXIT_OK = "create_exit_ok";
    private static final String LABEL_CREATE_EXIT_CANCEL = "create_exit_cancel";
    private static final String LABEL_DATA_FILLED = "data_filled";
    private static final String LABEL_PAYMENT_FILLED = "payment_filled";
    private static final String LABEL_DONE_SUCCESS = "done_success";
    private static final String LABEL_DONE_FAIL = "done_fail";
    private static final String LABEL_BUY_COLOR_CLICK = "buy_color_click";
    private static final String LABEL_BUY_TOP_CLICK = "buy_top_click";
    private static final String LABEL_BUY_SUCCESS = "buy_success";
    private static final String LABEL_BUY_FAIL_ = "buy_fail_";
    private static final String LABEL_BUY_CANCELLED = "buy_cancelled";

    private static final String ACTION_COMMON = "common";
    private static final String LABEL_RATE_ = "rate_";
    private static final String LABEL_RATE_REFRESH = "rate_refresh";
    private static final String LABEL_RATE_DONE = "rate_done";
    private static final String LABEL_CALL = "call";
    private static final String LABEL_LOGOUT = "logout";
    private static final String LABEL_SHARE = "share";

    private Tracker tracker;

    public Analytics(Tracker tracker) {
        this.tracker = tracker;
    }

    private void sendUiEvent(String action, String label) {
        if (BuildConfig.DEBUG) {
            return;
        }
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(CATEGORY_UI)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    public void onFilterBuy() {
        sendUiEvent(ACTION_FILTER, LABEL_BUY_CLICK);
    }

    public void onFilterSell() {
        sendUiEvent(ACTION_FILTER, LABEL_SELL_CLICK);
    }

    public void onFilterCity(String city) {
        sendUiEvent(ACTION_FILTER, LABEL_CITY_ + city);
    }

    public void onFilterCurrency() {
        sendUiEvent(ACTION_FILTER, LABEL_CURRENCY_CLICK);
    }

    public void onFilterSort(String sort) {
        sendUiEvent(ACTION_FILTER, LABEL_SORT_CLICK_ + sort);
    }

    public void onCreateNewAdvertFromMenu() {
        sendUiEvent(ACTION_ADVERT, LABEL_NEW_MENU);
    }

    public void onCreateNewAdvertFromMyAdverts() {
        sendUiEvent(ACTION_ADVERT, LABEL_NEW_MY_ADVERTS);
    }

    public void onActionsFromMenu() {
        sendUiEvent(ACTION_ADVERT, LABEL_ACTIONS_MENU);
    }

    public void onDoneFromMenuSuccess() {
        sendUiEvent(ACTION_ADVERT, LABEL_DONE_MENU_SUCCESS);
    }

    public void onDoneFromMenuDataNotValid() {
        sendUiEvent(ACTION_ADVERT, LABEL_DONE_MENU_DATA_NOT_VALID);
    }

    public void onEdit() {
        sendUiEvent(ACTION_ADVERT, LABEL_EDIT);
    }

    public void onEnable() {
        sendUiEvent(ACTION_ADVERT, LABEL_ENABLE);
    }

    public void onDisable() {
        sendUiEvent(ACTION_ADVERT, LABEL_DISABLE);
    }

    public void onDelete() {
        sendUiEvent(ACTION_ADVERT, LABEL_DELETE);
    }

    public void onDeleteOk() {
        sendUiEvent(ACTION_ADVERT, LABEL_DELETE_OK);
    }

    public void onDeleteCancel() {
        sendUiEvent(ACTION_ADVERT, LABEL_DELETE_CANCEL);
    }

    public void onCreateExit() {
        sendUiEvent(ACTION_CREATE, LABEL_CREATE_EXIT);
    }

    public void onCreateExitOk() {
        sendUiEvent(ACTION_CREATE, LABEL_CREATE_EXIT_OK);
    }

    public void onCreateExitCancel() {
        sendUiEvent(ACTION_CREATE, LABEL_CREATE_EXIT_CANCEL);
    }

    public void onCreateDataFilled() {
        sendUiEvent(ACTION_CREATE, LABEL_DATA_FILLED);
    }

    public void onCreatePaymentFilled() {
        sendUiEvent(ACTION_CREATE, LABEL_PAYMENT_FILLED);
    }

    public void onCreateDoneSuccess() {
        sendUiEvent(ACTION_CREATE, LABEL_DONE_SUCCESS);
    }

    public void onCreateDoneFail() {
        sendUiEvent(ACTION_CREATE, LABEL_DONE_FAIL);
    }

    public void onRate(int value) {
        sendUiEvent(ACTION_COMMON, LABEL_RATE_ + value);
    }

    public void onRateRefresh() {
        sendUiEvent(ACTION_COMMON, LABEL_RATE_REFRESH);
    }

    public void onRateDone() {
        sendUiEvent(ACTION_COMMON, LABEL_RATE_DONE);
    }

    public void onCall() {
        sendUiEvent(ACTION_COMMON, LABEL_CALL);
    }

    public void onBuyColorClick() {
        sendUiEvent(ACTION_CREATE, LABEL_BUY_COLOR_CLICK);
    }

    public void onBuyTopClick() {
        sendUiEvent(ACTION_CREATE, LABEL_BUY_TOP_CLICK);
    }

    public void onBuySuccess() {
        sendUiEvent(ACTION_CREATE, LABEL_BUY_SUCCESS);
    }

    public void onBuyFail(int errorCode) {
        sendUiEvent(ACTION_CREATE, LABEL_BUY_FAIL_ + errorCode);
    }

    public void onBuyCancelled() {
        sendUiEvent(ACTION_CREATE, LABEL_BUY_CANCELLED);
    }

    public void onLogout() {
        sendUiEvent(ACTION_COMMON, LABEL_LOGOUT);
    }

    public void onShare() {
        sendUiEvent(ACTION_COMMON, LABEL_SHARE);
    }
}
