package me.sieric.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PhoneBookTest {
    private PhoneBook phoneBook = new PhoneBook("testPhoneBook");

    @BeforeEach
    void setUp() {
        phoneBook.clear();
        phoneBook.add(new PhoneBook.PhoneBookRecord("kek", "lol"));
        phoneBook.add(new PhoneBook.PhoneBookRecord("qqq", "123"));
        phoneBook.add(new PhoneBook.PhoneBookRecord("qqq", "178"));
    }

    @Test
    void testContains() {
        phoneBook.add(new PhoneBook.PhoneBookRecord("kek", "lol"));
        assertTrue(phoneBook.contains(new PhoneBook.PhoneBookRecord("kek", "lol")));
        assertFalse(phoneBook.contains(new PhoneBook.PhoneBookRecord("kek", "heh")));
        assertFalse(phoneBook.contains(new PhoneBook.PhoneBookRecord("heh", "lol")));
    }

    @Test
    void testDelete() {
        assertTrue(phoneBook.delete(new PhoneBook.PhoneBookRecord("qqq", "123")));
        assertFalse(phoneBook.contains(new PhoneBook.PhoneBookRecord("qqq", "123")));
        assertTrue(phoneBook.contains(new PhoneBook.PhoneBookRecord("qqq", "178")));
        assertFalse(phoneBook.delete(new PhoneBook.PhoneBookRecord("kek", "heh")));
        phoneBook.add(new PhoneBook.PhoneBookRecord("qqq", "178"));
        assertTrue(phoneBook.delete(new PhoneBook.PhoneBookRecord("qqq", "178")));
        assertFalse(phoneBook.contains(new PhoneBook.PhoneBookRecord("qqq", "178")));
    }

    @Test
    void testChangeName() {
        assertFalse(phoneBook.changeName(new PhoneBook.PhoneBookRecord("kek", "heh"), "lol"));
        assertTrue(phoneBook.changeName(new PhoneBook.PhoneBookRecord("kek", "lol"), "heh"));
        assertTrue(phoneBook.contains(new PhoneBook.PhoneBookRecord("heh", "lol")));
        assertFalse(phoneBook.contains(new PhoneBook.PhoneBookRecord("kek", "lol")));
    }

    @Test
    void testChangeNumber() {
        assertFalse(phoneBook.changeNumber(new PhoneBook.PhoneBookRecord("kek", "heh"), "lol"));
        assertTrue(phoneBook.changeNumber(new PhoneBook.PhoneBookRecord("kek", "lol"), "heh"));
        assertTrue(phoneBook.contains(new PhoneBook.PhoneBookRecord("kek", "heh")));
        assertFalse(phoneBook.contains(new PhoneBook.PhoneBookRecord("kek", "lol")));
    }

    @Test
    void testGetAll() {
        HashSet<PhoneBook.PhoneBookRecord> set = new HashSet<>();
        set.add(new PhoneBook.PhoneBookRecord("kek", "lol"));
        set.add(new PhoneBook.PhoneBookRecord("qqq", "123"));
        set.add(new PhoneBook.PhoneBookRecord("qqq", "178"));

        HashSet<PhoneBook.PhoneBookRecord> set2 = new HashSet<>(phoneBook.getAll());
        assertEquals(set2, set);
    }

    @Test
    void testGetByName() {
        HashSet<PhoneBook.PhoneBookRecord> set = new HashSet<>();
        set.add(new PhoneBook.PhoneBookRecord("qqq", "123"));
        set.add(new PhoneBook.PhoneBookRecord("qqq", "178"));

        HashSet<PhoneBook.PhoneBookRecord> set2 = new HashSet<>(phoneBook.getByName("qqq"));
        assertEquals(set2, set);
    }

    @Test
    void testGetByNumber() {
        phoneBook.add(new PhoneBook.PhoneBookRecord("heh", "lol"));
        HashSet<PhoneBook.PhoneBookRecord> set = new HashSet<>();
        set.add(new PhoneBook.PhoneBookRecord("kek", "lol"));
        set.add(new PhoneBook.PhoneBookRecord("heh", "lol"));

        HashSet<PhoneBook.PhoneBookRecord> set2 = new HashSet<>(phoneBook.getByNumber("lol"));
        assertEquals(set2, set);
    }

}