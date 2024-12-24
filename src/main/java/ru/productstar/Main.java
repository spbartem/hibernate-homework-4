package ru.productstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.productstar.dao.entity.Contact;
import ru.productstar.repository.ContactRepository;

import java.util.List;

@SpringBootApplication
public class Main {

    private static final List<Contact> CONTACTS = List.of(
            new Contact("Ivan", "Ivanov", "iivanov@gmail.com", "1234567"),
            new Contact("Maria", "Ivanova", "mivanova@gmail.com", "7654321")
    );

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner demo(ContactRepository contactRepository) {
        return args -> {
            contactRepository.deleteAllInBatch();
            List<Contact> contacts = contactRepository.saveAll(CONTACTS);
            System.out.println(contacts);
        };
    }
}
