#!/bin/bash

run_test() {
  expr="$1"
  expected="$2"
  result=$(echo "$expr" | bc -l)

  # Floating-point comparison if result contains a dot
  if [[ "$expected" == *"."* ]]; then
    # Use awk to compare with a small tolerance
    if awk "BEGIN {exit ($result - $expected < 0.0001 && $result - $expected > -0.0001) ? 0 : 1}"; then
      echo "PASS: $expr ≈ $expected"
    else
      echo "FAIL: $expr expected $expected but got $result"
    fi
  else
    # Integer comparison
    if [[ "$result" == "$expected" ]]; then
      echo "PASS: $expr == $expected"
    else
      echo "FAIL: $expr expected $expected but got $result"
    fi
  fi
}

run_test "1 + 2" "3"
run_test "5 - 3" "2"
run_test "4 * 6" "24"
run_test "10 / 4" "2.5"
run_test "9999999999 * 9999999999" "99999999980000000001"

# Division by zero test (bc does not return non-zero exit code, so check stderr)
output=$(echo "1 / 0" | bc -l 2>&1)
if echo "$output" | grep -qi "divide by zero"; then
  echo "PASS: Division by zero triggers warning"
else
  echo "FAIL: Division by zero did not trigger warning"
fi

