public class PaymentProcessor {
    public String process(PaymentStrategy strategy, double amount) {
        return switch (strategy) {
            // Pattern matching + Extraction (Record Pattern)
            case CreditCard(var number, var expiry) ->
                    "Charging $" + amount + " to card " + number;

            case Crypto(var address, var coin) ->
                    "Sending " + amount + " equivalent of " + coin + " to " + address;

            case BankTransfer(var iban) ->
                    "Initiating wire transfer of $" + amount + " to " + iban;

            // No 'default' needed! The compiler knows we covered all permitted types.
        };
    }
}