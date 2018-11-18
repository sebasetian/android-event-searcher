package csci571.hw9.schema;

public class SearchEventSchema {
    public String name;
    public String type;
    public String id;
    public String url;
    public Sales sales;
    public Dates dates;
    public classification[] classifications;
    public PriceRange[] priceRanges ;
    public Seatmap seatmap;
    public EmbeddedVenues _embedded;
}

class Dates {
    StartDate start;
    Status status;
}
class StartDate {
    String localDate;
    String localTime;
    String dateTime;
}
class PriceRange {
    String type;
    String currency;
    double min;
    double max;
}
class EmbeddedVenues {
    Venues[] venues;
    Attractions[] attractions;
}
class Seatmap {
    String staticUrl;
}
class Attractions extends SearchEventSchema {
    String[] aliases;
}
class Venues extends SearchEventSchema {
    String postalCode;
    City city;
    State state;
    Country country;
    Address address;
    Location location;
    GeneralInfo generalInfo;
    BoxOfficeInfo boxOfficeInfo;
}
class City {
    String name;
}
class State extends City {

}
class Country extends City {

}
class Address {
    String line1;
    String line2;
}
class Location {
    String longitude;
    String latitude;
}
class GeneralInfo {
    String generalRule;
    String childRule;
}
class BoxOfficeInfo {
    String openHoursDetail;
    String phoneNumberDetail;
}
class classification {
    ClassificationType segment;
    ClassificationType genre;
    ClassificationType subGenre;
    ClassificationType type;
    ClassificationType subType;

}
class ClassificationType {
    String id;
    String name;
}

class Sales {
    PublicDate pub;
}
class PublicDate {
    String startDateTime;
    String endDateTaime;
}
class Status {
    String code;
}