package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;

import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT i FROM Item i WHERE i.request.id = :requestId")
    Set<Item> findRequest(@Param("requestId") Long requestId);

}
