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

public class FormFragment extends Fragment implements OnItemSelectedListener {

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
        mViewModel.keywordForAutoComplete.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                mViewModel.getAutoComplete();
            }
        });
        createSpinner((Spinner) formBinding.getRoot().findViewById(R.id.categorySpinner),formBinding.getRoot().getContext(),new ArrayList<>(Arrays.asList("All","Music","Sports","Arts & Theatre","Film","Miscellaneous")));
        createSpinner((Spinner) formBinding.getRoot().findViewById(R.id.unitSpinner),formBinding.getRoot().getContext(),new ArrayList<>(Arrays.asList("miles","kilometers")));
        setUpAutoComplete((AutoCompleteTextView) formBinding.getRoot().findViewById(R.id.keyword_text));

        return formBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void createSpinner(Spinner spinner, Context context,List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void setUpAutoComplete(AutoCompleteTextView view) {
        view.setAdapter(mViewModel.getAutoCompAdapter());

    }
}
