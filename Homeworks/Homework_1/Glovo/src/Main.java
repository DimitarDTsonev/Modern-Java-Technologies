import bg.sofia.uni.fmi.mjt.glovo.Glovo;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Main {
    public static void main(String[] args) {
        char[][] layout = {
//                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
//                {'#', 'R', '.', 'B', '.', 'A', '.', '.', 'A', '#'},
//                {'#', '.', '#', '#', '#', '.', '#', '#', '.', '#'},
//                {'#', '.', '.', '.', '#', '.', '.', '.', '.', '#'},
//                {'#', '#', '#', '.', '#', '#', '#', '#', '.', '#'},
//                {'#', 'C', '.', '.', '.', '.', '.', '#', '.', '#'},
//                {'#', '.', '#', '#', '#', '#', '.', '#', '.', '#'},
//                {'#', '.', '.', '.', '.', '#', '.', '.', '.', '#'},
//                {'#', 'B', '.', 'A', '.', '.', '.', 'A', '.', '#'},
//                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}

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

        try {
            Delivery cheapest = glovo.getCheapestDelivery(client, restaurant, foodItem);
            System.out.println("Cheapest Delivery: " + cheapest.getEstimatedTime() + " " + cheapest.getPrice());

            Delivery fastest = glovo.getFastestDelivery(client, restaurant, foodItem);
            System.out.println("Fastest Delivery: " + fastest. getEstimatedTime() + " " + fastest.getPrice());

            double maxPrice = 120.0;
            Delivery fastestUnderPrice = glovo.getFastestDeliveryUnderPrice(client, restaurant,
                    foodItem, maxPrice);
            System.out.println("Fastest Delivery Under Price: " +
                    fastestUnderPrice.getEstimatedTime() + " " + fastestUnderPrice.getPrice());

            int maxTime = 15;
            Delivery cheapestWithinTime = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant,
                    foodItem, maxTime);
            System.out.println("Cheapest Delivery Within Time Limit: " +
                    cheapestWithinTime.getEstimatedTime() + " " + cheapestWithinTime.getPrice());
        } catch (NoAvailableDeliveryGuyException e) {
            System.err.println(e.getMessage());
        }
    }
}
