package in.ashokit.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse{

    private int stausCode;
    private Instant timeStamp;
    private String errorMessage;
    private String requestURI;
}
