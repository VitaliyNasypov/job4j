package ru.job4j.tracker.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.model.ModelOrder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class OrdersStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @BeforeEach
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/resources/tableForTest.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @AfterEach
    public void dropTable() throws SQLException {
        pool.getConnection().prepareStatement("DROP TABLE ORDERS_STORE").execute();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescriptionTest() {
        OrdersStore store = new OrdersStore(pool);
        ModelOrder modelOrder = new ModelOrder();
        modelOrder.setName("name");
        modelOrder.setDescription("description");
        store.save(modelOrder);
        List<ModelOrder> all = (List<ModelOrder>) store.findAll();
        Assertions.assertTrue((all.size() == 1));
        Assertions.assertEquals("description", all.get(0).getDescription());
        Assertions.assertEquals(1, all.get(0).getId());
    }

    @Test
    public void shouldReturnUpdatedModelOrder() {
        OrdersStore store = new OrdersStore(pool);
        ModelOrder modelOrder = new ModelOrder();
        modelOrder.setName("name");
        modelOrder.setDescription("description");
        store.save(modelOrder);
        modelOrder.setDescription("new description");
        store.update(modelOrder);
        List<ModelOrder> all = (List<ModelOrder>) store.findAll();
        Assertions.assertTrue((all.size() == 1));
        Assertions.assertEquals("new description", all.get(0).getDescription());
        Assertions.assertEquals(1, all.get(0).getId());
    }

    @Test
    public void shouldFindModelOrderByIdInDatabase() {
        OrdersStore store = new OrdersStore(pool);
        ModelOrder modelOrder = new ModelOrder();
        modelOrder.setName("name");
        modelOrder.setDescription("description");
        store.save(modelOrder);
        ModelOrder searchResult = store.findById(1);
        Assertions.assertEquals("description", searchResult.getDescription());
        Assertions.assertEquals(1, searchResult.getId());
    }

    @Test
    public void shouldFindModelOrderByNameInDatabase() {
        OrdersStore store = new OrdersStore(pool);
        ModelOrder modelOrder = new ModelOrder();
        modelOrder.setName("name");
        modelOrder.setDescription("description");
        store.save(modelOrder);
        ModelOrder searchResult = store.findByName("name");
        Assertions.assertEquals("description", searchResult.getDescription());
        Assertions.assertEquals("name", searchResult.getName());
    }
}
