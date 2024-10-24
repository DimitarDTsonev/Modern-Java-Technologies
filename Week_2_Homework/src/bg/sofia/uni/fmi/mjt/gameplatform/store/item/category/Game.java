package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Game implements StoreItem {
    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private String genre;
    private double totalRating;
    private int ratingCount;

    public Game(String title, String genre, LocalDateTime releaseDate, BigDecimal price) {
        this.title = title;
        this.ratingCount = 0;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.price = price;
        this.totalRating = 0;
    }

    public String getGenre() {
        return genre;
    }

    public Game setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public double getRating() {
        return totalRating / ratingCount;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        this.totalRating += rating;
        this.ratingCount++;
    }
}