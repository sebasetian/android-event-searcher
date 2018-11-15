package csci571.hw9.Fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import csci571.hw9.FormViewModel;
import csci571.hw9.R;
import csci571.hw9.databinding.FormDataBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormFragment extends Fragment implements OnItemSelectedListener {

    private FormViewModel mViewModel;
    public FormDataBinding formBinding;
    public static FormFragment newInstance() {
        return new FormFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        formBinding = DataBindingUtil.inflate(inflater, R.layout.form_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(FormViewModel.class);
        formBinding.setViewModel(mViewModel);
        createSpinner((Spinner) formBinding.getRoot().findViewById(R.id.categorySpinner),formBinding.getRoot().getContext());
        return formBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
    private void createSpinner(Spinner spinner, Context context) {
        List<String> categories = new ArrayList<>(Arrays.asList("All","Music","Sports","Arts & Theatre","Film","Miscellaneous"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
