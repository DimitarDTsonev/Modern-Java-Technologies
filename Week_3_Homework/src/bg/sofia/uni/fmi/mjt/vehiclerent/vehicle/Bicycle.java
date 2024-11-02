package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.utils.Validator;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Bicycle extends Vehicle {
    private final double pricePerHour;
    private final double pricePerDay;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent   the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or
     *                                       the period is not valid (end date is before start date)
     */
    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        Validator.validateRentingPeriod(startOfRent, endOfRent);
        Validator.validateBicycleRentalDuration(startOfRent, endOfRent);

        Duration rentalDuration = Duration.between(startOfRent, endOfRent);
        long hours = rentalDuration.toHours();
        long days = rentalDuration.toDays();

        return days > 0 ? days * pricePerDay + (hours % 24) * pricePerHour : hours * pricePerHour;
    }
}