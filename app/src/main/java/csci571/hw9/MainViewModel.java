package csci571.hw9;

import android.arch.lifecycle.ViewModel;
import csci571.hw9.Model.WebServices;

public class MainViewModel extends ViewModel {
    private WebServices mWebservice = WebServices.getInstance();
    public void init() {
    }

}
