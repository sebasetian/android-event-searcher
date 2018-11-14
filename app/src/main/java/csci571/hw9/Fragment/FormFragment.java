package csci571.hw9.Fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import csci571.hw9.FormViewModel;
import csci571.hw9.R;
import csci571.hw9.databinding.FormDataBinding;

public class FormFragment extends Fragment {

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
        return formBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
