package ru.job4j.tracker.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.model.ModelOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrdersStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersStore.class.getName());
    private final BasicDataSource pool;

    public OrdersStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public ModelOrder save(ModelOrder modelOrder) {
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement(
                     "INSERT INTO ORDERS_STORE(name, description, created) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            pr.setString(1, modelOrder.getName());
            pr.setString(2, modelOrder.getDescription());
            pr.setTimestamp(3, modelOrder.getCreated());
            pr.execute();
            ResultSet id = pr.getGeneratedKeys();
            if (id.next()) {
                modelOrder.setId(id.getInt(1));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return modelOrder;
    }

    public ModelOrder update(ModelOrder modelOrder) {
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement(
                     "UPDATE ORDERS_STORE SET name = ?,description = ?, created = ? WHERE id=?")) {
            pr.setString(1, modelOrder.getName());
            pr.setString(2, modelOrder.getDescription());
            pr.setTimestamp(3, modelOrder.getCreated());
            pr.setInt(4, modelOrder.getId());
            pr.execute();
            ResultSet id = pr.getGeneratedKeys();
            if (id.next()) {
                modelOrder.setId(id.getInt(1));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return modelOrder;
    }

    public Collection<ModelOrder> findAll() {
        List<ModelOrder> rsl = new ArrayList<>();
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement("SELECT * FROM ORDERS_STORE")) {
            try (ResultSet rs = pr.executeQuery()) {
                while (rs.next()) {
                    rsl.add(
                            new ModelOrder(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getString("description"),
                                    rs.getTimestamp(4)
                            )
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rsl;
    }

    public ModelOrder findById(int id) {
        ModelOrder rsl = null;
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement(
                     "SELECT * FROM ORDERS_STORE WHERE id = ?")) {
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                rsl = new ModelOrder(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp(4)
                );
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rsl;
    }

    public ModelOrder findByName(String name) {
        ModelOrder rsl = null;
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement(
                     "SELECT * FROM ORDERS_STORE WHERE name = ?")) {
            pr.setString(1, name);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                rsl = new ModelOrder(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp(4)
                );
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rsl;
    }
}
