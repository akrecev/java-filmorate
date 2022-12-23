package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EntityActions {

    int eventId;

    @Positive(message = "Ошибка при указании индекса пользователя у сущности EntityActions, " +
            "значение должно быть положительным.")
    long userId;

    @Positive(message = "Ошибка при указании индекса сущности субъекта, значение должно быть положительным.")
    long entityId;

    @Length(max = 10,
            message = "Значение длинны типа операции слишком длинное."
    )
    String eventType;

    @Length(max = 10,
            message = "Значение длинны описания операции слишком длинное."
    )
    String operation;

    @NonNull()
    long timestamp;
}
