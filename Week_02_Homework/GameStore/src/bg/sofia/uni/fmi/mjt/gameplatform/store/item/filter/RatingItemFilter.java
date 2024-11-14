package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class RatingItemFilter implements ItemFilter {
    private double minRating;

    public RatingItemFilter(double minRating) {
        this.minRating = minRating;
    }

    public double getMinRating() {
        return minRating;
    }

    public RatingItemFilter setMinRating(double minRating) {
        this.minRating = minRating;
        return this;
    }

    /**
     * Checks if the given store item matches the filter.
     *
     * @param item the store item to be checked
     * @return true if the store item matches the filter, false otherwise
     */
    @Override
    public boolean matches(StoreItem item) {
        return item.getRating() >= minRating;
    }
}
