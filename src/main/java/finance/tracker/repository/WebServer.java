package finance.tracker.repository;

import com.google.gson.Gson;
import finance.tracker.model.BaseTransaction;

import java.sql.Connection;

import static spark.Spark.*;

public class WebServer {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();
        Connection conn = DatabaseConnector.connect();
        TransactionDAO dao = new TransactionDAO(conn);

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

            boolean success = dao.insertTransaction(incoming);
            res.type("application/json");
            return gson.toJson(success ? "✅ Transaction saved!" : "❌ Failed to save transaction.");
        });
    }
}
