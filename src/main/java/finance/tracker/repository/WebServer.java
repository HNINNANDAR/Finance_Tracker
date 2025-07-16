package finance.tracker.repository;

import com.google.gson.Gson;
import finance.tracker.model.BaseTransaction;
import finance.tracker.service.TransactionService;

import java.sql.Connection;

import static spark.Spark.*;

public class WebServer {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();
        Connection conn = DatabaseConnector.getInstance();
        TransactionDAO dao = new TransactionDAO(conn);
        TransactionService service = new TransactionService(dao);

        // Test route
        get("/", (req, res) -> "✅ Finance Tracker Backend is running!");

        // POST route to add a transaction

        post("/transactions", (req, res) -> {
            System.out.println("📥 Incoming JSON: " + req.body());  // log JSON
            BaseTransaction incoming = gson.fromJson(req.body(), BaseTransaction.class);

            // You can add manual validation here
            if (!incoming.validate()) {
                res.status(400);
                return "Invalid transaction data.";
            }

            boolean success = service.addTransaction(incoming);
            res.type("application/json");
            return gson.toJson(success ? "✅ Transaction saved!" : "❌ Failed to save transaction.");
        });
    }
}
