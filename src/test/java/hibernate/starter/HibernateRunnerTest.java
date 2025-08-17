package hibernate.starter;

import repository.User;
import org.junit.jupiter.api.Test;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

class HibernateRunnerTest {

    @Test
    public void testHibernateApi() {
        var user = User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.com")
                .age(22)
                .createdAt(LocalDateTime.now())
                .build();

        var sql = """
                insert into
                %s
                (%s)
                values
                (%s)
                """;

        var tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(table -> table.schema() + "." + table.name())
                .orElse(user.getClass().getName());
    }


}