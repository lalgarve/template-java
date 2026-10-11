package io.deployo.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Every table this service owns lives in its own schema, never in {@code public}, with Flyway's
 * history named after the schema -- so several services can share one PostgreSQL instance, even
 * one database, without any name colliding (memory/constitution.md, "Banco de dados: um schema
 * por serviço"). Checked against the real database after Flyway ran, not against configuration:
 * a table created in the wrong place fails here.
 *
 * TODO (ao usar este template): trocar o schema pelo do serviço e acrescentar as tabelas
 * criadas pelas migrations em tablesLiveInTheServiceSchema.
 */
@SpringBootTest
class SchemaLayoutTest {

    private static final String SCHEMA = "deployo_template_java";
    private static final String HISTORY_TABLE = "deployo_template_java_schema_history";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void tablesLiveInTheServiceSchema() {
        assertThat(tablesIn(SCHEMA)).contains(HISTORY_TABLE);
    }

    @Test
    void nothingIsCreatedInThePublicSchema() {
        assertThat(tablesIn("public")).isEmpty();
        assertThat(jdbcTemplate.queryForList(
                "select c.relname from pg_class c join pg_namespace n on n.oid = c.relnamespace"
                        + " where n.nspname = 'public' and c.relkind = 'S'",
                String.class)).isEmpty();
    }

    @Test
    void flywayHistoryUsesTheSchemaNamedTableOnly() {
        assertThat(jdbcTemplate.queryForList(
                "select table_schema || '.' || table_name from information_schema.tables"
                        + " where table_name = 'flyway_schema_history'",
                String.class)).isEmpty();
    }

    private List<String> tablesIn(String schema) {
        return jdbcTemplate.queryForList(
                "select table_name from information_schema.tables where table_schema = ?",
                String.class, schema);
    }
}
