package lesson03;

import java.util.HashMap;
import java.util.HashSet;

public class Phonebook {
    HashMap<String, HashSet<String>> phoneBook;

    public Phonebook() {
        this.phoneBook = new HashMap<>();
    }

    // метод добавления контакта в тлф.книгу
    public void addContact(String name, String phoneNumber) {
        HashSet<String> newContact = phoneBook.getOrDefault(name, new HashSet<>());
        newContact.add(phoneNumber);
        phoneBook.put(name, newContact);
    }

    // метод поиска контакта в тлф.книге
    public void getContact(String name) {
        System.out.println(name + " " + phoneBook.getOrDefault(name, new HashSet<>()));
    }
}
