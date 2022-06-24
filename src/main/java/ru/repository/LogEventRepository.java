package ru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.entity.LogEventEntity;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface LogEventRepository extends JpaRepository<LogEventEntity, Long> {
    List<LogEventEntity> findAllByShopUnitIdAndCreationDateBetween(String shopUnitId, OffsetDateTime from, OffsetDateTime to);
    List<LogEventEntity> findAllByCreationDateBetween(OffsetDateTime from, OffsetDateTime to);
    List<LogEventEntity> findAllByCreationDateAfter(OffsetDateTime timestamp);
    List<LogEventEntity> findAllByShopUnitId(String shopUnitId);
    void deleteByShopUnitId(String shopUnitId);
}
