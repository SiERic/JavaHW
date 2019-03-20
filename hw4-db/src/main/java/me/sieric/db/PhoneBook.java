package me.sieric.db;

import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import xyz.morphia.Datastore;
import xyz.morphia.Morphia;
import xyz.morphia.query.Query;

import java.util.List;
import java.util.Objects;

/** Phone book database using MongoDB and Morphia*/
public class PhoneBook {

    private final Datastore datastore;

    public PhoneBook(String name) {
        Morphia morphia = new Morphia();
        morphia.mapPackage("me.sieric.db");
        datastore = morphia.createDatastore(new MongoClient(), name);
    }

    /** Class to store records */
    @Entity
    static public class PhoneBookRecord {

        @Id
        private ObjectId id;
        private String name;
        private String number;

        public PhoneBookRecord() {}

        public PhoneBookRecord(String name, String number) {
            this.name = name;
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PhoneBookRecord that = (PhoneBookRecord) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(number, that.number);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, number);
        }

        public String getName() {
            return name;
        }

        public String getNumber() {
            return number;
        }
    }

    private Query<PhoneBookRecord> equalsQuery(PhoneBookRecord record) {
        return datastore.createQuery(PhoneBookRecord.class).field("name").equal(record.name).field("number").equal(record.number);
    }

    /**
     * Checks if phone book contains a given record
     * @param record - a record to check
     * @return true if contains, false otherwise
     */
    public boolean contains(PhoneBookRecord record) {
        return equalsQuery(record).get() != null;
    }

    /**
     * Adds a record to phone book
     * @param record - a record to add
     */
    public boolean add(PhoneBookRecord record) {
        if (contains(record)) {
            return false;
        }
        datastore.save(record);
        return true;
    }

    /**
     * Deletes a record from phone book
     * @param record - a record to delete
     * @return true if record was in phone book, false otherwise
     */
    public boolean delete(PhoneBookRecord record) {
        if (!contains(record)) {
            return false;
        } else {
            datastore.findAndDelete(equalsQuery(record));
            return true;
        }
    }

    /**
     * Changes a name in a given record
     * @param record - a record to change
     * @param newName - a new name for a record
     * @return true if record was in phone book, false otherwise
     */
    public boolean changeName(PhoneBookRecord record, String newName) {
        if (!contains(record)) {
            return false;
        } else {
            datastore.findAndModify(equalsQuery(record), datastore.createUpdateOperations(PhoneBookRecord.class).set("name", newName));
            return true;
        }
    }

    /**
     * Changes a number in a given record
     * @param record - a record to change
     * @param newNumber - a new number for a record
     * @return true if record was in phone book, false otherwise
     */
    public boolean changeNumber(PhoneBookRecord record, String newNumber) {
        if (!contains(record)) {
            return false;
        } else {
            datastore.findAndModify(equalsQuery(record), datastore.createUpdateOperations(PhoneBookRecord.class).set("number", newNumber));
            return true;
        }
    }

    /**
     * Returns a list of all records in a phone book
     * @return a list of all records in a phone book
     */
    public List<PhoneBookRecord> getAll() {
        return datastore.find(PhoneBookRecord.class).asList();
    }

    /** Returns a list of records in a phone book with given name
     * @param name - a name to filter
     * @return a list of records in a phone book with given name
     */
    public List<PhoneBookRecord> getByName(String name) {
        return datastore.find(PhoneBookRecord.class).field("name").equal(name).asList();
    }

    /**
     * Returns a list of records in a phone book with given number
     * @param number - a number to filter
     * @return a list of records in a phone book with given name
     */
    public List<PhoneBookRecord> getByNumber(String number) {
        return datastore.find(PhoneBookRecord.class).field("number").equal(number).asList();
    }

    /** Clears database */
    public void clear() {
        datastore.getDB().dropDatabase();
    }
}