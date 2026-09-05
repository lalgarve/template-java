package io.deployo.template;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

/**
 * Smoke test for the actual entry point -- proves the application boots for real through its
 * own main(), not just through Spring's test context loader. Random port (avoids clashing
 * with anything already listening) and its own isolated in-memory H2 database (avoids
 * interfering with other tests' schema state).
 *
 * Overrides both the datasource URL AND driver-class-name: when CI runs with
 * SPRING_PROFILES_ACTIVE=docker, application-docker.yml sets driver-class-name to
 * org.postgresql.Driver, which otherwise wins over this test's H2 URL and fails with
 * "Driver org.postgresql.Driver claims to not accept jdbcUrl, jdbc:h2:...". Found the hard
 * way in deployo-api-key's own copy of this test, which only overrode the URL.
 *
 * TODO (ao usar este template): renomear esta classe e o pacote io.deployo.template.
 */
class TemplateApplicationTests {

    @Test
    void mainBootsTheApplication() {
        assertThatCode(() -> TemplateApplication.main(new String[] {
                "--server.port=0",
                "--spring.datasource.url=jdbc:h2:mem:deployo_template_java_boot;MODE=PostgreSQL",
                "--spring.datasource.driver-class-name=org.h2.Driver"
        })).doesNotThrowAnyException();
    }
}
