package pro.alex_zaitsev.currency.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.Bind;
import pro.alex_zaitsev.currency.R;
import pro.alex_zaitsev.currency.interfaces.callback.BaseListFragmentCallback;
import pro.alex_zaitsev.currency.view.AnimatedViewSwitcher;

/**
 * Created by rocknow on 13.03.2015.
 */
public abstract class BaseListFragment<C extends BaseListFragmentCallback> extends BaseFragment<C> {

    protected AnimatedViewSwitcher viewSwitcher;
    protected boolean isLoading;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(android.R.id.list)
    ListView listView;
    @Bind(android.R.id.empty)
    ViewGroup emptyView;
    @Bind(android.R.id.content)
    View contentView;
    @Bind(android.R.id.progress)
    View progressMain;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list, container, false);
        ButterKnife.bind(this, view);
        viewSwitcher = new AnimatedViewSwitcher(contentView, progressMain);
        initSwipeRefreshLayout();
        initEmptyView(LayoutInflater.from(getActivity()));
        startLoading();
        return view;
    }

    private void initSwipeRefreshLayout() {
            swipeRefreshLayout.setColorSchemeColors(
                    getResources().getColor(R.color.primary_dark));
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (!isLoading) {
                        isLoading = true;
                        swipeRefreshLayout.setRefreshing(true);
                        onLoadStarted();
                    }
                }
            });
    }

    public void startLoading() {
        viewSwitcher.showPrimary(false);
        if (callback.isOnline()) {
            onLoadStarted();
        } else {
            callback.notifyNotOnline();
            checkEmptyView();
        }
    }

    private void showContent(boolean showContent) {
        if (showContent) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    protected void checkEmptyView() {
        showContent(getCurrentItemsCount() > 0);
        viewSwitcher.showPrimary(true);
    }

    protected void onLoadCompleted() {
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    protected abstract void initEmptyView(LayoutInflater inflater);

    protected abstract int getCurrentItemsCount();

    protected abstract void onLoadStarted();

}
