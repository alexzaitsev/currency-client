package pro.alex_zaitsev.currency.object.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Date;

import pro.alex_zaitsev.currency.object.WhoRide;
import pro.alex_zaitsev.currency.utils.InAppPurchase;

/**
 * Created by rocknow on 01.03.2015.
 */
public class Advert implements Parcelable {

    private String id;
    private Date createdAt;
    private float amount;
    private float rate;
    private String currencyFrom;
    private String currencyTo;
    private long districtId;
    private long cityId;
    private long countryId;
    private WhoRide whoRide;
    private String comment;
    private String ownerId;
    private String phone;
    private int lifeTime = InAppPurchase.LIFE_TIME_DEFAULT;
    private boolean inParts;
    private boolean isEnabled = true;
    private boolean isColored = false;
    private boolean isTop = false;
    private String source;
    /**
     * 0 - buy, 1 - sell
     */
    private int buySell;

    public Advert() {}

    public Advert(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public WhoRide getWhoRide() {
        return whoRide;
    }

    public void setWhoRide(WhoRide whoRide) {
        this.whoRide = whoRide;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public boolean isInParts() {
        return inParts;
    }

    public void setInParts(boolean inParts) {
        this.inParts = inParts;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isColored() {
        return isColored;
    }

    public void setIsColored(boolean isColored) {
        this.isColored = isColored;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getBuySell() {
        return buySell;
    }

    public void setBuySell(int buySell) {
        this.buySell = buySell;
    }

    public void fill(Advert advert) {
        this.createdAt = advert.getCreatedAt() == null ? null :
                new Date(advert.getCreatedAt().getTime());
        this.amount = advert.getAmount();
        this.rate = advert.getRate();
        this.currencyFrom = advert.getCurrencyFrom();
        this.currencyTo = advert.getCurrencyTo();
        this.districtId = advert.getDistrictId();
        this.cityId = advert.getCityId();
        this.countryId = advert.getCountryId();
        this.whoRide = advert.getWhoRide() == null ? null :
                WhoRide.values()[advert.getWhoRide().ordinal()];
        this.comment = advert.getComment();
        this.ownerId = advert.getOwnerId();
        this.phone = advert.getPhone();
        this.lifeTime = advert.getLifeTime();
        this.inParts = advert.isInParts();
        this.isEnabled = advert.isEnabled();
        this.isColored = advert.isColored();
        this.isTop = advert.isTop();
        this.source = advert.getSource();
        this.buySell = advert.getBuySell();
    }

    public void fillSmart(Advert advert) {
        String ownerId = TextUtils.isEmpty(advert.getOwnerId()) ? this.ownerId : advert.getOwnerId();
        fill(advert);
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Advert advert = (Advert) o;

        return !(id != null ? !id.equals(advert.id) : advert.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.createdAt == null ? -1 : this.createdAt.getTime());
        dest.writeFloat(this.amount);
        dest.writeFloat(this.rate);
        dest.writeString(this.currencyFrom);
        dest.writeString(this.currencyTo);
        dest.writeLong(this.districtId);
        dest.writeLong(this.cityId);
        dest.writeLong(this.countryId);
        dest.writeInt(this.whoRide == null ? -1 : this.whoRide.ordinal());
        dest.writeString(this.comment);
        dest.writeString(this.ownerId);
        dest.writeString(this.phone);
        dest.writeInt(this.lifeTime);
        dest.writeByte(inParts ? (byte) 1 : (byte) 0);
        dest.writeByte(isEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(isColored ? (byte) 1 : (byte) 0);
        dest.writeByte(isTop ? (byte) 1 : (byte) 0);
        dest.writeString(source);
        dest.writeInt(buySell);
    }

    private Advert(Parcel in) {
        this.id = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.amount = in.readFloat();
        this.rate = in.readFloat();
        this.currencyFrom = in.readString();
        this.currencyTo = in.readString();
        this.districtId = in.readLong();
        this.cityId = in.readLong();
        this.countryId = in.readLong();
        int tmpWhoRide = in.readInt();
        this.whoRide = tmpWhoRide == -1 ? null : WhoRide.values()[tmpWhoRide];
        this.comment = in.readString();
        this.ownerId = in.readString();
        this.phone = in.readString();
        this.lifeTime = in.readInt();
        this.inParts = in.readByte() != 0;
        this.isEnabled = in.readByte() != 0;
        this.isColored = in.readByte() != 0;
        this.isTop = in.readByte() != 0;
        this.source = in.readString();
        this.buySell = in.readInt();
    }

    public static final Parcelable.Creator<Advert> CREATOR = new Parcelable.Creator<Advert>() {
        public Advert createFromParcel(Parcel source) {
            return new Advert(source);
        }

        public Advert[] newArray(int size) {
            return new Advert[size];
        }
    };

}
