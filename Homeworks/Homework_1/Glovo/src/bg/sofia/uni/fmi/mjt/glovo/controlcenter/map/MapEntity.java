package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public record MapEntity(Location location, MapEntityType type) {

    public MapEntity {

        if (location == null || type == null) {

            throw new IllegalArgumentException("The location and type cannot be null");
        }
    }

    public Location getLocation() {

        return location;
    }

    public MapEntityType getType() {

        return type;
    }

}