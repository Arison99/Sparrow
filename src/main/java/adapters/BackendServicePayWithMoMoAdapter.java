package adapters;

import core.BackendService;

import java.util.Date;
import java.util.Map;

public class BackendServicePayWithMoMoAdapter {
    public static String handlePayWithMoMoRequest(String query) {
        Map<String, String> params = parseQuery(query);
        String phone = params.get("phone");
        String merchant = params.get("merchant");
        String amountStr = params.get("amount");
        double amount = 0;
        try { amount = Double.parseDouble(amountStr); } catch (Exception e) { return "{\"error\":\"Invalid amount\"}"; }
        String result = payWithMoMo(phone, merchant, amount);
        return "{\"result\":\"" + result + "\"}";
    }
    public static String handlePayWithMoMoRequestXml(String query) {
        Map<String, String> params = parseQuery(query);
        String phone = params.get("phone");
        String merchant = params.get("merchant");
        String amountStr = params.get("amount");
        double amount = 0;
        try { amount = Double.parseDouble(amountStr); } catch (Exception e) { return "<error>Invalid amount</error>"; }
        String result = payWithMoMo(phone, merchant, amount);
        return "<result>" + result + "</result>";
    }
    private static String payWithMoMo(String phone, String merchant, double amount) {
        if (phone == null || merchant == null || amount <= 0) return "Invalid parameters.";
        if (!BackendService.getUsers().containsKey(phone)) return "User not found.";
        BackendService.User user = BackendService.getUsers().get(phone);
        if (user.getBalance() < amount) return "Insufficient funds.";
        user.addTransaction(new BackendService.Transaction("T"+System.currentTimeMillis(), "Paid to merchant " + merchant, -amount, new Date()));
        return "Payment successful.";
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
