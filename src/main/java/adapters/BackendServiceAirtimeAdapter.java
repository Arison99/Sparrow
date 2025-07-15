package adapters;

import core.BackendService;

import java.util.Date;
import java.util.Map;

public class BackendServiceAirtimeAdapter {
    public static String handleAirtimeRequest(String query) {
        Map<String, String> params = parseQuery(query);
        String phone = params.get("phone");
        String amountStr = params.get("amount");
        double amount = 0;
        try { amount = Double.parseDouble(amountStr); } catch (Exception e) { return "{\"error\":\"Invalid amount\"}"; }
        String result = buyAirtime(phone, amount);
        return "{\"result\":\"" + result + "\"}";
    }
    public static String handleAirtimeRequestXml(String query) {
        Map<String, String> params = parseQuery(query);
        String phone = params.get("phone");
        String amountStr = params.get("amount");
        double amount = 0;
        try { amount = Double.parseDouble(amountStr); } catch (Exception e) { return "<error>Invalid amount</error>"; }
        String result = buyAirtime(phone, amount);
        return "<result>" + result + "</result>";
    }
    private static String buyAirtime(String phone, double amount) {
        if (phone == null || amount <= 0) return "Invalid parameters.";
        if (!BackendService.getUsers().containsKey(phone)) return "User not found.";
        BackendService.User user = BackendService.getUsers().get(phone);
        if (user.getBalance() < amount) return "Insufficient funds.";
        user.addTransaction(new BackendService.Transaction("T"+System.currentTimeMillis(), "Airtime purchase", -amount, new Date()));
        return "Airtime purchased successfully.";
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
