package com.example.library_management.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.library_management.entity.Inventory;

@Repository
public interface InventoryElasticsearchRepository extends ElasticsearchRepository<Inventory, Long> {
    // Thêm các phương thức tìm kiếm tùy chỉnh nếu cần
}
