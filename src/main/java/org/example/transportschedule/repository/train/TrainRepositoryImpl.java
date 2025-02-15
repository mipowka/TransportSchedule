package org.example.transportschedule.repository.train;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.example.transportschedule.model.entity.Train;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Реализация кастомного репозитория для работы с поездами.
 * Использует JPA Criteria API для построения запросов и выполнения их через EntityManager.
 */
@Slf4j
@Repository
public class TrainRepositoryImpl implements TrainRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Поиск поездов, которые отправляются из одного города и прибывают в другой.
     * Используется JPA Criteria API для формирования запроса, который ищет поезда
     * по городам отправления и прибытия, включая промежуточные остановки.
     *
     * @param cityFrom город отправления
     * @param cityTo   город прибытия
     * @return список поездов, соответствующих критериям поиска
     */
    @Override
    public List<Train> findTrainsByCities(String cityFrom, String cityTo) {
        log.info("Поиск поездов из '{}' в '{}'", cityFrom, cityTo);

        // Создаем объект CriteriaBuilder для построения запроса
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Train> cq = cb.createQuery(Train.class);
        Root<Train> trainRoot = cq.from(Train.class);

        // LEFT JOIN для проверки наличия cityFrom в списке остановок
        Join<Train, String> stopFromJoin = trainRoot.join("stopList", JoinType.LEFT);
        // LEFT JOIN для проверки наличия cityTo в списке остановок
        Join<Train, String> stopToJoin = trainRoot.join("stopList", JoinType.LEFT);

        log.debug("Созданы JOIN-ы для поиска остановок");

        // Условие для отправления: либо поле cityFrom, либо один из элементов stopList равен cityFrom
        Predicate departurePredicate = cb.or(
                cb.equal(trainRoot.get("cityFrom"), cityFrom),
                cb.equal(stopFromJoin, cityFrom)
        );

        // Условие для прибытия: либо поле cityTo, либо один из элементов stopList равен cityTo
        Predicate arrivalPredicate = cb.or(
                cb.equal(trainRoot.get("cityTo"), cityTo),
                cb.equal(stopToJoin, cityTo)
        );

        log.debug("Сформированы условия для отправления и прибытия");

        // Формируем запрос с условием по отправлению и прибытии
        cq.select(trainRoot)
                .distinct(true) // Избегаем дублирования записей из-за join'ов
                .where(cb.and(departurePredicate, arrivalPredicate));

        // Выполняем запрос в БД и получаем результаты
        log.info("Выполняем запрос в БД...");
        List<Train> result = entityManager.createQuery(cq).getResultList();
        log.info("Найдено {} поездов", result.size());

        return result;
    }
}