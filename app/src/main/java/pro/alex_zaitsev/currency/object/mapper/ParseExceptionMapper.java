package pro.alex_zaitsev.currency.object.mapper;

import android.content.Context;

import com.parse.ParseException;

import pro.alex_zaitsev.currency.R;

/**
 * Created by rocknow on 09.03.2015.
 */
public class ParseExceptionMapper {

    public static String getMessage(Context context, ParseException ex) {
        if (ex == null) {
            return context.getString(R.string.error_signup_general);
        }
        int stringId;
        switch (ex.getCode()) {
            case ParseException.INVALID_EMAIL_ADDRESS:
                stringId = R.string.error_invalid_email;
                break;
            case ParseException.EMAIL_NOT_FOUND:
                stringId = R.string.error_invalid_email;
                break;
            default:
                stringId = R.string.error_signup_general;
                break;
        }
        return context.getString(stringId);
    }
}
