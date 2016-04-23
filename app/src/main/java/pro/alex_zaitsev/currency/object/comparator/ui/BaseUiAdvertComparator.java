package pro.alex_zaitsev.currency.object.comparator.ui;

import java.util.Comparator;

import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 16.03.2015.
 */
public abstract class BaseUiAdvertComparator implements Comparator<Advert> {

    @Override
    public int compare(Advert left, Advert right) {
        return left.isTop() && right.isTop() || !left.isTop() && !right.isTop() ?
                compareValues(left, right) : (left.isTop() ? -1 : 1);
    }

    abstract public int compareValues(Advert left, Advert right);
}
