package concept.com.example.club.core.checkin.dto;

import concept.com.example.club.core.checkin.enumeration.StatusCheckin;

public record CheckinRequestDTO(
        String user_id,
        StatusCheckin statusCheckin
) {}
