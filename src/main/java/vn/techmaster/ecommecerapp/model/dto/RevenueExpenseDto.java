package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueExpenseDto {
    int month;
    int year;
    long revenue;
    long expense;
}
