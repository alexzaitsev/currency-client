package pro.alex_zaitsev.currency.object.mapper;

import android.text.TextUtils;

import com.parse.ParseObject;

import java.util.Date;
import java.util.Random;

import pro.alex_zaitsev.currency.manager.ParseManager;
import pro.alex_zaitsev.currency.object.WhoRide;
import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 01.03.2015.
 */
public class AdvertObjectMapper {

    public static final String FIELD_CREATED_AT = "createdAt";
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_RATE = "rate";
    public static final String FIELD_CURRENCY_FROM = "currency_from";
    public static final String FIELD_CURRENCY_TO = "currency_to";
    public static final String FIELD_COMMENT = "comment";
    public static final String FIELD_WHO_RIDE = "who_ride";
    public static final String FIELD_PHONE = "phone_number";
    public static final String FIELD_LIFE_TIME = "life_time";
    public static final String FIELD_COUNTRY_ID = "country_id";
    public static final String FIELD_CITY_ID = "city_id";
    public static final String FIELD_DISTRICT_ID = "district_id";
    public static final String FIELD_OWNER_ID = "owner_id";
    public static final String FIELD_ENABLED = "enabled";
    public static final String FIELD_IN_PARTS = "in_parts";
    public static final String FIELD_IS_COLORED = "is_colored";
    public static final String FIELD_IS_TOP = "is_top";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_SOURCE_TIME = "source_time";
    public static final String FIELD_SOURCE_DATE = "source_date";
    public static final String FIELD_BUY_SELL = "buy_sell";

    public static ParseObject advertToParse(Advert advert) {
        ParseObject obj = new ParseObject(ParseManager.CLASS_NAME_ADVERT);
        obj.setObjectId(advert.getId());
        if (advert.getCreatedAt() != null) {
            obj.put(FIELD_CREATED_AT, advert.getCreatedAt());
        }
        obj.put(FIELD_AMOUNT, advert.getAmount());
        obj.put(FIELD_RATE, advert.getRate());
        obj.put(FIELD_CURRENCY_FROM, checkString(advert.getCurrencyFrom()));
        obj.put(FIELD_CURRENCY_TO, checkString(advert.getCurrencyTo()));
        obj.put(FIELD_COMMENT, checkString(advert.getComment()));
        obj.put(FIELD_WHO_RIDE, advert.getWhoRide().ordinal());
        obj.put(FIELD_PHONE, checkString(advert.getPhone()));
        obj.put(FIELD_LIFE_TIME, advert.getLifeTime());
        obj.put(FIELD_COUNTRY_ID, advert.getCountryId());
        obj.put(FIELD_CITY_ID, advert.getCityId());
        obj.put(FIELD_DISTRICT_ID, advert.getDistrictId());
        obj.put(FIELD_OWNER_ID, checkString(advert.getOwnerId()));
        obj.put(FIELD_ENABLED, advert.isEnabled());
        obj.put(FIELD_IN_PARTS, advert.isInParts());
        obj.put(FIELD_IS_COLORED, advert.isColored());
        obj.put(FIELD_IS_TOP, advert.isTop());
        obj.put(FIELD_SOURCE, checkString(advert.getSource()));
        obj.put(FIELD_BUY_SELL, advert.getBuySell());
        return obj;
    }

    public static Advert parseToAdvert(ParseObject obj) {
        String objectId = obj.getObjectId();
        Date createdAt = obj.getCreatedAt();
        float amount = Float.parseFloat(obj.get(FIELD_AMOUNT).toString());
        float rate = Float.parseFloat(obj.get(FIELD_RATE).toString());
        String currencyFrom = obj.getString(FIELD_CURRENCY_FROM);
        String currencyTo = obj.getString(FIELD_CURRENCY_TO);
        String comment = obj.getString(FIELD_COMMENT);
        WhoRide whoRide = WhoRide.values()[(obj.getInt(FIELD_WHO_RIDE))];
        String phone = obj.getString(FIELD_PHONE);
        int lifeTime = obj.getInt(FIELD_LIFE_TIME);
        int countryId = obj.getInt(FIELD_COUNTRY_ID);
        int cityId = obj.getInt(FIELD_CITY_ID);
        int districtId = obj.getInt(FIELD_DISTRICT_ID);
        String ownerId = obj.getString(FIELD_OWNER_ID);
        boolean isEnabled = obj.getBoolean(FIELD_ENABLED);
        boolean inParts = obj.getBoolean(FIELD_IN_PARTS);
        boolean isColored = obj.getBoolean(FIELD_IS_COLORED);
        boolean isTop = obj.getBoolean(FIELD_IS_TOP);
        String source = obj.getString(FIELD_SOURCE);
        Date sourceDate = obj.getDate(FIELD_SOURCE_DATE);
        int buySell = obj.getInt(FIELD_BUY_SELL);

        if (!TextUtils.isEmpty(source) && sourceDate != null) {
            createdAt = sourceDate;
        }

        Advert advert = new Advert(objectId);
        advert.setCreatedAt(createdAt);
        advert.setAmount(amount);
        advert.setRate(rate);
        advert.setCurrencyFrom(currencyFrom);
        advert.setCurrencyTo(currencyTo);
        advert.setComment(comment);
        advert.setWhoRide(whoRide);
        advert.setPhone(phone);
        advert.setLifeTime(lifeTime);
        advert.setCountryId(countryId);
        advert.setCityId(cityId);
        advert.setDistrictId(districtId);
        advert.setOwnerId(ownerId);
        advert.setIsEnabled(isEnabled);
        advert.setInParts(inParts);
        advert.setIsColored(isColored);
        advert.setIsTop(isTop);
        advert.setSource(source);
        advert.setBuySell(buySell);

        //boolean is = rand.nextInt(100) < 30;
        //advert.setIsColored(is);
        //advert.setIsTop(is);
        // if (!is) {
        // advert.setSource("minfin");
        // }
        return advert;
    }

    private static Random rand = new Random(System.currentTimeMillis());

    private static String checkString(String value) {
        return value == null ? "" : value;
    }
}
