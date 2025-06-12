package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.EmployeeDTO;
import com.example.Backend_IE303.dto.EmployeeShiftDTO;
import com.example.Backend_IE303.entity.Employee;
import com.example.Backend_IE303.entity.EmployeeShift;
import com.example.Backend_IE303.exceptions.ResourceNotFoundException;
import com.example.Backend_IE303.repository.EmployeeRepository;
import com.example.Backend_IE303.repository.EmployeeShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeShiftService {

    @Autowired
    private EmployeeShiftRepository employeeShiftRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeShiftDTO createShift(EmployeeShiftDTO shiftDTO) {
        Employee employee = employeeRepository.findById(shiftDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        EmployeeShift shift = new EmployeeShift();
        shift.setEmployee(employee);
        shift.setDate(shiftDTO.getDate());
        shift.setShiftType(shiftDTO.getShiftType());

        EmployeeShift savedShift = employeeShiftRepository.save(shift);
        return convertToDTO(savedShift);
    }

    public EmployeeShiftDTO updateShift(Integer id, EmployeeShiftDTO shiftDTO) {
        EmployeeShift shift = employeeShiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found"));

        Employee employee = employeeRepository.findById(shiftDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        shift.setEmployee(employee);
        shift.setDate(shiftDTO.getDate());
        shift.setShiftType(shiftDTO.getShiftType());

        EmployeeShift updatedShift = employeeShiftRepository.save(shift);
        return convertToDTO(updatedShift);
    }

    public void deleteShift(Integer id) {
        if (!employeeShiftRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shift not found");
        }
        employeeShiftRepository.deleteById(id);
    }

    public EmployeeShiftDTO getShift(Integer id) {
        EmployeeShift shift = employeeShiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found"));
        return convertToDTO(shift);
    }

    public List<EmployeeShiftDTO> getShiftsByEmployee(Integer employeeId) {
        return employeeShiftRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeShiftDTO> getWeeklyShifts(LocalDateTime date) {
        LocalDateTime startOfWeek = date.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfWeek = startOfWeek.plusDays(7).withHour(23).withMinute(59).withSecond(59);

        return employeeShiftRepository.findByDateBetween(startOfWeek, endOfWeek)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeShiftDTO> getEmployeeWeeklyShifts(Integer employeeId, LocalDateTime date) {
        LocalDateTime startOfWeek = date.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfWeek = startOfWeek.plusDays(7).withHour(23).withMinute(59).withSecond(59);

        return employeeShiftRepository.findByEmployeeIdAndDateBetween(employeeId, startOfWeek, endOfWeek)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getEmployeesOfDay(LocalDateTime date) {
        List<EmployeeShift> shifts = employeeShiftRepository.findByDate(date);
        List<EmployeeDTO> employees = new ArrayList<>();
        for (EmployeeShift shift : shifts) {
            Employee employee = shift.getEmployee();
            EmployeeDTO employeeDTO = new EmployeeDTO(employee);
            employees.add(employeeDTO);
        }
        return employees;
    }

    private EmployeeShiftDTO convertToDTO(EmployeeShift shift) {
        EmployeeShiftDTO dto = new EmployeeShiftDTO();
        dto.setId(shift.getId());
        dto.setEmployeeId(shift.getEmployee().getId());
        dto.setDate(shift.getDate());
        dto.setShiftType(shift.getShiftType());
        dto.setTime_in(shift.getTime_in());
        dto.setTime_out(shift.getTime_out());
        return dto;
    }

    public EmployeeShiftDTO setTime_in( Integer shiftId, LocalDateTime time_in) {
        EmployeeShift shift = employeeShiftRepository.findById(shiftId).orElseThrow(() -> new ResourceNotFoundException("Shift not found"));
        shift.setTime_in(time_in);
        employeeShiftRepository.save(shift);
        return convertToDTO(shift);
    }

    public EmployeeShiftDTO setTime_out( Integer shiftId, LocalDateTime time_out) {
        EmployeeShift shift = employeeShiftRepository.findById(shiftId).orElseThrow(() -> new ResourceNotFoundException("Shift not found"));
        shift.setTime_out(time_out);
        employeeShiftRepository.save(shift);
        return convertToDTO(shift);
    }
}