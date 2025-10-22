package com.stockmanagement.controller;


import com.stockmanagement.entity.Item;
import com.stockmanagement.service.AddItemService;
import com.stockmanagement.service.DeleteItemService;
import com.stockmanagement.service.UpdateItemService;
import com.stockmanagement.service.ViewItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private AddItemService addItemService;

    @Autowired
    private ViewItemService viewItemService;

    @Autowired
    private UpdateItemService updateItemService;

    @Autowired
    private DeleteItemService deleteItemService;

    @GetMapping
    public String home() {
        return "redirect:/"; // serve static index.html at root
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Inventory/Inventory-dashboard";
    }

    @GetMapping("/list")
    public String listItems(Model model) {
        List<Item> items = viewItemService.getAllItems();
        model.addAttribute("items", items);
        return "items-list";
    }
    
    @GetMapping("/api/list")
    @ResponseBody
    public List<Item> listItemsApi() {
        return viewItemService.getAllItems();
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addItem(@RequestBody Item item, Model model) {
        try {
            addItemService.addItem(item);
            return ResponseEntity.ok("Item added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding item: " + e.getMessage());
        }
    }
    
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<String> addItemApi(@RequestBody Item item) {
        try {
            addItemService.addItem(item);
            return ResponseEntity.ok("Item added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding item: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<String> updateItem(@PathVariable int id, @RequestBody Item item, Model model) {
        try {
            item.setId(id);
            updateItemService.updateItem(item);
            return ResponseEntity.ok("Item updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating item: " + e.getMessage());
        }
    }
    
    @PutMapping("/api/update/{id}")
    @ResponseBody
    public ResponseEntity<String> updateItemApi(@PathVariable int id, @RequestBody Item item) {
        try {
            item.setId(id);
            updateItemService.updateItem(item);
            return ResponseEntity.ok("Item updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating item: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteItem(@PathVariable int id, Model model) {
        try {
            deleteItemService.deleteItem(id);
            return ResponseEntity.ok("Item deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting item: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteItemApi(@PathVariable int id) {
        try {
            deleteItemService.deleteItem(id);
            return ResponseEntity.ok("Item deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting item: " + e.getMessage());
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("item", new Item());
        return "add-item";
    }

    @PostMapping("/add/form")
    public String addItemForm(@ModelAttribute Item item, Model model) {
        try {
            addItemService.addItem(item);
            model.addAttribute("message", "Item added successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error adding item: " + e.getMessage());
        }
        return "redirect:/items/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        try {
            Optional<Item> optionalItem = viewItemService.getItemById(id);
            Item item = optionalItem.orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));
            model.addAttribute("item", item);
            return "update-item";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/items/list";
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching item: " + e.getMessage());
            return "redirect:/items/list";
        }
    }

    @PostMapping("/update/{id}")
    public String updateItemForm(@PathVariable int id, @ModelAttribute Item item, Model model) {
        try {
            item.setId(id);
            updateItemService.updateItem(item);
            model.addAttribute("message", "Item updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating item: " + e.getMessage());
        }
        return "redirect:/items/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteItemForm(@PathVariable int id, Model model) {
        try {
            deleteItemService.deleteItem(id);
            model.addAttribute("message", "Item deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting item: " + e.getMessage());
        }
        return "redirect:/items/list";
    }
}
