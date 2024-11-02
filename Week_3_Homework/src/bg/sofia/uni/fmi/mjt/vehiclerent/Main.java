package bg.sofia.uni.fmi.mjt.vehiclerent;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Car;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.FuelType;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.vehiclerent.driver.AgeGroup;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        RentalService rentalService = new RentalService();
        LocalDateTime rentStart = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        Driver experiencedDriver = new Driver(AgeGroup.EXPERIENCED);

        Vehicle electricCar = new Car("1", "Tesla Model 3", FuelType.ELECTRICITY, 4, 1000, 150, 10);
        rentalService.rentVehicle(experiencedDriver, electricCar, rentStart);

        double priceToPay = 0; // 770.0
        try {
            priceToPay = rentalService.returnVehicle(electricCar, rentStart.plusDays(5));
        } catch (InvalidRentingPeriodException e) {
            throw new RuntimeException(e);
        }
        System.out.println(priceToPay);

        Vehicle dieselCar = new Car("2", "Toyota Auris", FuelType.DIESEL, 4, 500, 80, 5);
        rentalService.rentVehicle(experiencedDriver, dieselCar, rentStart);
        try {
            priceToPay = rentalService.returnVehicle(dieselCar, rentStart.plusDays(5)); // 80*5 + 3*5 + 4*5 = 435.0
        } catch (InvalidRentingPeriodException e) {
            throw new RuntimeException(e);
        }
        System.out.println(priceToPay);
        System.out.println("This is just for test");
    }
}
