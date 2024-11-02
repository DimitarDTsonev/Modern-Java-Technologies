package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.utils;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

public class Validator {
    private static final int CARAVAN_MIN_RENTAL_PERIOD = 1;
    private static final int BICYCLE_MAX_RENTAL_PERIOD = 7;

    /**
     * Used to validate the starting time of the vehicle.
     *
     * @param startOfRent the time that the renting had stared.
     */
    public static void validateStartingPeriod(LocalDateTime startOfRent) {
        if (startOfRent == null) {
            throw new IllegalArgumentException(ExpMessages.START_RENT_TIME_NULL);
        }
    }

    /**
     * Used to validate the ending time of the vehicle.
     *
     * @param endOfRent the time that the renting had finished.
     */
    public static void validateEndingPeriod(LocalDateTime endOfRent) {
        if (endOfRent == null) {
            throw new IllegalArgumentException(ExpMessages.END_RENT_TIME_NULL);
        }
    }

    /**
     * Used to validate the whole period of the rent.
     *
     * @param startOfRent the time that the renting had stared.
     * @param endOfRent   the time that the renting had finished.
     * @throws IllegalArgumentException      in case startOfRent or endOfRent is null
     * @throws InvalidRentingPeriodException in case ending period is before or is the same as the starting period.
     */
    public static void validateRentingPeriod(LocalDateTime startOfRent,
                                             LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        validateStartingPeriod(startOfRent);
        validateEndingPeriod(endOfRent);
        if (endOfRent.isBefore(startOfRent) || endOfRent.isEqual(startOfRent)) {
            throw new InvalidRentingPeriodException(ExpMessages.INVALID_RENTING_PERIOD);
        }
    }

    /**
     * Used to validate the driver of the vehicle.
     *
     * @param driver the renter of the vehicle.
     * @throws IllegalArgumentException in case driver is null
     */
    public static void validateDriver(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException(ExpMessages.DRIVER_NULL);
        }
    }

    /**
     * Used to validate the current vehicle.
     *
     * @param vehicle the current vehicle.
     * @throws IllegalArgumentException in case vehicle is null
     */
    public static void validateVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException(ExpMessages.VEHICLE_NULL);
        }
    }

    /**
     * Used to validate the Bicycle max time of renting.
     *
     * @param startOfRent starting period of the rent.
     * @param endOfRent   ending period of the rent.
     * @throws InvalidRentingPeriodException in case ending period is before or is the same as the starting period.
     */
    public static void validateBicycleRentalDuration(LocalDateTime startOfRent,
                                                     LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        Duration rentalDuration = Duration.between(startOfRent, endOfRent);

        if (rentalDuration.toDays() >= BICYCLE_MAX_RENTAL_PERIOD) {
            throw new InvalidRentingPeriodException(ExpMessages.WRONG_BICYCLE_RENT_PERIOD);
        }
    }

    /**
     * Used to validate the Caravan min time of renting.
     *
     * @param startOfRent starting period of the rent.
     * @param endOfRent   ending period of the rent.
     * @throws InvalidRentingPeriodException in case ending period is before or is the same as the starting period.
     */
    public static void validateCaravanRentalDuration(LocalDateTime startOfRent,
                                                     LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        Duration rentalDuration = Duration.between(startOfRent, endOfRent);

        if (rentalDuration.toDays() < CARAVAN_MIN_RENTAL_PERIOD) {
            throw new InvalidRentingPeriodException(ExpMessages.WRONG_CARAVAN_RENT_PERIOD);
        }
    }

    /**
     * Used to validate if the vehicle is already rented.
     *
     * @param isRented boolean that shows if the vehicle is rented.
     * @throws VehicleAlreadyRentedException in case the vehicle is already rented.
     */
    public static void validateIsRented(boolean isRented) {
        if (isRented) {
            throw new VehicleAlreadyRentedException(ExpMessages.VEHICLE_ALREADY_RENTED);
        }
    }

    /**
     * Used to validate if the vehicle is not rented.
     *
     * @param isRented    boolean that shows if the vehicle is rented.
     * @throws InvalidRentingPeriodException in case ending period is before or is the same as the starting period.
     */
    public static void validateNotRented(boolean isRented) throws InvalidRentingPeriodException {
        if (!isRented) {
            throw new VehicleNotRentedException(ExpMessages.VEHICLE_NOT_RENTED);
        }
    }
}