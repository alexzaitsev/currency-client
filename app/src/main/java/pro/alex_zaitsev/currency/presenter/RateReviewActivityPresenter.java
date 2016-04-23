package pro.alex_zaitsev.currency.presenter;

import android.content.Intent;
import android.net.Uri;

import pro.alex_zaitsev.currency.Constants;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.activity.RateReviewActivity;
import pro.alex_zaitsev.currency.presenter.core.ActivityPresenter;
import pro.alex_zaitsev.currency.utils.IntentHelper;

/**
 * Created by rocknow on 15.03.2015.
 */
public class RateReviewActivityPresenter extends ActivityPresenter<RateReviewActivity> {

    private static final int MIN_RATING_GOOD = 4;
    private static final String MAILTO = "mailto";

    public RateReviewActivityPresenter(RateReviewActivity activity) {
        super(activity);
    }

    public boolean isRatingGood(int rating) {
        return rating >= MIN_RATING_GOOD;
    }

    public void goMarket() {
        IntentHelper.openMarket(activity);
    }

    public void sendEmail(int rating, String feedback) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                MAILTO, Constants.EMAIL_FEEDBACK, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.subject_feedback, rating));
        intent.putExtra(Intent.EXTRA_TEXT, feedback);
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.email_chooser)));
    }
}
