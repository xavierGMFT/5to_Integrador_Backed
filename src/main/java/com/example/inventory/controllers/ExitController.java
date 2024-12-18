package com.example.inventory.controllers;

import com.example.inventory.models.Exit;
import com.example.inventory.repositories.ExitRepository;
import com.example.inventory.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// Aplica CORS al controlador
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/exits")
public class ExitController {
    private final ExitRepository exitRepository;
    private final ProductRepository productRepository;

    public ExitController(ExitRepository exitRepository, ProductRepository productRepository) {
        this.exitRepository = exitRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Exit> getAllExits() {
        return exitRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<Exit> createExit(@RequestBody Exit exit) {
        return (ResponseEntity<Exit>) productRepository.findById(exit.getProduct().getId()).map(product -> {
            if (product.getQuantity() >= exit.getQuantity()) {
                product.setQuantity(product.getQuantity() - exit.getQuantity());
                productRepository.save(product);
                return ResponseEntity.ok(exitRepository.save(exit));
            } else {
                return ResponseEntity.badRequest().build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExit(@PathVariable Long id) {
        return exitRepository.findById(id).map(exit -> {
            exitRepository.delete(exit);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
