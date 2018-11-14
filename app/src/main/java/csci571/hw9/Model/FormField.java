package csci571.hw9.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

public class FormField extends BaseObservable {

    private String keyword;
    private String category;
    private int distance;
    private String distanceUnit;
    private String fromWhere;


    private String Location;
    public final ObservableField<Integer> keywordError = new ObservableField<>();
    public final ObservableField<Integer> locationError = new ObservableField<>();
    public final ObservableField<String> keywordObservable = new ObservableField<>();


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }
    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

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
