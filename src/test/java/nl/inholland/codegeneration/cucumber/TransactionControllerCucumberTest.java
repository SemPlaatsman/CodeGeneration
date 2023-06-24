// Path: src\test\java\nl\inholland\codegeneration\cucumber\TransactionControllerCucumberTest.java

package nl.inholland.codegeneration.cucumber;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"nl.inholland.codegeneration.stepdefinitions/TransactionControllerSteps.java"},
    plugin = {"pretty", "html:target/cucumber-reports"}
)

public class TransactionControllerCucumberTest { }
