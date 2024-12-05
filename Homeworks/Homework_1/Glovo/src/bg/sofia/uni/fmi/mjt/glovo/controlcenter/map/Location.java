package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidLocationEntityException;

public record Location(int x, int y) {

    public Location {
        if (x < 0 || y < 0) {

            throw new InvalidLocationEntityException("Invalid coordinates");
        }
    }
}
