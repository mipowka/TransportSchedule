package org.example.transportschedule.service.bus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.transportschedule.constant.Constants;
import org.example.transportschedule.exception.BusNotFoundException;
import org.example.transportschedule.mapper.bus.BusMapper;
import org.example.transportschedule.model.dto.BusDTO;
import org.example.transportschedule.model.entity.Bus;
import org.example.transportschedule.repository.bus.BusRepository;
import org.example.transportschedule.service.redis.RedisService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusServiceImpl implements BusService {
    private final BusRepository busRepository;
    private final RedisService redisService;
    private final BusMapper busMapper;

    @Override
    public BusDTO getBusById(long id) {
        // Проверяем наличие данных в Redis
        BusDTO busFromRedis = redisService
                .getFromRedis(Constants.BUS_CACHE_KEY_PREFIX + id, BusDTO.class);

        // Если данные найдены в Redis, возвращаем их
        if (busFromRedis != null) {
            return busFromRedis;
        }

        // Если данных нет в Redis, загружаем их из базы данных
        BusDTO busFromDb = busMapper.mapToBusDTO(busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException(id)));

        // Сохраняем результат в Redis для последующего использования
        redisService.addToRedis(Constants.BUS_CACHE_KEY_PREFIX + id, busFromDb, 30L);

        // Возвращаем объект DTO
        return busFromDb;
    }

    @Transactional
    @Override
    public BusDTO addBus(BusDTO bus) {
        // Сохраняем автобус в базу данных
        log.info(busMapper.mapToBusEntity(bus).toString());
        busRepository.save(busMapper.mapToBusEntity(bus));

        // Очищаем кэш с данными о всех автобусах, так как добавление нового автобуса меняет список
        redisService.clearPageable(Constants.BUSES_CACHE_KEY_PREFIX_WITH_PAGE);

        // Возвращаем DTO добавленного автобуса
        return bus;
    }

    @Transactional
    @Override
    public BusDTO updateBus(long id, BusDTO bus) {
        // Ищем автобус в базе данных по id
        Bus busToUpdate = busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException(id));

        // Обновляем поля автобуса
        busToUpdate.setCityFrom(bus.cityFrom());
        busToUpdate.setCityTo(bus.cityTo());
        busToUpdate.setDateOfDeparture(bus.dateOfDeparture());
        busToUpdate.setDateOfArrival(bus.dateOfArrival());
        busToUpdate.setPrice(bus.price());

        // Сохраняем обновленный автобус в базе данных
        BusDTO savedBus = busMapper.mapToBusDTO(busRepository.save(busToUpdate));

        // Удаляем старый кэш для обновленного автобуса
        redisService.removeFromRedis(Constants.BUS_CACHE_KEY_PREFIX + id);
        // Сохраняем новый кэш для обновленного автобуса
        redisService.addToRedis(Constants.BUS_CACHE_KEY_PREFIX + id, savedBus, 30L);

        // Очищаем кэш с данными о всех автобусах, так как обновление автобуса меняет список
        redisService.clearPageable(Constants.BUSES_CACHE_KEY_PREFIX_WITH_PAGE);

        // Возвращаем DTO обновленного автобуса
        return savedBus;
    }

    @Transactional
    @Override
    public void deleteBus(long id) {
        // Проверяем, существует ли автобус с данным id в базе данных
        if (busRepository.existsById(id)) {
            // Удаляем автобус из базы данных
            busRepository.deleteById(id);

            // Удаляем кэш для удаленного автобуса
            redisService.removeFromRedis(Constants.BUS_CACHE_KEY_PREFIX + id);
            // Очищаем кэш с данными о всех автобусах, так как удаление автобуса меняет список
            redisService.clearPageable(Constants.BUSES_CACHE_KEY_PREFIX_WITH_PAGE);
        } else {
            // Если автобус не найден, выбрасываем исключение
            throw new BusNotFoundException(id);
        }
    }

    @Override
    public Page<BusDTO> getAllBuses(Pageable pageable) {
        String cacheKey = Constants.BUSES_CACHE_KEY_PREFIX_WITH_PAGE + "_" + pageable.getPageNumber()
                + "_" + pageable.getPageSize();

        List<BusDTO> cachedList = redisService.getAll(cacheKey, BusDTO.class);

        if (cachedList != null && !cachedList.isEmpty()) {
            return new PageImpl<>(cachedList, pageable, cachedList.size());
        }

        Page<Bus> busesFromDb = busRepository.findAll(pageable);
        redisService.addToRedis(cacheKey, busesFromDb, 30L);

        return busesFromDb.map(busMapper::mapToBusDTO);
    }

    @Override
    public List<String> getRouteFromCity(String city) {
        // Получаем все маршруты для указанного города отправления
        return busRepository.findAllByCityFrom(city).stream()
                .map(bus -> bus.getCityFrom() + " - " + bus.getCityTo())
                .toList();
    }
}

