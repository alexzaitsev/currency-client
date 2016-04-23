package pro.alex_zaitsev.currency.event;

import com.parse.ParseException;

import java.util.List;

import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 01.03.2015.
 */
public class AdvertGetEvent extends ParseEvent {

    private List<Advert> advertList;

    public AdvertGetEvent(ParseException parseException) {
        super(parseException);
    }

    public AdvertGetEvent(List<Advert> advertList) {
        super(null);
        this.advertList = advertList;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && advertList != null;
    }

    public List<Advert> getAdvertList() {
        return advertList;
    }
}
