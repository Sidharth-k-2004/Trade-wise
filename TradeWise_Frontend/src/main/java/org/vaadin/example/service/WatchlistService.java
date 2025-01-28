package org.vaadin.example.service;

import org.vaadin.example.Stock;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class WatchlistService {
    private static WatchlistService instance;
    private List<Stock> watchlist = new ArrayList<>();

    private WatchlistService() {}

    public static synchronized WatchlistService getInstance() {
        if (instance == null) {
            instance = new WatchlistService();
        }
        return instance;
    }

    public void addToWatchlist(Stock stock) {
        if (!watchlist.contains(stock)) {
            watchlist.add(stock);
        }
    }

    public void removeFromWatchlist(Stock stock) {
        watchlist.remove(stock);
    }

    public List<Stock> getWatchlist() {
        return new ArrayList<>(watchlist);
    }
}

