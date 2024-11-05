package com.example.library_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library_management.entity.Inventory;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.elasticsearch.InventoryElasticsearchRepository;
import com.example.library_management.repository.jpa.InventoryRepository;

@Service
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final InventoryElasticsearchRepository inventoryElasticsearchRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository,
                            InventoryElasticsearchRepository inventoryElasticsearchRepository){
        this.inventoryRepository = inventoryRepository;
        this.inventoryElasticsearchRepository = inventoryElasticsearchRepository;
    }

    // Lấy tất cả Inventory
    public List<Inventory> getAllInventories(){
        return inventoryRepository.findAll();
    }

    // Lấy Inventory theo ID
    public Optional<Inventory> getInventoryById(Long id){
        return inventoryRepository.findById(id);
    }

    // Tạo Inventory mới
    public Inventory createInventory(Inventory inventory){
        Inventory savedInventory = inventoryRepository.save(inventory);
        inventoryElasticsearchRepository.save(savedInventory);
        return savedInventory;
    }

    // Cập nhật Inventory
    public Inventory updateInventory(Long id, Inventory inventoryDetails){
        Inventory updatedInventory = inventoryRepository.findById(id).map(inventory -> {
            inventory.setTotalStock(inventoryDetails.getTotalStock());
            inventory.setAvailableStock(inventoryDetails.getAvailableStock());
            // Cập nhật các thuộc tính khác nếu cần
            return inventoryRepository.save(inventory);
        }).orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id " + id));
        
        // Cập nhật trong Elasticsearch
        inventoryElasticsearchRepository.save(updatedInventory);
        return updatedInventory;
    }

    // Xóa Inventory
    public void deleteInventory(Long id){
        inventoryRepository.deleteById(id);
        inventoryElasticsearchRepository.deleteById(id);
    }

    // Thêm các phương thức nghiệp vụ khác nếu cần
}
