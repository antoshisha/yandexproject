package ru.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.entity.LogEventEntity;
import ru.entity.ShopUnit;
import ru.repository.LogEventRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class LogEventService {
    @Autowired
    LogEventRepository logEventRepository;

    @Transactional
    public void createLogChangePriceEvent(ShopUnit shopUnit, LogEventEntity.LogEventType logEventType) {
        LogEventEntity logEvent = new LogEventEntity();
        logEvent.setShopUnitId(shopUnit.getId());
        logEvent.setName(shopUnit.getName());
        if (shopUnit.getParentId() != null) {
            logEvent.setParentId(shopUnit.getParentId().getId());
        }
        logEvent.setType(shopUnit.getType());
        logEvent.setPrice(shopUnit.getPrice());
        logEvent.setLogEventType(logEventType);
        logEvent.setCreationDate(shopUnit.getUpdateDate());
        logEventRepository.save(logEvent);
    }

    public List<LogEventEntity> getLogEventsUpdatedInThe24Hours(OffsetDateTime dateTime) {
        List<LogEventEntity> logEvents = logEventRepository.findAllByCreationDateBetween(dateTime.minusDays(1), dateTime);
        return logEvents;
    }

    public List<LogEventEntity> getLogEventsForShopUnitBetween(String shopUnitId, OffsetDateTime from, OffsetDateTime to) {
        List<LogEventEntity> events = logEventRepository.findAllByShopUnitIdAndCreationDateBetween(shopUnitId, from, to);
        return events;
    }

    public List<LogEventEntity> getAllLogEventsForNode(String id) {
        return logEventRepository.findAllByShopUnitId(id);
    }

    public void deleteLogEventsByShopUnitId(String shopUnitId) {
        logEventRepository.deleteByShopUnitId(shopUnitId);
    }


}
