package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;

public class GameStore implements StoreAPI {
    private StoreItem[] avaliableItems;
    private static final String VAN40 = "Van40";
    private static final String YO100= "100YO";


    public GameStore(StoreItem[] avaliableItems) {
        this.avaliableItems = avaliableItems;
    }

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
                if(matchesAll) {
                    filteredItems[count++] = item;
                }
            }
        }

        StoreItem[] result = new StoreItem[count];
        System.arraycopy(filteredItems, 0, result, 0, count);
        return result;
    }

    @Override
    public void applyDiscount(String promoCode) {
        BigDecimal discountMultipliyer = BigDecimal.ONE;

        if(VAN40.equals(promoCode)) {
            discountMultipliyer = new BigDecimal("0.60");
        } else if(YO100.equals(promoCode)) {
            discountMultipliyer = BigDecimal.ZERO;
        }

        for (StoreItem item : avaliableItems) {
            BigDecimal newPrice = item.getPrice().multiply(discountMultipliyer).setScale(2, BigDecimal.ROUND_HALF_UP);
            item.setPrice(newPrice);
        }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if (rating < 1 || rating > 5) {
            return false;
        }

        item.rate(rating);
        return true;
    }
}
