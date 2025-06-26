import java.util.*;

public class TradingApp {
    private Map<String, Stock> market;
    private User user;
    private Scanner scanner;

    public TradingApp() {
        market = new HashMap<String, Stock>();
        scanner = new Scanner(System.in);
        populateMarket();
        setupUser();
    }

    private void populateMarket() {
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 180.00));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 250.00));
        market.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 130.00));
        market.put("AMZN", new Stock("AMZN", "Amazon Inc.", 145.00));
    }

    private void setupUser() {
        File file = new File("portfolio.txt");
        if (file.exists()) {
            user = User.loadFromFile("portfolio.txt");
            if (user != null) {
                System.out.println("Loaded portfolio for " + user.getName());
                return;
            }
        }

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        user = new User(name, 1000.0);
        System.out.println("Welcome, " + name + "! Starting balance: $10,000.00");
    }


    public void run() {
        while (true) {
            System.out.println("\n📊 Stock Trading Menu:");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    viewMarket();
                    break;
                case "2":
                    buyStock();
                    break;
                case "3":
                    sellStock();
                    break;
                case "4":
                    user.showPortfolio(market);
                    break;
                case "5":
                    user.saveToFile("protfolio.txt");
                    System.out.println("Portfolio saved. Exiting.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewMarket() {
        System.out.println("\n Available Stocks:");
        for (Stock stock : market.values()) {
            System.out.printf("%s (%s) - $%.2f\n",
                stock.getSymbol(), stock.getCompanyName(), stock.getPrice());
        }
    }

    private void buyStock() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = market.get(symbol);
        if (stock == null) {
            System.out.println("⚠️ Invalid stock symbol.");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        if (user.buyStock(symbol, stock.getPrice(), qty)) {
            System.out.println("Purchase successful.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private void sellStock() {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = market.get(symbol);
        if (stock == null) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        if (user.sellStock(symbol, stock.getPrice(), qty)) {
            System.out.println("Sale successful.");
        } else {
            System.out.println("You do not have enough shares.");
        }
    }

    public static void main(String[] args) {
        TradingApp app = new TradingApp();
        app.run();
    }
}
