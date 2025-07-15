package ui;

import core.BackendService;
import adapters.BackendServiceSendMoneyAdapter;
import adapters.BackendServiceAirtimeAdapter;
import adapters.BackendServicePayWithMoMoAdapter;
import adapters.BackendServicePaymentsAdapter;
import adapters.BackendServiceSavingsLoansAdapter;
import adapters.BackendServiceFinancialServicesAdapter;
import adapters.BackendServiceInvestInsureAdapter;
import adapters.BackendServiceJsonAdapter;
import adapters.BackendServiceXmlAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Controller
public class AdminController {
    
    // Helper class for transaction display
    public static class TransactionDisplay {
        private final String id;
        private final String phone;
        private final String description;
        private final double amount;
        private final Date date;
        
        public TransactionDisplay(String id, String phone, String description, double amount, Date date) {
            this.id = id;
            this.phone = phone;
            this.description = description;
            this.amount = amount;
            this.date = date;
        }
        
        public String getId() { return id; }
        public String getPhone() { return phone; }
        public String getDescription() { return description; }
        public double getAmount() { return amount; }
        public Date getDate() { return date; }
    }
    
    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/addUser")
    public String addUser(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "pin") String pin,
            @RequestParam(name = "balance") double balance,
            Model model) {
        boolean added = BackendService.addUser(phone, pin, balance);
        model.addAttribute("message", added ? "User added successfully." : "User already exists.");
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/addTransaction")
    public String addTransaction(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "amount") double amount,
            Model model) {
        String txnId = "T" + System.currentTimeMillis();
        boolean added = BackendService.addTransaction(phone, txnId, description, amount, new Date());
        model.addAttribute("message", added ? "Transaction added successfully." : "User not found.");
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/sendMoney")
    public String sendMoney(
            @RequestParam(name = "fromPhone") String fromPhone,
            @RequestParam(name = "toPhone") String toPhone,
            @RequestParam(name = "amount") double amount,
            Model model) {
        String query = "from=" + fromPhone + "&to=" + toPhone + "&amount=" + amount;
        String result = BackendServiceSendMoneyAdapter.handleSendMoneyRequest(query);
        model.addAttribute("message", "Send Money: " + result);
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/buyAirtime")
    public String buyAirtime(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "amount") double amount,
            Model model) {
        String query = "phone=" + phone + "&amount=" + amount;
        String result = BackendServiceAirtimeAdapter.handleAirtimeRequest(query);
        model.addAttribute("message", "Buy Airtime: " + result);
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/payWithMoMo")
    public String payWithMoMo(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "merchant") String merchant,
            @RequestParam(name = "amount") double amount,
            Model model) {
        String query = "phone=" + phone + "&merchant=" + merchant + "&amount=" + amount;
        String result = BackendServicePayWithMoMoAdapter.handlePayWithMoMoRequest(query);
        model.addAttribute("message", "Pay with MoMo: " + result);
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/payBill")
    public String payBill(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "biller") String biller,
            @RequestParam(name = "amount") double amount,
            Model model) {
        String query = "phone=" + phone + "&biller=" + biller + "&amount=" + amount;
        String result = BackendServicePaymentsAdapter.handlePaymentsRequest(query);
        model.addAttribute("message", "Pay Bill: " + result);
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/savingsLoans")
    public String savingsLoans(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "action") String action,
            @RequestParam(name = "amount") double amount,
            Model model) {
        String query = "phone=" + phone + "&action=" + action + "&amount=" + amount;
        String result = BackendServiceSavingsLoansAdapter.handleSavingsLoansRequest(query);
        model.addAttribute("message", "Savings & Loans: " + result);
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/financialServices")
    public String financialServices(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "service") String service,
            @RequestParam(name = "amount") double amount,
            Model model) {
        String query = "phone=" + phone + "&service=" + service + "&amount=" + amount;
        String result = BackendServiceFinancialServicesAdapter.handleFinancialServicesRequest(query);
        model.addAttribute("message", "Financial Services: " + result);
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    @PostMapping("/admin/investInsure")
    public String investInsure(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "action") String action,
            @RequestParam(name = "amount") double amount,
            Model model) {
        String query = "phone=" + phone + "&action=" + action + "&amount=" + amount;
        String result = BackendServiceInvestInsureAdapter.handleInvestInsureRequest(query);
        model.addAttribute("message", "Invest & Insure: " + result);
        model.addAttribute("users", BackendService.getUsers().values());
        return "admin";
    }

    // API endpoints for JSON responses
    @GetMapping("/api/users")
    @ResponseBody
    public String getUsersJson() {
        return BackendServiceJsonAdapter.getAllUsersJson();
    }

    @GetMapping("/api/users/xml")
    @ResponseBody
    public String getUsersXml() {
        return BackendServiceXmlAdapter.getAllUsersXml();
    }    @PostMapping("/api/users/balance")
    @ResponseBody
    public String getUserBalance(
            @RequestParam(name = "phone") String phone) {
        BackendService.User user = BackendService.getUsers().get(phone);
        if (user != null) {
            return "{\"phone\":\"" + phone + "\",\"balance\":" + user.getBalance() + "}";
        }
        return "{\"error\":\"User not found\"}";
    }    @PostMapping("/api/users/transactions")
    @ResponseBody
    public String getUserTransactions(
            @RequestParam(name = "phone") String phone) {
        BackendService.User user = BackendService.getUsers().get(phone);
        if (user != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"phone\":\"").append(phone).append("\",\"transactions\":[");
            boolean first = true;
            for (BackendService.Transaction txn : user.getTransactions()) {
                if (!first) sb.append(",");
                sb.append("{\"id\":\"").append(txn.getId()).append("\",\"description\":\"")
                  .append(txn.getDescription()).append("\",\"amount\":").append(txn.getAmount())
                  .append(",\"date\":\"").append(txn.getDate()).append("\"}");
                first = false;
            }
            sb.append("]}");
            return sb.toString();
        }
        return "{\"error\":\"User not found\"}";
    }

    // Navigation routes
    @GetMapping("/admin/users")
    public String usersPage(Model model) {
        model.addAttribute("users", BackendService.getUsers().values());
        return "users";
    }

    @GetMapping("/admin/services")
    public String servicesPage(Model model) {
        return "services";
    }

    @GetMapping("/admin/transactions")
    public String transactionsPage(Model model) {
        // Calculate transaction statistics
        int totalTransactions = 0;
        double totalCredits = 0;
        double totalDebits = 0;
        
        for (BackendService.User user : BackendService.getUsers().values()) {
            totalTransactions += user.getTransactions().size();
            for (BackendService.Transaction txn : user.getTransactions()) {
                if (txn.getAmount() > 0) {
                    totalCredits += txn.getAmount();
                } else {
                    totalDebits += Math.abs(txn.getAmount());
                }
            }
        }
        
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("totalCredits", String.format("$%.2f", totalCredits));
        model.addAttribute("totalDebits", String.format("$%.2f", totalDebits));
        model.addAttribute("netFlow", String.format("$%.2f", totalCredits - totalDebits));
        
        // Get recent transactions (last 20)
        java.util.List<TransactionDisplay> recentTransactions = new java.util.ArrayList<>();
        for (BackendService.User user : BackendService.getUsers().values()) {
            for (BackendService.Transaction txn : user.getTransactions()) {
                recentTransactions.add(new TransactionDisplay(
                    txn.getId(), 
                    user.getPhoneNumber(), 
                    txn.getDescription(), 
                    txn.getAmount(), 
                    txn.getDate()
                ));
            }
        }
        
        // Sort by date (newest first)
        recentTransactions.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        // Limit to 20 most recent
        if (recentTransactions.size() > 20) {
            recentTransactions = recentTransactions.subList(0, 20);
        }
        
        model.addAttribute("recentTransactions", recentTransactions);
        return "transactions";
    }

    @GetMapping("/admin/api-docs")
    public String apiDocsPage() {
        return "api-docs";
    }

    // Navigation routes for individual service templates
    @GetMapping("/admin/service/send-money")
    public String sendMoneyPage(Model model) {
        model.addAttribute("users", BackendService.getUsers().values());
        return "send-money";
    }

    @GetMapping("/admin/service/buy-airtime")
    public String buyAirtimePage(Model model) {
        model.addAttribute("users", BackendService.getUsers().values());
        return "buy-airtime";
    }

    @GetMapping("/admin/service/pay-with-momo")
    public String payWithMoMoPage(Model model) {
        model.addAttribute("users", BackendService.getUsers().values());
        return "pay-with-momo";
    }

    @GetMapping("/admin/service/pay-bill")
    public String payBillPage(Model model) {
        model.addAttribute("users", BackendService.getUsers().values());
        return "pay-bill";
    }

    @GetMapping("/admin/service/savings-loans")
    public String savingsLoansPage(Model model) {
        model.addAttribute("users", BackendService.getUsers().values());
        return "savings-loans";
    }

    @GetMapping("/admin/service/invest-insure")
    public String investInsurePage(Model model) {
        model.addAttribute("users", BackendService.getUsers().values());
        return "invest-insure";
    }
}
