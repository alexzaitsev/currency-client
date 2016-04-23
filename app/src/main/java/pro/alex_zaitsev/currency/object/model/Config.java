package pro.alex_zaitsev.currency.object.model;

/**
 * Created by rocknow on 18.03.2015.
 */
public class Config {

    private String licenceKey;
    private int advertLimitationHour;
    private int lastVersionCode;
    private boolean isLastVersionCritical;
    private String lastVersionMessage;

    public String getLicenceKey() {
        return licenceKey;
    }

    public void setLicenceKey(String licenceKey) {
        this.licenceKey = licenceKey;
    }

    public int getAdvertLimitationHour() {
        return advertLimitationHour;
    }

    public void setAdvertLimitationHour(int advertLimitationHour) {
        this.advertLimitationHour = advertLimitationHour;
    }

    public int getLastVersionCode() {
        return lastVersionCode;
    }

    public void setLastVersionCode(int lastVersionCode) {
        this.lastVersionCode = lastVersionCode;
    }

    public boolean isLastVersionCritical() {
        return isLastVersionCritical;
    }

    public void setIsLastVersionCritical(boolean isLastVersionCritical) {
        this.isLastVersionCritical = isLastVersionCritical;
    }

    public String getLastVersionMessage() {
        return lastVersionMessage;
    }

    public void setLastVersionMessage(String lastVersionMessage) {
        this.lastVersionMessage = lastVersionMessage;
    }
}
