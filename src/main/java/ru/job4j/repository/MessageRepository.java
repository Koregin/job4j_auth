package ru.job4j.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
