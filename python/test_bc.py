import unittest
import subprocess

def run_bc(expression):
    """Run a bc expression and return the output as a float or raise error."""
    try:
        # Add newline if not present
        if not expression.endswith('\n'):
            expression += '\n'

        result = subprocess.run(['bc', '-l'],
                                input=expression,
                                stdout=subprocess.PIPE,
                                stderr=subprocess.PIPE,
                                text=True)
        output = result.stdout.strip()
        if output == "":
            raise RuntimeError(f"Empty output for expression: {expression.strip()}")
        return float(output)
    except ValueError:
        raise RuntimeError(f"Invalid numeric output from bc: '{result.stdout.strip()}'")

class TestBCOperations(unittest.TestCase):

    def test_addition(self):
        self.assertAlmostEqual(run_bc("1 + 2"), 3.0)

    def test_subtraction(self):
        self.assertAlmostEqual(run_bc("5 - 3"), 2.0)

    def test_multiplication(self):
        self.assertAlmostEqual(run_bc("4 * 6"), 24.0)

    def test_division(self):
        self.assertAlmostEqual(run_bc("10 / 4"), 2.5)

    def test_division_by_zero(self):
        with self.assertRaises(RuntimeError):
            run_bc("1 / 0")

    def test_large_numbers(self):
        self.assertAlmostEqual(run_bc("9999999999 * 9999999999"), 99999999980000000001.0)

    def test_invalid_expression(self):
        with self.assertRaises(RuntimeError):
            run_bc("2 + ")

if __name__ == '__main__':
    unittest.main()
