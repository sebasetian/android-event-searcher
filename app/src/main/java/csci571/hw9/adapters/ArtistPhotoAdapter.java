package csci571.hw9.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ArtistPhotoAdapter extends RecyclerView.Adapter<ArtistPhotoAdapter.ArtistPhotoViewHolder> {

    @NonNull
    @Override
    public ArtistPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistPhotoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ArtistPhotoViewHolder extends RecyclerView.ViewHolder {

        public ArtistPhotoViewHolder(View itemView) {
            super(itemView);
        }
    }
}
