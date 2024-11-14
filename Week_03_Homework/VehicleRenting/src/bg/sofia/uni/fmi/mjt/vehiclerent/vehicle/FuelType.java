package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public enum FuelType {
    DIESEL(3),
    PETROL(3),
    HYBRID(1),
    ELECTRICITY(0),
    HYDROGEN(0);

    private final int dailyTax;

    FuelType(int dailyTax) {
        this.dailyTax = dailyTax;
    }

    /**
     * Used to get the daily tax of the vehicles witch uses fuel.
     *
     * @return the daily tax.
     */
    public int getDailyTax() {
        return dailyTax;
    }
}