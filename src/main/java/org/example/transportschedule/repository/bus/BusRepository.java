package org.example.transportschedule.repository.bus;

import org.example.transportschedule.model.entity.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    Page<Bus> findAll(Pageable pageable);

    List<Bus> findAllByCityFrom(String cityFrom);
}
