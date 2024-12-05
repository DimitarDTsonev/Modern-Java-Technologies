package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public abstract sealed class MotorVehicle extends Vehicle permits Car, Caravan {
    private FuelType fuelType;
    public final int SEAT_PRICE = 5;

    public MotorVehicle(String id, String model) {
        super(id, model);
    }
}