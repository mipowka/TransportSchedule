package org.example.transportschedule.controller.train;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.transportschedule.model.dto.TrainDTO;
import org.example.transportschedule.service.train.TrainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
@Tag(name = "Train Controller", description = "Управление расписанием поездов")
public class TrainController {

    private final TrainService trainService;

    @Operation(summary = "Получение поезда по ID", description = "Возвращает информацию о поезде по его ID")
    @ApiResponse(responseCode = "200", description = "Поезд найден")
    @ApiResponse(responseCode = "404", description = "Поезд не найден")
    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrainById(
            @Parameter(description = "ID поезда") @PathVariable long id) {
        TrainDTO trainDTO = trainService.getTrainById(id);
        return ResponseEntity.ok(trainDTO);
    }

    @Operation(summary = "Добавление нового поезда", description = "Создает новый поезд с указанными параметрами")
    @ApiResponse(responseCode = "200", description = "Поезд успешно добавлен")
    @ApiResponse(responseCode = "400", description = "Некорректные данные поезда")
    @PostMapping
    public ResponseEntity<TrainDTO> addTrain(
            @Parameter(description = "Данные нового поезда") @RequestBody @Valid TrainDTO trainDTO) {
        TrainDTO createdTrain = trainService.addTrain(trainDTO);
        return ResponseEntity.ok(createdTrain);
    }

    @Operation(summary = "Обновление информации по поезду", description = "Обновляет информацию по существующему поезду")
    @ApiResponse(responseCode = "200", description = "Поезд успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Поезд не найден")
    @ApiResponse(responseCode = "400", description = "Некорректные данные поезда")
    @PutMapping("/{id}")
    public ResponseEntity<TrainDTO> updateTrain(
            @Parameter(description = "ID обновляемого поезда") @PathVariable long id,
            @Parameter(description = "Обновленные данные поезда") @RequestBody @Valid TrainDTO trainDTO) {
        TrainDTO updatedTrain = trainService.updateTrain(id, trainDTO);
        return ResponseEntity.ok(updatedTrain);
    }

    @Operation(summary = "Удаление поезда", description = "Удаляет поезд по ID")
    @ApiResponse(responseCode = "204", description = "Поезд успешно удален")
    @ApiResponse(responseCode = "404", description = "Поезд не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(
            @Parameter(description = "ID удаляемого поезда") @PathVariable long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получение всех поездов", description = "Возвращает список всех поездов с постраничной разбивкой")
    @ApiResponse(responseCode = "200", description = "Список поездов получен")
    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    @GetMapping
    public ResponseEntity<Page<TrainDTO>> getAllTrains(
            @PageableDefault(sort = "dateOfArrival") Pageable pageable) {
        Page<TrainDTO> trainsPage = trainService.getAllTrains(pageable);
        return ResponseEntity.ok(trainsPage);
    }

    @Operation(summary = "Получение маршрутов из указанного города", description = "Возвращает список маршрутов, начинающихся из указанного города")
    @ApiResponse(responseCode = "200", description = "Список маршрутов получен")
    @ApiResponse(responseCode = "400", description = "Некорректный параметр запроса")
    @GetMapping("/route")
    public ResponseEntity<List<String>> getRouteFromCity(
            @Parameter(description = "Город отправления") @RequestParam("city") String city) {
        List<String> routes = trainService.getRouteFromCity(city);
        return ResponseEntity.ok(routes);
    }

    @Operation(summary = "Поиск поездов по городам отправления и прибытия", description = "Возвращает список поездов между двумя городами, включая промежуточные остановки")
    @ApiResponse(responseCode = "200", description = "Список поездов найден")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    @ApiResponse(responseCode = "404", description = "Поезда не найдены")
    @GetMapping("/search")
    public ResponseEntity<List<TrainDTO>> findTrainsByCities(
            @Parameter(description = "Город отправления") @RequestParam("cityFrom") String cityFrom,
            @Parameter(description = "Город прибытия") @RequestParam("cityTo") String cityTo) {
        List<TrainDTO> trains = trainService.findTrainsByCities(cityFrom, cityTo);
        return ResponseEntity.ok(trains);
    }
}