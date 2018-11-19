package csci571.hw9;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import csci571.hw9.fragmentpager.InfoFragmentPagerAdapter;
import csci571.hw9.fragmentpager.MainFragmentPagerAdapter;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ViewPager viewPager = findViewById(R.id.infoViewPager);
        InfoFragmentPagerAdapter adapter = new InfoFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        TabLayout tabs = findViewById(R.id.tabLayout2);
        tabs.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.resultToolbar);
        toolbar.setTitle(getIntent().getStringExtra("EVENT_TITLE"));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

    }
    public void goBack() {
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_twitter:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
