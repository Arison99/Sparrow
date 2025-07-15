package adapters;

import core.BackendService;

import java.util.Date;
import java.util.Map;

public class BackendServiceInvestInsureAdapter {
    public static String handleInvestInsureRequest(String query) {
        Map<String, String> params = parseQuery(query);
        String phone = params.get("phone");
        String action = params.get("action");
        String amountStr = params.get("amount");
        double amount = 0;
        try { amount = Double.parseDouble(amountStr); } catch (Exception e) { return "{\"error\":\"Invalid amount\"}"; }
        String result = process(phone, action, amount);
        return "{\"result\":\"" + result + "\"}";
    }
    public static String handleInvestInsureRequestXml(String query) {
        Map<String, String> params = parseQuery(query);
        String phone = params.get("phone");
        String action = params.get("action");
        String amountStr = params.get("amount");
        double amount = 0;
        try { amount = Double.parseDouble(amountStr); } catch (Exception e) { return "<error>Invalid amount</error>"; }
        String result = process(phone, action, amount);
        return "<result>" + result + "</result>";
    }
    private static String process(String phone, String action, double amount) {
        if (phone == null || action == null || amount <= 0) return "Invalid parameters.";
        if (!BackendService.getUsers().containsKey(phone)) return "User not found.";
        BackendService.User user = BackendService.getUsers().get(phone);
        if ("invest".equalsIgnoreCase(action)) {
            user.addTransaction(new BackendService.Transaction("T"+System.currentTimeMillis(), "Investment", -amount, new Date()));
            return "Investment successful.";
        } else if ("insure".equalsIgnoreCase(action)) {
            user.addTransaction(new BackendService.Transaction("T"+System.currentTimeMillis(), "Insurance premium paid", -amount, new Date()));
            return "Insurance payment successful.";
        } else {
            return "Unknown action.";
        }
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
