package com.stockmanagement.service;

import com.stockmanagement.entity.Item;
import java.util.Scanner;

public class UpdateItem {

    private final UpdateItemService updateItemService;

    public UpdateItem(UpdateItemService updateItemService) {
        this.updateItemService = updateItemService;
    }

    public void updateItemConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item ID to update: ");
        long id = scanner.nextLong();
        scanner.nextLine();  // Clear buffer
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter new price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();  // Clear buffer
        System.out.print("Enter new category: ");
        String category = scanner.nextLine();

        Item item = new Item(id, name, quantity, java.math.BigDecimal.valueOf(price), category);
        updateItemService.updateItem(item);
        System.out.println("Item with ID " + id + " updated.");
    }
}