package org.example.transportschedule.controller.bus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.transportschedule.model.dto.BusDTO;
import org.example.transportschedule.service.bus.BusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buses")
@Tag(name = "Bus Controller", description = "Управление расписанием автобусов")
public class BusController {
    private final BusService busService;

    @Operation(summary = "Получение автобуса по ID", description = "Возвращает информацию об автобусе по его ID")
    @ApiResponse(responseCode = "200", description = "Автобус найден")
    @ApiResponse(responseCode = "404", description = "Автобус не найден")
    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> getBusById(
            @Parameter(description = "ID автобуса") @PathVariable long id) {
        return ResponseEntity.ok(busService.getBusById(id));
    }

    @Operation(summary = "Получение всех автобусов", description = "Возвращает список всех автобусов с постраничной разбивкой")
    @ApiResponse(responseCode = "200", description = "Список автобусов получен")
    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    @GetMapping
    public ResponseEntity<Page<BusDTO>> getAllBuses(@PageableDefault(sort = "dateOfArrival") Pageable pageable) {
        return ResponseEntity.ok(busService.getAllBuses(pageable));
    }

    @Operation(summary = "Получение маршрутов из указанного города", description = "Возвращает список маршрутов, начинающихся из указанного города")
    @ApiResponse(responseCode = "200", description = "Список маршрутов получен")
    @ApiResponse(responseCode = "400", description = "Некорректный параметр запроса")
    @GetMapping("/route")
    public ResponseEntity<List<String>> getAllBusRoutes(
            @Parameter(description = "Город отправления") @RequestParam String city) {
        return ResponseEntity.ok(busService.getRouteFromCity(city));
    }

    @Operation(summary = "Добавление нового автобуса", description = "Создает новый автобус с указанными параметрами")
    @ApiResponse(responseCode = "200", description = "Автобус успешно добавлен")
    @ApiResponse(responseCode = "400", description = "Некорректные данные автобуса")
    @PostMapping
    public ResponseEntity<BusDTO> addBus(
            @Parameter(description = "Данные нового автобуса") @RequestBody @Valid BusDTO busDTO) {
        return ResponseEntity.ok(busService.addBus(busDTO));
    }

    @Operation(summary = "Удаление автобуса", description = "Удаляет автобус по ID")
    @ApiResponse(responseCode = "204", description = "Автобус успешно удален")
    @ApiResponse(responseCode = "404", description = "Автобус не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBus(
            @Parameter(description = "ID удаляемого автобуса") @PathVariable long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление информации по автобусу", description = "Обновляет информацию по существующему автобусу")
    @ApiResponse(responseCode = "200", description = "Автобус успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Автобус не найден")
    @ApiResponse(responseCode = "400", description = "Некорректные данные автобуса")
    @PutMapping("/{id}")
    public ResponseEntity<BusDTO> updateBus(
            @Parameter(description = "ID обновляемого автобуса") @PathVariable long id,
            @Parameter(description = "Обновленные данные автобуса") @RequestBody @Valid BusDTO busDTO) {
        return ResponseEntity.ok(busService.updateBus(id, busDTO));
    }


}
