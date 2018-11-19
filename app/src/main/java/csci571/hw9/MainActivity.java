package csci571.hw9;

import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import csci571.hw9.fragmentpager.MainFragmentPagerAdapter;
import csci571.hw9.model.PrefHelper;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefHelper.initInstance(this);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.mainViewPager);
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        TabLayout tabs = findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);
    }
}
