package io.deployo.template;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

/**
 * Structural rules checked on every build. Add the project's own rules here -- e.g. module
 * boundaries (memory/constitution.md, "Fronteira entre módulos e serviços").
 */
class ArchitectureTest {

    private static final String BASE_PACKAGE = "io.deployo.template";

    /**
     * Missing infrastructure is an error, never a skip (memory/constitution.md, "Testes exigem a
     * infraestrutura de pé"), so no test decides at runtime whether to run. Named by string, not
     * by class literal: a class literal would make this test depend on Assumptions itself.
     */
    @Test
    void noTestSkipsItselfWithAssumptions() {
        JavaClasses testClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.ONLY_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);

        noClasses().should().dependOnClassesThat()
                .haveFullyQualifiedName("org.junit.jupiter.api.Assumptions")
                .check(testClasses);
    }
}
