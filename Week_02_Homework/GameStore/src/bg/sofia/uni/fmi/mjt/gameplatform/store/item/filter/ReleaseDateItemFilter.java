package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.time.LocalDateTime;

public class ReleaseDateItemFilter implements ItemFilter {
    private LocalDateTime lowerBound;
    private LocalDateTime upperBound;

    public ReleaseDateItemFilter(LocalDateTime lowerBound, LocalDateTime upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public LocalDateTime getLowerBound() {
        return lowerBound;
    }

    public ReleaseDateItemFilter setLowerBound(LocalDateTime lowerBound) {
        this.lowerBound = lowerBound;
        return this;
    }

    public LocalDateTime getUpperBound() {
        return upperBound;
    }

    public ReleaseDateItemFilter setUpperBound(LocalDateTime upperBound) {
        this.upperBound = upperBound;
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
        LocalDateTime releaseDate = item.getReleaseDate();
        return (releaseDate.isAfter(lowerBound)) || (releaseDate.isEqual(lowerBound)) &&
                (releaseDate.isBefore(upperBound)) || (releaseDate.isEqual(upperBound));
    }
}
