package org.example.transportschedule.mapper.bus;

import org.example.transportschedule.model.dto.BusDTO;
import org.example.transportschedule.model.entity.Bus;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BusMapper {
    BusDTO mapToBusDTO(Bus bus);

    Bus mapToBusEntity(BusDTO busDTO);
}
