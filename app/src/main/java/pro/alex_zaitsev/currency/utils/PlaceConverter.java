package pro.alex_zaitsev.currency.utils;

import pro.alex_zaitsev.currency.Constants;

/**
 * Created by rocknow on 04.03.2015.
 */
public class PlaceConverter {

    public static int getCityId(int listIndex) {
        return listIndex;
    }

    public static int getListIndex(int cityId) {
        return cityId == Constants.CITY_ID_ALL ? Constants.CITY_ID_KIEV : cityId;
    }

    public static int getListIndexAll (int cityPosition) {
        return cityPosition + 1;
    }

    public static int getCityIdAll (int listIndex) {
        return listIndex - 1;
    }
}
