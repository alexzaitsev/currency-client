package pro.alex_zaitsev.currency.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.interfaces.callback.LoginFragmentCallback;
import pro.alex_zaitsev.currency.presenter.CreateAdvertActivityPresenter;
import pro.alex_zaitsev.currency.view.AnimatedViewSwitcher;

import static eu.inmite.android.lib.validations.form.annotations.RegExp.EMAIL;

/**
 * Created by rocknow on 05.03.2015.
 */
public class LoginFragment extends BaseFragment<LoginFragmentCallback> {

    private static final int ORDER_EMAIL = 1;
    private static final int ORDER_PASSWORD = 2;
    private static final int ORDER_PASSWORD_REPEAT = 3;

    private AnimatedViewSwitcher viewSwitcher;

    @Bind(R.id.txt_login_info)
    TextView txtLoginInfo;
    @Bind(R.id.email)
    @RegExp(value = EMAIL, messageId = R.string.error_invalid_email, order = ORDER_EMAIL)
    EditText editEmail;
    @Bind(R.id.password)
    @MinLength(messageId = R.string.error_invalid_password,
            order = ORDER_PASSWORD,
            value = CreateAdvertActivityPresenter.MIN_PASSWORD_LENGTH)
    EditText editPassword;
    @Bind(R.id.password_repeat)
    @MinLength(messageId = R.string.error_invalid_password,
            order = ORDER_PASSWORD_REPEAT,
            value = CreateAdvertActivityPresenter.MIN_PASSWORD_LENGTH)
    EditText editPasswordRepeat;
    @Bind(android.R.id.progress)
    View progress;
    @Bind(R.id.email_sign_in_button)
    Button btnLogin;
    @Bind(R.id.txt_sign_up)
    TextView txtSignup;
    @Bind(R.id.txt_forgot)
    TextView txtForgot;
    @Bind(R.id.login_form)
    View layoutLogin;
    @Bind(R.id.txt_password_info)
    TextView txtPasswordInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        txtLoginInfo.setVisibility(callback.isShowInfo() ? View.VISIBLE : View.GONE);
        txtLoginInfo.setText(getString(R.string.login_info, callback.getShowData()));
        viewSwitcher = new AnimatedViewSwitcher(layoutLogin, progress);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        SpannableString signupText = new SpannableString(getString(R.string.action_sign_up));
        signupText.setSpan(new UnderlineSpan(), 0, signupText.length(), 0);
        txtSignup.setText(signupText);
        txtSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toggleSignUpMode();
            }
        });
        SpannableString forgotText = new SpannableString(getString(R.string.action_forgot));
        forgotText.setSpan(new UnderlineSpan(), 0, forgotText.length(), 0);
        txtForgot.setText(forgotText);
        txtForgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FormValidator.validate(LoginFragment.this, forgotValidator);
            }
        });

        return view;
    }

    private IValidationCallback forgotValidator = new IValidationCallback() {
        @Override
        public void validationComplete(boolean result, List<FormValidator.ValidationFail> failedValidations) {
            boolean validated = true;
            for (FormValidator.ValidationFail validationFail : failedValidations) {
                if (validationFail.order == ORDER_EMAIL) {
                    editEmail.setError(validationFail.message);
                    validated = false;
                    break;
                }
            }
            if (validated) {
                showProgress(true);
                callback.onForgotClick(editEmail.getText().toString());
            }
        }
    };

    public boolean isDataValid() {
        return FormValidator.validate(this, new SimpleErrorPopupCallback(getActivity()));
    }

    private void showProgress(boolean show) {
        viewSwitcher.showPrimary(!show);
    }

    private void toggleSignUpMode() {
        boolean signUp = editPasswordRepeat.getVisibility() == View.GONE;
        editPasswordRepeat.setVisibility(signUp ? View.VISIBLE : View.GONE);
        txtPasswordInfo.setVisibility(signUp ? View.VISIBLE : View.GONE);
        btnLogin.setText(signUp ? R.string.action_sign_up : R.string.action_sign_in);
        SpannableString forgotText = new SpannableString(getString(
                signUp ? R.string.action_sign_in : R.string.action_sign_up));
        forgotText.setSpan(new UnderlineSpan(), 0, forgotText.length(), 0);
        txtSignup.setText(forgotText);
    }

    private void login() {
        if (isDataValid()) {
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            if (editPasswordRepeat.getVisibility() == View.VISIBLE) {
                // sign up
                String passwordRepeat = editPasswordRepeat.getText().toString();
                if (password.equals(passwordRepeat)) {
                    showProgress(true);
                    callback.onSignupClick(email, password);
                } else {
                    editPasswordRepeat.setError(getString(R.string.passwords_not_equal));
                }
            } else {
                // login
                showProgress(true);
                callback.onLoginClick(email, password);
            }
        }
    }

    public void finishLogin() {
        if (!isAdded()) {
            return;
        }
        showProgress(false);
    }
}
