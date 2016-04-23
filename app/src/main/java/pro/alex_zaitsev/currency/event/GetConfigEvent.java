package pro.alex_zaitsev.currency.event;

import pro.alex_zaitsev.currency.object.model.Config;

/**
 * Created by rocknow on 25.03.2015.
 */
public class GetConfigEvent extends ApiEvent {

    private Config config;

    public GetConfigEvent(Config config) {
        this.config = config;
    }

    public boolean isSuccess() {
        return config != null;
    }

    public Config getConfig() {
        return config;
    }
}
