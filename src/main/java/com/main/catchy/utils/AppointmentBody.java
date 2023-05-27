package com.main.catchy.utils;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentBody {
    private long appointmentId;
    private Long mentorId;
    private Long menteeId;
    private String Status;
    private LocalDateTime creationDate;
    private String scheduledtime;
    private LocalDateTime scheduledDate;
}
