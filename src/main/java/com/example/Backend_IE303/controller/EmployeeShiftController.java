package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.EmployeeShiftDTO;
import com.example.Backend_IE303.service.EmployeeShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
public class EmployeeShiftController {

    @Autowired
    private EmployeeShiftService employeeShiftService;

    @PostMapping
    public ResponseEntity<EmployeeShiftDTO> createShift(@RequestBody EmployeeShiftDTO shiftDTO) {
        EmployeeShiftDTO createdShift = employeeShiftService.createShift(shiftDTO);
        return new ResponseEntity<>(createdShift, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeShiftDTO> updateShift(@PathVariable Integer id,
            @RequestBody EmployeeShiftDTO shiftDTO) {
        EmployeeShiftDTO updatedShift = employeeShiftService.updateShift(id, shiftDTO);
        return ResponseEntity.ok(updatedShift);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Integer id) {
        employeeShiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeShiftDTO> getShift(@PathVariable Integer id) {
        EmployeeShiftDTO shift = employeeShiftService.getShift(id);
        return ResponseEntity.ok(shift);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeShiftDTO>> getShiftsByEmployee(@PathVariable Integer employeeId) {
        List<EmployeeShiftDTO> shifts = employeeShiftService.getShiftsByEmployee(employeeId);
        return ResponseEntity.ok(shifts);
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<EmployeeShiftDTO>> getWeeklyShifts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<EmployeeShiftDTO> shifts = employeeShiftService.getWeeklyShifts(date);
        return ResponseEntity.ok(shifts);
    }

    @GetMapping("/employee/{employeeId}/weekly")
    public ResponseEntity<List<EmployeeShiftDTO>> getEmployeeWeeklyShifts(
            @PathVariable Integer employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<EmployeeShiftDTO> shifts = employeeShiftService.getEmployeeWeeklyShifts(employeeId, date);
        return ResponseEntity.ok(shifts);
    }
}