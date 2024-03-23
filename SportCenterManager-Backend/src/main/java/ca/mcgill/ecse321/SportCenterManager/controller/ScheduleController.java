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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportCenterManager.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.SportCenterManager.model.Schedule;
import ca.mcgill.ecse321.SportCenterManager.service.ScheduleService;

@RestController
public class ScheduleController {
    @Autowired
    ScheduleService service;

    @Operation( summary = "Update the information of a session by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK: Successfully updated billing information.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseResponseDto.class),
                                    examples = {
                                            @ExampleObject(value = "{" +
                                                    "\"startTime\" : \"2024-03-22T03:03:06.561Z\", " +
                                                    "\"endTime\" : \"2024-03-22T03:03:06.561Z\" }")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Not Found: null time not allowed.",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(value = "{\"message\" : \"Cannot have an empty time.\"}")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Not Found: invalid opening hour.",
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
                                            "\"startTime\" : \"2024-03-22T03:03:06.561Z\", " +
                                            "\"endTime\" : \"2024-03-22T03:03:06.561Z\"}")
                            }
                    )
            )
            @RequestBody ScheduleRequestDto schedule){
        Time opening = schedule.getOpeningHour();
        Time closing = schedule.getClosingHour();
        Schedule modifiedschedule = service.updateSchedule(opening,closing);
        return modifiedschedule;
    }

}