package csci571.hw9.Fragment;

import static csci571.hw9.FormViewModel.REQUEST_LOCATION;

import android.Manifest;
import android.Manifest.permission;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import csci571.hw9.FormViewModel;
import csci571.hw9.R;
import csci571.hw9.ResultActivity;
import csci571.hw9.databinding.FormDataBinding;
import csci571.hw9.generated.callback.OnClickListener;

public class FormFragment extends Fragment {

    private FormViewModel mViewModel;
    public FormDataBinding formBinding;

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(FormViewModel.class);
        formBinding = DataBindingUtil.inflate(inflater, R.layout.form_fragment, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(formBinding.getRoot().getContext(),
                                                          android.R.layout.select_dialog_item,
                                                          new String[0]);
        mViewModel.init();
        mViewModel.setAutoCompAdapter(adapter);
        formBinding.setViewModel(mViewModel);
        setUpAutoComplete(
            (AutoCompleteTextView) formBinding.getRoot().findViewById(R.id.keyword_text));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        formBinding.getRoot().findViewById(R.id.submitBtn).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Form Fragment", "onClick: ");
                    getLocation();
                }
            });
        return formBinding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (ContextCompat
                    .checkSelfPermission(formBinding.getRoot().getContext(),
                                         permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                mViewModel.currLocationSource.onNext(location);
                                Intent intent = new Intent(getActivity(),ResultActivity.class);
                                Log.d("Form Fragment", "getLocation: permission granted start");
                                startActivity(intent);
                            }});
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void setUpAutoComplete(AutoCompleteTextView view) {
        view.setAdapter(mViewModel.getAutoCompAdapter());
    }
    public void getLocation() {
        if (mViewModel.form.isFromHere.get() && ContextCompat
            .checkSelfPermission(formBinding.getRoot().getContext(),
                                 permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        } else {
            if (mViewModel.form.isFromHere.get()) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            mViewModel.currLocationSource.onNext(location);
                            Intent intent = new Intent(getActivity(),ResultActivity.class);
                            Log.d("Form Fragment", "getLocation: onsuccess start");
                            startActivity(intent);
                        }});
            } else {
                mViewModel.getLocationFromAddress();
                Intent intent = new Intent(getActivity(),ResultActivity.class);
                Log.d("Form Fragment", "getLocation: not here start");
                startActivity(intent);
            }
        }
    }
}
