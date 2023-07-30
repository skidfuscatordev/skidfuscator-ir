package dev.skidfuscator.test;

import dev.skidfuscator.Main;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ObfTest {

    @Test
    public void testRuntime() {
        new Main().main(new String[]{
                "src/test/resources/test.jar"
        });
    }
}
