package org.example.transportschedule.service.bus;

import org.example.transportschedule.model.dto.BusDTO;

import java.util.List;

public interface BusService {
    BusDTO getBusById(long id);
    BusDTO addBus(BusDTO bus);
    BusDTO updateBus(long id, BusDTO bus);
    void deleteBus(long id);
    List<BusDTO> getAllBuses(int page);
    List<String> getRouteFromCity(String city);
}
