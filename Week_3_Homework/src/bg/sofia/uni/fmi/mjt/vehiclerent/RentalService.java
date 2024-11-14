package bg.sofia.uni.fmi.mjt.vehiclerent;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.utils.Validator;

import java.time.LocalDateTime;

public class RentalService {

    /**
     * Simulates renting of the vehicle. Makes all required validations and then the provided driver "rents" the provided
     * vehicle with start time @startOfRent
     *
     * @param driver                          the designated driver of the vehicle
     * @param vehicle                         the chosen vehicle to be rented
     * @param startOfRent                     the start time of the rental
     * @throws IllegalArgumentException       if any of the passed arguments are null
     * @throws VehicleAlreadyRentedException  in case the provided vehicle is already rented
     */
    public void rentVehicle(Driver driver, Vehicle vehicle, LocalDateTime startOfRent) {
        Validator.validateIsRented(vehicle.isRented());
        Validator.validateDriver(driver);
        Validator.validateVehicle(vehicle);
        Validator.validateStartingPeriod(startOfRent);

        vehicle.rent(driver, startOfRent);
    }

    /**
     * This method simulates rental return - it includes validation of the arguments that throw the listed exceptions
     * in case of errors. The method returns the expected total price for the rental - price for the vehicle plus
     * additional tax for the driver, if it is applicable
     *
     * @param vehicle                        the rented vehicle
     * @param endOfRent                      the end time of the rental
     * @return price for the rental
     * @throws IllegalArgumentException      in case @endOfRent or @vehicle is null
     * @throws VehicleNotRentedException     in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the endOfRent is before the start of rental, or the vehicle
     *                                       does not allow the passed period for rental, e.g. Caravans must be rented for at least a day.
     */
    public double returnVehicle(Vehicle vehicle, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        Validator.validateNotRented(vehicle.isRented());
        Validator.validateVehicle(vehicle);
        Validator.validateRentingPeriod(vehicle.getStartRentTime(), endOfRent);

        double price = vehicle.calculateRentalPrice(vehicle.getStartRentTime(), endOfRent);
        vehicle.returnBack(endOfRent);
        return price;
    }
}
