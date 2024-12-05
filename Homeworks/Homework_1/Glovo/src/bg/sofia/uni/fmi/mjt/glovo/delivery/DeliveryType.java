package bg.sofia.uni.fmi.mjt.glovo.delivery;

public enum DeliveryType {

    CAR(5.0, 3),
    BIKE(3.0, 5);

    double pricePerKM;
    int timePerKM;

    DeliveryType(double pricePerKM, int timePerKM) {

        this.pricePerKM = pricePerKM;
        this.timePerKM = timePerKM;
    }

    public double getPricePerKM() {

        return pricePerKM;
    }

    public int getTimePerKM() {

        return timePerKM;
    }

}