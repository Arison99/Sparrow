package adapters;

import core.BackendService;

import java.util.Date;
import java.util.Map;

public class BackendServiceSendMoneyAdapter {
    // Parse query string and call BackendService logic
    public static String handleSendMoneyRequest(String query) {
        Map<String, String> params = parseQuery(query);
        String from = params.get("from");
        String to = params.get("to");
        String amountStr = params.get("amount");
        double amount = 0;
        try { amount = Double.parseDouble(amountStr); } catch (Exception e) { return "{\"error\":\"Invalid amount\"}"; }
        String result = sendMoney(from, to, amount);
        return "{\"result\":\"" + result + "\"}";
    }
    public static String handleSendMoneyRequestXml(String query) {
        Map<String, String> params = parseQuery(query);
        String from = params.get("from");
        String to = params.get("to");
        String amountStr = params.get("amount");
        double amount = 0;
        try { amount = Double.parseDouble(amountStr); } catch (Exception e) { return "<error>Invalid amount</error>"; }
        String result = sendMoney(from, to, amount);
        return "<result>" + result + "</result>";
    }
    private static String sendMoney(String from, String to, double amount) {
        if (from == null || to == null || amount <= 0) return "Invalid parameters.";
        if (!BackendService.getUsers().containsKey(from)) return "Sender not found.";
        if (!BackendService.getUsers().containsKey(to)) return "Recipient not found.";
        BackendService.User sender = BackendService.getUsers().get(from);
        BackendService.User recipient = BackendService.getUsers().get(to);
        if (sender.getBalance() < amount) return "Insufficient funds.";
        sender.addTransaction(new BackendService.Transaction("T"+System.currentTimeMillis(), "Sent to " + to, -amount, new Date()));
        recipient.addTransaction(new BackendService.Transaction("T"+System.currentTimeMillis(), "Received from " + from, amount, new Date()));
        return "Success";
    }
    private static Map<String, String> parseQuery(String query) {
        java.util.HashMap<String, String> map = new java.util.HashMap<>();
        if (query == null) return map;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) map.put(kv[0], kv[1]);
        }
        return map;
    }
}
