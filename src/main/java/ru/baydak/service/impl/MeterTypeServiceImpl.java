package ru.baydak.service.impl;

import ru.baydak.loggingstarter.annotations.LoggableInfo;
import ru.baydak.loggingstarter.annotations.LoggableTime;
import ru.baydak.repository.MeterTypeRepository;
import ru.baydak.dto.MeterTypeRequest;
import ru.baydak.model.entity.MeterType;
import ru.baydak.service.MeterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link MeterTypeService} interface.
 */
@Service
@LoggableInfo
@RequiredArgsConstructor
public class MeterTypeServiceImpl implements MeterTypeService {

    private final MeterTypeRepository meterTypeRepository;

    @Override
    public List<MeterType> showAvailableMeterTypes() {
        return meterTypeRepository.findAll();
    }

    @Override
    @LoggableTime
    public MeterType save(MeterTypeRequest request) {
        MeterType meterType = MeterType.builder().typeName(request.typeName())
                .build();

        return meterTypeRepository.save(meterType);
    }
}
