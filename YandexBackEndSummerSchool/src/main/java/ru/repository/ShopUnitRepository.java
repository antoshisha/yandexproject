package ru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.entity.ShopUnit;

@Repository
public interface ShopUnitRepository extends JpaRepository<ShopUnit, Long> {
    ShopUnit findById(String id);
}
