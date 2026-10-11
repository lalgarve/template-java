package io.deployo.template;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

/**
 * Smoke test for the actual entry point -- proves the application boots for real through its
 * own main(), not just through Spring's test context loader. Random port avoids clashing with
 * anything already listening; the database is the real PostgreSQL of the default {@code docker}
 * profile, which has to be up ({@code docker compose up -d --wait db}).
 *
 * TODO (ao usar este template): renomear esta classe e o pacote io.deployo.template.
 */
class TemplateApplicationTests {

    @Test
    void mainBootsTheApplication() {
        assertThatCode(() -> TemplateApplication.main(new String[] {"--server.port=0"}))
                .doesNotThrowAnyException();
    }
}
