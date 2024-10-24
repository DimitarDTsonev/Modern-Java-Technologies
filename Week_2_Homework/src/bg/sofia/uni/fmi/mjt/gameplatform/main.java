package bg.sofia.uni.fmi.mjt.gameplatform;

import bg.sofia.uni.fmi.mjt.gameplatform.store.GameStore;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.Game;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.DLC;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.GameBundle;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.PriceItemFilter;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ReleaseDateItemFilter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class main {
    public static void main() {
        Game game1 = new Game("Cyberpunk 2077", "RPG", LocalDateTime.of(2020, 12, 10, 0, 0), new BigDecimal("59.99"));
        Game game2 = new Game("The Witcher 3", "RPG", LocalDateTime.of(2015, 5, 19, 0, 0), new BigDecimal("39.99"));
        Game game3 = new Game("Among Us", "Party", LocalDateTime.of(2018, 11, 16, 0, 0), new BigDecimal("4.99"));

        DLC dlc = new DLC("Blood and Wine", new BigDecimal("19.99"), LocalDateTime.of(2016, 5, 31, 0, 0), game2);
        Game[] bundleGames = {game1, game2};
        GameBundle bundle = new GameBundle("CD Projekt Bundle", new BigDecimal("79.99"), LocalDateTime.of(2020, 12, 10, 0, 0), bundleGames);

        GameStore store = new GameStore(new Game[] {game1, game2, game3});

        PriceItemFilter priceFilter = new PriceItemFilter(new BigDecimal("10.00"), new BigDecimal("60.00"));
        ReleaseDateItemFilter dateFilter = new ReleaseDateItemFilter(LocalDateTime.of(2015, 1, 1, 0, 0), LocalDateTime.now());

        System.out.println("Items between $10 and $60 released after 2015:");
        var filteredItems = store.findItemByFilters(new PriceItemFilter[] {priceFilter});
        for (var item : filteredItems) {
            System.out.println(item.getTitle() + " - $" + item.getPrice());
        }

        store.applyDiscount("VAN40");
        System.out.println("Prices after 40% discount:");
        for (var item : store.findItemByFilters(new PriceItemFilter[] {})) {
            System.out.println(item.getTitle() + " - $" + item.getPrice());
        }

        store.rateItem(game1, 5);
        store.rateItem(game1, 4);
        System.out.println("Cyberpunk 2077 rating: " + game1.getRating());
    }
}
