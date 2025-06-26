import java.io.*;
import java.util.*;

public class User {
    private String name;
    private double balance;
    private Map<String, Integer> portfolio;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.portfolio = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public Map<String, Integer> getPortfolio() {
        return portfolio;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean buyStock(String symbol, double price, int quantity) {
        double cost = price * quantity;
        if (balance >= cost) {
            balance -= cost;
            portfolio.put(symbol, portfolio.getOrDefault(symbol, 0) + quantity);
            return true;
        }
        return false;
    }

    public boolean sellStock(String symbol, double price, int quantity) {
        int owned = portfolio.getOrDefault(symbol, 0);
        if (owned >= quantity) {
            balance += price * quantity;
            if (owned == quantity) {
                portfolio.remove(symbol);
            } else {
                portfolio.put(symbol, owned - quantity);
            }
            return true;
        }
        return false;
    }

    public void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n📈 Portfolio of " + name);
        if (portfolio.isEmpty()) {
            System.out.println("No stocks owned.");
        } else {
            double totalValue = 0;
            for (String symbol : portfolio.keySet()) {
                int qty = portfolio.get(symbol);
                double currentPrice = market.get(symbol).getPrice();
                totalValue += qty * currentPrice;
                System.out.printf("%s (%d shares) - $%.2f each, Total: $%.2f\n",
                    symbol, qty, currentPrice, qty * currentPrice);
            }
            System.out.printf("Total Portfolio Value: $%.2f\n", totalValue);
        }
        System.out.printf("Available Balance: $%.2f\n", balance);
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(name + "\n");
            writer.write(String.format("%.2f\n", balance));
            for (String symbol : portfolio.keySet()) {
                writer.write(symbol + "," + portfolio.get(symbol) + "\n");
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error saving portfolio: " + e.getMessage());
        }
    }

    public static User loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String name = reader.readLine();
            double balance = Double.parseDouble(reader.readLine());
            User user = new User(name, balance);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String symbol = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    user.portfolio.put(symbol, quantity);
                }
            }
            return user;
        } catch (FileNotFoundException e) {
            return null; // First-time run
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Error loading portfolio: " + e.getMessage());
            return null;
        }
    }
}
