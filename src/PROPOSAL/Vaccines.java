package PROPOSAL;

import java.util.Stack;

public class Vaccines {
    private String vaccineName;
    private String manufacturer; 
    private String expiryDate;
    private Stack<String> vialStack; 

    public Vaccines(String vaccineName, int initialQty, String manufacturer, String expiryDate) {
        this.vaccineName = vaccineName;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.vialStack = new Stack<>();

        for (int i = 1; i <= initialQty; i++) {
            vialStack.push("Vial-" + i); 
        }
    }

    public String getVaccineName() { 
        return vaccineName; 
    }

    public String getManufacturer() { 
        return manufacturer; 
    }

    public String getExpiryDate() { 
        return expiryDate; 
    }

    public int getCurrentQuantity() {
        return vialStack.size();
    }

    public boolean dispenseVial() {
        if (!vialStack.isEmpty()) {
            String usedVial = vialStack.pop(); 
            System.out.println("   -> Dispensed: " + usedVial + " from batch " + vaccineName);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Batch: " + vaccineName + " | Mfr: " + manufacturer + 
               " | Exp: " + expiryDate + " | Current Stock: " + vialStack.size() + " vials";
    }
}