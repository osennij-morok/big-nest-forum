package net.gastipatis.bignestforum.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import net.gastipatis.bignestforum.domain.account.AccountRole;

import java.util.Optional;

@Converter(autoApply = true)
public class AccountRoleConverter implements AttributeConverter<AccountRole, String> {

    @Override
    public String convertToDatabaseColumn(AccountRole attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public AccountRole convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return AccountRole.valueOf(dbData);
    }
}
