package csci571.hw9;

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
import csci571.hw9.Model.FormPostData;
import csci571.hw9.Model.WebServices;
import csci571.hw9.Model.FormField;
import csci571.hw9.Schema.LocationSchema;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormViewModel extends ViewModel {
    public FormField form;
    private MutableLiveData<FormPostData> formData;
    private WebServices mWebService = WebServices.getInstance();
    private ArrayAdapter<String> autoCompAdapter;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    public PublishSubject<Location> currLocationSource = PublishSubject.create();
    public final List<String> UNIT_LIST = new ArrayList<>(Arrays.asList("Miles","Kilometers"));
    public final List<String> CATEGORY_LIST = new ArrayList<>(Arrays.asList("All","Music","Sports","Arts & Theatre","Film","Miscellaneous"));
    public final static int REQUEST_LOCATION = 0;
    public void init() {
        Log.d("formViewModel", "init");
        form = new FormField();
        formData = new MutableLiveData<>();
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
                Log.d("formViewModel", "currLocationSource: ");
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
                getAutoComplete();
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
        if(form.isValid()) {
            FormPostData data = new FormPostData();
            data.lat = lat;
            data.lng = lng;

            data.keyword = form.keyword.get();
            data.category = getCategoryItem(form.categoryIdx.get());
            data.distance = form.distance.get() != null ? form.distance.get() : 10;

            data.distanceUnit = getUnitItem(form.distanceUnitIdx.get());
            Log.d("formViewModel", "onSubmit: distanceUnit " + data.distanceUnit);
            mWebService.postFrom(data);
        }
    }
    public MutableLiveData<FormPostData> getSubmit() {
        return formData;
    }
    @BindingAdapter("error")
    public static void setError(EditText editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }

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
    @Override
    public void onCleared() {
        mDisposables.dispose();
    }
}
