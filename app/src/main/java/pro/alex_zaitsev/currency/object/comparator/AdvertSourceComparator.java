package pro.alex_zaitsev.currency.object.comparator;

import java.util.Comparator;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.Constants;
import pro.alex_zaitsev.currency.object.comparator.ui.AdvertAmountComparator;
import pro.alex_zaitsev.currency.object.comparator.ui.AdvertRateComparator;
import pro.alex_zaitsev.currency.object.comparator.ui.AdvertTimeComparator;
import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 29.03.2015.
 */
public class AdvertSourceComparator implements Comparator<Advert> {

    private AdvertTimeComparator createdAtComparator = new AdvertTimeComparator();
    private AdvertRateComparator rateComparator = new AdvertRateComparator();
    private AdvertAmountComparator amountComparator = new AdvertAmountComparator();

    private Comparator<Advert> getCurrentFilterComparator() {
        switch (App.getSharedPrefManager().getFilterSort()) {
            case Constants.SORT_RATE:
                return rateComparator;
            case Constants.SORT_AMOUNT:
                return amountComparator;
            default:
                return createdAtComparator;
        }
    }

    @Override
    public int compare(Advert left, Advert right) {
        return getCurrentFilterComparator().compare(left, right);
    }
}
