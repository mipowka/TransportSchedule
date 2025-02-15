package org.example.transportschedule.service.train;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.transportschedule.exception.TrainNotFoundException;
import org.example.transportschedule.mapper.train.TrainMapper;
import org.example.transportschedule.model.dto.TrainDTO;
import org.example.transportschedule.model.entity.Train;
import org.example.transportschedule.repository.train.TrainRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {

    private final TrainRepository trainRepository;
    private final TrainMapper trainMapper;

    /**
     * Получение поезда по id.
     * Если поезд с указанным id не найден, генерируется исключение {@link TrainNotFoundException}.
     *
     * @param id идентификатор поезда
     * @return объект TrainDTO
     * @throws TrainNotFoundException если поезд с таким id не найден
     */
    @Override
    public TrainDTO getTrainById(long id) {
        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new TrainNotFoundException(id));
        return trainMapper.mapToTrainDTO(train);
    }

    /**
     * Добавление нового поезда в систему.
     * Сначала объект TrainDTO преобразуется в сущность Train, которая сохраняется в базе данных.
     *
     * @param trainDTO объект поезда, который нужно добавить
     * @return добавленный объект TrainDTO
     */
    @Transactional
    @Override
    public TrainDTO addTrain(TrainDTO trainDTO) {
        Train trainToSave = trainMapper.mapToTrainEntity(trainDTO);
        trainRepository.save(trainToSave);
        return trainDTO;
    }

    /**
     * Обновление информации о поезде с указанным id.
     * Если поезд с таким id не найден, генерируется исключение {@link TrainNotFoundException}.
     *
     * @param id идентификатор поезда, который нужно обновить
     * @param trainDTO новые данные для поезда
     * @return обновленный объект TrainDTO
     * @throws TrainNotFoundException если поезд с таким id не найден
     */
    @Transactional
    @Override
    public TrainDTO updateTrain(long id, TrainDTO trainDTO) {
        Train existingTrain = trainRepository.findById(id)
                .orElseThrow(() -> new TrainNotFoundException(id));

        // Обновляем поля сущности
        existingTrain.setCityFrom(trainDTO.cityFrom());
        existingTrain.setCityTo(trainDTO.cityTo());
        existingTrain.setPrice(trainDTO.price());
        existingTrain.setDateOfDeparture(trainDTO.dateOfDeparture());
        existingTrain.setDateOfArrival(trainDTO.dateOfArrival());
        existingTrain.setStopList(trainDTO.stopList());

        Train updatedTrain = trainRepository.save(existingTrain);
        return trainMapper.mapToTrainDTO(updatedTrain);
    }

    /**
     * Удаление поезда по id.
     * Если поезд с таким id не найден, генерируется исключение {@link TrainNotFoundException}.
     *
     * @param id идентификатор поезда, который нужно удалить
     * @throws TrainNotFoundException если поезд с таким id не найден
     */
    @Transactional
    @Override
    public void deleteTrain(long id) {
        trainRepository.deleteById(id);
    }

    /**
     * Получение всех поездов с постраничной разбивкой.
     *
     * @param pageable параметры пагинации
     * @return страница объектов TrainDTO
     */
    @Override
    public Page<TrainDTO> getAllTrains(Pageable pageable) {
        Page<Train> trainsFromDb = trainRepository.findAll(pageable);
        return trainsFromDb.map(trainMapper::mapToTrainDTO);
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
        return trainRepository.findAllByCityFrom(city)
                .stream()
                .map(train -> train.getCityFrom() + " - " + train.getCityTo())
                .toList();
    }

    /**
     * Поиск поездов, которые отправляются из одного города и прибывают в другой.
     * Поезда могут быть как прямыми, так и с промежуточными остановками.
     *
     * @param cityFrom город отправления
     * @param cityTo город прибытия
     * @return список объектов TrainDTO, которые соответствуют поисковым критериям
     */
    @Override
    public List<TrainDTO> findTrainsByCities(String cityFrom, String cityTo) {
        List<Train> trainsByCities = trainRepository.findTrainsByCities(cityFrom, cityTo);
        return trainsByCities.stream()
                .map(trainMapper::mapToTrainDTO)
                .toList();
    }
}