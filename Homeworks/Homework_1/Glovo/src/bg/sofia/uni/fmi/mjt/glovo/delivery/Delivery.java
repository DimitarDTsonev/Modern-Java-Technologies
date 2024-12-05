package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public class Delivery {

    private final Location clientLocation;
    private final Location restaurantLocation;
    private final Location deliveryGuyLocation;
    private final String foodItem;
    private final double price;
    private final int estimatedTime;

    public Delivery(Location client, Location restaurant, Location deliveryGuy,
                    String foodItem, double price, int estimatedTime) {

        this.clientLocation = client;
        this.restaurantLocation = restaurant;
        this.deliveryGuyLocation = deliveryGuy;
        this.foodItem = foodItem;
        this.price = price;
        this.estimatedTime = estimatedTime;
    }

    /**
     * Returns the client location
     *
     * @return Location containing the coordinates of the client
     */
    public Location getClientLocation() {

        return clientLocation;
    }

    /**
     * Return the restaurant location
     *
     * @return Location containing the coordinates of the restaurant
     */
    public Location getRestaurantLocation() {

        return restaurantLocation;
    }

    /**
     * Return the delivery guy location
     *
     * @return Location containing the coordinates of the delivery guy
     */
    public Location getDeliveryGuyLocation() {

        return deliveryGuyLocation;
    }

    /**
     * Returns the food name of the client
     *
     * @return String of the food name
     */
    public String getFoodItem() {

        return foodItem;
    }

    /**
     * Returns the price of delivery of the order
     *
     * @return double the price of the order
     */
    public double getPrice() {

        return price;
    }

    /**
     * Returns the estimated time for the delivery of the order
     *
     * @return int estimated time
     */
    public int getEstimatedTime() {

        return estimatedTime;
    }

}
