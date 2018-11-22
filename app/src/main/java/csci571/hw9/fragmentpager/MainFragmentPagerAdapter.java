package csci571.hw9.fragmentpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import csci571.hw9.fragment.FavoriteFragment;
import csci571.hw9.fragment.FormFragment;
import csci571.hw9.R;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FormFragment.getInstance();
        } else {
            return FavoriteFragment.getInstance();
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.search);
            case 1:
                return mContext.getString(R.string.favorite);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
