package pro.alex_zaitsev.currency.object.mapper;

import android.content.Context;

import com.parse.ParseConfig;

import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.object.model.Config;
import timber.log.Timber;

/**
 * Created by rocknow on 18.03.2015.
 */
public class ConfigObjectMapper {

    public static final String FIELD_LICENCE_KEY = "licence_key";
    public static final String FIELD_ADVERT_LIMITATION_HOUR = "advert_limitation_hour";
    public static final String FIELD_LAST_VERSION_CODE = "last_version_code";
    public static final String FIELD_IS_LAST_VERSION_CRITICAL = "is_last_version_critical";
    public static final String FIELD_LAST_VERSION_MESSAGE_ = "last_version_message_";

    public static Config parseToConfig(Context context, ParseConfig parseObject) {
        String licenceKey = parseObject.getString(FIELD_LICENCE_KEY);
        int advertLimitationHour = parseObject.getInt(FIELD_ADVERT_LIMITATION_HOUR);
        int lastVersionCode = parseObject.getInt(FIELD_LAST_VERSION_CODE);
        boolean isLastVersionCritical = parseObject.getBoolean(FIELD_IS_LAST_VERSION_CRITICAL);
        String lastVersionMessage = "";
        try {
            lastVersionMessage = parseObject.getString(FIELD_LAST_VERSION_MESSAGE_ +
                    context.getString(R.string.language_code));
        } catch (Exception e) {
            Timber.i(e, e.getMessage());
        }

        Config config = new Config();
        config.setLicenceKey(licenceKey);
        config.setAdvertLimitationHour(advertLimitationHour);
        config.setLastVersionCode(lastVersionCode);
        config.setIsLastVersionCritical(isLastVersionCritical);
        config.setLastVersionMessage(lastVersionMessage);
        return config;
    }
}
