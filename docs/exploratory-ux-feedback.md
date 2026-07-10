# E-Wallet Exploratory Testing Ideas and UX Feedback

## Exploratory Test Ideas for Wallet Top-up

1. Top-up with multiple rapid submissions
   - Try submitting a credit card top-up repeatedly within seconds.
   - Confirm the app prevents duplicate transactions and displays a clear "processing" state.

2. Top-up using an invalid stored payment method
   - Use a saved bank account or card that has expired or been revoked.
   - Verify the app presents an immediate error and does not create a pending transaction.

3. Top-up after changing currency or locale
   - Switch the app language or currency settings before topping up.
   - Confirm the displayed amount, fees, and confirmation screen remain consistent.

## UI/UX Feedback for an E-Wallet App

- Make the home screen balance more prominent with a large font and clear label like "Available balance".
- Use one-tap quick actions for the most common tasks: Top-up, Send Money, Pay Bill, and Scan QR.
- Provide an explicit confirmation step with estimated fees before completing a payment.
- Add inline validation on every field so users see errors as they type, especially for card number, expiry date, and phone number.
- Offer a clearer transaction status indicator in history: Completed, Pending, Failed, Refunded.
- When registering or logging in, show a progress indicator for verification steps (OTP, biometric setup).
- For merchant flows, show a clear summary of withdrawal fees, expected settlement time, and bank destination information.
- Provide contextual help or examples for sensitive fields like "amount" and "phone number".
