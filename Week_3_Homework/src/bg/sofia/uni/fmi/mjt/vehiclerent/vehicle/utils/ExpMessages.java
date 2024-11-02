package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.utils;

public class ExpMessages {
    public static final String VEHICLE_ALREADY_RENTED = "This vehicle is already rented.";
    public static final String VEHICLE_NOT_RENTED = "This vehicle is not currently rented.";
    public static final String END_RENT_TIME_NULL = "End rent time cannot be null.";
    public static final String START_RENT_TIME_NULL = "Start rent time cannot be null.";
    public static final String INVALID_RENTING_PERIOD = "End rent time cannot be before the start rent time.";
    public static final String VEHICLE_NULL = "Vehicle cannot be null.";
    public static final String DRIVER_NULL = "Driver cannot be null.";
    public static final String WRONG_CARAVAN_RENT_PERIOD = "Caravan must be rented for more than 24 hours.";
    public static final String WRONG_BICYCLE_RENT_PERIOD = "Bicycle must not be rented for more than one week.";
}