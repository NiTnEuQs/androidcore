package fr.dtrx.androidcore.fragments;

import android.content.Intent;

import androidx.fragment.app.Fragment;

@SuppressWarnings("unused")
public class ActivityResultFragment extends Fragment {

    private OnActivityResultListener activityResultListener;

    public ActivityResultFragment() {
    }

    public OnActivityResultListener getActivityResultListener() {
        return activityResultListener;
    }

    public void setActivityResultListener(OnActivityResultListener activityResultListener) {
        this.activityResultListener = activityResultListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityResultListener != null) {
            activityResultListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
