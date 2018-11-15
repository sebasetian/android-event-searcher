package csci571.hw9.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private List<String> autoCompleteTitles;
    public AutoCompleteAdapter(@NonNull Context context,
                               int resource) {
        super(context, resource);
        autoCompleteTitles = new ArrayList<>();
    }
    public void setData(List<String> list) {
        autoCompleteTitles = new ArrayList<>(list);
    }

    @Override
    public int getCount() {
        return autoCompleteTitles.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return autoCompleteTitles.get(position);
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = autoCompleteTitles;
                    filterResults.count = autoCompleteTitles.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
