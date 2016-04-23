package pro.alex_zaitsev.currency.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.Constants;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.interfaces.callback.AdvertFragmentCallback;
import pro.alex_zaitsev.currency.object.WhoRide;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.utils.PlaceConverter;
import pro.alex_zaitsev.currency.utils.Toaster;
import pro.alex_zaitsev.currency.utils.provider.CurrencyProvider;
import pro.alex_zaitsev.currency.utils.provider.LocationProvider;

/**
 * Created by rocknow on 28.02.2015.
 */
public class AdvertFragment extends BaseFragment<AdvertFragmentCallback> {

    private static final String EXTRA_ADVERT = "EXTRA_ADVERT";

    private Advert advert;

    @Bind(R.id.spinner_buy_sell)
    Spinner spinnerBuySell;
    @Bind(R.id.edit_amount)
    @NotEmpty(messageId = R.string.required, order = 1)
    EditText editAmount;
    @Bind(R.id.spinner_currency_from)
    Spinner spinnerCurrencyFrom;
    @Bind(R.id.edit_rate)
    @NotEmpty(messageId = R.string.required, order = 2)
    EditText editRate;
    @Bind(android.R.id.text1)
    TextView txtCurrencyTo;
    @Bind(R.id.spinner_city)
    Spinner spinnerCity;
    @Bind(R.id.check_who_ride)
    CheckBox checkCanRide;
    @Bind(R.id.check_in_parts)
    CheckBox checkInParts;
    @Bind(R.id.txt_phone_example)
    TextView txtPhoneExample;
    @Bind(R.id.edit_phone)
    @MinLength(messageId = R.string.phone_validation,
            order = 3,
            value = Constants.PHONE_NUMBER_LENGTH)
    EditText editPhone;
    @Bind(R.id.txt_comment)
    TextView txtComment;
    @Bind(R.id.edit_comment)
    EditText editComment;
    @Bind(R.id.btn_call)
    View btnCall;
    @Bind(R.id.layout_in_parts)
    View layoutInParts;

    public static AdvertFragment newInstance(Advert advert) {
        AdvertFragment fragment = new AdvertFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ADVERT, advert);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(EXTRA_ADVERT)) {
                advert = args.getParcelable(EXTRA_ADVERT);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advert, container, false);
        ButterKnife.bind(this, view);

        initUi();
        if (advert == null) {
            fillDate();
        } else {
            fillDate(advert);
        }
        updateInPartsVisibility();
        updateEditMode();

        return view;
    }

    private void initUi() {
        SpinnerAdapter buySellAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner,
                getResources().getStringArray(R.array.buy_sell));
        spinnerBuySell.setAdapter(buySellAdapter);
        spinnerBuySell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateInPartsVisibility(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        SpinnerAdapter currencyAdapterFrom = new ArrayAdapter<>(getActivity(), R.layout.item_spinner,
                App.getCurrencyProvider().getCurrencyFrom());
        spinnerCurrencyFrom.setAdapter(currencyAdapterFrom);

        txtCurrencyTo.setText(Constants.DEFAULT_TO_CURRENCY);

        SpinnerAdapter cityAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner,
                getResources().getStringArray(R.array.cities));
        spinnerCity.setAdapter(cityAdapter);

        btnCall.setVisibility(callback.isBtnCallVisible() ? View.VISIBLE : View.GONE);
        btnCall.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           callback.onBtnCallClick();
                                       }
                                   });
    }

    public void fillDate(Advert advert) {
        if (!isAdded()) {
            return;
        }
        CurrencyProvider currencyProvider = new CurrencyProvider();
        spinnerBuySell.setSelection(advert.getBuySell());
        editAmount.setText(advert.getAmount() + "");
        spinnerCurrencyFrom.setSelection(currencyProvider.getPositionFrom(advert.getCurrencyFrom()));
        editRate.setText(advert.getRate() + "");
        spinnerCity.setSelection(PlaceConverter.getListIndex((int) advert.getCityId()));
        checkCanRide.setChecked(advert.getWhoRide() == WhoRide.CAN_RIDE);
        checkInParts.setChecked(advert.isInParts());
        editPhone.setText(advert.getPhone());
        editComment.setText(advert.getComment());
    }

    public void fillDate() {
        if (!isAdded()) {
            return;
        }
        spinnerBuySell.setSelection(callback.getFilterBuySell());
        spinnerCity.setSelection(callback.getCityPosition());
    }

    public void updateEditMode() {
        if (!isAdded()) {
            return;
        }
        boolean isEditMode = callback.isEditMode();
        spinnerBuySell.setEnabled(isEditMode);
        editAmount.setEnabled(isEditMode);
        spinnerCurrencyFrom.setEnabled(isEditMode);
        editRate.setEnabled(isEditMode);
        spinnerCity.setEnabled(isEditMode);
        checkCanRide.setEnabled(isEditMode);
        checkInParts.setEnabled(isEditMode);
        editPhone.setEnabled(isEditMode);
        editComment.setEnabled(isEditMode);
        txtComment.setText(isEditMode ? R.string.comment_edit_mode : R.string.comment);
        txtPhoneExample.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }

    private void updateInPartsVisibility(int buySell) {
        layoutInParts.setVisibility(buySell == Constants.POSITION_BUY ? View.GONE : View.VISIBLE);
    }

    private void updateInPartsVisibility() {
        int buySell = advert == null ? callback.getFilterBuySell() : advert.getBuySell();
        updateInPartsVisibility(buySell);
    }

    public boolean isDataValid() {
        return isAdded() &&
                FormValidator.validate(this, new SimpleErrorPopupCallback(getActivity())) &&
                isCurrencyValid();
    }

    public Advert getData() {
        if (!isAdded()) {
            return null;
        }
        Advert advert = this.advert == null ? new Advert() : this.advert;
        advert.setBuySell(spinnerBuySell.getSelectedItemPosition());
        advert.setAmount(Float.parseFloat(editAmount.getText().toString()));
        advert.setRate(Float.parseFloat(editRate.getText().toString()));
        advert.setCurrencyFrom(getCurrencyFrom());
        advert.setCurrencyTo(Constants.DEFAULT_TO_CURRENCY);
        advert.setCityId(new PlaceConverter().getCityId(spinnerCity.getSelectedItemPosition()));
        advert.setWhoRide(checkCanRide.isChecked() ? WhoRide.CAN_RIDE : WhoRide.CANNOT_RIDE);
        advert.setInParts(checkInParts.isChecked());
        advert.setPhone(editPhone.getText().toString());
        advert.setComment(editComment.getText().toString());
        advert.setCountryId(new LocationProvider().getCountryCode());
        return advert;
    }

    private boolean isCurrencyValid() {
        if (getCurrencyFrom().equals(Constants.DEFAULT_TO_CURRENCY)) {
            Toaster.makeToastShort(getActivity(), R.string.toast_warn_currency);
            return false;
        }
        return true;
    }

    private String getCurrencyFrom() {
        return spinnerCurrencyFrom.getSelectedItem().toString().toLowerCase(Locale.US);
    }
}
