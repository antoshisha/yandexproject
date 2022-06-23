package ru.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.entity.LogEventEntity;
import ru.repository.LogEventRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class LogEventService {
    @Autowired
    LogEventRepository logEventRepository;

    @Transactional
    public void createLogChangePriceEvent(String shopUnitId, LogEventEntity.LogEventType logEventType, Integer price) {
        LogEventEntity logEvents = new LogEventEntity();
        logEvents.setShopUnitId(shopUnitId);
        logEvents.setLogEventType(logEventType);
        logEvents.setUpdatedField(price.toString());
        logEventRepository.save(logEvents);
    }

    @Transactional
    public List<LogEventEntity> getLogEventsForShopUnitInThe24Hours(String shopUnitId) {
        LocalDate date = LocalDate.now().minusDays(1);
        List<LogEventEntity> logEvents = logEventRepository.findAllByShopUnitIdAndCreationDateAfter(shopUnitId, java.sql.Date.valueOf(date));
        return logEvents;
    }

    @Transactional
    public List<LogEventEntity> getLogEventsForShopUnitBetween(String shopUnitId, Date from, Date to) {
        List<LogEventEntity> events = logEventRepository.findAllByShopUnitIdAndCreationDateBetween(shopUnitId, from, to);
        return events;
    }
}
