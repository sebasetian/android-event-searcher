package csci571.hw9.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import csci571.hw9.R;
import csci571.hw9.databinding.ResultItemDataBinding;
import csci571.hw9.model.PrefHelper;
import csci571.hw9.schema.*;
import csci571.hw9.viewmodel.ResultViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

public class ResultViewAdapter extends RecyclerView.Adapter<ResultViewAdapter.ResultViewHolder> {
    private List<SearchEventSchema> mEvents = new ArrayList<>();
    private ResultViewModel mViewModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    public void setData(List<SearchEventSchema> list) {
        mEvents = list;
        notifyDataSetChanged();
    }
    public void setViewModel(ResultViewModel viewModel) {
        mViewModel = viewModel;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ResultItemDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.recycler_view_item,parent,false);
        ResultViewHolder holder = new ResultViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bindEvent(mEvents.get(position));
        holder.bindViewModel(mViewModel);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {

    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        View mView;
        SearchEventSchema mEvent;
        ResultItemDataBinding mBinding;
        ResultViewModel mViewModel;
        ResultViewHolder(View itemView) {
            super(itemView);
        }
        void setBinding(ResultItemDataBinding binding) {
            mBinding = binding;
        }
        void bindEvent(SearchEventSchema event) {
            mEvent = event;
            mBinding.setEvent(event);
            mView = itemView;
            setTypeImg(event);
            setText(event);
            setPref();
        }
        void setPref() {
            final String id = mEvent.id;
            final PrefHelper helper = PrefHelper.getInstance();
            if (helper.contains(id)) {
                ((ImageView) mView
                    .findViewById(R.id.favBtn)).setImageResource(R.drawable.heart_fill_red);
            } else {
                ((ImageView) mView
                    .findViewById(R.id.favBtn)).setImageResource(R.drawable.heart_outline_black);
            }
            mDisposable.add(
                helper.prefChangeSource.subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    Log.d("prefChangeSource", "accept: ");
                    if (s.equals(id)) {
                        if (helper.contains(id)) {
                            mViewModel.makeToast(mEvent.name + " was added to favorites");
                            ((ImageView) mView
                                .findViewById(R.id.favBtn))
                                .setImageResource(R.drawable.heart_fill_red);
                        } else {
                            mViewModel.makeToast(mEvent.name + " was removed from favorites");
                            ((ImageView) mView
                                .findViewById(R.id.favBtn))
                                .setImageResource(R.drawable.heart_outline_black);
                        }
                    }
                }
            }));


        }
        void bindViewModel(final ResultViewModel viewModel) {
            mViewModel = viewModel;
            ((ImageView) mView.findViewById(R.id.favBtn)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefHelper helper = PrefHelper.getInstance();
                    helper.switchPref(mEvent.id,mEvent);
                }
            });
        }
        void setTypeImg(SearchEventSchema event) {
            switch (event.classifications[0].segment.name) {
                case "Music" :
                    ((ImageView) mView
                        .findViewById(R.id.resultImg)).setImageResource(R.drawable.music_icon);
                    break;
                case "Sports" :
                    ((ImageView) mView
                        .findViewById(R.id.resultImg)).setImageResource(R.drawable.sport_icon);
                    break;
                case "Arts & Theatre":
                    ((ImageView) mView
                        .findViewById(R.id.resultImg)).setImageResource(R.drawable.art_icon);
                    break;
                case "Film":
                    ((ImageView) mView
                        .findViewById(R.id.resultImg)).setImageResource(R.drawable.film_icon);
                    break;
                case "Miscellaneous":
                    ((ImageView) mView
                        .findViewById(R.id.resultImg)).setImageResource(R.drawable.miscellaneous_icon);
                    break;
            }
        }
        void setText(SearchEventSchema event) {
            ((TextView) mView.findViewById(R.id.resultTitle)).setText(event.name);
            if (event._embedded.attractions != null && event._embedded.attractions.length > 0) {
                ((TextView) mView
                    .findViewById(R.id.resultArtist)).setText(event._embedded.attractions[0].name);
            }
            String Date = event.dates.start.localDate;
            if (event.dates.start.localTime != null) {
                Date +=" " + event.dates.start.localTime;
            }
            ((TextView) mView.findViewById(R.id.resultDate)).setText(Date);
            mView.findViewById(R.id.resultItem).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.goToInfo(mEvent);
                }
            });
        }
    }
}
