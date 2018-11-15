package csci571.hw9.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import csci571.hw9.FormViewModel;
import csci571.hw9.R;
import csci571.hw9.databinding.FormDataBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormFragment extends Fragment {

    private FormViewModel mViewModel = new FormViewModel();
    public FormDataBinding formBinding;
    public static FormFragment newInstance() {
        return new FormFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("FormFragment", "onCreateView");
        formBinding = DataBindingUtil.inflate(inflater, R.layout.form_fragment, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(formBinding.getRoot().getContext(),android.R.layout.select_dialog_item,new String[0]);
        mViewModel.init();
        mViewModel.setAutoCompAdapter(adapter);
        formBinding.setViewModel(mViewModel);
        setUpAutoComplete((AutoCompleteTextView) formBinding.getRoot().findViewById(R.id.keyword_text));
        return formBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void setUpAutoComplete(AutoCompleteTextView view) {
        view.setAdapter(mViewModel.getAutoCompAdapter());
    }
}
