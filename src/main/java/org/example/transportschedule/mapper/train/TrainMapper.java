package org.example.transportschedule.mapper.train;

import org.example.transportschedule.model.dto.TrainDTO;
import org.example.transportschedule.model.entity.Train;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainMapper {
    TrainDTO mapToTrainDTO(Train train);

    Train mapToTrainEntity(TrainDTO trainDTO);
}
