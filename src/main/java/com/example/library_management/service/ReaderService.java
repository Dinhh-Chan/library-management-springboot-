package com.example.library_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.library_management.entity.Reader;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.elasticsearch.ReaderElasticsearchRepository;
import com.example.library_management.repository.jpa.ReaderRepository;

@Service
public class ReaderService {
    
    private final ReaderRepository readerRepository;
    private final ReaderElasticsearchRepository readerElasticsearchRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ReaderService(ReaderRepository readerRepository,
                         ReaderElasticsearchRepository readerElasticsearchRepository,
                         PasswordEncoder passwordEncoder){
        this.readerRepository = readerRepository;
        this.readerElasticsearchRepository = readerElasticsearchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Lấy tất cả người đọc
    public List<Reader> getAllReaders(){
        return readerRepository.findAll();
    }

    // Lấy người đọc theo ID
    public Optional<Reader> getReaderById(Long id){
        return readerRepository.findById(id);
    }

    // Tạo người đọc mới
    public Reader createReader(Reader reader){
        // Mã hóa mật khẩu trước khi lưu
        reader.setPassword(passwordEncoder.encode(reader.getPassword()));
        Reader savedReader = readerRepository.save(reader);
        readerElasticsearchRepository.save(savedReader);
        return savedReader;
    }

    // Cập nhật người đọc
    public Reader updateReader(Long id, Reader readerDetails){
        Reader updatedReader = readerRepository.findById(id).map(reader -> {
            reader.setContactInfo(readerDetails.getContactInfo());
            reader.setQuota(readerDetails.getQuota());
            reader.setUsername(readerDetails.getUsername());
            if(readerDetails.getPassword() != null && !readerDetails.getPassword().isEmpty()){
                reader.setPassword(passwordEncoder.encode(readerDetails.getPassword()));
            }
            reader.setRole(readerDetails.getRole());
            // Cập nhật các thuộc tính khác nếu cần
            return readerRepository.save(reader);
        }).orElseThrow(() -> new ResourceNotFoundException("Reader not found with id " + id));
        
        // Cập nhật trong Elasticsearch
        readerElasticsearchRepository.save(updatedReader);
        return updatedReader;
    }

    // Xóa người đọc
    public void deleteReader(Long id){
        readerRepository.deleteById(id);
        readerElasticsearchRepository.deleteById(id);
    }

    // Tìm người đọc theo username
    public List<Reader> findByUsername(String username) {
        return readerElasticsearchRepository.findByUsernameContaining(username);
    }

    // Tìm người đọc chứa từ khóa trong username
    public List<Reader> searchByUsernameKeyword(String keyword) {
        return readerElasticsearchRepository.findByUsernameContaining(keyword);
    }
}
