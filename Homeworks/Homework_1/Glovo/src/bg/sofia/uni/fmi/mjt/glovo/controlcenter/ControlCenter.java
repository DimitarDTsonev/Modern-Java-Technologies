package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ControlCenter implements ControlCenterApi {

    private final MapEntity[][] map;
    private final List<MapEntity> deliveryGuys;
    private final int rows;
    private final int cols;
    private final int directionsCount = 4;

    /**
     * Makes the map from the layout that is char[][] to MapEntity
     *
     * @param layout char[][] of the layout
     */
    public ControlCenter(char[][] layout) {

        this.map = convertToMapEntities(layout);
        this.deliveryGuys = findDeliveryGuys();
        this.rows = map.length;
        this.cols = map[0].length;
    }

    /**
     * Finds the optimal delivery person for a given delivery task. The method
     * selects the best delivery option based on the provided cost and time constraints.
     * If no valid delivery path exists, it returns null.
     *
     * @param restaurantLocation The location of the restaurant to start the delivery from.
     * @param clientLocation     The location of the client receiving the delivery.
     * @param maxPrice           The maximum price allowed for the delivery. Use -1 for no cost constraint.
     * @param maxTime            The maximum time allowed for the delivery. Use -1 for no time constraint.
     * @param shippingMethod     The method for shipping the delivery.
     * @return A DeliveryInfo object containing the optimal delivery guy, the total cost,
     * the total time, and the delivery type. Returns null if no valid path is found.
     */
    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation,
                                               double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        List<DeliveryInfo> potentialDeliveries = new ArrayList<>();

        for (MapEntity deliveryGuy : deliveryGuys) {
            int distanceToRestaurant = bfs(deliveryGuy.location(), restaurantLocation);
            if (distanceToRestaurant == -1) continue;

            int distanceToClient = bfs(restaurantLocation, clientLocation);
            if (distanceToClient == -1) continue;

            int totalDistance = distanceToRestaurant + distanceToClient;

            DeliveryType deliveryType = getDeliveryType(deliveryGuy.type());

            double totalPrice = totalDistance * deliveryType.getPricePerKM();
            int totalTime = totalDistance * deliveryType.getTimePerKM();

            if ((maxPrice == -1 || totalPrice <= maxPrice) && (maxTime == -1 || totalTime <= maxTime)) {
                DeliveryInfo info = new DeliveryInfo(
                        deliveryGuy.location(),
                        totalPrice,
                        totalTime,
                        deliveryType
                );
                potentialDeliveries.add(info);
            }
        }

        if (potentialDeliveries.isEmpty()) {
            return null;
        }

        DeliveryInfo optimal = null;
        for (DeliveryInfo info : potentialDeliveries) {
            if (optimal == null) {
                optimal = info;
                continue;
            }
            if (shippingMethod == ShippingMethod.CHEAPEST) {
                if (info.price() < optimal.price()) {
                    optimal = info;
                }
            } else {
                if (info.estimatedTime() < optimal.estimatedTime()) {
                    optimal = info;
                }
            }
        }

        return optimal;
    }

    /**
     * Returns the map
     *
     * @return A MapEntity[][] containing the map
     */
    @Override
    public MapEntity[][] getLayout() {
        return map;
    }

    /**
     * Converting the layout char[][] to MapEntity
     *
     * @param layout    char[][] of the layout
     * @return          MapEntity of the new map
     */
    private MapEntity[][] convertToMapEntities(char[][] layout) {
        int rows = layout.length;
        int cols = layout[0].length;
        MapEntity[][] mapEntities = new MapEntity[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Location location = new Location(i, j);
                MapEntityType type = MapEntityType.fromChar(layout[i][j]);
                mapEntities[i][j] = new MapEntity(location, type);
            }
        }
        return mapEntities;
    }

    /**
     * Finding the delivery guys.
     *
     * @return List<MapEntity> list of the delivery guys
     */
    private List<MapEntity> findDeliveryGuys() {
        List<MapEntity> deliveryGuys = new ArrayList<>();
        for (MapEntity[] row : map) {
            for (MapEntity entity : row) {
                if (entity.type() == MapEntityType.DELIVERY_GUY_CAR ||
                        entity.type() == MapEntityType.DELIVERY_GUY_BIKE) {
                    deliveryGuys.add(entity);
                }
            }
        }
        return deliveryGuys;
    }

    /**
     * Finding the type of the delivery guy.
     *
     * @return DeliveryType of the MapEntity
     */
    private DeliveryType getDeliveryType(MapEntityType type) {
        switch (type) {
            case DELIVERY_GUY_CAR:
                return DeliveryType.CAR;
            case DELIVERY_GUY_BIKE:
                return DeliveryType.BIKE;
            default:
                throw new IllegalArgumentException("Invalid MapEntityType for delivery guy.");
        }
    }

    /**
     * Breath First Search algorithm to find the length of the fastest road between two coordinates in the map
     *
     * @param start starting location of searching
     * @param end   ending point of searching if found
     * @return      the length of the closet path
     */
    private int bfs(Location start, Location end) {
        if (start.equals(end)) return 0;

        boolean[][] visited = new boolean[rows][cols];
        int[][] distance = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                distance[i][j] = -1;
            }
        }

        Queue<Location> queue = new LinkedList<>();
        queue.add(start);
        visited[start.x()][start.y()] = true;
        distance[start.x()][start.y()] = 0;

        int[] dirX = {-1, 1, 0, 0};
        int[] dirY = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            for (int i = 0; i < directionsCount; i++) {
                int newX = current.x() + dirX[i];
                int newY = current.y() + dirY[i];

                if (isValid(newX, newY) && !visited[newX][newY] &&
                        map[newX][newY].type() != MapEntityType.WALL) {

                    visited[newX][newY] = true;
                    distance[newX][newY] = distance[current.x()][current.y()] + 1;
                    Location neighbor = new Location(newX, newY);
                    queue.add(neighbor);

                    if (neighbor.equals(end)) {
                        return distance[newX][newY];
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Validates the coordinates for map in the class.
     *
     * @param x  First coordinate of the Location
     * @param y  Second coordinate of the Location
     * @return   True if the coordinates are possible for the map
     */
    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }
}