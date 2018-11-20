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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class InfoActivity extends AppCompatActivity implements EventInfoFragment.OnFragmentInteractionListener,
                                                                ArtistFragment.OnFragmentInteractionListener,
                                                               VenueFragment.OnFragmentInteractionListener,
                                                               UpcomingFragment.OnFragmentInteractionListener {

    private InfoViewModel mViewModel;
    private PrefHelper helper = PrefHelper.getInstance();
    private Gson gson = new Gson();
    private CompositeDisposable mDisposable = new CompositeDisposable();
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
        onPrepareOptionsMenu(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                optionsItemSelected(item);
                return false;
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

    public void onPrepareOptionsMenu(Toolbar toolbar) {
        Menu menu = toolbar.getMenu();
        final MenuItem fav = menu.findItem(R.id.action_favorite);
        if (helper.contains(gson.fromJson(getIntent().getStringExtra("EVENT"), SearchEventSchema.class).id)) {
            fav.setIcon(R.drawable.heart_fill_white);
        } else {
            fav.setIcon(R.drawable.heart_outline_white);
        }
        mDisposable.add(
            helper.prefChangeSource.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (s.equals(gson.fromJson(getIntent().getStringExtra("EVENT"), SearchEventSchema.class).id))
                    if (helper.contains(s)) {
                        fav.setIcon(R.drawable.heart_fill_white);
                    } else {
                        fav.setIcon(R.drawable.heart_outline_white);
                    }
                }
        }));
    }

    public void optionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_twitter:
                openTwitter();
                break;
            case R.id.action_favorite:
                helper.switchPref(mViewModel.getSearchEvent().id,mViewModel.getSearchEvent());
                break;

        }
    }
    private void openTwitter() {
        SearchEventSchema event = mViewModel.getSearchEvent();
        String url = "https://twitter.com/intent/tweet?";
        String text = "text=Check out " + event.name + "located at " + event._embedded.venues[0].name + ". Website: " + event.url;
        String hashTag = "&hashtags=CSCI571EventSearch";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url + text + hashTag));
        startActivity(intent);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onDestroy() {
        ArtistFragment.destroyInstance();
        EventInfoFragment.destroyInstance();
        UpcomingFragment.destroyInstance();
        VenueFragment.destroyInstance();
        super.onDestroy();
    }
}
