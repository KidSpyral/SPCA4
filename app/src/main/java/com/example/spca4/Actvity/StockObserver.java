package com.example.spca4.Actvity;

import com.example.spca4.Model.Items;

import java.util.List;

public interface StockObserver {
    void onStockUpdate(List<Items> stockList);
}
