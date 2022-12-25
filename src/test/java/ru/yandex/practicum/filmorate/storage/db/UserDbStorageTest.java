package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    void userDbStorageTest() {

        // testGet
        User user = userStorage.find(1).get();

        assertNotNull(user);
        assertEquals(user.getEmail(), "a@a.com", "Email does not match");
        assertEquals(user.getLogin(), "user1", "Login does not match");
        assertEquals(user.getName(), "userName1", "Name does not match");
        assertEquals(user.getBirthday(), LocalDate.of(2000, 1, 1),
                "Birthday does not match");

        // testGetAll
        List<User> users = userStorage.getAll();

        assertNotNull(users.get(0));
        assertEquals(users.get(0).getEmail(), "a@a.com", "Email does not match");
        assertEquals(users.get(0).getLogin(), "user1", "Login does not match");
        assertEquals(users.get(0).getName(), "userName1", "Name does not match");
        assertEquals(users.get(0).getBirthday(), LocalDate.of(2000, 1, 1),
                "Birthday does not match");

        assertNotNull(users.get(1));
        assertEquals(users.get(1).getEmail(), "b@b.com", "Email does not match");
        assertEquals(users.get(1).getLogin(), "user2", "Login does not match");
        assertEquals(users.get(1).getName(), "userName2", "Name does not match");
        assertEquals(users.get(1).getBirthday(), LocalDate.of(2000, 1, 2),
                "Birthday does not match");

        IndexOutOfBoundsException indexOutOfBoundsException
                = Assertions.assertThrows(IndexOutOfBoundsException.class, () -> users.get(2),
                "IndexOutOfBoundsException was expected");
        Assertions.assertEquals("Index 2 out of bounds for length 2", indexOutOfBoundsException.getMessage());

        assertEquals(Optional.empty(), userStorage.find(3));


        // testSave
        User userNew = new User();
        userNew.setEmail("c@c.com");
        userNew.setLogin("user3");
        userNew.setName("userName3");
        userNew.setBirthday(LocalDate.of(2000, 1, 3));

        userStorage.save(userNew);
        User userSave = userStorage.find(3).get();

        assertNotNull(userSave);
        assertEquals(userSave.getEmail(), "c@c.com", "Email does not match");
        assertEquals(userSave.getLogin(), "user3", "Login does not match");
        assertEquals(userSave.getName(), "userName3", "Name does not match");
        assertEquals(userSave.getBirthday(), LocalDate.of(2000, 1, 3),
                "Birthday does not match");

        // testUpdate
        User userBefore = userStorage.find(1).get();

        assertNotNull(userBefore);
        assertEquals(userBefore.getEmail(), "a@a.com", "Email does not match");
        assertEquals(userBefore.getLogin(), "user1", "Login does not match");
        assertEquals(userBefore.getName(), "userName1", "Name does not match");
        assertEquals(userBefore.getBirthday(), LocalDate.of(2000, 1, 1),
                "Birthday does not match");

        userBefore.setLogin("userUpdate");
        userBefore.setName("userUpdateName");
        userBefore.setBirthday(LocalDate.of(2000, 1, 3));
        userStorage.update(userBefore);
        User userAfter = userStorage.find(1).get();

        assertNotNull(userAfter);
        assertEquals(userAfter.getLogin(), "userUpdate", "Login does not match");
        assertEquals(userAfter.getName(), "userUpdateName", "Name does not match");
        assertEquals(userAfter.getBirthday(), LocalDate.of(2000, 1, 3),
                "Birthday does not match");

        // testDelete
        userStorage.delete(3);
        assertEquals(Optional.empty(), userStorage.find(3));

    }
}