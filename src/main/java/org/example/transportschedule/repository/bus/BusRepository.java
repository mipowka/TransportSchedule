package org.example.transportschedule.repository.bus;

import org.example.transportschedule.model.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    List<Bus> findAllByCityFrom(String cityFrom);
}
