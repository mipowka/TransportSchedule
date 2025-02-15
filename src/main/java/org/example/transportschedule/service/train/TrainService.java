package org.example.transportschedule.service.train;

import org.example.transportschedule.model.dto.TrainDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrainService {
    TrainDTO getTrainById(long id);

    TrainDTO addTrain(TrainDTO train);

    TrainDTO updateTrain(long id, TrainDTO train);

    void deleteTrain(long id);

    Page<TrainDTO> getAllTrains(Pageable pageable);

    List<String> getRouteFromCity(String city);

    List<TrainDTO> findTrainsByCities(String cityFrom, String cityTo);
}
