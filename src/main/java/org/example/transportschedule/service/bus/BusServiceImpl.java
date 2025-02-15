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

    /**
     * Получение автобуса по id с использованием кеша.
     * Если автобус не найден в кеше, запрос выполняется к основной базе данных.
     *
     * @param id идентификатор автобуса
     * @return объект BusDTO
     * @throws BusNotFoundException если автобус с таким id не найден
     */
    @Override
    public BusDTO getBusById(long id) {
        log.info("Запрос автобуса с id: {}", id);

        // Проверяем наличие данных в Redis
        BusDTO busFromRedis = redisService
                .getFromRedis(Constants.BUS_CACHE_KEY_PREFIX + id, BusDTO.class);

        // Если данные найдены в Redis, возвращаем их
        if (busFromRedis != null) {
            log.info("Автобус с id: {} найден в кеше", id);
            return busFromRedis;
        }

        log.info("Автобус с id: {} не найден в кеше, обращаемся к БД", id);

        // Если данных нет в Redis, загружаем их из базы данных
        BusDTO busFromDb = busMapper.mapToBusDTO(busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException(id)));

        // Сохраняем результат в Redis для последующего использования
        redisService.addToRedis(Constants.BUS_CACHE_KEY_PREFIX + id, busFromDb, 30L);

        // Возвращаем объект DTO
        return busFromDb;
    }

    /**
     * Добавление нового автобуса в систему.
     * После добавления очищается кеш с данными о всех автобусах, так как список изменился.
     *
     * @param bus объект автобуса, который нужно добавить
     * @return добавленный объект BusDTO
     */
    @Transactional
    @Override
    public BusDTO addBus(BusDTO bus) {
        log.info("Добавление нового автобуса: {}", bus);

        // Сохраняем автобус в базу данных
        busRepository.save(busMapper.mapToBusEntity(bus));

        // Очищаем кэш с данными о всех автобусах, так как добавление нового автобуса меняет список
        redisService.clearPageable(Constants.BUSES_CACHE_KEY_PREFIX_WITH_PAGE);

        // Возвращаем DTO добавленного автобуса
        return bus;
    }

    /**
     * Обновление информации об автобусе с указанным id.
     * После обновления очищается кеш для данного автобуса и кеш с данными о всех автобусах.
     *
     * @param id идентификатор автобуса, который нужно обновить
     * @param bus новые данные для автобуса
     * @return обновленный объект BusDTO
     * @throws BusNotFoundException если автобус с таким id не найден
     */
    @Transactional
    @Override
    public BusDTO updateBus(long id, BusDTO bus) {
        log.info("Обновление автобуса с id: {} данными: {}", id, bus);

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

    /**
     * Удаление автобуса по id.
     * После удаления очищается кеш для данного автобуса и кеш с данными о всех автобусах.
     *
     * @param id идентификатор автобуса, который нужно удалить
     * @throws BusNotFoundException если автобус с таким id не найден
     */
    @Transactional
    @Override
    public void deleteBus(long id) {
        log.info("Удаление автобуса с id: {}", id);

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

    /**
     * Получение всех автобусов с постраничной разбивкой.
     * Если данные найдены в кеше, они возвращаются из него, иначе запрос выполняется к базе данных.
     *
     * @param pageable параметры пагинации
     * @return страница объектов BusDTO
     */
    @Override
    public Page<BusDTO> getAllBuses(Pageable pageable) {
        log.info("Запрос всех автобусов, страница: {}", pageable);

        // Формируем ключ для кеша на основе номера страницы и размера
        String cacheKey = Constants.BUSES_CACHE_KEY_PREFIX_WITH_PAGE + "_" + pageable.getPageNumber()
                + "_" + pageable.getPageSize();

        // Получаем закешированный список BusDTO
        List<BusDTO> cachedList = redisService.getAll(cacheKey, BusDTO.class);

        // Если данные найдены в кеше, возвращаем их
        if (cachedList != null && !cachedList.isEmpty()) {
            log.info("Найдены данные в кеше для страницы: {}", pageable);
            return new PageImpl<>(cachedList, pageable, cachedList.size());
        }

        log.info("Данных в кеше для страницы: {} нет, обращаемся к БД", pageable);

        // Если данных нет в кеше, загружаем их из базы данных
        Page<Bus> busesFromDb = busRepository.findAll(pageable);

        // Сохраняем результат в Redis для последующего использования
        redisService.addToRedis(cacheKey, busesFromDb, 30L);

        // Возвращаем страницу с объектами BusDTO
        return busesFromDb.map(busMapper::mapToBusDTO);
    }

    /**
     * Получение всех маршрутов из указанного города.
     * Формирует список маршрутов в формате "город отправления - город назначения".
     *
     * @param city город отправления
     * @return список маршрутов в виде строк
     */
    @Override
    public List<String> getRouteFromCity(String city) {
        log.info("Запрос маршрутов автобусов из города: {}", city);

        // Получаем все маршруты для указанного города отправления
        return busRepository.findAllByCityFrom(city).stream()
                .map(bus -> bus.getCityFrom() + " - " + bus.getCityTo())
                .toList();
    }
}