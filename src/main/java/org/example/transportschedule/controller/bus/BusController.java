package org.example.transportschedule.controller.bus;

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
public class BusController {
    private final BusService busService;

    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> getBusById(@PathVariable long id) {
        return ResponseEntity.ok(busService.getBusById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BusDTO>> getAllBuses(@PageableDefault(sort = "dateOfArrival") Pageable pageable) {
        return ResponseEntity.ok(busService.getAllBuses(pageable));
    }

    @GetMapping("/routes")
    public ResponseEntity<List<String>> getAllBusRoutes(@RequestParam String city) {
        return ResponseEntity.ok(busService.getRouteFromCity(city));
    }

    @PostMapping
    public ResponseEntity<BusDTO> addBus(@Valid @RequestBody BusDTO busDTO) {
        return ResponseEntity.ok(busService.addBus(busDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBus(@PathVariable long id) {
        busService.deleteBus(id);
        return ResponseEntity.ok("Bus deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusDTO> updateBus(@PathVariable long id, @Valid @RequestBody BusDTO busDTO) {
        return ResponseEntity.ok(busService.updateBus(id, busDTO));
    }


}
