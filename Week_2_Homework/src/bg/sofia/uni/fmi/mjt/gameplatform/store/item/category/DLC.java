package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DLC implements StoreItem {
    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private Game game;
    private double totalRating;
    private int ratingCount;

    public DLC(String title, BigDecimal price, LocalDateTime releaseDate, Game game) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.game = game;
        totalRating = 0;
        ratingCount = 0;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public Game getGame() {
        return game;
    }

    public DLC setGame(Game game) {
        this.game = game;
        return this;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public double getRating() {
        return totalRating / ratingCount;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) this.price = BigDecimal.ZERO;
        else this.price = price;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        totalRating += rating;
        ratingCount++;
    }
}
