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

    @Override
    public boolean matches(StoreItem item) {
        return item.getRating() >= minRating;
    }
}
