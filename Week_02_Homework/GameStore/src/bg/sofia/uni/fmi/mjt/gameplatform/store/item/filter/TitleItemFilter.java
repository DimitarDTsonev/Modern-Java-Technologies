package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class TitleItemFilter implements ItemFilter {
    private String title;
    boolean caseSensitive;

    public TitleItemFilter(String title, boolean caseSensitive) {
        this.title = title;
        this.caseSensitive = caseSensitive;
    }

    public String getTitle() {
        return title;
    }

    public TitleItemFilter setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public TitleItemFilter setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
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
        String itemTitle = item.getTitle();

        if (caseSensitive) {
            return itemTitle.contains(title);
        }

        return itemTitle.toLowerCase().contains(title.toLowerCase());
    }
}
