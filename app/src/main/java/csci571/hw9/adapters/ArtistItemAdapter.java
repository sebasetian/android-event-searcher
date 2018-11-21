package csci571.hw9.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.RecycledViewPool;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import csci571.hw9.R;
import csci571.hw9.databinding.ArtistItemDataBinding;
import csci571.hw9.schema.ArtistInfo;
import csci571.hw9.viewmodel.InfoViewModel;

import java.util.ArrayList;
import java.util.List;

public class ArtistItemAdapter extends RecyclerView.Adapter<ArtistItemAdapter.ArtistItemViewHolder> {
    private List<ArtistInfo> mArtists = new ArrayList<>();
    private InfoViewModel mViewModel;
    private RecyclerView.RecycledViewPool mViewPool;
    public void setData(List<ArtistInfo> list) {
        mArtists = list;
        notifyDataSetChanged();
    }
    public void setViewModel(InfoViewModel viewModel) {
        mViewModel = viewModel;
        notifyDataSetChanged();
    }
    public ArtistItemAdapter() {
        mViewPool = new RecycledViewPool();
    }
    @NonNull
    @Override
    public ArtistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArtistItemDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                                R.layout.recycler_artist_item,parent,false);

        ArtistItemViewHolder holder = new ArtistItemViewHolder(binding.getRoot());
        ((RecyclerView)binding.getRoot().findViewById(R.id.imgView)).setRecycledViewPool(mViewPool);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistItemViewHolder holder, int position) {
        holder.mBinding.setArtist(mArtists.get(position));
        holder.setArtist(mArtists.get(position));
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {

    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }
    class ArtistItemViewHolder extends RecyclerView.ViewHolder {
        ArtistPhotoAdapter mAdapter;
        ArtistItemDataBinding mBinding;
        ArtistInfo mArtist;
        View mView;
        public void setArtist(ArtistInfo artist) {
            mArtist = artist;

            TextView spotifyText = mView.findViewById(R.id.SpotifyUrl);
            spotifyText.setClickable(true);
            spotifyText.setMovementMethod(LinkMovementMethod.getInstance());
            String url = "<a href='" + mArtist.url.spotify + "'> Spotify</a>";
            spotifyText.setText(Html.fromHtml(url));

            RecyclerView imgView = mView.findViewById(R.id.imgView);
            imgView.setAdapter(mAdapter);
            imgView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
            mAdapter.setData(mArtist);
        }
        public void setBinding(ArtistItemDataBinding binding) {
            mBinding = binding;
        }
        public ArtistItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mAdapter = new ArtistPhotoAdapter();
        }
    }
}
