package com.example.library_management.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.library_management.entity.Book;
import com.example.library_management.entity.Borrowing;
import com.example.library_management.entity.Inventory;
import com.example.library_management.entity.Reader;
import com.example.library_management.enums.BorrowingStatus;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.elasticsearch.BorrowingElasticsearchRepository;
import com.example.library_management.repository.jpa.BookRepository;
import com.example.library_management.repository.jpa.BorrowingRepository;
import com.example.library_management.repository.jpa.ReaderRepository;

@Service
public class BorrowingService {
    
    private final BorrowingRepository borrowingRepository;
    private final BorrowingElasticsearchRepository borrowingElasticsearchRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BorrowingService(BorrowingRepository borrowingRepository,
                            BorrowingElasticsearchRepository borrowingElasticsearchRepository,
                            ReaderRepository readerRepository,
                            BookRepository bookRepository){
        this.borrowingRepository = borrowingRepository;
        this.borrowingElasticsearchRepository = borrowingElasticsearchRepository;
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
    }

    // Lấy tất cả các lần mượn
    public List<Borrowing> getAllBorrowings(){
        return borrowingRepository.findAll();
    }

    // Lấy lần mượn theo ID
    public Borrowing getBorrowingById(Long id){
        return borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id " + id));
    }

    // Tạo lần mượn mới
    @Transactional
    public Borrowing createBorrowing(Long readerId, Long bookId, LocalDate borrowDate, LocalDate returnDate){
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with id " + readerId));
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + bookId));
        
        // Kiểm tra số lượng sách có sẵn
        Inventory inventory = book.getInventory();
        if(inventory == null || inventory.getAvailableStock() <= 0){
            throw new IllegalStateException("No available stock for book id " + bookId);
        }
        
        // Giảm số lượng sách có sẵn
        inventory.setAvailableStock(inventory.getAvailableStock() - 1);
        // Cập nhật Inventory
        bookRepository.save(book); // Đảm bảo thay đổi được lưu
        
        // Tạo Borrowing
        Borrowing borrowing = new Borrowing();
        borrowing.setReader(reader);
        borrowing.setBook(book);
        borrowing.setBorrowDate(borrowDate);
        borrowing.setReturnDate(returnDate);
        borrowing.setStatus(BorrowingStatus.DANG_MUON);
        
        Borrowing savedBorrowing = borrowingRepository.save(borrowing);
        borrowingElasticsearchRepository.save(savedBorrowing);
        return savedBorrowing;
    }

    // Cập nhật lần mượn (ví dụ: trả sách)
    @Transactional
    public Borrowing returnBook(Long borrowingId, LocalDate actualReturnDate){
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id " + borrowingId));
        
        if(borrowing.getStatus() == BorrowingStatus.DA_TRA){
            throw new IllegalStateException("Book already returned");
        }
        
        borrowing.setActualReturnDate(actualReturnDate);
        borrowing.setStatus(BorrowingStatus.DA_TRA);
        
        // Tăng số lượng sách có sẵn
        Inventory inventory = borrowing.getBook().getInventory();
        if(inventory != null){
            inventory.setAvailableStock(inventory.getAvailableStock() + 1);
            bookRepository.save(borrowing.getBook());
        }
        
        Borrowing updatedBorrowing = borrowingRepository.save(borrowing);
        borrowingElasticsearchRepository.save(updatedBorrowing);
        return updatedBorrowing;
    }

    // Xóa lần mượn
    public void deleteBorrowing(Long id){
        borrowingRepository.deleteById(id);
        borrowingElasticsearchRepository.deleteById(id);
    }

    // Thêm các phương thức nghiệp vụ khác nếu cần

    // Tìm kiếm các lần mượn theo trạng thái
    public List<Borrowing> findByStatus(BorrowingStatus status){
        return borrowingElasticsearchRepository.findByStatus(status);
    }

    // Tìm kiếm các lần mượn chứa từ khóa trong trạng thái
    public List<Borrowing> searchByStatusKeyword(String keyword){
        return borrowingElasticsearchRepository.findByStatusContaining(keyword);
    }
    public List<Borrowing> searchBorrowingsByBorrowDateRange(LocalDate startDate, LocalDate endDate) {
        return borrowingRepository.findByBorrowDateBetween(startDate, endDate);
    }
        public List<Borrowing> getBorrowingsByWeek(int year, int week) {
        // Sử dụng WeekFields để xác định ngày bắt đầu của tuần
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = LocalDate
                .now()
                .withYear(year)
                .with(weekFields.weekOfWeekBasedYear(), week)
                .with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return borrowingRepository.findByBorrowDateBetween(startOfWeek, endOfWeek);
    }

    /**
     * Tìm các giao dịch mượn trong một tháng cụ thể của năm
     * @param year Năm cần tìm
     * @param month Số tháng trong năm (1-12)
     * @return Danh sách các giao dịch mượn trong tháng đó
     */
    public List<Borrowing> getBorrowingsByMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return borrowingRepository.findByBorrowDateBetween(start, end);
    }

    /**
     * Tìm các giao dịch mượn trong một năm cụ thể
     * @param year Năm cần tìm
     * @return Danh sách các giao dịch mượn trong năm đó
     */
    public List<Borrowing> getBorrowingsByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return borrowingRepository.findByBorrowDateBetween(start, end);
    }
    public long countBorrowingsByStatus(List<Borrowing> borrowings, BorrowingStatus status) {
        return borrowings.stream().filter(b -> b.getStatus() == status).count();
    }
    public List<Borrowing> searchBorrowingsByStatus(BorrowingStatus status) {
        return borrowingRepository.findByStatus(status);
    }


}
