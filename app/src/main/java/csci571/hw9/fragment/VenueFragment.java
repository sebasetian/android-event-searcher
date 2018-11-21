package csci571.hw9.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import csci571.hw9.R;
import csci571.hw9.databinding.VenueDataBinding;
import csci571.hw9.schema.SearchEventSchema;
import csci571.hw9.viewmodel.InfoViewModel;

public class VenueFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private static VenueFragment mFragment;
    public ObservableBoolean isNoData = new ObservableBoolean(false);
    private VenueDataBinding mBinding;
    private MapView mMapView;
    public VenueFragment() {
    }

    public static VenueFragment newInstance() {
        if (mFragment == null) mFragment = new VenueFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_venue,container,false);
        mBinding.setFragment(this);
        SearchEventSchema event = ViewModelProviders.of(getActivity()).get(InfoViewModel.class).getSearchEvent();
        if (event._embedded == null || event._embedded.venues == null || event._embedded.venues.length == 0) {
            isNoData.set(true);
        } else {
            isNoData.set(false);
            mBinding.setVenue(event._embedded.venues[0]);
            mMapView = mBinding.mapView;
            mMapView.onCreate(null);
            mMapView.getMapAsync(this);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                           + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMapView.onDestroy();
        mListener = null;
    }

    public static void destroyInstance() {
        mFragment = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMinZoomPreference(15f);
        if (mBinding.getVenue() != null) {
            LatLng latLng = new LatLng(Double.valueOf(mBinding.getVenue().location.latitude),Double.valueOf(mBinding.getVenue().location.longitude));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            googleMap.addMarker(markerOptions);
        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
