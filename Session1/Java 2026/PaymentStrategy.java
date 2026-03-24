public sealed interface PaymentStrategy
        permits CreditCard, Crypto, BankTransfer {}

public record CreditCard(String cardNumber, String expiry) implements PaymentStrategy {}
public record Crypto(String walletAddress, String currency) implements PaymentStrategy {}
public record BankTransfer(String iban) implements PaymentStrategy {}