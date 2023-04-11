//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.flab.inventory.application.DecreaseInventoryProcessor;
//import com.flab.inventory.application.command.DecreaseInventoryCommand;
//import com.flab.inventory.domain.Inventory;
//import com.flab.inventory.domain.Inventory.InventoryState;
//import com.flab.inventory.domain.Inventory.SaleStatus;
//import com.flab.inventory.domain.InventoryRepository;
//import com.flab.inventory.domain.data.ItemQuantity;
//import com.flab.inventory.infrastructure.config.InventoryRepositoryConfig;
//import com.flab.inventory.infrastructure.jpaconfig.InventoryJpaConfig;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionStatus;
//
//@ContextConfiguration(
//    classes = {
//        InventoryRepositoryConfig.class,
//        InventoryJpaConfig.class,
//    }
//)
//@DataJpaTest
//@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
//@TestPropertySource(locations = "classpath:application-test.yml")
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//public class test {
//
//    @Autowired
//    private InventoryRepository inventoryRepository;
//
//    @Autowired
//    private PlatformTransactionManager transactionManager;
//
//
//
//    @Rollback(value = false)
//    @Test
//    @DisplayName("동시에 100명의 요청이 들어와 재고가 감소한다.")
//    void test5() throws InterruptedException {
//        //Arrange
//        int THREAD_COUNT = 2;
//        int NUM_THREADS = 32;
//        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
//        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
//        Inventory inventory = Inventory.create(1L, SaleStatus.ON_SALE, "test", 100, InventoryState.INVENTORY_SAFE);
//        Long inventoryId = inventoryRepository.save(inventory).getId();
//        List<ItemQuantity> itemQuantities = List.of(createItemQuantity(1L, 1));
//        DecreaseInventoryCommand command = createDecreaseInventoryCommand(itemQuantities);
//        var sut = new DecreaseInventoryProcessor(inventoryRepository);
//
//        //Act
//        for (int i = 0; i < THREAD_COUNT; i++) {
//            executorService.submit(() -> {
//                try {
//                    TransactionStatus status = transactionManager.getTransaction(null);
//                    sut.execute(command);
//                    transactionManager.commit(status);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        //Assert
//        assertThat(inventoryRepository.findById(inventoryId).getQuantity()).isZero();
//    }
//
//
//    @Rollback(value = false)
//    @Test
//    @DisplayName("동시에 100명의 요청이 들어와 재고가 감소한다.")
//    void test6() throws InterruptedException {
//        //Arrange
//        int THREAD_COUNT = 2;
//        int NUM_THREADS = 32;
//        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
//        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
//        Inventory inventory = Inventory.create(1L, SaleStatus.ON_SALE, "test", 100, InventoryState.INVENTORY_SAFE);
//        Long inventoryId = inventoryRepository.save(inventory).getId();
//        var sut = new DecreaseInventoryProcessor(inventoryRepository);
//
//        //Act
//        for (int i = 0; i < THREAD_COUNT; i++) {
//            long Id = i + 1;
//            executorService.submit(() -> {
//                try {
//                    TransactionStatus status = transactionManager.getTransaction(null);
//                    List<ItemQuantity> itemQuantities = List.of(createItemQuantity(Id, 1));
//                    DecreaseInventoryCommand command = createDecreaseInventoryCommand(itemQuantities);
//                    sut.execute(command);
//                    transactionManager.commit(status);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        //Assert
//        assertThat(inventoryRepository.findById(inventoryId).getQuantity()).isZero();
//    }
//
//    private DecreaseInventoryCommand createDecreaseInventoryCommand(List<ItemQuantity> itemQuantities) {
//        return new DecreaseInventoryCommand(itemQuantities);
//    }
//
//    private ItemQuantity createItemQuantity(Long itemId, Integer count) {
//        return new ItemQuantity(itemId, count);
//    }
//
//}
//
