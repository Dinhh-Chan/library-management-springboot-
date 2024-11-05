package com.example.library_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library_management.dto.ReaderRequest;
import com.example.library_management.entity.Reader;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.service.ReaderService;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {
    
    private final ReaderService readerService;

    public ReaderController(ReaderService readerService){
        this.readerService = readerService;
    }

    // Lấy tất cả người đọc
    @GetMapping
    public List<Reader> getAllReaders(){
        return readerService.getAllReaders();
    }

    // Lấy người đọc theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable Long id){
        return readerService.getReaderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo người đọc mới
    @PostMapping
    public ResponseEntity<Reader> createReader(@RequestBody ReaderRequest readerRequest){
        Reader reader = readerRequest.toReader();
        Reader createdReader = readerService.createReader(reader);
        return ResponseEntity.status(201).body(createdReader);
    }

    // Cập nhật người đọc
    @PutMapping("/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable Long id, @RequestBody ReaderRequest readerRequest){
        try {
            Reader readerDetails = readerRequest.toReader();
            Reader updatedReader = readerService.updateReader(id, readerDetails);
            return ResponseEntity.ok(updatedReader);
        } catch (ResourceNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa người đọc
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id){
        try {
            readerService.deleteReader(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Tìm kiếm độc giả theo username chính xác
     * GET /api/readers/search?username=johndoe
     */
    @GetMapping("/search")
    public ResponseEntity<List<Reader>> searchReadersByUsername(@RequestParam String username){
        List<Reader> readers = readerService.findByUsername(username);
        return ResponseEntity.ok(readers);
    }

    /**
     * Tìm kiếm độc giả chứa từ khóa trong username
     * GET /api/readers/search/keyword?keyword=john
     */
    @GetMapping("/search/keyword")
    public ResponseEntity<List<Reader>> searchReadersByUsernameContaining(@RequestParam String keyword){
        List<Reader> readers = readerService.findByUsernameContaining(keyword);
        return ResponseEntity.ok(readers);
    }
}
