package ru.productstar;

import ru.productstar.dao.entity.Contact;
import ru.productstar.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(classes = Main.class)
@Sql("classpath:contact.sql")
public class ContactRepositoryTests {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactRepositoryTests(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    private static final Contact IVAN = new Contact(
            1000L, "Ivan", "Ivanov", "iivanov@gmail.com", "1234567"
    );

    private static final Contact MARIA = new Contact(
            2000L, "Maria", "Ivanova", "mivanova@gmail.com", "7654321"
    );

    /**
     * There are two contacts inserted in the database in contact.sql.
     */
    private static final List<Contact> PERSISTED_CONTACTS = List.of(IVAN, MARIA);

    @Test
    void addContact() {
        var contact = new Contact("Jackie", "Chan", "jchan@gmail.com", "1234567890");
        var contactId = contactRepository.save(contact).getId();
        contact.setId(contactId);
        var contactInDb = contactRepository.findById(contactId)
                .orElseThrow(IllegalArgumentException::new);


        assertThat(contactInDb).isEqualTo(contact);
    }

    @Test
    void getContact() {
        var contact = contactRepository.findById(IVAN.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(contact).isEqualTo(IVAN);
    }

    @Test
    void getAllContacts() {
        var contacts = contactRepository.findAll();

        assertThat(contacts).containsAll(PERSISTED_CONTACTS);
    }

    @Test
    void updatePhoneNumber() {
        var contact = new Contact("Jekyll", "Hide", "jhide@gmail.com", "");
        var contactId = contactRepository.save(contact).getId();

        var newPhone = "777-77-77";
        contactRepository.updatePhone(contactId, newPhone);

        var updatedContact = contactRepository.findById(contactId)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(updatedContact.getPhone()).isEqualTo(newPhone);
    }

    @Test
    void updateEmail() {
        var contact = new Contact("Captain", "America", "", "");
        var contactId = contactRepository.save(contact).getId();

        var newEmail = "cap@gmail.com";
        contactRepository.updateEmail(contactId, newEmail);

        var updatedContact = contactRepository.findById(contactId)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(updatedContact.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void deleteContact() {
        var contact = new Contact("To be", "Deleted", "", "");
        var contactId = contactRepository.save(contact).getId();

        contactRepository.deleteById(contactId);

        var deletedContact = contactRepository.findById(contactId);
        assertThat(deletedContact).isNotPresent();
    }
}
