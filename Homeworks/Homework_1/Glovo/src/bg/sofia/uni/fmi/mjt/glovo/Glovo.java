package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenterApi;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderEntityException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Glovo implements GlovoApi {

    private final ControlCenterApi controlCenter;

    /**
     * Makes the map from the layout that is char[][] to MapEntity using a function from
     * ControlCenter class
     *
     * @param layout char[][] of the layout
     */
    public Glovo(char[][] layout) {
        this.controlCenter = new ControlCenter(layout);
    }

    /**
     * Returns the cheapest delivery option for a specified food item from a restaurant to a client location.
     *
     * @param client     The delivery destination, represented by a MapEntity.
     * @param restaurant The location of the restaurant from which the food item is sourced,
     *                   represented by a MapEntity.
     * @param foodItem   The name of the food item to be delivered.
     * @return A Delivery object representing the cheapest available delivery option within the
     * specified constraints.
     * @throws InvalidOrderException           If there is no client or restaurant at the specified location on
     *                                         the map, or if the location is outside the map's defined boundaries.
     * @throws NoAvailableDeliveryGuyException If no delivery guys are available to complete the delivery.
     */
    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException {

        validateOrder(client, restaurant, foodItem);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
                restaurant.location(),
                client.location(),
                -1,
                -1,
                ShippingMethod.CHEAPEST
        );

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("No available delivery guy for the requested route.");
        }

        return createDelivery(client, restaurant, deliveryInfo, foodItem);
    }

    /**
     * Returns the fastest delivery option for a specified food item from a restaurant to a
     * client location.
     *
     * @param client     The delivery destination, represented by a MapEntity.
     * @param restaurant The location of the restaurant from which the food item is sourced,
     *                   represented by a MapEntity.
     * @param foodItem   The name of the food item to be delivered.
     * @return A Delivery object representing the fastest available delivery option within the specified
     * constraints.
     * @throws InvalidOrderException           If there is no client or restaurant at the specified location on
     *                                         the map, or if the location is outside the map's defined boundaries.
     * @throws NoAvailableDeliveryGuyException If no delivery guys are available to complete the delivery.
     */
    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException {

        validateOrder(client, restaurant, foodItem);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
                restaurant.location(),
                client.location(),
                -1,
                -1,
                ShippingMethod.FASTEST
        );

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("No available delivery guy for the requested route.");
        }

        return createDelivery(client, restaurant, deliveryInfo, foodItem);
    }

    /**
     * Returns the fastest delivery option under a specified price for a given food item from a restaurant
     * to a client location.
     *
     * @param client     The delivery destination, represented by a MapEntity.
     * @param restaurant The location of the restaurant from which the food item is sourced,
     *                   represented by a MapEntity.
     * @param foodItem   The name of the food item to be delivered.
     * @param maxPrice   The maximum price the client is willing to pay for the delivery.
     * @return A Delivery object representing the fastest available delivery option under the specified
     * price limit.
     * @throws InvalidOrderException           If there is no client or restaurant at the specified location
     *                                         on the map,or if the location is outside the map's defined boundaries.
     * @throws NoAvailableDeliveryGuyException If no delivery guys are available to complete the delivery.
     */
    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant,
                                                 String foodItem, double maxPrice)
            throws NoAvailableDeliveryGuyException {

        validateOrder(client, restaurant, foodItem);
        validateMaxPrice(maxPrice);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
                restaurant.location(),
                client.location(),
                maxPrice,
                -1,
                ShippingMethod.FASTEST
        );

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("No available delivery guy within the price constraint.");
        }

        return createDelivery(client, restaurant, deliveryInfo, foodItem);
    }

    /**
     * Returns the cheapest delivery option within a specified time limit for a given food item from a restaurant
     * to a client location.
     *
     * @param client     The delivery destination, represented by a MapEntity.
     * @param restaurant The location of the restaurant from which the food item is sourced,
     *                   represented by a MapEntity.
     * @param foodItem   The name of the food item to be delivered.
     * @param maxTime    The maximum allowable delivery time in minutes.
     * @return A Delivery object representing the cheapest available delivery option within the specified
     * time limit.
     * @throws InvalidOrderException           If there is no client or restaurant at the specified location
     *                                         on the map, or if the location is outside the map's defined boundaries.
     * @throws NoAvailableDeliveryGuyException If no delivery guys are available to complete the delivery.
     */
    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant,
                                                       String foodItem, int maxTime)
            throws NoAvailableDeliveryGuyException {

        validateOrder(client, restaurant, foodItem);
        validateMaxTime(maxTime);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
                restaurant.location(),
                client.location(),
                -1,
                maxTime,
                ShippingMethod.CHEAPEST
        );

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("No available delivery guy within the time limit.");
        }

        return createDelivery(client, restaurant, deliveryInfo, foodItem);
    }

    /**
     * Validates the data of the parameters client, restaurant, foodItem
     *
     * @param client        MapEntity of the client that is making the order that cannot be null
     * @param restaurant    MapEntity of the restaurant that should execute the order that cannot be null
     * @param foodItem      The name of the food that cannot be null or empty
     */
    private void validateOrder(MapEntity client, MapEntity restaurant, String foodItem) {

        if (client == null || restaurant == null || foodItem == null || foodItem.isEmpty()) {
            throw new InvalidOrderException("Client, restaurant, and food item must be provided.");
        }

        if (client.type() != MapEntityType.CLIENT || restaurant.type() != MapEntityType.RESTAURANT) {
            throw new InvalidOrderException("Invalid client or restaurant type.");
        }
    }

    /**
     * Validates the price that cannot be less than zero
     *
     * @param maxPrice double of the price for the order
     */
    private void validateMaxPrice(double maxPrice) {

        if (maxPrice < 0) {
            throw new InvalidOrderEntityException("Max price cannot be negative.");
        }
    }

    /**
     * Validates the time that cannot be less than zero
     *
     * @param maxTime int of the time for the order
     */
    private void validateMaxTime(int maxTime) {

        if (maxTime < 0) {
            throw new InvalidOrderEntityException("Max time cannot be negative.");
        }
    }

    /**
     * Make the delivery
     *
     * @param client        the location of the client
     * @param restaurant    the location of the restaurant
     * @param info          the information for the delivery
     * @param foodItem      the name of the food
     * @return              the newly made Delivery
     */
    private Delivery createDelivery(MapEntity client, MapEntity restaurant, DeliveryInfo info, String foodItem) {

        return new Delivery(
                client.location(),
                restaurant.location(),
                info.deliveryGuyLocation(),
                foodItem,
                info.price(),
                info.estimatedTime()
        );
    }
}