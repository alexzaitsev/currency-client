package pro.alex_zaitsev.currency.utils;

import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.MutableDateTime;

import java.util.Locale;

import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.object.model.Advert;

/**
 * Created by rocknow on 16.03.2015.
 */
public class Share {

    public static Intent getShareIntent(Context context, Advert advert) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, getShareText(context, advert));
        return intent;
    }

    private static String getShareText(Context context, Advert advert) {
        return context.getString(R.string.share_text,
                advert.getAmount() + " " + advert.getCurrencyFrom().toUpperCase(Locale.US),
                advert.getRate() + " " + advert.getCurrencyTo().toUpperCase(Locale.US),
                getDateString(context, advert),
                context.getString(R.string.app_name),
                context.getString(R.string.url));
    }

    private static String getDateString(Context context, Advert advert) {
        String in = context.getString(R.string.date_in);
        String lessHour = context.getString(R.string.date_hour_less);

        MutableDateTime time = new MutableDateTime(advert.getCreatedAt());
        time.addHours(advert.getLifeTime());
        if (time.isBeforeNow()) {
            return lessHour;
        } else {
            int daysBetween = Days.daysBetween(DateTime.now(), time.toDateTime()).getDays();
            int hoursBetween = Hours.hoursBetween(DateTime.now(), time.toDateTime()).getHours() +
                    (time.getMinuteOfHour() > 0 ? 1 : 0);
            int hoursLeft = hoursBetween >= 24 ? hoursBetween % 24 : hoursBetween;
            if (daysBetween > 0) {
                return in + " " + getDayString(context, daysBetween) +
                        (hoursLeft == 0 ? "" : " " + getHourString(context, hoursLeft));
            } else if (hoursBetween > 0) {
                return in + " " + getHourString(context, hoursBetween);
            } else {
                return lessHour;
            }
        }
    }

    private static String getDayString(Context context, int days) {
        return context.getResources().getQuantityString(R.plurals.days, days, days);
    }

    private static String getHourString(Context context, int hours) {
        return context.getResources().getQuantityString(R.plurals.hours, hours, hours);
    }
}
