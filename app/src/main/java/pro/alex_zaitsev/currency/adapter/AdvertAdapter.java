package pro.alex_zaitsev.currency.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.Constants;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.object.WhoRide;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.utils.Currency;
import pro.alex_zaitsev.currency.utils.MaterialShapeGenerator;
import pro.alex_zaitsev.currency.utils.PlaceConverter;

/**
 * Created by rocknow on 01.03.2015.
 */
public class AdvertAdapter extends BaseContextAdapter<Advert> {

    private int colorBgEnabled;
    private int colorBgDisabled;
    private int colorBgColored;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.ADAPTER_TIME_FORMAT);
    private MaterialShapeGenerator shapeGenerator;
    private boolean isShowAllCities;
    private String[] cities;

    public AdvertAdapter(Context context, List<Advert> list) {
        super(context, list);
        colorBgEnabled = context.getResources().getColor(R.color.bg);
        colorBgDisabled = context.getResources().getColor(R.color.bg_disabled);
        colorBgColored = context.getResources().getColor(R.color.bg_colored);
        shapeGenerator = new MaterialShapeGenerator(context);
        cities = context.getResources().getStringArray(R.array.cities);
        recalculate();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_advert, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Advert item = getItem(i);

        holder.txtDate.setBackgroundDrawable(shapeGenerator.generateAndCacheCircleDrawable());
        holder.txtDate.setText(sdf.format(item.getCreatedAt()));
        holder.txtRate.setText(String.format("%.2f", item.getRate()));

        String currency = "";
        int currencyResId = Currency.getCurrencyResId(item.getCurrencyFrom());
        if (currencyResId != -1) {
            currency = context.getString(currencyResId);
        }
        holder.txtAmount.setText((int) item.getAmount() + " " + currency);
        holder.checkWhoRide.setChecked(item.getWhoRide() == WhoRide.CAN_RIDE);
        if (isShowAllCities) {
            holder.layoutInParts.setVisibility(View.GONE);
            holder.layoutCity.setVisibility(View.VISIBLE);
            holder.txtCity.setText(cities[PlaceConverter.getListIndex((int) item.getCityId())]);
        } else {
            holder.layoutInParts.setVisibility(View.VISIBLE);
            holder.layoutCity.setVisibility(View.GONE);
            holder.checkInParts.setChecked(item.isInParts());
        }

        int bgColor = item.isEnabled() ? (item.isColored() ? colorBgColored : colorBgEnabled) : colorBgDisabled;
        holder.layoutCard.setCardBackgroundColor(bgColor);
        return view;
    }

    private void recalculate() {
        isShowAllCities = App.getSharedPrefManager().getCityId() == Constants.CITY_ID_ALL;
    }

    @Override
    public void notifyDataSetChanged() {
        recalculate();
        super.notifyDataSetChanged();
    }

    public class ViewHolder {

        @Bind(R.id.layout_card)
        CardView layoutCard;
        @Bind(R.id.txt_date)
        TextView txtDate;
        @Bind(R.id.txt_rate)
        TextView txtRate;
        @Bind(R.id.txt_amount)
        TextView txtAmount;
        @Bind(R.id.check_who_ride)
        CheckBox checkWhoRide;
        @Bind(R.id.check_in_parts)
        CheckBox checkInParts;
        @Bind(R.id.layout_in_parts)
        View layoutInParts;
        @Bind(R.id.layout_city)
        View layoutCity;
        @Bind(R.id.txt_city)
        TextView txtCity;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
