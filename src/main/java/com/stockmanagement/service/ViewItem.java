package com.stockmanagement.service;

import com.stockmanagement.entity.Item;

import java.util.List;

public class ViewItem {

    private final ViewItemService viewItemService;

    public ViewItem(ViewItemService viewItemService) {
        this.viewItemService = viewItemService;
    }

    public void viewItemConsole() {
        List<Item> items = viewItemService.getAllItems();
        if (items.isEmpty()) {
            System.out.println("No items in inventory.");
        } else {
            System.out.println("Inventory Items:");
            for (Item item : items) {
                System.out.println("ID: " + item.getId() + ", Name: " + item.getName() + ", Quantity: " + item.getQuantity() + ", Price: " + item.getPrice() + ", Category: " + item.getCategory());
            }
        }
    }
}