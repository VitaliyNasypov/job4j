package ru.job4j.tracker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.model.Item;

import java.sql.Timestamp;
import java.util.List;

public class HbmTrackerTest {

    @Test
    public void shouldAddedNewItem() {
        HbmTracker hbmTracker = new HbmTracker();
        Item item = hbmTracker.add(new Item("name",
                "description", new Timestamp(100L)));
        Item searchResult = hbmTracker.findById(String.valueOf(item.getId()));
        Assertions.assertEquals(item, searchResult);
    }

    @Test
    public void shouldReplaceValueItemInDatabase() {
        HbmTracker hbmTracker = new HbmTracker();
        Item item = hbmTracker.add(new Item("name",
                "description", new Timestamp(100L)));
        item.setName("new name");
        hbmTracker.replace(String.valueOf(item.getId()), item);
        Item searchResult = hbmTracker.findById(String.valueOf(item.getId()));
        Assertions.assertEquals(item, searchResult);
        Assertions.assertEquals("new name", searchResult.getName());
    }

    @Test
    public void shouldDeleteItemInDatabase() {
        HbmTracker hbmTracker = new HbmTracker();
        Item item = hbmTracker.add(new Item("name",
                "description", new Timestamp(100L)));
        hbmTracker.delete(String.valueOf(item.getId()));
        Item searchResult = hbmTracker.findById(String.valueOf(item.getId()));
        List<Item> itemList = hbmTracker.findAll();
        Assertions.assertNull(searchResult);
        Assertions.assertEquals(itemList.size(), 0);
    }

    @Test
    public void shouldFindAllItemsInDatabase() {
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.add(new Item("name",
                "description", new Timestamp(100L)));
        hbmTracker.add(new Item("name",
                "description", new Timestamp(100L)));
        hbmTracker.add(new Item("name",
                "description", new Timestamp(100L)));
        Assertions.assertEquals(hbmTracker.findAll().size(), 3);
    }

    @Test
    public void shouldFindByNameItemsInDatabase() {
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.add(new Item("name",
                "description", new Timestamp(100L)));
        List<Item> itemList = hbmTracker.findByName("name");
        Assertions.assertEquals(itemList.size(), 1);
        Assertions.assertEquals(itemList.get(0).getName(), "name");
    }

    @Test
    public void shouldFindByIdItemInDatabase() {
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.add(new Item("name",
                "description", new Timestamp(100L)));
        Item searchResult = hbmTracker.findById("1");
        Assertions.assertEquals(1, searchResult.getId());
    }
}
