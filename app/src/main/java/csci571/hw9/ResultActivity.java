package csci571.hw9;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import csci571.hw9.Fragment.ResultFragment.OnListFragmentInteractionListener;
import csci571.hw9.Fragment.dummy.DummyContent.DummyItem;

public class ResultActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search Results");
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ab_share_pack_mtrl_alpha);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyItem item) {

    }
}
