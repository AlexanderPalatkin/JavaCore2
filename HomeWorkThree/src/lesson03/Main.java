package lesson03;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        // 1 задание
        String[] animals = {"dog", "cat", "cow", "horse", "wolf", "monkey", "elephant", "dog", "cow", "pig", "cat", "dog"};
        HashMap<String, Integer> typesOfAnimals = new HashMap<>();
        for (String x : animals) {
            typesOfAnimals.put(x, typesOfAnimals.getOrDefault(x, 0) + 1);
        }
        System.out.println(typesOfAnimals);

        // 2 задание
        Phonebook phonebook = new Phonebook();
        // добавляем контакт в телефонную книгу
        phonebook.addContact("Ivanov", "89213185632");
        phonebook.addContact("Petrov", "89214545634");
        phonebook.addContact("Ivanov", "89213565639");
        phonebook.addContact("Sidorov", "89117113385");

        // поиск контакта
        phonebook.getContact("Ivanov");
    }
}
