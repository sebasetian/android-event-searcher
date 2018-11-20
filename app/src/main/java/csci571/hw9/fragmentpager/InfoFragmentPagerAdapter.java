package csci571.hw9.fragmentpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import csci571.hw9.R;
import csci571.hw9.fragment.ArtistFragment;
import csci571.hw9.fragment.EventInfoFragment;
import csci571.hw9.fragment.UpcomingFragment;
import csci571.hw9.fragment.VenueFragment;

public class InfoFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    public InfoFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return EventInfoFragment.newInstance();
        } else if (position == 1) {
            return ArtistFragment.newInstance();
        } else if (position == 2) {
            return VenueFragment.newInstance();
        } else {
            return UpcomingFragment.newInstance();
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.eventTitle);
            case 1:
                return mContext.getString(R.string.artistTitle);
            case 2:
                return mContext.getString(R.string.venueTitle);
            case 3:
                return mContext.getString(R.string.upcomingTitle);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
