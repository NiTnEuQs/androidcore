package fr.dtrx.androidcore.basics;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import fr.dtrx.androidcore.utils.KeyboardUtils;
import fr.dtrx.androidcore.utils.NetworkUtils;

@SuppressWarnings("unused")
public abstract class BaseFragment extends Fragment {

    protected Bundle bundle;
    protected View view;
    protected BaseActivity parentActivity;
    protected ViewDataBinding binding;
    protected ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeData();

        bundle = getArguments();
        viewGroup = container;

        initializeView(savedInstanceState);
        initializeListeners();

        return view;
    }

    public abstract int layoutResId();

    /**
     * Views initialization
     */
    public void initializeData() {
    }

    /**
     * Views initialization
     */
    public void initializeView(Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), layoutResId(), viewGroup, false);

        view = binding.getRoot();

        if (getActivity() instanceof BaseActivity) {
            parentActivity = (BaseActivity) getActivity();
            parentActivity.initializeToolbar();
        }
    }

    /**
     * Listeners initialization
     */
    public void initializeListeners() {
    }

    protected void hideKeyboard() {
        KeyboardUtils.hideKeyboard(getActivity());
    }

    protected boolean isConnected() {
        return NetworkUtils.isConnected(getContext());
    }

    protected void back() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends ViewDataBinding> T getBinding() {
        return (T) binding;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Activity> T getParentActivity() {
        return (T) parentActivity;
    }

}
