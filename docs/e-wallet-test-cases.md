# E-Wallet Test Case Design

## 1. User Registration Flow

| Test Case ID | Description | Steps | Expected Result | Edge Case / Notes |
|---|---|---|---|---|
| REG-001 | Register with valid email and phone | 1. Open app 2. Choose registration 3. Enter valid email, phone, password, verification code 4. Submit | Registration succeeds and user lands on wallet dashboard | Normal flow |
| REG-002 | Register with already used email | 1. Enter email already registered 2. Complete other fields 3. Submit | App shows "Email already in use" error | Duplicate account validation |
| REG-003 | Register with invalid email format | 1. Enter `user@domain` 2. Submit | Validation message for invalid email | Input validation |
| REG-004 | Register with weak password | 1. Enter password `123` 2. Submit | Show password strength error and prevent registration | Password policy |
| REG-005 | Register without phone number | 1. Leave phone empty 2. Submit | Show "Phone number is required" | Required field validation |
| REG-006 | Register with phone number already linked to another account | 1. Enter existing phone 2. Submit | Show duplicate phone error | Duplicate account check |
| REG-007 | Register using biometric setup prompt | 1. Complete registration 2. Accept biometric enrollment | Biometric login setup prompt appears | Usability validation |

## 2. Wallet Top-Up Feature

| Test Case ID | Description | Steps | Expected Result | Notes |
|---|---|---|---|---|
| TOPUP-001 | Top-up via bank transfer normal flow | 1. Open Top-up 2. Choose bank transfer 3. Enter amount 4. Confirm | Top-up submitted and balance updates after confirmation | Normal flow |
| TOPUP-002 | Top-up via credit card normal flow | 1. Choose credit card 2. Enter valid card details 3. Confirm | Payment completes and wallet balance increases | Normal flow |
| TOPUP-003 | Top-up via linked bank account | 1. Choose linked account 2. Select amount 3. Confirm | Top-up succeeds and transaction appears in history | Normal flow |
| TOPUP-004 | Top-up amount zero or negative | 1. Enter `0` or `-10` 2. Submit | Show validation error and block transaction | Input validation |
| TOPUP-005 | Top-up without selecting payment method | 1. Do not select method 2. Submit | Show required selection error | UI validation |
| TOPUP-006 | Top-up with expired credit card | 1. Enter expired card details 2. Submit | Show card expiry error | Payment gateway validation |
| TOPUP-007 | Top-up when network disconnects during payment | 1. Start payment 2. Cut network 3. Resume | App shows retry or failure option, no duplicate charge | Resilience test |

## 3. Regression Test Cases

| Test Case ID | Description | Steps | Expected Result | Coverage |
|---|---|---|---|---|
| REG-101 | Verify no double charge on credit card top-up | 1. Top-up via credit card 2. Complete payment 3. Confirm transaction appears once | Only one transaction is recorded and balance is correct | Fixed issue regression |
| REG-102 | Verify correct balance after sending money | 1. Send money to another user 2. Confirm success 3. Check wallet balance | Balance decreases exactly by send amount and fees | Fixed issue regression |
| REG-103 | Verify transaction history still works after payment fixes | 1. Perform a top-up 2. Perform send money 3. Open transaction history | All transactions display correctly with filters | Regression for history functionality |
| REG-104 | Verify limits are enforced on transfer and top-up | 1. Attempt send amount above limit 2. Attempt top-up above limit | App blocks requests with limit message | Limit enforcement check |
| REG-105 | Verify bill payments remain functional | 1. Pay electricity bill 2. Confirm payment success 3. Check transaction record | Bill payment completes and logs correctly | Functional regression check |

## 4. Recommended Report Format

- Use a spreadsheet layout with the following columns:
  - Test Case ID
  - Feature
  - Description
  - Preconditions
  - Steps
  - Expected Result
  - Actual Result
  - Status
  - Comments
- Keep regression and exploratory test cases in separate sheets or sections for maintainability.
