package org.example.transportschedule.repository.train;

import org.example.transportschedule.model.entity.Train;

import java.util.List;

public interface TrainRepositoryCustom {

    List<Train> findTrainsByCities(String cityFrom, String cityTo);
}
