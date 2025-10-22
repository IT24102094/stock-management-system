package com.stockmanagement.service;

import com.stockmanagement.entity.Product;
import com.stockmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
    @Transactional
    public class ProductService {

        @Autowired
        private ProductRepository productRepository;

        public List<Product> getAllProducts() {
            return productRepository.findAll();
        }

        public Optional<Product> getProductById(Long id) {
            return productRepository.findById(id);
        }

        public Product createProduct(Product product) {
            product.setCreatedDate(LocalDateTime.now());
            product.setUpdatedDate(LocalDateTime.now());
            return productRepository.save(product);
        }

        public Product updateProduct(Long id, Product productDetails) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setQuantityInStock(productDetails.getQuantityInStock());
            product.setSku(productDetails.getSku());
            product.setCategory(productDetails.getCategory());
            product.setUpdatedDate(LocalDateTime.now());

            return productRepository.save(product);
        }

        public void deleteProduct(Long id) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            productRepository.delete(product);
        }

        public Product updateStock(Long id, Integer quantity) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getQuantityInStock() < quantity) {
                throw new RuntimeException("Insufficient stock");
            }

            product.setQuantityInStock(product.getQuantityInStock() - quantity);
            product.setUpdatedDate(LocalDateTime.now());

            return productRepository.save(product);
        }
        
        /**
         * Restore stock to inventory (for returns or cancelled bills)
         */
        public Product restoreStock(Long id, Integer quantity) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setQuantityInStock(product.getQuantityInStock() + quantity);
            product.setUpdatedDate(LocalDateTime.now());

            return productRepository.save(product);
        }

        public List<Product> getAvailableProducts() {
            return productRepository.findByQuantityInStockGreaterThan(0);
        }
    }


