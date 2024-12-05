package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.utils.Validator;

import java.time.LocalDateTime;

public abstract sealed class Vehicle permits Bicycle, MotorVehicle {
    private final String id;
    private final String model;
    private Driver driver;
    private LocalDateTime startRentTime;
    private boolean isRented;

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
        this.isRented = false;
    }

    /**
     * Simulates rental of the vehicle. The vehicle now is considered rented by the provided driver and the start of the rental is the provided date.
     *
     * @param driver        the driver that wants to rent the vehicle.
     * @param startRentTime the start time of the rent
     * @throws VehicleAlreadyRentedException in case the vehicle is already rented by someone else or by the same driver.
     */
    public void rent(Driver driver, LocalDateTime startRentTime) {
        try {
            Validator.validateIsRented(isRented);

            this.startRentTime = startRentTime;
            this.driver = driver;
            this.isRented = true;

        } catch (VehicleAlreadyRentedException e) {
            System.err.println("Error for renting a Vehicle: " + e.getMessage());
        }
    }

    /**
     * Simulates end of rental for the vehicle - it is no longer rented by a driver.
     *
     * @param rentalEnd time of end of rental
     * @throws IllegalArgumentException      in case @rentalEnd is null
     * @throws VehicleNotRentedException     in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     *                                       in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     *                                       and the driver tries to return them after an hour.
     */
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        Validator.validateNotRented(isRented);
        Validator.validateRentingPeriod(startRentTime, rentalEnd);

        driver = null;
        isRented = false;
    }

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param  startOfRent                     the beginning of the rental
     * @param  endOfRent                       the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException   in case the vehicle cannot be rented for that period of time or
     *                                         the period is not valid (end date is before start date)
     */
    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;

    /**
     * Used to get the driver that rented the vehicle.
     *
     * @return the driver.
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Used to get the starting rent time of the vehicle.
     *
     * @return start rent period
     */
    public LocalDateTime getStartRentTime() {
        return startRentTime;
    }

    /**
     * Used to get the is the vehicle is already rented.
     *
     * @return true if the vehicle is rented, otherwise false.
     */
    public boolean isRented() {
        return isRented;
    }
}