package csci571.hw9.schema;

public class SearchEventSchema {
    public String name;
    public String type;
    public String id;
    public String url;
    public Sales sales;
    public Dates dates;
    public Classification[] classifications;
    public PriceRange[] priceRanges;
    public Seatmap seatmap;
    public EmbeddedVenues _embedded;
}

