package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.utils.Validator;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Car extends MotorVehicle {
    private final FuelType fuelType;
    private final int numberOfSeats;
    private final double pricePerHour;
    private final double pricePerDay;
    private final double pricePerWeek;

    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.pricePerHour = pricePerHour;
        this.pricePerDay = pricePerDay;
        this.pricePerWeek = pricePerWeek;
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

        Duration rentalDuration = Duration.between(startOfRent, endOfRent);
        long days = rentalDuration.toDays();
        long weeks = days / 7;
        days %= 7;
        long hours = rentalDuration.toHours() % 24;

        return weeks * pricePerWeek +
                days * pricePerDay +
                hours * pricePerHour +
                fuelType.getDailyTax() * (days + weeks * 7) +
                numberOfSeats * SEAT_PRICE +
                getDriver().ageGroup().getAgeGroup();
    }
}