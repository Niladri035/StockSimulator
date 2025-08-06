import java.util.*;

class User {
    private String name;
    private double balance;
    private Map<String, Integer> portfolio;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.portfolio = new HashMap<>();
    }

    public String getName() { return name; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Map<String, Integer> getPortfolio() { return portfolio; }
}

class Stock {
    private String ticker;
    private double price;

    public Stock(String ticker, double price) {
        this.ticker = ticker;
        this.price = price;
    }

    public String getTicker() { return ticker; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

class MarketService {
    private Map<String, Stock> market;
    private Random rand;

    public MarketService() {
        this.market = new HashMap<>();
        this.rand = new Random();
        initializeMarket();
    }

    private void initializeMarket() {
        market.put("AAPL", new Stock("AAPL", 150));
        market.put("GOOG", new Stock("GOOG", 2800));
        market.put("TSLA", new Stock("TSLA", 750));
    }

    public void updateMarketPrices() {
        for (Stock stock : market.values()) {
            double change = (rand.nextDouble() - 0.5) * 10;
            stock.setPrice(Math.max(1, stock.getPrice() + change));
        }
    }

    public Map<String, Stock> getMarket() { return market; }

    public void printMarket() {
        System.out.println("\nCurrent Market Prices:");
        for (Stock stock : market.values()) {
            System.out.printf("%s: $%.2f\n", stock.getTicker(), stock.getPrice());
        }
    }
}

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static MarketService marketService = new MarketService();
    private static User user = new User("Niladri", 10000);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            marketService.updateMarketPrices();
            marketService.printMarket();
            printMenu();

            int choice = sc.nextInt();
            switch (choice) {
                case 1: buyStock(); break;
                case 2: sellStock(); break;
                case 3: viewPortfolio(); break;
                case 4: running = false; break;
                default: System.out.println("Invalid choice");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Buy Stock");
        System.out.println("2. Sell Stock");
        System.out.println("3. View Portfolio");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private static void buyStock() {
        System.out.print("Enter stock ticker: ");
        String ticker = sc.next().toUpperCase();
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        Stock stock = marketService.getMarket().get(ticker);
        if (stock == null) {
            System.out.println("Stock not found!");
            return;
        }

        double cost = stock.getPrice() * qty;
        if (cost > user.getBalance()) {
            System.out.println("Not enough balance!");
            return;
        }

        user.setBalance(user.getBalance() - cost);
        user.getPortfolio().put(ticker, user.getPortfolio().getOrDefault(ticker, 0) + qty);
        System.out.println("Bought " + qty + " shares of " + ticker);
    }

    private static void sellStock() {
        System.out.print("Enter stock ticker: ");
        String ticker = sc.next().toUpperCase();
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        Map<String, Integer> portfolio = user.getPortfolio();
        if (!portfolio.containsKey(ticker) || portfolio.get(ticker) < qty) {
            System.out.println("You don't have enough shares!");
            return;
        }

        Stock stock = marketService.getMarket().get(ticker);
        double revenue = stock.getPrice() * qty;
        user.setBalance(user.getBalance() + revenue);
        portfolio.put(ticker, portfolio.get(ticker) - qty);
        if (portfolio.get(ticker) == 0) portfolio.remove(ticker);
        System.out.println("Sold " + qty + " shares of " + ticker);
    }

    private static void viewPortfolio() {
        System.out.println("\nUser: " + user.getName());
        System.out.println("Balance: $" + user.getBalance());
        System.out.println("Portfolio:");
        for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
            System.out.printf("%s: %d shares\n", entry.getKey(), entry.getValue());
        }
    }
}