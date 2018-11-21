package csci571.hw9.schema;

public class Venues extends SearchEventSchema {
    String postalCode;
    public City city;
    public State state;
    public Country country;
    public Address address;
    public Location location;
    public GeneralInfo generalInfo;
    public BoxOfficeInfo boxOfficeInfo;
}
