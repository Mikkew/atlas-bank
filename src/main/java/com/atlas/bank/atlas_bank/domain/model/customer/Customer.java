package com.atlas.bank.atlas_bank.domain.model.customer;

import com.atlas.bank.atlas_bank.domain.model.shared.Email;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private Email email;

    private CustomerStatus status;

    private LocalDateTime createdAt;

    public boolean isActive() {
        return status == CustomerStatus.ACTIVE;
    }
}
