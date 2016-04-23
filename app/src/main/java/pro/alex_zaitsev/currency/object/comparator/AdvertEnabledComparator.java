package pro.alex_zaitsev.currency.object.comparator;

import java.util.Comparator;

import pro.alex_zaitsev.currency.object.comparator.ui.AdvertTimeComparator;
import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 16.03.2015.
 */
public class AdvertEnabledComparator implements Comparator<Advert> {

    private AdvertTimeComparator createdAtComparator = new AdvertTimeComparator();

    @Override
    public int compare(Advert left, Advert right) {
        return left.isEnabled() && right.isEnabled() || !left.isEnabled() && !right.isEnabled()?
                createdAtComparator.compare(left, right) : (left.isEnabled() ? -1 : 1);
    }
}
