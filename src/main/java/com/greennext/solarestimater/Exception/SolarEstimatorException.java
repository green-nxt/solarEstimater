package com.greennext.solarestimater.Exception;

import com.greennext.solarestimater.model.ErrorDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@NoArgsConstructor
public class SolarEstimatorException extends RuntimeException {

    private ErrorDetails errorDetails;

    public SolarEstimatorException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }

    public SolarEstimatorException(int errorCode, String errorMessage, boolean isSuccess) {
        super(errorMessage);
        this.errorDetails = new ErrorDetails(isSuccess, errorCode, errorMessage);
    }

}
