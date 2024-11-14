package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;

public class GameStore implements StoreAPI {
    private StoreItem[] avaliableItems;
    private static final String VAN40 = "Van40";
    private static final String YO100 = "100YO";
    private static final int RATING_MIN = 1;
    private static final int RATING_MAX = 5;

    public GameStore(StoreItem[] avaliableItems) {
        this.avaliableItems = avaliableItems;
    }

    /**
     * Finds all store items that match the given filters.
     *
     * @param itemFilters the filters to be applied
     * @return an array of store items that match all filters or an empty array if no such items are found
     */
    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        StoreItem[] filteredItems = new StoreItem[avaliableItems.length];
        int count = 0;

        for (StoreItem item : avaliableItems) {
            boolean matchesAll = true;

            for (ItemFilter filter : itemFilters) {
                if (!filter.matches(item)) {
                    matchesAll = false;
                    break;
                }
                if (matchesAll) {
                    filteredItems[count++] = item;
                }
            }
        }

        StoreItem[] result = new StoreItem[count];
        System.arraycopy(filteredItems, 0, result, 0, count);
        return result;
    }

    /**
     * Applies a promo code discount to all store items.
     * If the promo code is not valid, no discount is applied.
     *
     * @param promoCode the promo code to be applied
     */
    @Override
    public void applyDiscount(String promoCode) {
        BigDecimal discountMultipliyer = BigDecimal.ONE;

        if (VAN40.equals(promoCode)) {
            discountMultipliyer = new BigDecimal("0.60");
        } else if (YO100.equals(promoCode)) {
            discountMultipliyer = BigDecimal.ZERO;
        }

        for (StoreItem item : avaliableItems) {
            BigDecimal newPrice = item.getPrice().multiply(discountMultipliyer).setScale(2, BigDecimal.ROUND_HALF_UP);
            item.setPrice(newPrice);
        }
    }

    /**
     * Rates a store item.
     *
     * @param item the item to be rated
     * @param rating the rating to be given in the range [1, 5]
     * @return true if the item is successfully rated, false otherwise
     */
    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if (rating < RATING_MIN || rating > RATING_MAX) {
            return false;
        }

        item.rate(rating);
        return true;
    }
}