package hardware;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TelcoProviderService {
    
    public static class ProviderInfo {
        private String mcc; // Mobile Country Code
        private String mnc; // Mobile Network Code
        private String name;
        private String country;
        private String balanceUSSD;
        private boolean supportsMobileMoney;
        private String mobileMoneyUSSD;
        private String brand;
        private String[] ussdCodes;
        private String currency;
        private String[] services;
        
        public ProviderInfo(String mcc, String mnc, String name, String country, String brand) {
            this.mcc = mcc;
            this.mnc = mnc;
            this.name = name;
            this.country = country;
            this.brand = brand;
            this.supportsMobileMoney = false;
        }
        
        // Getters and setters
        public String getMcc() { return mcc; }
        public String getMnc() { return mnc; }
        public String getName() { return name; }
        public String getCountry() { return country; }
        public String getBalanceUSSD() { return balanceUSSD; }
        public void setBalanceUSSD(String balanceUSSD) { this.balanceUSSD = balanceUSSD; }
        public boolean supportsMobileMoney() { return supportsMobileMoney; }
        public void setSupportsMobileMoney(boolean supportsMobileMoney) { this.supportsMobileMoney = supportsMobileMoney; }
        public String getMobileMoneyUSSD() { return mobileMoneyUSSD; }
        public void setMobileMoneyUSSD(String mobileMoneyUSSD) { this.mobileMoneyUSSD = mobileMoneyUSSD; }
        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }
        public String[] getUssdCodes() { return ussdCodes; }
        public void setUssdCodes(String[] ussdCodes) { this.ussdCodes = ussdCodes; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String[] getServices() { return services; }
        public void setServices(String[] services) { this.services = services; }
    }
    
    private Map<String, ProviderInfo> providers = new HashMap<>();
    
    public TelcoProviderService() {
        initializeAfricanProviders();
    }
    
    private void initializeAfricanProviders() {
        // Nigeria
        addNigerianProviders();
        
        // Kenya
        addKenyanProviders();
        
        // Uganda
        addUgandanProviders();
        
        // Tanzania
        addTanzanianProviders();
        
        // Ghana
        addGhanaianProviders();
        
        // South Africa
        addSouthAfricanProviders();
        
        // Rwanda
        addRwandanProviders();
        
        // Zambia
        addZambianProviders();
        
        // Ethiopia
        addEthiopianProviders();
        
        // Cameroon
        addCameroonianProviders();
    }
    
    private void addNigerianProviders() {
        // MTN Nigeria
        ProviderInfo mtnNg = new ProviderInfo("621", "30", "MTN Nigeria", "Nigeria", "MTN");
        mtnNg.setBalanceUSSD("*556#");
        mtnNg.setSupportsMobileMoney(true);
        mtnNg.setMobileMoneyUSSD("*737#");
        mtnNg.setCurrency("NGN");
        mtnNg.setUssdCodes(new String[]{"*556#", "*737#", "*131#", "*123#"});
        mtnNg.setServices(new String[]{"Voice", "Data", "SMS", "Mobile Money", "Airtime"});
        providers.put("621-30", mtnNg);
        
        // Airtel Nigeria
        ProviderInfo airtelNg = new ProviderInfo("621", "20", "Airtel Nigeria", "Nigeria", "Airtel");
        airtelNg.setBalanceUSSD("*123#");
        airtelNg.setSupportsMobileMoney(true);
        airtelNg.setMobileMoneyUSSD("*432#");
        airtelNg.setCurrency("NGN");
        airtelNg.setUssdCodes(new String[]{"*123#", "*432#", "*140#"});
        airtelNg.setServices(new String[]{"Voice", "Data", "SMS", "Airtel Money", "Airtime"});
        providers.put("621-20", airtelNg);
        
        // Glo Nigeria
        ProviderInfo gloNg = new ProviderInfo("621", "50", "Globacom Nigeria", "Nigeria", "Glo");
        gloNg.setBalanceUSSD("*124#");
        gloNg.setSupportsMobileMoney(false);
        gloNg.setCurrency("NGN");
        gloNg.setUssdCodes(new String[]{"*124#", "*777#", "*135#"});
        gloNg.setServices(new String[]{"Voice", "Data", "SMS", "Airtime"});
        providers.put("621-50", gloNg);
        
        // 9mobile Nigeria
        ProviderInfo etisalatNg = new ProviderInfo("621", "60", "9mobile Nigeria", "Nigeria", "9mobile");
        etisalatNg.setBalanceUSSD("*232#");
        etisalatNg.setSupportsMobileMoney(false);
        etisalatNg.setCurrency("NGN");
        etisalatNg.setUssdCodes(new String[]{"*232#", "*229#", "*200#"});
        etisalatNg.setServices(new String[]{"Voice", "Data", "SMS", "Airtime"});
        providers.put("621-60", etisalatNg);
    }
    
    private void addKenyanProviders() {
        // Safaricom Kenya (M-Pesa)
        ProviderInfo safaricom = new ProviderInfo("639", "02", "Safaricom Kenya", "Kenya", "Safaricom");
        safaricom.setBalanceUSSD("*144#");
        safaricom.setSupportsMobileMoney(true);
        safaricom.setMobileMoneyUSSD("*334#");
        safaricom.setCurrency("KES");
        safaricom.setUssdCodes(new String[]{"*144#", "*334#", "*100#", "*234#"});
        safaricom.setServices(new String[]{"Voice", "Data", "SMS", "M-Pesa", "Airtime"});
        providers.put("639-02", safaricom);
        
        // Airtel Kenya
        ProviderInfo airtelKe = new ProviderInfo("639", "03", "Airtel Kenya", "Kenya", "Airtel");
        airtelKe.setBalanceUSSD("*544#");
        airtelKe.setSupportsMobileMoney(true);
        airtelKe.setMobileMoneyUSSD("*334#");
        airtelKe.setCurrency("KES");
        airtelKe.setUssdCodes(new String[]{"*544#", "*334#", "*175#"});
        airtelKe.setServices(new String[]{"Voice", "Data", "SMS", "Airtel Money", "Airtime"});
        providers.put("639-03", airtelKe);
        
        // Telkom Kenya
        ProviderInfo telkomKe = new ProviderInfo("639", "07", "Telkom Kenya", "Kenya", "Telkom");
        telkomKe.setBalanceUSSD("*544#");
        telkomKe.setSupportsMobileMoney(true);
        telkomKe.setMobileMoneyUSSD("*334#");
        telkomKe.setCurrency("KES");
        telkomKe.setUssdCodes(new String[]{"*544#", "*334#"});
        telkomKe.setServices(new String[]{"Voice", "Data", "SMS", "T-Kash", "Airtime"});
        providers.put("639-07", telkomKe);
    }
    
    private void addUgandanProviders() {
        // MTN Uganda
        ProviderInfo mtnUg = new ProviderInfo("641", "10", "MTN Uganda", "Uganda", "MTN");
        mtnUg.setBalanceUSSD("*131#");
        mtnUg.setSupportsMobileMoney(true);
        mtnUg.setMobileMoneyUSSD("*165#");
        mtnUg.setCurrency("UGX");
        mtnUg.setUssdCodes(new String[]{"*131#", "*165#", "*150#"});
        mtnUg.setServices(new String[]{"Voice", "Data", "SMS", "MTN Mobile Money", "Airtime"});
        providers.put("641-10", mtnUg);
        
        // Airtel Uganda
        ProviderInfo airtelUg = new ProviderInfo("641", "01", "Airtel Uganda", "Uganda", "Airtel");
        airtelUg.setBalanceUSSD("*131#");
        airtelUg.setSupportsMobileMoney(true);
        airtelUg.setMobileMoneyUSSD("*185#");
        airtelUg.setCurrency("UGX");
        airtelUg.setUssdCodes(new String[]{"*131#", "*185#", "*175#"});
        airtelUg.setServices(new String[]{"Voice", "Data", "SMS", "Airtel Money", "Airtime"});
        providers.put("641-01", airtelUg);
        
        // Africell Uganda
        ProviderInfo africellUg = new ProviderInfo("641", "14", "Africell Uganda", "Uganda", "Africell");
        africellUg.setBalanceUSSD("*144#");
        africellUg.setSupportsMobileMoney(false);
        africellUg.setCurrency("UGX");
        africellUg.setUssdCodes(new String[]{"*144#", "*155#"});
        africellUg.setServices(new String[]{"Voice", "Data", "SMS", "Airtime"});
        providers.put("641-14", africellUg);
    }
    
    private void addTanzanianProviders() {
        // Vodacom Tanzania (M-Pesa)
        ProviderInfo vodacomTz = new ProviderInfo("640", "04", "Vodacom Tanzania", "Tanzania", "Vodacom");
        vodacomTz.setBalanceUSSD("*100#");
        vodacomTz.setSupportsMobileMoney(true);
        vodacomTz.setMobileMoneyUSSD("*150*00#");
        vodacomTz.setCurrency("TZS");
        vodacomTz.setUssdCodes(new String[]{"*100#", "*150*00#", "*147#"});
        vodacomTz.setServices(new String[]{"Voice", "Data", "SMS", "M-Pesa", "Airtime"});
        providers.put("640-04", vodacomTz);
        
        // Airtel Tanzania
        ProviderInfo airtelTz = new ProviderInfo("640", "05", "Airtel Tanzania", "Tanzania", "Airtel");
        airtelTz.setBalanceUSSD("*144#");
        airtelTz.setSupportsMobileMoney(true);
        airtelTz.setMobileMoneyUSSD("*150*60#");
        airtelTz.setCurrency("TZS");
        airtelTz.setUssdCodes(new String[]{"*144#", "*150*60#", "*175#"});
        airtelTz.setServices(new String[]{"Voice", "Data", "SMS", "Airtel Money", "Airtime"});
        providers.put("640-05", airtelTz);
        
        // Tigo Tanzania
        ProviderInfo tigoTz = new ProviderInfo("640", "02", "Tigo Tanzania", "Tanzania", "Tigo");
        tigoTz.setBalanceUSSD("*150#");
        tigoTz.setSupportsMobileMoney(true);
        tigoTz.setMobileMoneyUSSD("*150*01#");
        tigoTz.setCurrency("TZS");
        tigoTz.setUssdCodes(new String[]{"*150#", "*150*01#", "*147#"});
        tigoTz.setServices(new String[]{"Voice", "Data", "SMS", "Tigo Pesa", "Airtime"});
        providers.put("640-02", tigoTz);
    }
    
    private void addGhanaianProviders() {
        // MTN Ghana
        ProviderInfo mtnGh = new ProviderInfo("620", "01", "MTN Ghana", "Ghana", "MTN");
        mtnGh.setBalanceUSSD("*124#");
        mtnGh.setSupportsMobileMoney(true);
        mtnGh.setMobileMoneyUSSD("*170#");
        mtnGh.setCurrency("GHS");
        mtnGh.setUssdCodes(new String[]{"*124#", "*170#", "*138#"});
        mtnGh.setServices(new String[]{"Voice", "Data", "SMS", "MTN Mobile Money", "Airtime"});
        providers.put("620-01", mtnGh);
        
        // Vodafone Ghana
        ProviderInfo vodafoneGh = new ProviderInfo("620", "02", "Vodafone Ghana", "Ghana", "Vodafone");
        vodafoneGh.setBalanceUSSD("*127#");
        vodafoneGh.setSupportsMobileMoney(true);
        vodafoneGh.setMobileMoneyUSSD("*110#");
        vodafoneGh.setCurrency("GHS");
        vodafoneGh.setUssdCodes(new String[]{"*127#", "*110#", "*135#"});
        vodafoneGh.setServices(new String[]{"Voice", "Data", "SMS", "Vodafone Cash", "Airtime"});
        providers.put("620-02", vodafoneGh);
        
        // AirtelTigo Ghana
        ProviderInfo airteltigo = new ProviderInfo("620", "06", "AirtelTigo Ghana", "Ghana", "AirtelTigo");
        airteltigo.setBalanceUSSD("*100#");
        airteltigo.setSupportsMobileMoney(true);
        airteltigo.setMobileMoneyUSSD("*110#");
        airteltigo.setCurrency("GHS");
        airteltigo.setUssdCodes(new String[]{"*100#", "*110#", "*175#"});
        airteltigo.setServices(new String[]{"Voice", "Data", "SMS", "AirtelTigo Money", "Airtime"});
        providers.put("620-06", airteltigo);
    }
    
    private void addSouthAfricanProviders() {
        // Vodacom South Africa
        ProviderInfo vodacomZa = new ProviderInfo("655", "01", "Vodacom South Africa", "South Africa", "Vodacom");
        vodacomZa.setBalanceUSSD("*135#");
        vodacomZa.setSupportsMobileMoney(false);
        vodacomZa.setCurrency("ZAR");
        vodacomZa.setUssdCodes(new String[]{"*135#", "*111#", "*147#"});
        vodacomZa.setServices(new String[]{"Voice", "Data", "SMS", "Airtime"});
        providers.put("655-01", vodacomZa);
        
        // MTN South Africa
        ProviderInfo mtnZa = new ProviderInfo("655", "10", "MTN South Africa", "South Africa", "MTN");
        mtnZa.setBalanceUSSD("*141#");
        mtnZa.setSupportsMobileMoney(false);
        mtnZa.setCurrency("ZAR");
        mtnZa.setUssdCodes(new String[]{"*141#", "*136#", "*123#"});
        mtnZa.setServices(new String[]{"Voice", "Data", "SMS", "Airtime"});
        providers.put("655-10", mtnZa);
        
        // Cell C South Africa
        ProviderInfo cellc = new ProviderInfo("655", "07", "Cell C South Africa", "South Africa", "Cell C");
        cellc.setBalanceUSSD("*147#");
        cellc.setSupportsMobileMoney(false);
        cellc.setCurrency("ZAR");
        cellc.setUssdCodes(new String[]{"*147#", "*111#"});
        cellc.setServices(new String[]{"Voice", "Data", "SMS", "Airtime"});
        providers.put("655-07", cellc);
    }
    
    private void addRwandanProviders() {
        // MTN Rwanda
        ProviderInfo mtnRw = new ProviderInfo("635", "10", "MTN Rwanda", "Rwanda", "MTN");
        mtnRw.setBalanceUSSD("*182#");
        mtnRw.setSupportsMobileMoney(true);
        mtnRw.setMobileMoneyUSSD("*182#");
        mtnRw.setCurrency("RWF");
        mtnRw.setUssdCodes(new String[]{"*182#", "*160#"});
        mtnRw.setServices(new String[]{"Voice", "Data", "SMS", "MTN Mobile Money", "Airtime"});
        providers.put("635-10", mtnRw);
        
        // Airtel Rwanda
        ProviderInfo airtelRw = new ProviderInfo("635", "14", "Airtel Rwanda", "Rwanda", "Airtel");
        airtelRw.setBalanceUSSD("*131#");
        airtelRw.setSupportsMobileMoney(true);
        airtelRw.setMobileMoneyUSSD("*500#");
        airtelRw.setCurrency("RWF");
        airtelRw.setUssdCodes(new String[]{"*131#", "*500#", "*175#"});
        airtelRw.setServices(new String[]{"Voice", "Data", "SMS", "Airtel Money", "Airtime"});
        providers.put("635-14", airtelRw);
    }
    
    private void addZambianProviders() {
        // MTN Zambia
        ProviderInfo mtnZm = new ProviderInfo("645", "01", "MTN Zambia", "Zambia", "MTN");
        mtnZm.setBalanceUSSD("*303#");
        mtnZm.setSupportsMobileMoney(true);
        mtnZm.setMobileMoneyUSSD("*303#");
        mtnZm.setCurrency("ZMW");
        mtnZm.setUssdCodes(new String[]{"*303#", "*335#"});
        mtnZm.setServices(new String[]{"Voice", "Data", "SMS", "MTN Mobile Money", "Airtime"});
        providers.put("645-01", mtnZm);
        
        // Airtel Zambia
        ProviderInfo airtelZm = new ProviderInfo("645", "02", "Airtel Zambia", "Zambia", "Airtel");
        airtelZm.setBalanceUSSD("*200#");
        airtelZm.setSupportsMobileMoney(true);
        airtelZm.setMobileMoneyUSSD("*334#");
        airtelZm.setCurrency("ZMW");
        airtelZm.setUssdCodes(new String[]{"*200#", "*334#", "*175#"});
        airtelZm.setServices(new String[]{"Voice", "Data", "SMS", "Airtel Money", "Airtime"});
        providers.put("645-02", airtelZm);
    }
    
    private void addEthiopianProviders() {
        // Ethio Telecom
        ProviderInfo ethioTelecom = new ProviderInfo("636", "01", "Ethio Telecom", "Ethiopia", "Ethio Telecom");
        ethioTelecom.setBalanceUSSD("*804#");
        ethioTelecom.setSupportsMobileMoney(true);
        ethioTelecom.setMobileMoneyUSSD("*847#");
        ethioTelecom.setCurrency("ETB");
        ethioTelecom.setUssdCodes(new String[]{"*804#", "*847#", "*800#"});
        ethioTelecom.setServices(new String[]{"Voice", "Data", "SMS", "TeleBirr", "Airtime"});
        providers.put("636-01", ethioTelecom);
    }
    
    private void addCameroonianProviders() {
        // MTN Cameroon
        ProviderInfo mtnCm = new ProviderInfo("624", "01", "MTN Cameroon", "Cameroon", "MTN");
        mtnCm.setBalanceUSSD("*126#");
        mtnCm.setSupportsMobileMoney(true);
        mtnCm.setMobileMoneyUSSD("*126#");
        mtnCm.setCurrency("XAF");
        mtnCm.setUssdCodes(new String[]{"*126#", "*150#"});
        mtnCm.setServices(new String[]{"Voice", "Data", "SMS", "MTN Mobile Money", "Airtime"});
        providers.put("624-01", mtnCm);
        
        // Orange Cameroon
        ProviderInfo orangeCm = new ProviderInfo("624", "02", "Orange Cameroon", "Cameroon", "Orange");
        orangeCm.setBalanceUSSD("*144#");
        orangeCm.setSupportsMobileMoney(true);
        orangeCm.setMobileMoneyUSSD("*144#");
        orangeCm.setCurrency("XAF");
        orangeCm.setUssdCodes(new String[]{"*144#", "*155#"});
        orangeCm.setServices(new String[]{"Voice", "Data", "SMS", "Orange Money", "Airtime"});
        providers.put("624-02", orangeCm);
    }
    
    public ProviderInfo getProviderByMccMnc(String mcc, String mnc) {
        return providers.get(mcc + "-" + mnc);
    }
    
    public ProviderInfo getProviderByName(String name) {
        return providers.values().stream()
            .filter(provider -> provider.getName().toLowerCase().contains(name.toLowerCase()))
            .findFirst()
            .orElse(null);
    }
    
    public List<ProviderInfo> getProvidersByCountry(String country) {
        return providers.values().stream()
            .filter(provider -> provider.getCountry().equalsIgnoreCase(country))
            .sorted(Comparator.comparing(ProviderInfo::getName))
            .toList();
    }
    
    public List<ProviderInfo> getMobileMoneyProviders() {
        return providers.values().stream()
            .filter(ProviderInfo::supportsMobileMoney)
            .sorted(Comparator.comparing(ProviderInfo::getCountry)
                .thenComparing(ProviderInfo::getName))
            .toList();
    }
    
    public List<ProviderInfo> getAllProviders() {
        return new ArrayList<>(providers.values());
    }
    
    public Set<String> getSupportedCountries() {
        return providers.values().stream()
            .map(ProviderInfo::getCountry)
            .collect(Collectors.toSet());
    }
    
    public boolean isKnownProvider(String mcc, String mnc) {
        return providers.containsKey(mcc + "-" + mnc);
    }
}
