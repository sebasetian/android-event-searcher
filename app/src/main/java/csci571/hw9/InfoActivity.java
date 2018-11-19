package csci571.hw9;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.gson.Gson;
import csci571.hw9.fragment.ArtistFragment;
import csci571.hw9.fragment.ArtistFragment.OnFragmentInteractionListener;
import csci571.hw9.fragment.EventInfoFragment;
import csci571.hw9.fragment.ResultFragment.OnListFragmentInteractionListener;
import csci571.hw9.fragment.UpcomingFragment;
import csci571.hw9.fragment.VenueFragment;
import csci571.hw9.fragmentpager.InfoFragmentPagerAdapter;
import csci571.hw9.fragmentpager.MainFragmentPagerAdapter;
import csci571.hw9.model.PrefHelper;
import csci571.hw9.schema.SearchEventSchema;
import csci571.hw9.viewmodel.InfoViewModel;

public class InfoActivity extends AppCompatActivity implements EventInfoFragment.OnFragmentInteractionListener,
                                                                ArtistFragment.OnFragmentInteractionListener,
                                                               VenueFragment.OnFragmentInteractionListener,
                                                               UpcomingFragment.OnFragmentInteractionListener {

    InfoViewModel mViewModel;
    PrefHelper helper = PrefHelper.getInstance();
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ViewPager viewPager = findViewById(R.id.infoViewPager);
        InfoFragmentPagerAdapter adapter = new InfoFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        TabLayout tabs = findViewById(R.id.tabLayout2);
        tabs.setupWithViewPager(viewPager);
        mViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        mViewModel.init(gson.fromJson(getIntent().getStringExtra("EVENT"), SearchEventSchema.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.resultToolbar);
        toolbar.setTitle(mViewModel.getSearchEvent().name);
        toolbar.inflateMenu(R.menu.otherbtn);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem fav = menu.findItem(R.id.action_favorite);
        if (helper.contains(gson.fromJson(getIntent().getStringExtra("EVENT"), SearchEventSchema.class).id)) {
            fav.setIcon(R.drawable.heart_fill_white);
        } else {
            fav.setIcon(R.drawable.heart_outline_white);
        }
        helper.getPref().registerOnSharedPreferenceChangeListener(
            new OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    if (key.equals(gson.fromJson(getIntent().getStringExtra("EVENT"), SearchEventSchema.class).id)) {
                        if (helper.contains(key)) {
                            fav.setIcon(R.drawable.heart_fill_white);
                        } else {
                            fav.setIcon(R.drawable.heart_outline_white);
                        }
                    }
                }
            });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_twitter:

                return true;

            case R.id.action_favorite:
                helper.switchPref(mViewModel.getSearchEvent().id,mViewModel.getSearchEvent());
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
