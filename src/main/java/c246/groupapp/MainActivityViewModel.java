package c246.groupapp;

import androidx.lifecycle.ViewModel;

/**
 * Class just to store and keep track whether or not a user is currently signing in.
 */
public class MainActivityViewModel extends ViewModel {

    private boolean mIsSigningIn;

    public MainActivityViewModel() {
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }

}
