package pro.alex_zaitsev.currency.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;

import butterknife.ButterKnife;
import butterknife.Bind;
import pro.alex_zaitsev.currency.App;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.event.AdvertDeletedEvent;
import pro.alex_zaitsev.currency.event.AdvertUpdatedEvent;
import pro.alex_zaitsev.currency.fragment.AdvertFragment;
import pro.alex_zaitsev.currency.interfaces.callback.AdvertFragmentCallback;
import pro.alex_zaitsev.currency.object.model.Advert;
import pro.alex_zaitsev.currency.presenter.DetailsActivityPresenter;
import pro.alex_zaitsev.currency.utils.Share;
import pro.alex_zaitsev.currency.utils.Toaster;
import pro.alex_zaitsev.currency.view.AnimatedViewSwitcher;

/**
 * Created by rocknow on 01.03.2015.
 */
public class DetailsActivity extends BaseUpActivity<DetailsActivityPresenter>
        implements AdvertFragmentCallback {

    private static final String EXTRA_ADVERT = "EXTRA_ADVERT";
    private AdvertFragment advertFragment;
    private Advert advert;
    private boolean isEditMode = false;
    private AnimatedViewSwitcher viewSwitcher;

    @Bind(android.R.id.progress)
    View progress;
    @Bind(R.id.content)
    View content;

    @Override
    protected void setPresenter() {
        presenter = new DetailsActivityPresenter(this);
    }

    public static Intent newIntent(Context context, Advert advert) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_ADVERT, advert);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        advert = getIntent().getParcelableExtra(EXTRA_ADVERT);
        setFragment(AdvertFragment.newInstance(advert));
        viewSwitcher = new AnimatedViewSwitcher(content, progress);
    }

    @Override
    public void onBackPressed() {
        if (isEditMode) {
            new MaterialDialog.Builder(this)
                    .title(R.string.dialog_cancel_title)
                    .content(R.string.dialog_cancel_edit_message)
                    .positiveText(R.string.yes_exit)
                    .negativeText(R.string.no_stay)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            DetailsActivity.super.onBackPressed();
                        }
                    }).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEditMode) {
            getMenuInflater().inflate(R.menu.done, menu);
        } else {
            getMenuInflater().inflate(presenter.isUserOwner(advert) ?
                    R.menu.edit_share : R.menu.share, menu);
            MenuItem shareItem = menu.findItem(R.id.action_share);
            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
            shareActionProvider.setShareIntent(Share.getShareIntent(this, advert));
            shareActionProvider.setOnShareTargetSelectedListener(
                    new ShareActionProvider.OnShareTargetSelectedListener() {
                        @Override
                        public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                            App.getAnalytics().onShare();
                            return true;
                        }
                    });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            App.getAnalytics().onActionsFromMenu();
            onShowActionsClick();
            return true;
        } else if (id == R.id.action_done) {
            hideKeyboard();
            if (advertFragment.isDataValid()) {
                advert.fillSmart(advertFragment.getData());
                presenter.updateAdvert(advert);
                App.getAnalytics().onDoneFromMenuSuccess();
            } else {
                App.getAnalytics().onDoneFromMenuDataNotValid();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onShowActionsClick() {
        new BottomSheet.Builder(this).
                sheet(advert.isEnabled() ? R.menu.details_off : R.menu.details_on).
                listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.action_edit:
                                onEditClick();
                                break;
                            case R.id.action_visibility_off:
                                // make visibility off
                                onEnableClick(false);
                                break;
                            case R.id.action_visibility_on:
                                // make visible
                                onEnableClick(true);
                                break;
                            case R.id.action_delete:
                                onDeleteClick();
                                break;
                        }
                    }
                }).show();
    }

    private void onEditClick() {
        App.getAnalytics().onEdit();
        isEditMode = true;
        advertFragment.updateEditMode();
        supportInvalidateOptionsMenu();
    }

    private void onDeleteClick() {
        App.getAnalytics().onDelete();
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_delete_title)
                .content(R.string.dialog_delete_message)
                .positiveText(R.string.action_delete)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        App.getAnalytics().onDeleteOk();
                        presenter.deleteAdvert(advert);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        App.getAnalytics().onDeleteCancel();
                    }
                })
                .show();
    }

    private void onEnableClick(boolean enable) {
        if (enable) {
            App.getAnalytics().onEnable();
        } else {
            App.getAnalytics().onDisable();
        }
        advert.setIsEnabled(enable);
        viewSwitcher.showPrimary(false);
        presenter.updateAdvert(advert);
    }

    private void setFragment(AdvertFragment fragment) {
        advertFragment = fragment;
        super.setFragment(R.id.content, fragment);
    }

    @Override
    public boolean isEditMode() {
        return isEditMode;
    }

    @Override
    public boolean isBtnCallVisible() {
        return !presenter.isUserOwner(advert);
    }

    @Override
    public void onBtnCallClick() {
        App.getAnalytics().onCall();
        if (!TextUtils.isEmpty(advert.getPhone())) {
            presenter.startCall(advert.getPhone());
        }
    }

    @Override
    public int getCityPosition() {
        return presenter.getCityPosition();
    }

    @Override
    public int getFilterBuySell() {
        return -1;
    }

    public void onEvent(AdvertDeletedEvent event) {
        if (event.isSuccess()) {
            Toaster.makeToastShort(this, R.string.toast_deleted_success);
            setResult(RESULT_FIRST_USER);
            finish();
        } else {
            Toaster.makeToastShort(this, R.string.toast_deleted_fail);
        }
    }

    public void onEvent(AdvertUpdatedEvent event) {
        if (isEditMode) {
            isEditMode = false;
            supportInvalidateOptionsMenu();
        }
        viewSwitcher.showPrimary(true);
        Toaster.makeToastShort(this, event.isSuccess() ?
                R.string.toast_update_success : R.string.toast_update_fail);
        if (event.isSuccess()) {
            advert = event.getAdvert();
            setFragment(AdvertFragment.newInstance(advert));
            setResult(RESULT_FIRST_USER);
        } else {
            advertFragment.updateEditMode();
        }
    }
}
