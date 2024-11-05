package com.example.library_management.repository.elasticsearch;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.library_management.entity.Borrowing;
import com.example.library_management.enums.BorrowingStatus;

@Repository
public interface BorrowingElasticsearchRepository extends ElasticsearchRepository<Borrowing, Long>{
    List<Borrowing> findByStatus(BorrowingStatus status);
    List<Borrowing> findByStatusContaining(String keyword);
    List<Borrowing> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate);
}
