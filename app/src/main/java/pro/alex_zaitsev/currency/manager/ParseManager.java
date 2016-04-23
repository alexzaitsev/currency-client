package pro.alex_zaitsev.currency.manager;

import android.content.Context;
import android.content.Intent;

import com.parse.ConfigCallback;
import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.event.AdvertDeletedEvent;
import pro.alex_zaitsev.currency.event.AdvertGetEvent;
import pro.alex_zaitsev.currency.event.AdvertSavedEvent;
import pro.alex_zaitsev.currency.event.AdvertUpdatedEvent;
import pro.alex_zaitsev.currency.event.ForgetEvent;
import pro.alex_zaitsev.currency.event.GetConfigEvent;
import pro.alex_zaitsev.currency.event.LoginEvent;
import pro.alex_zaitsev.currency.event.MyAdvertCountEvent;
import pro.alex_zaitsev.currency.event.MyAdvertGetEvent;
import pro.alex_zaitsev.currency.event.SignupEvent;
import pro.alex_zaitsev.currency.object.mapper.AdvertObjectMapper;
import pro.alex_zaitsev.currency.object.mapper.ConfigObjectMapper;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.object.model.Config;

/**
 * Created by rocknow on 28.02.2015.
 */
public class ParseManager {

    public static final String CLASS_NAME_ADVERT = "Advert";

    public ParseManager(Context context) {
        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(context.getString(R.string.parse_app_id))
                .clientKey(context.getString(R.string.parse_client_key))
                .server("http://currencyua.herokuapp.com/parse/") // The trailing slash is important.
                .build());
    }

    // =========================================
    // ANALYTICS
    // =========================================

    public void trackAppOpened(Intent intent) {
        ParseAnalytics.trackAppOpenedInBackground(intent);
    }

    // =========================================
    // CONFIG
    // =========================================

    public void getConfigInBackground(final Context context) {
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig parseConfig, ParseException e) {
                if (parseConfig != null && e == null) {
                    Config config = getConfig(context);
                    App.getEventBus().post(new GetConfigEvent(config));
                }
            }
        });
    }

    public Config getConfig(Context context) {
        ParseConfig parseConfig = ParseConfig.getCurrentConfig();
        return parseConfig == null ? null : ConfigObjectMapper.parseToConfig(context, parseConfig);
    }

    // =========================================
    // ADVERT
    // =========================================

    public void saveAdvert(final Advert advert) {
        final ParseObject obj = AdvertObjectMapper.advertToParse(advert);
        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    advert.setId(obj.getObjectId());
                    advert.setCreatedAt(obj.getCreatedAt());
                    App.getEventBus().post(new AdvertSavedEvent(advert));
                } else {
                    App.getEventBus().post(new AdvertSavedEvent(e));
                }
            }
        });
    }

    public void getAdverts(int skip, int limit, int countryId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_NAME_ADVERT);
        query.whereEqualTo(AdvertObjectMapper.FIELD_ENABLED, true);
        query.whereEqualTo(AdvertObjectMapper.FIELD_COUNTRY_ID, countryId);
        query.addAscendingOrder(AdvertObjectMapper.FIELD_CREATED_AT);
        query.setSkip(skip);
        query.setLimit(limit);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    List<Advert> advertList = new ArrayList<>();
                    if (parseObjects != null && !parseObjects.isEmpty()) {
                        for (ParseObject obj : parseObjects) {
                            Advert advert = AdvertObjectMapper.parseToAdvert(obj);
                            advertList.add(advert);
                        }
                    }
                    App.getEventBus().post(new AdvertGetEvent(advertList));
                } else {
                    App.getEventBus().post(new AdvertGetEvent(e));
                }
            }
        });
    }

    public void getMyAdverts() {
        if (getUser() == null) {
            return;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_NAME_ADVERT);
        query.whereEqualTo(AdvertObjectMapper.FIELD_OWNER_ID, getUser().getObjectId());
        query.addAscendingOrder(AdvertObjectMapper.FIELD_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    List<Advert> advertList = new ArrayList<>();
                    if (parseObjects != null && !parseObjects.isEmpty()) {
                        for (ParseObject obj : parseObjects) {
                            Advert advert = AdvertObjectMapper.parseToAdvert(obj);
                            advertList.add(advert);
                        }
                    }
                    App.getEventBus().post(new MyAdvertGetEvent(advertList));
                } else {
                    App.getEventBus().post(new MyAdvertGetEvent(e));
                }
            }
        });
    }

    public void getMyAdvertsCount() {
        if (getUser() == null) {
            return;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_NAME_ADVERT);
        query.whereEqualTo(AdvertObjectMapper.FIELD_OWNER_ID, getUser().getObjectId());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null) {
                    App.getEventBus().postSticky(new MyAdvertCountEvent(i));
                } else {
                    App.getEventBus().postSticky(new MyAdvertCountEvent(e));
                }
            }
        });
    }

    public void deleteAdvert(final Advert advert) {
        AdvertObjectMapper.advertToParse(advert).deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    App.getEventBus().post(new AdvertDeletedEvent(advert));
                } else {
                    App.getEventBus().post(new AdvertDeletedEvent(e));
                }
            }
        });
    }

    public void updateAdvert(final Advert advert) {
        AdvertObjectMapper.advertToParse(advert).saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    App.getEventBus().post(new AdvertUpdatedEvent(advert));
                } else {
                    App.getEventBus().post(new AdvertUpdatedEvent(e));
                }
            }
        });
    }

    // =========================================
    // USER
    // =========================================

    public void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    App.getEventBus().post(new LoginEvent(user));
                } else {
                    App.getEventBus().post(new LoginEvent(e));
                }
            }
        });
    }

    public void signUp(String username, String password) {
        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(username);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    App.getEventBus().post(new SignupEvent(ParseUser.getCurrentUser()));
                } else {
                    App.getEventBus().post(new SignupEvent(e));
                }
            }
        });
    }

    public ParseUser getUser() {
        return ParseUser.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return ParseUser.getCurrentUser() != null;
    }

    public void restorePassword(String email) {
        ParseUser.requestPasswordResetInBackground(email,
                new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        App.getEventBus().post(new ForgetEvent(e));
                    }
                });
    }

    public void signOut() {
        ParseUser.logOut();
    }
}
