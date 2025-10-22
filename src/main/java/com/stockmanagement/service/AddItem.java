package com.stockmanagement.service;

import com.stockmanagement.entity.Item;

import java.util.Scanner;

public class AddItem {

    private final AddItemService addItemService;

    public AddItem(AddItemService addItemService) {
        this.addItemService = addItemService;
    }

    public void addItemConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();  // Clear buffer
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        Item item = new Item(0L, name, quantity, java.math.BigDecimal.valueOf(price), category);
        addItemService.addItem(item);
        System.out.println("Item added: " + name + " (Category: " + category + ")");
    }
}