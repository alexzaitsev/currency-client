package pro.alex_zaitsev.currency.object.comparator.ui;

import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 16.03.2015.
 */
public class AdvertAmountComparator extends BaseUiAdvertComparator {

    @Override
    public int compareValues(Advert left, Advert right) {
        return Float.valueOf(left.getAmount()).compareTo(right.getAmount());
    }
}
