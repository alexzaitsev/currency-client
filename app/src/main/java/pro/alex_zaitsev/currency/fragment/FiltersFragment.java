package pro.alex_zaitsev.currency.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import pro.alex_zaitsev.currency.Constants;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.interfaces.callback.FiltersFragmentCallback;

/**
 * Created by rocknow on 26.04.2015.
 */
public class FiltersFragment extends BaseFragment<FiltersFragmentCallback> {

    @Bind(R.id.txt_found_count)
    TextView txtFoundCount;
    @Bind(R.id.divider_found_count)
    View dividerFoundCount;
    @Bind(R.id.radio_buy)
    RadioButton radioBuy;
    @Bind(R.id.radio_sell)
    RadioButton radioSell;
    @Bind(R.id.spinner_city)
    Spinner spinnerCity;
    @Bind(R.id.check_usd)
    CheckBox checkUsd;
    @Bind(R.id.check_eur)
    CheckBox checkEur;
    @Bind(R.id.check_rub)
    CheckBox checkRub;
    @Bind(R.id.spinner_sort)
    Spinner spinnerSort;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        ButterKnife.bind(this, view);

        if (callback.getFilterBuySell() == 0) {
            radioBuy.setChecked(true);
        } else {
            radioSell.setChecked(true);
        }
        radioBuy.setOnCheckedChangeListener(buySellClickListener);

        String[] cities = getResources().getStringArray(R.array.cities);
        List<String> cityData = new ArrayList<>(Arrays.asList(cities));
        cityData.add(0, getString(R.string.all_cities));
        SpinnerAdapter cityAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, cityData);
        spinnerCity.setAdapter(cityAdapter);
        spinnerCity.setSelection(callback.getFilterCityPosition());
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                callback.onFilterCityClick(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] currencies = callback.getFilterCurrencies();
        checkUsd.setChecked(isEnabled(currencies, Constants.USD));
        checkEur.setChecked(isEnabled(currencies, Constants.EUR));
        checkRub.setChecked(isEnabled(currencies, Constants.RUB));
        checkUsd.setOnClickListener(currencyClickListener);
        checkEur.setOnClickListener(currencyClickListener);
        checkRub.setOnClickListener(currencyClickListener);

        SpinnerAdapter sortAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner,
                getResources().getStringArray(R.array.sort));
        spinnerSort.setAdapter(sortAdapter);
        spinnerSort.setSelection(callback.getFilterSort());
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                callback.onFilterSortClick(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private CompoundButton.OnCheckedChangeListener buySellClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            int buyOrSell = radioBuy.isChecked() ? Constants.POSITION_BUY : Constants.POSITION_SELL;
            callback.onFilterBuySellClick(buyOrSell);
        }
    };

    private View.OnClickListener currencyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CheckBox currCheck = (CheckBox) view;
            if (!checkUsd.isChecked() && !checkEur.isChecked() && !checkRub.isChecked()) {
                currCheck.setChecked(true);
            }
            List<String> currencies = new ArrayList<>();
            if (checkUsd.isChecked()) {
                currencies.add(Constants.USD);
            }
            if (checkEur.isChecked()) {
                currencies.add(Constants.EUR);
            }
            if (checkRub.isChecked()) {
                currencies.add(Constants.RUB);
            }
            callback.onFilterCurrencyClick(currencies.toArray(new String[currencies.size()]));
        }
    };

    public void setFoundCount(int foundCount, int allCount) {
        if (isAdded()) {
            dividerFoundCount.setVisibility(View.VISIBLE);
            txtFoundCount.setVisibility(View.VISIBLE);
            txtFoundCount.setText(getString(R.string.found_from, foundCount, allCount));
        }
    }

    private boolean isEnabled(String[] currencies, String currency) {
        for (String cur : currencies) {
            if (cur.equalsIgnoreCase(currency)) {
                return true;
            }
        }
        return false;
    }
}
