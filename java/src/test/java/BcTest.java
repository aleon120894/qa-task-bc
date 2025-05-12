import java.io.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BcTest {

    static class BcResult {
        String stdout;
        String stderr;

        BcResult(String stdout, String stderr) {
            this.stdout = stdout;
            this.stderr = stderr;
        }
    }

    BcResult runBc(String expression) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("bc", "-l");
        Process process = pb.start();

        try (
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))
        ) {
            writer.write(expression);
            writer.newLine();
            writer.flush();
            writer.close();

            String stdout = stdoutReader.readLine();
            String stderr = stderrReader.readLine();

            process.waitFor();
            return new BcResult(stdout, stderr);
        }
    }

    @Test
    void testValidExpression() throws Exception {
        BcResult result = runBc("4 + 2 * 3");
        assertEquals("10", result.stdout);
        assertNull(result.stderr, "Expected no error, but got: " + result.stderr);
    }

    @Test
    void testSyntaxError() throws Exception {
        BcResult result = runBc(" *924328793497843");
        assertNull(result.stdout);
        assertNotNull(result.stderr);
        assertTrue(result.stderr.contains("syntax error"));
    }

    @Test
    void testDivideByZero() throws Exception {
        BcResult result = runBc("1/0");
        assertNull(result.stdout);
        assertNotNull(result.stderr);
        assertTrue(result.stderr.toLowerCase().contains("divide by zero"));
    }

    @Test
    void testNegativeScaleWarning() throws Exception {
        BcResult result = runBc("scale=-4; -0.4");
        assertEquals("-.4", result.stdout);
        assertNotNull(result.stderr);
        assertTrue(result.stderr.toLowerCase().contains("negative scale"));
    }

    @Test
    void testHexError() throws Exception {
        BcResult result = runBc("0x13");
        assertNull(result.stdout);
        assertNotNull(result.stderr);
        assertTrue(result.stderr.toLowerCase().contains("syntax error"));
    }

    @Test
    void testFloatingPointOutput() throws Exception {
        BcResult result = runBc("scale=2; 5.0/-3");
        assertEquals("-1.66", result.stdout);
        assertNull(result.stderr);
    }
}
