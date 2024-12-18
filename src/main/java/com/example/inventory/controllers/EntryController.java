package com.example.inventory.controllers;

import com.example.inventory.models.Entry;
import com.example.inventory.models.Product;
import com.example.inventory.repositories.EntryRepository;
import com.example.inventory.repositories.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Aplica CORS al controlador
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/entries")
public class EntryController {
    private final EntryRepository entryRepository;
    private final ProductRepository productRepository; // Inyección del repositorio de productos

    // Constructor con inyección de dependencias
    public EntryController(EntryRepository entryRepository, ProductRepository productRepository) {
        this.entryRepository = entryRepository;
        this.productRepository = productRepository; // Inicializar el repositorio de productos
    }

    @GetMapping
    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody Entry entry) {
        try {
            // Recupera el producto desde la base de datos
            Product product = entry.getProduct();
            if (product == null || product.getId() == null) {
                return ResponseEntity.badRequest().body("El producto es inválido.");
            }
    
            Product existingProduct = productRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
    
            // Actualiza la cantidad del producto
            Integer newQuantity = (existingProduct.getQuantity() == null ? 0 : existingProduct.getQuantity()) + entry.getQuantity();
            existingProduct.setQuantity(newQuantity);
    
            // Guarda los cambios en el producto
            productRepository.save(existingProduct);
    
            // Guarda la entrada
            Entry savedEntry = entryRepository.save(entry);
    
            return ResponseEntity.ok(savedEntry);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la entrada: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEntry(@PathVariable Long id) {
        return entryRepository.findById(id).map(entry -> {
            entryRepository.delete(entry);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
