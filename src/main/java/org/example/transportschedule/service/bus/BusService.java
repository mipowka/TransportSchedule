package org.example.transportschedule.service.bus;

import org.example.transportschedule.model.dto.BusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BusService {
    BusDTO getBusById(long id);
    BusDTO addBus(BusDTO bus);
    BusDTO updateBus(long id, BusDTO bus);
    void deleteBus(long id);
    Page<BusDTO> getAllBuses(Pageable pageable);
    List<String> getRouteFromCity(String city);
}
