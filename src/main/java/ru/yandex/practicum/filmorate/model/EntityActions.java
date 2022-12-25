package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;


@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityActions {

    int eventId;

    @Positive
    long userId;

    @Positive
    long entityId;

    @Length(max = 10)
    String eventType;

    @Length(max = 10)
    String operation;

    @NonNull()
    long timestamp;
}
