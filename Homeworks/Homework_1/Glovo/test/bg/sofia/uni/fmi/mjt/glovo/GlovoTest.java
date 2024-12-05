package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GlovoTest {

    @Test
    void testGetCheapestDelivery() throws NoAvailableDeliveryGuyException {
        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', 'B', 'R', '.'},
                {'.', '.', '#', '.', '#'},
                {'#', 'C', '.', 'A', '.'},
                {'#', '.', '#', '#', '#'}
        };

        Glovo glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(3, 1), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 3), MapEntityType.RESTAURANT);
        String foodItem = "Pizza";

        Delivery delivery = glovo.getCheapestDelivery(client, restaurant, foodItem);

        assertNotNull(delivery, "Delivery should not be null");
        assertEquals(11, delivery.getPrice(), "Price should be calculated correctly");
        assertEquals(5, delivery.getEstimatedTime(), "Estimated time should be calculated correctly");
    }

    @Test
    void testGetFastestDelivery() throws NoAvailableDeliveryGuyException {
        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', 'B', 'R', '.'},
                {'.', '.', '#', '.', '#'},
                {'#', 'C', '.', 'A', '.'},
                {'#', '.', '#', '#', '#'}
        };

        Glovo glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(1, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(3, 1), MapEntityType.RESTAURANT);
        String foodItem = "Sushi";

        Delivery delivery = glovo.getFastestDelivery(client, restaurant, foodItem);

        assertNotNull(delivery, "Delivery should not be null");
        assertEquals(13, delivery.getPrice(), "Price should be calculated correctly");
        assertEquals(3, delivery.getEstimatedTime(), "Estimated time should be calculated correctly");
    }

    @Test
    void testGetFastestDeliveryUnderPrice() throws NoAvailableDeliveryGuyException {
        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', 'B', 'R', '.'},
                {'.', '.', '#', '.', '#'},
                {'#', 'C', '.', 'A', '.'},
                {'#', '.', '#', '#', '#'}
        };

        Glovo glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(1, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(3, 1), MapEntityType.RESTAURANT);
        String foodItem = "Burger";

        double maxPrice = 12;

        Delivery delivery = glovo.getFastestDeliveryUnderPrice(client, restaurant, foodItem, maxPrice);

        assertNotNull(delivery, "Delivery should not be null");
        //assertThrows(NoAvailableDeliveryGuyException.class, () -> { "Delivery price should be under the max price" };
    }

    @Test
    void testGetCheapestDeliveryWithinTimeLimit() throws NoAvailableDeliveryGuyException {
        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', 'B', 'R', '.'},
                {'.', '.', '#', '.', '#'},
                {'#', 'C', '.', 'A', '.'},
                {'#', '.', '#', '#', '#'}
        };

        GlovoApi glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(1, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(3, 1), MapEntityType.RESTAURANT);
        String foodItem = "Pasta";

        int maxTime = 4;

        Delivery delivery = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, foodItem, maxTime);

        assertNotNull(delivery, "Delivery should not be null");
        assertTrue(delivery.getEstimatedTime() <= maxTime, "Delivery time should be within the time limit");
    }

    @Test
    void testNoAvailableDeliveryGuyException() {
        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', '.', 'R', '.'},
                {'.', '.', '#', '.', '#'},
                {'#', 'C', '.', '.', '.'},
                {'#', '.', '#', '#', '#'}
        };

        GlovoApi glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(1, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(3, 1), MapEntityType.RESTAURANT);
        String foodItem = "Salad";

        assertThrows(NoAvailableDeliveryGuyException.class, () -> {
            glovo.getCheapestDelivery(client, restaurant, foodItem);
        }, "Should throw NoAvailableDeliveryGuyException when no delivery guys are available");
    }

    @Test
    void testInvalidOrderException() {
        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', 'B', '.', '.'},
                {'.', '.', '#', '.', '#'},
                {'#', 'C', '.', 'A', '.'},
                {'#', '.', '#', '#', '#'}
        };

        GlovoApi glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(1, 3), MapEntityType.CLIENT);
        // Invalid restaurant location
        MapEntity restaurant = new MapEntity(new Location(3, 1), MapEntityType.ROAD);
        String foodItem = "Steak";

        assertThrows(InvalidOrderException.class, () -> {
            glovo.getFastestDelivery(client, restaurant, foodItem);
        }, "Should throw InvalidOrderException for invalid restaurant location");
    }

    @Test
    void testDeliveryWithDifferentLayout() throws NoAvailableDeliveryGuyException {
        char[][] layout = {
                {'R', '.', '.', '.', 'C'},
                {'.', '#', '#', '#', '.'},
                {'A', '.', 'B', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '#', '.', '#', '.'}
        };

        GlovoApi glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(4, 0), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(0, 0), MapEntityType.RESTAURANT);
        String foodItem = "Ice Cream";

        Delivery delivery = glovo.getCheapestDelivery(client, restaurant, foodItem);

        assertNotNull(delivery, "Delivery should not be null");
    }

    @Test
    void testGetCheapestAndFastestDeliveryOn10x10Map() throws NoAvailableDeliveryGuyException {
        char[][] layout = {
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', 'R', '.', '.', '.', '.', 'A', '.', 'B', '#'},
                {'#', '.', '#', '#', '#', '.', '#', '#', '.', '#'},
                {'#', '.', '.', '.', '#', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '.', '#', '#', '#', '#', '.', '#'},
                {'#', 'C', '.', '.', '.', '.', '.', '#', '.', '#'},
                {'#', '.', '#', '#', '#', '#', '.', '#', '.', '#'},
                {'#', '.', '.', '.', '.', '#', '.', '.', '.', '#'},
                {'#', 'D', '.', 'E', '.', '.', '.', 'F', '.', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };

        GlovoApi glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(1, 5), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);
        String foodItem = "Sandwich";

        Delivery cheapestDelivery = glovo.getCheapestDelivery(client, restaurant, foodItem);
        assertNotNull(cheapestDelivery, "The cheapest delivery should be null");
        assertTrue(cheapestDelivery.getPrice() > 0, "The price of the delivery must be positive");
        assertTrue(cheapestDelivery.getEstimatedTime() > 0, "Estimated time must be positive");

        Delivery fastestDelivery = glovo.getFastestDelivery(client, restaurant, foodItem);
        assertNotNull(fastestDelivery, "The fastest delivery should be null");
        assertTrue(fastestDelivery.getPrice() > 0, "The price of the delivery must be positive");
        assertTrue(fastestDelivery.getEstimatedTime() > 0, "Estimated time must be positive");

        // Тест за най-бърза доставка под определена цена
        double maxPrice = 20.0;
        Delivery fastestUnderPrice = glovo.getFastestDeliveryUnderPrice(client, restaurant, foodItem, maxPrice);
        assertNotNull(fastestUnderPrice, "The fastest delivery must not be null");
        assertTrue(fastestUnderPrice.getPrice() <= maxPrice, "The price of the delivery must be positive");
        assertTrue(fastestUnderPrice.getEstimatedTime() > 0, "Estimated time must be positive");

        int maxTime = 15;
        Delivery cheapestWithinTime = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, foodItem, maxTime);
        assertNotNull(cheapestWithinTime, "The cheapest delivery must not be null");
        assertTrue(cheapestWithinTime.getPrice() > 0, "The price of the delivery must be positive");
        assertTrue(cheapestWithinTime.getEstimatedTime() <= maxTime, "The delivery time must be in the time limit");
    }

    @Test
    void testNoAvailableDeliveryGuysOn10x10Map() {
        char[][] layout = {
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', 'R', '.', '.', '.', '.', '.', '.', 'B', '#'},
                {'#', '.', '#', '#', '#', '.', '#', '#', '.', '#'},
                {'#', '.', '.', '.', '#', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '.', '#', '#', '#', '#', '.', '#'},
                {'#', 'C', '.', '.', '.', '.', '.', '#', '.', '#'},
                {'#', '.', '#', '#', '#', '#', '.', '#', '.', '#'},
                {'#', '.', '.', '.', '.', '#', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };

        GlovoApi glovo = new Glovo(layout);

        char[][] layoutNoDeliveryGuys = {
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', 'R', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '#', '#', '#', '.', '#', '#', '.', '#'},
                {'#', '.', '.', '.', '#', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '.', '#', '#', '#', '#', '.', '#'},
                {'#', 'C', '.', '.', '.', '.', '.', '#', '.', '#'},
                {'#', '.', '#', '#', '#', '#', '.', '#', '.', '#'},
                {'#', '.', '.', '.', '.', '#', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };

        GlovoApi glovoNoDelivery = new Glovo(layoutNoDeliveryGuys);

        MapEntity client = new MapEntity(new Location(1, 5), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);
        String foodItem = "Salad";

        assertThrows(NoAvailableDeliveryGuyException.class, () -> {
            glovoNoDelivery.getCheapestDelivery(client, restaurant, foodItem);
        }, "Throws NoAvailableDeliveryGuyException when there are no delivery guys");
    }

    @Test
    void testInvalidOrderOn10x10Map() {
        char[][] layout = {
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', 'R', '.', '.', '.', '.', 'A', '.', 'B', '#'},
                {'#', '.', '#', '#', '#', '.', '#', '#', '.', '#'},
                {'#', '.', '.', '.', '#', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '.', '#', '#', '#', '#', '.', '#'},
                {'#', 'C', '.', '.', '.', '.', '.', '#', '.', '#'},
                {'#', '.', '#', '#', '#', '#', '.', '#', '.', '#'},
                {'#', '.', '.', '.', '.', '#', '.', '.', '.', '#'},
                {'#', 'D', '.', 'E', '.', '.', '.', 'F', '.', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };

        GlovoApi glovo = new Glovo(layout);

        // Дефиниране на клиент и ресторант с невалиден тип
        MapEntity client = new MapEntity(new Location(1, 5), MapEntityType.CLIENT);
        // Ресторант с тип DELIVERY_GUY_CAR вместо RESTAURANT
        MapEntity invalidRestaurant = new MapEntity(new Location(6, 1), MapEntityType.DELIVERY_GUY_CAR);
        String foodItem = "Pizza";

        assertThrows(InvalidOrderException.class, () -> {
            glovo.getFastestDelivery(client, invalidRestaurant, foodItem);
        }, "Throws InvalidOrderException for not valid restaurant type");
    }

    @Test
    void testMultipleDeliveryGuysOn10x10Map() throws NoAvailableDeliveryGuyException {
        char[][] layout = {
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', 'R', '.', '.', '.', 'A', 'B', '.', 'C', '#'},
                {'#', '.', '#', '#', '#', '.', '#', '#', '.', '#'},
                {'#', '.', '.', '.', '#', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '.', '#', '#', '#', '#', '.', '#'},
                {'#', 'B', '#', '.', '.', '.', '.', '#', '.', '#'},
                {'#', '.', '#', '#', '#', '#', '.', '#', '.', '#'},
                {'#', '.', '.', '#', '#', '#', '.', '.', '.', '#'},
                {'#', 'A', '.', 'B', '#', '.', '.', 'A', '.', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };

        GlovoApi glovo = new Glovo(layout);

        MapEntity client = new MapEntity(new Location(1, 5), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 1), MapEntityType.RESTAURANT);
        String foodItem = "Burger";

        Delivery cheapestDelivery = glovo.getCheapestDelivery(client, restaurant, foodItem);
        assertNotNull(cheapestDelivery, "The cheapest delivery must not be null");
        assertTrue(cheapestDelivery.getPrice() > 0, "The price of the delivery must be positive");
        assertTrue(cheapestDelivery.getEstimatedTime() > 0, "Estimated time should be calculated correctly");
        assertEquals(cheapestDelivery.getPrice(), 3 * 12);
        assertEquals(cheapestDelivery.getEstimatedTime(), 5 * 12);

        Delivery fastestDelivery = glovo.getFastestDelivery(client, restaurant, foodItem);
        assertNotNull(fastestDelivery, "The fastest delivery must not be null");
        assertTrue(fastestDelivery.getPrice() > 0, "The price of the delivery must be positive");
        assertTrue(fastestDelivery.getEstimatedTime() > 0, "Estimated time should be calculated correctly");
    }
}