package csci571.hw9.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

public class FormField extends BaseObservable {

    public ObservableField<String> keyword;
    public ObservableInt categoryIdx;
    public ObservableField<Integer> distance;
    public ObservableInt distanceUnitIdx;
    public ObservableField<String> fromWhere;
    public ObservableField<String> location;

    public final ObservableField<Integer> keywordError = new ObservableField<>();
    public final ObservableField<Integer> locationError = new ObservableField<>();
    public final ObservableField<String> keywordObservable = new ObservableField<>();

    @Bindable
    public boolean isValid() {
        return isKeywordValid() && isLocationValid();
    }
    public boolean isKeywordValid() {
        return true;
    }
    public boolean isLocationValid() {
        return true;
    }

}
