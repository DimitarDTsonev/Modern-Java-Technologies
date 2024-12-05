package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidLocationEntityException;

public enum MapEntityType {
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B'),
    RESTAURANT('R'),
    ROAD('.'),
    WALL('#');

    private final char symbol;

    MapEntityType(final char symbol) {

        this.symbol = symbol;
    }

    public char getSymbol() {

        return symbol;
    }

    public static MapEntityType fromChar(char symbol) {

        for (final MapEntityType type : MapEntityType.values()) {

            if (type.getSymbol() == symbol) {

                return type;
            }
        }

        throw new InvalidLocationEntityException("Invalid symbol: " + symbol);
    }

}