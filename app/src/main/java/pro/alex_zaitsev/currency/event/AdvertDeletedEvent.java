package pro.alex_zaitsev.currency.event;

import com.parse.ParseException;

import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 01.03.2015.
 */
public class AdvertDeletedEvent extends ParseEvent {

    private Advert advert;

    public AdvertDeletedEvent(ParseException exception) {
        super(exception);
    }

    public AdvertDeletedEvent(Advert advert) {
        super(null);
        this.advert = advert;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && advert != null;
    }

    public Advert getAdvert() {
        return advert;
    }
}
