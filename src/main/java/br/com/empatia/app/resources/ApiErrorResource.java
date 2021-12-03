package br.com.empatia.app.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResource {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "GMT-3")
    private Instant timestamp;
    private int status;
    private String message;
    private String path;
    private List<String> fields;

    public ApiErrorResource(int status, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
