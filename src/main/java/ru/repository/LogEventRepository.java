package ru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.entity.LogEventEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface LogEventRepository extends JpaRepository<LogEventEntity, Long> {
    List<LogEventEntity> findAllByShopUnitIdAndCreationDateBetween(String shopUnitId, Date from, Date to);
    List<LogEventEntity> findAllByShopUnitIdAndCreationDateAfter(String shopUnitId, Date timestamp);
}
