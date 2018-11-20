package csci571.hw9.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import csci571.hw9.R;
import csci571.hw9.schema.ArtistInfo;
import csci571.hw9.schema.CustomImg;

public class ArtistPhotoAdapter extends RecyclerView.Adapter<ArtistPhotoAdapter.ArtistPhotoViewHolder> {
    private ArtistInfo mArtist;
    public void setData(ArtistInfo artist) {
        mArtist = artist;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ArtistPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistPhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_photo_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistPhotoViewHolder holder, int position) {
        Log.d("ArtistPhotoAdapter", "onBindViewHolder: " + mArtist.Imgs.get(position).link);
        Glide.with(holder.itemView)
             .load(mArtist.Imgs.get(position).link)
             .into((ImageView) holder.itemView.findViewById(R.id.resultImg));
    }

    @Override
    public int getItemCount() {
        return mArtist.Imgs.size();
    }


    class ArtistPhotoViewHolder extends RecyclerView.ViewHolder {
        public ArtistPhotoViewHolder(View itemView) {
            super(itemView);
            Glide.get(itemView.getContext()).clearMemory();
            Glide.get(itemView.getContext()).clearMemory();
        }

    }

}
