package org.example.transportschedule.repository.train;

import org.example.transportschedule.model.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Long>, TrainRepositoryCustom {

    List<Train> findAllByCityFrom(String cityFrom);

}
