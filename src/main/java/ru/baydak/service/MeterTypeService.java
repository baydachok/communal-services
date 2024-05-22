package ru.baydak.service;

import ru.baydak.dto.MeterTypeRequest;
import ru.baydak.model.entity.MeterType;

import java.util.List;

/**
 * The service interface for meter type functionality.
 */
public interface MeterTypeService {

    List<MeterType> showAvailableMeterTypes();

    MeterType save(MeterTypeRequest meterType);
}
