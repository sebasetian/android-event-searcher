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
import csci571.hw9.schema.*;
import csci571.hw9.viewmodel.MainViewModel;
import java.util.ArrayList;
import java.util.List;

public class ResultViewAdapter extends RecyclerView.Adapter<ResultViewAdapter.ResultViewHolder> {
    private List<SearchEventSchema> mEvents = new ArrayList<>();
    private MainViewModel mViewModel;
    public void setData(List<SearchEventSchema> list) {
        mEvents = list;
        notifyDataSetChanged();
    }
    public void setViewModel(MainViewModel viewModel) {
        mViewModel = viewModel;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ResultItemDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.recycler_view_item,parent,false);
        Log.d("ResultViewAdapter", "onCreateViewHolder: ");
        return new ResultViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bindEvent(mEvents.get(position));
        Log.d("ResultViewAdapter", "onBindViewHolder: ");
        holder.bindViewModel(mViewModel);
    }

    @Override
    public int getItemCount() {
        Log.d("ResultViewAdapter", "getItemCount: " + mEvents.size());
        return mEvents.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        View mView;
        SearchEventSchema mEvent;
        ResultViewHolder(View itemView) {
            super(itemView);
        }
        void bindEvent(SearchEventSchema event) {
            mEvent = event;
            mView = itemView;
            setTypeImg(event);
            setText(event);
            ((ImageView) mView
                .findViewById(R.id.favBtn)).setImageResource(R.drawable.heart_outline_black);
        }
        void bindViewModel(final MainViewModel viewModel) {
            ((ImageView) mView.findViewById(R.id.favBtn)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.switchFav(mEvent);
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
            ((TextView) mView
                .findViewById(R.id.resultArtist)).setText(event._embedded.attractions[0].name);
            String Date = event.dates.start.localDate;
            if (event.dates.start.localTime != null) {
                Date += event.dates.start.localTime;
            }
            ((TextView) mView.findViewById(R.id.resultDate)).setText(Date);
        }
    }
}
