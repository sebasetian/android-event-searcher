package csci571.hw9.schema;


public class SongkickEvent {
    public String displayName = "";
    public String uri = "";
    public SongkickDate start = new SongkickDate();
    public String type = "";
    public SongkickArtist[] performance = new SongkickArtist[1];

}
