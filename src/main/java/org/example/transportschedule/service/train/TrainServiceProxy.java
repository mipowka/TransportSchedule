package org.example.transportschedule.service.train;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.transportschedule.constant.Constants;
import org.example.transportschedule.model.dto.TrainDTO;
import org.example.transportschedule.service.redis.RedisService;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class TrainServiceProxy implements TrainService {

    /**
     * Делегирование вызовов основному сервису без кеширования.
     * Рекомендуется убрать из основной реализации логику кеширования.
     */
    private final TrainService trainServiceImpl;

    private final RedisService redisService;

    /**
     * Получение поезда по id с использованием кеша.
     * Если поезд не найден в кеше, запрос выполняется к основной базе данных.
     *
     * @param id идентификатор поезда
     * @return TrainDTO объект поезда
     */
    @Override
    public TrainDTO getTrainById(long id) {
        String cacheKey = Constants.TRAIN_CACHE_KEY_PREFIX + id;
        log.info("Запрос поезда с id: {}", id);

        TrainDTO cachedTrain = redisService.getFromRedis(cacheKey, TrainDTO.class);
        if (cachedTrain != null) {
            log.info("Найден в кеше поезд с id: {}", id);
            return cachedTrain;
        }

        log.info("Поезд с id: {} не найден в кеше, обращаемся к БД", id);
        TrainDTO trainDTO = trainServiceImpl.getTrainById(id);
        redisService.addToRedis(cacheKey, trainDTO, 30L);
        return trainDTO;
    }

    /**
     * Получение всех поездов с пагинацией с использованием кеша.
     * Если данные не найдены в кеше, запрос выполняется к основной базе данных.
     *
     * @param pageable параметры для пагинации
     * @return страница с объектами TrainDTO
     */
    @Override
    public Page<TrainDTO> getAllTrains(Pageable pageable) {
        // Формируем ключ на основе номера страницы и размера
        String cacheKey = Constants.TRAINS_CACHE_KEY_PREFIX_WITH_PAGE + "_" + pageable.getPageNumber() + "_" + pageable.getPageSize();
        log.info("Запрос всех поездов, страница: {}", pageable);

        // Получаем закешированный список TrainDTO
        List<TrainDTO> cachedList = redisService.getAll(cacheKey, TrainDTO.class);
        if (cachedList != null && !cachedList.isEmpty()) {
            log.info("Найдены данные в кеше для страницы: {}", pageable);
            // Здесь totalElements можно сохранить отдельно, если требуется
            return new PageImpl<>(cachedList, pageable, cachedList.size());
        }

        log.info("Данных в кеше для страницы: {} нет, обращаемся к БД", pageable);
        Page<TrainDTO> page = trainServiceImpl.getAllTrains(pageable);
        // Сохраняем в кеш только содержимое страницы (list)
        redisService.addToRedis(cacheKey, page.getContent(), 30L);
        return page;
    }

    /**
     * Добавление нового поезда.
     * После добавления можно сбросить или обновить кеши при необходимости.
     *
     * @param trainDTO объект поезда, который нужно добавить
     * @return добавленный объект TrainDTO
     */
    @Transactional
    @Override
    public TrainDTO addTrain(TrainDTO trainDTO) {
        log.info("Добавление нового поезда: {}", trainDTO);
        return trainServiceImpl.addTrain(trainDTO);
    }

    /**
     * Обновление информации о поезде.
     * Сначала происходит обновление поезда, затем сброс кеша (если требуется).
     *
     * @param id идентификатор поезда, который нужно обновить
     * @param trainDTO обновленные данные о поезде
     * @return обновленный объект TrainDTO
     */
    @Transactional
    @Override
    public TrainDTO updateTrain(long id, TrainDTO trainDTO) {
        log.info("Обновление поезда с id: {} данными: {}", id, trainDTO);
        return trainServiceImpl.updateTrain(id, trainDTO);
    }

    /**
     * Удаление поезда по id.
     * Кеш может быть обновлен или удален после операции удаления.
     *
     * @param id идентификатор поезда, который нужно удалить
     */
    @Transactional
    @Override
    public void deleteTrain(long id) {
        log.info("Удаление поезда с id: {}", id);
        trainServiceImpl.deleteTrain(id);
    }

    /**
     * Получение списка маршрутов, исходящих из указанного города.
     *
     * @param city название города отправления
     * @return список маршрутов
     */
    @Override
    public List<String> getRouteFromCity(String city) {
        log.info("Запрос маршрутов из города: {}", city);
        return trainServiceImpl.getRouteFromCity(city);
    }

    /**
     * Поиск поездов между двумя городами.
     * Поезда могут быть найдены как прямые, так и с промежуточными остановками.
     *
     * @param cityFrom город отправления
     * @param cityTo город прибытия
     * @return список поездов, удовлетворяющих условиям
     */
    @Override
    public List<TrainDTO> findTrainsByCities(String cityFrom, String cityTo) {
        log.info("Поиск поездов с городами отправления '{}' и прибытия '{}'", cityFrom, cityTo);
        return trainServiceImpl.findTrainsByCities(cityFrom, cityTo);
    }
}