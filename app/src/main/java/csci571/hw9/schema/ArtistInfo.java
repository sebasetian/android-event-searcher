package csci571.hw9.schema;

import android.databinding.ObservableBoolean;
import java.util.ArrayList;
import java.util.List;

public class ArtistInfo {
    public String name = "";
    public SpotifyUrl url = new SpotifyUrl();
    public int popularity;
    public Followers followers = new Followers();
    public List<CustomImg> Imgs = new ArrayList<>();
    public ObservableBoolean isMusic = new ObservableBoolean(true);
    public void setNonMusic() {
        isMusic.set(false);
    }
}

