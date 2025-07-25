package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) = LOWER(:name) or LOWER(i.description) = LOWER(:name)) and i.available = true")
    List<Item> getItemsByText(@Param("name") String name);

}
