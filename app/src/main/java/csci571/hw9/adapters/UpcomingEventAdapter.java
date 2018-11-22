package csci571.hw9.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import csci571.hw9.R;
import csci571.hw9.databinding.UpcomingEventDataBinding;
import csci571.hw9.databinding.UpcomingItemDataBinding;
import csci571.hw9.fragment.UpcomingFragment;
import csci571.hw9.schema.SongkickEvent;
import java.util.ArrayList;
import java.util.List;

public class UpcomingEventAdapter extends RecyclerView.Adapter<UpcomingEventAdapter.UpcomingItemViewHolder> {
    private List<SongkickEvent> mEvents = new ArrayList<>();

    public void setEvents(List<SongkickEvent> list) {
        mEvents = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UpcomingEventAdapter.UpcomingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                          int viewType) {
        Log.d("UpcomingEventAdapter", "onCreateViewHolder: ");
        UpcomingItemDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                                                                      .from(parent.getContext()), R.layout.recycler_upcoming_item, parent, false);
        binding.setFragment(UpcomingFragment.newInstance());
        UpcomingItemViewHolder holder = new UpcomingItemViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingEventAdapter.UpcomingItemViewHolder holder,
                                 int position) {
        Log.d("UpcomingEventAdapter", "onBindViewHolder: " + position);
        holder.mBinding.setEvent(mEvents.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("UpcomingEventAdapter", "getItemCount: ");
        return mEvents.size();
    }

    class UpcomingItemViewHolder extends RecyclerView.ViewHolder {
        UpcomingItemDataBinding mBinding;

        public void setBinding(UpcomingItemDataBinding binding) {
            mBinding = binding;
        }

        public UpcomingItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
