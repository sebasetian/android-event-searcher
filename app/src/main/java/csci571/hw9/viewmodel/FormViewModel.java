package csci571.hw9.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import csci571.hw9.model.FormPostData;
import csci571.hw9.model.WebServices;
import csci571.hw9.model.FormField;
import csci571.hw9.schema.LocationSchema;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormViewModel extends ViewModel {
    public FormField form;
    private WebServices mWebService = WebServices.getInstance();
    private ArrayAdapter<String> autoCompAdapter;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    public ObservableBoolean isKeywordEmpty = new ObservableBoolean(false);
    public ObservableBoolean isLocationEmpty = new ObservableBoolean(false);
    public PublishSubject<Location> currLocationSource = PublishSubject.create();
    public final List<String> UNIT_LIST = new ArrayList<>(Arrays.asList("Miles","Kilometers"));
    public final List<String> CATEGORY_LIST = new ArrayList<>(Arrays.asList("All","Music","Sports","Arts & Theatre","Film","Miscellaneous"));
    public final static int REQUEST_LOCATION = 0;
    public void init() {
        form = new FormField();
        initFormField();
        mDisposables.add(mWebService.autoCompleteSource.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                refreshAutoComplete(list);
            }
        }));
        mDisposables.add(currLocationSource.subscribe(new Consumer<Location>() {
            @Override
            public void accept(Location location) throws Exception {
                onSubmit(location.getLatitude(),location.getLongitude());
            }
        }));
        mDisposables.add(mWebService.locationSource.subscribe(new Consumer<LocationSchema>() {
            @Override
            public void accept(LocationSchema locationSchema) throws Exception {
                onSubmit(locationSchema.lat,locationSchema.lng);
            }
        }));

    }
    private void initFormField() {
        form.keyword = new ObservableField<>();
        form.keyword.set("");
        form.categoryIdx = new ObservableInt();
        form.categoryIdx.set(0);
        form.distance = new ObservableField<>();
        form.distanceUnitIdx = new ObservableInt();
        form.distanceUnitIdx.set(0);
        form.isFromHere = new ObservableBoolean();
        form.isFromHere.set(true);
        form.location = new ObservableField<>();
        form.location.set("");
        form.keyword.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (form.keyword.get() != null && form.keyword.get().length() > 0) {
                    isKeywordEmpty.set(false);
                }
                getAutoComplete();
            }
        });
        form.location.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (form.location.get() != null && form.location.get().length() > 0) {
                    isLocationEmpty.set(false);
                }
            }
        });
    }
    public void getLocationFromAddress() {
        mWebService.getLocationFromAddress(form.location.get());
    }
    public ArrayAdapter<String> getAutoCompAdapter() {
        return autoCompAdapter;
    }
    private void refreshAutoComplete(List<String> list) {
        autoCompAdapter.clear();
        autoCompAdapter.addAll(list);
        autoCompAdapter.notifyDataSetChanged();
    }
    public void switchToHere() {
        form.isFromHere.set(true);
    }
    public void switchToOther() {
        form.isFromHere.set(false);
    }
    public void setAutoCompAdapter(ArrayAdapter<String> autoCompAdapter) {
        this.autoCompAdapter = autoCompAdapter;
    }
    public void getAutoComplete() {
        mWebService.autocomplete(form.keyword.get());
    }

    public void onSubmit(double lat,double lng) {
            FormPostData data = new FormPostData();
            data.lat = lat;
            data.lng = lng;

            data.keyword = form.keyword.get();
            data.category = getCategoryItem(form.categoryIdx.get());
            data.distance = form.distance.get() != null ? form.distance.get() : 10;

            data.distanceUnit = getUnitItem(form.distanceUnitIdx.get());
            mWebService.postFrom(data);
    }
    private String getCategoryItem(int idx) {
        return CATEGORY_LIST.get(idx);
    }
    private String getUnitItem(int idx) {
        if(idx == 0) {
            return "miles";
        } else {
            return "km";
        }
    }
    public void clear() {
        form.keyword.set("");
        form.categoryIdx.set(0);
        form.distance.set(null);
        form.distanceUnitIdx.set(0);
        form.isFromHere.set(true);
        form.location.set("");
        isKeywordEmpty.set(false);
        isLocationEmpty.set(false);
    }
    @Override
    public void onCleared() {
        mDisposables.dispose();
    }
}
