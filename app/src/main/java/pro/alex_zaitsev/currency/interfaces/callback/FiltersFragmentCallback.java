package pro.alex_zaitsev.currency.interfaces.callback;

/**
 * Created by rocknow on 26.04.2015.
 */
public interface FiltersFragmentCallback extends Callback {

    void onFilterCityClick(int i);

    int getFilterCityPosition();

    void onFilterCurrencyClick(String[] currencies);

    String[] getFilterCurrencies();

    int getFilterSort();

    void onFilterSortClick(int i);

    void onFilterBuySellClick(int buyOrSell);

    int getFilterBuySell();
}
