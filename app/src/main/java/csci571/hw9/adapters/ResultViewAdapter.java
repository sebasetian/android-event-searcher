package csci571.hw9.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import csci571.hw9.R;
import csci571.hw9.schema.SearchEventSchema;
import java.util.List;

public class ResultViewAdapter extends RecyclerView.Adapter<ResultViewAdapter.ResultViewHolder> {
    List<SearchEventSchema> mEvents;
    public void setData(List<SearchEventSchema> list) {
        mEvents = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(mEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        public ResultViewHolder(View itemView) {
            super(itemView);
        }
        public void bind(SearchEventSchema event) {

        }
    }
}
