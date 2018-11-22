package csci571.hw9.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

public class FormField extends BaseObservable {

    public ObservableField<String> keyword;
    public ObservableInt categoryIdx;
    public ObservableField<Integer> distance;
    public ObservableInt distanceUnitIdx;
    public ObservableBoolean isFromHere;
    public ObservableField<String> location;

    public final ObservableField<Integer> keywordError = new ObservableField<>();
    public final ObservableField<Integer> locationError = new ObservableField<>();


}
