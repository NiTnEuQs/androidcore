package fr.dtrx.androidcore.basics;

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

@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public abstract class BaseFragment<T extends BaseActivity, Y extends ViewDataBinding> extends Fragment {

    protected T parentActivity;
    protected Y binding;

    protected Bundle bundle;
    protected View view;
    protected ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        viewGroup = container;

        initializeData();
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
        parentActivity = (T) getActivity();

        if (parentActivity != null) {
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

}
