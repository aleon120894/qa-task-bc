import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BcTest {

    String runBc(String expression) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("bc", "-l");  // Add the -l option here
        pb.redirectErrorStream(true); // Merge stderr into stdout
        Process process = pb.start();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            writer.write(expression);
            writer.newLine();
            writer.flush();
            process.getOutputStream().close();

            String output = reader.readLine();
            int exitCode = process.waitFor();

            if (exitCode != 0 || output == null || output.contains("error") || output.isEmpty()) {
                throw new RuntimeException("bc failed or returned error output");
            }

            return output;
        }
    }

    @Test
    void testAddition() throws IOException, InterruptedException {
        assertEquals(3.0, Double.parseDouble(runBc("1 + 2")), 0.0001);
    }

    @Test
    void testSubtraction() throws IOException, InterruptedException {
        assertEquals(2.0, Double.parseDouble(runBc("5 - 3")), 0.0001);
    }

    @Test
    void testMultiplication() throws IOException, InterruptedException {
        assertEquals(24.0, Double.parseDouble(runBc("4 * 6")), 0.0001);
    }

    @Test
    void testDivision() throws IOException, InterruptedException {
        assertEquals(2.5, Double.parseDouble(runBc("10 / 4")), 0.0001);
    }

    @Test
    void testLargeNumbers() throws IOException, InterruptedException {
        assertEquals(99999999980000000001.0, Double.parseDouble(runBc("9999999999 * 9999999999")), 1.0);
    }

    @Test
    void testDivisionByZero() {
        assertThrows(RuntimeException.class, () -> {
            try {
                runBc("1 / 0");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
