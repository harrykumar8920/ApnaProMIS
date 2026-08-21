package com.pams.dto;



import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DynamicReportResponseDTO {

    /**
     * Key   → Column name
     * Value → Column value
     */
    private Map<String, Object> row;
}
