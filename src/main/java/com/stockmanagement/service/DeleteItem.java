package com.stockmanagement.service;

import java.util.Scanner;

public class DeleteItem {

    private final DeleteItemService deleteItemService;

    public DeleteItem(DeleteItemService deleteItemService) {
        this.deleteItemService = deleteItemService;
    }

    public void deleteItemConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item ID to delete: ");
        int id = scanner.nextInt();
        deleteItemService.deleteItem(id);
        System.out.println("Item with ID " + id + " deleted.");
    }
}