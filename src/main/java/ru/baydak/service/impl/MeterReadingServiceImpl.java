package ru.baydak.service.impl;

import ru.baydak.exception.InvalidCredentialsException;
import ru.baydak.exception.UserNotFoundException;
import ru.baydak.loggingstarter.annotations.LoggableInfo;
import ru.baydak.loggingstarter.annotations.LoggableTime;
import ru.baydak.model.entity.User;
import ru.baydak.repository.MeterReadingRepository;
import ru.baydak.exception.NotValidArgumentException;
import ru.baydak.model.entity.MeterReading;
import ru.baydak.model.entity.MeterType;
import ru.baydak.service.MeterReadingService;
import ru.baydak.service.MeterTypeService;
import ru.baydak.service.UserService;
import ru.baydak.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@LoggableInfo
@LoggableTime
@RequiredArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final UserService userService;
    private final MeterTypeService meterTypeService;

    @Override
    public List<MeterReading> getCurrentMeterReadings(String login) {
        List<MeterReading> meterReadings = meterReadingRepository.findAllByUserId(getIdByLogin(login));

        Map<Long, List<MeterReading>> readingsByType = meterReadings.stream()
                .collect(Collectors.groupingBy(MeterReading::getTypeId));

        List<MeterReading> lastReadings = new ArrayList<>();

        for (List<MeterReading> readings : readingsByType.values()) {
            if (!readings.isEmpty()) {
                MeterReading lastReading = readings.get(readings.size() - 1);
                lastReadings.add(lastReading);
            }
        }

        return lastReadings;
    }

    @Override
    public MeterReading submitMeterReading(Integer counterNumber, Long meterTypeId, String login) {

        if (counterNumber == null || login == null || meterTypeId == null) {
            throw new NotValidArgumentException("Пожалуйста, заполните все пустые поля.");
        }

        List<MeterType> allTypes = meterTypeService.showAvailableMeterTypes();
        if (meterTypeId > allTypes.size() || meterTypeId <= 0) {
            throw new NotValidArgumentException("Пожалуйста, введите корректный тип показаний.");
        }

        Long id = getIdByLogin(login);
        LocalDate now = LocalDate.now();
        List<MeterReading> existingReadings = meterReadingRepository.findAllByUserId(id);

        boolean alreadyExists = existingReadings.stream()
                .anyMatch(reading -> reading.getTypeId().equals(meterTypeId) &&
                                     DateTimeUtils.isSameMonth(DateTimeUtils.parseDateFromString(reading.getReadingDate()), now));

        if (alreadyExists) {
            throw new InvalidCredentialsException("Запись для данного типа счетчика уже существует в текущем месяце.");
        }

        MeterReading meterReading = MeterReading.builder()
                .counterNumber(counterNumber)
                .typeId(meterTypeId)
                .readingDate(DateTimeUtils.parseDate(LocalDate.now()))
                .userId(id)
                .build();

        return meterReadingRepository.save(meterReading);
    }

    @Override
    public List<MeterReading> getMeterReadingsByMonthAndYear(Integer year, Integer month, String login) {
        List<MeterReading> allReadings = meterReadingRepository.findAllByUserId(getIdByLogin(login));
        List<MeterReading> currentReadings = new ArrayList<>();
        YearMonth filterFromUser = YearMonth.of(year, month);

        for (MeterReading meterReading : allReadings) {
            LocalDate readingDate = DateTimeUtils.parseDateFromString(meterReading.getReadingDate());
            YearMonth readingYearMonth = YearMonth.of(readingDate.getYear(), readingDate.getMonth());

            if (readingYearMonth.equals(filterFromUser)) {
                currentReadings.add(meterReading);
            }
        }

        return currentReadings;
    }

    @Override
    public List<MeterReading> getMeterReadingHistory(String login) {
        return meterReadingRepository.findAllByUserId(getIdByLogin(login));
    }

    private Long getIdByLogin(String login) {
        Optional<User> userOptional = userService.getUserByLogin(login);
        if (userOptional.isPresent()) {
            return userOptional.get().getId();
        } else {
            throw new UserNotFoundException("User not found for login: " + login);
        }
    }
}
