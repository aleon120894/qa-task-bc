# 🧪 Arithmetic Testing with `bc`

This project provides automated unit tests for arithmetic operations using the Unix `bc` command-line calculator. It demonstrates how to validate CLI tools using both Bash scripting and Python automation.

---

## ✅ Overview

The tests cover:

- Addition (`+`)
- Subtraction (`-`)
- Multiplication (`*`)
- Division (`/`)
- Division by zero detection
- Large number arithmetic
- Floating-point result precision

Implemented in:
- `Bash` (pure shell-based logic)
- `Python` (via `unittest` + `subprocess`)

---

## 📁 Files

| File          | Description                              |
|---------------|------------------------------------------|
| `test.sh`     | Bash script that runs arithmetic tests   |
| `test_bc.py`  | Python script using `unittest` framework |

---

## ▶️ How to Run

### 🔹 1. Bash Version

```bash
   chmod +x test.sh
   ./test.sh 
```


### 🔹 2. Python Version

```bash
   python3 test_bc.py
   ```
