package study.phonemanagement.service.phone.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.entity.phone.Storage;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListPhoneResponse {
    private Long id;

    private String name;

    private Manufacturer manufacturer;

    private Storage storage;

    private Status status;

    private Integer price;

    private Integer quantity;

    private String color;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdDate;
}
