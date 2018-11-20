package csci571.hw9.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ArtistItemAdapter extends RecyclerView.Adapter<ArtistItemAdapter.ArtistItemViewHolder> {

    @NonNull
    @Override
    public ArtistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    class ArtistItemViewHolder extends RecyclerView.ViewHolder {

        public ArtistItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
