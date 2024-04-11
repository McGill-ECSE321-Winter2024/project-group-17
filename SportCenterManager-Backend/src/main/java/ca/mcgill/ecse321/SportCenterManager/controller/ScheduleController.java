package ca.mcgill.ecse321.SportCenterManager.controller;


import java.sql.Time;

import ca.mcgill.ecse321.SportCenterManager.dto.CourseResponseDto;
import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.SportCenterManager.dto.ScheduleResponseDto;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.service.ScheduleService;

@CrossOrigin(origins = "*")
@RestController
public class ScheduleController {
    @Autowired
    ScheduleService service;

    @Operation( summary = "Update the schedule.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully updated schedule.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"startTime\" : \"09:00:00\", " +
                                                    "\"endTime\" : \"23:00:00\" }")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: null time not allowed.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Cannot have an empty time.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: invalid opening hour.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Cannot have closing hour occur before opening hour.\"}")
                                    }
                            )
                    )

            })
    @PostMapping("/schedule")
    public Schedule updateSchedule(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = ScheduleRequestDto.class),
                            examples = {
                                    @ExampleObject(value = "{" +
                                            "\"startTime\" : \"09:00:00\", " +
                                            "\"endTime\" : \"23:00:00\"}")
                            }
                    )
            )
            @RequestBody ScheduleRequestDto schedule){
        Time opening = schedule.getOpeningHour();
        Time closing = schedule.getClosingHour();
        Schedule modifiedSchedule = service.updateSchedule(opening,closing);
        return modifiedSchedule;
    }
    @GetMapping("/schedule")
    public ScheduleResponseDto getSchedule(){
        Schedule schedule = service.findSchedule();
        return new ScheduleResponseDto(schedule);
    }

    
}