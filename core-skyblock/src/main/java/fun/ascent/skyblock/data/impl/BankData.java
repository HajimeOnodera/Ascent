package fun.ascent.skyblock.data.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankData {
    private long lastClaimedInterest = 0L;
    private List<Transaction> transactions = new ArrayList<>();
    private double amount = 0.0;
    private double balanceLimit = 50000000.0;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Transaction {
        public long timestamp;
        public double amount;
        public String originator;
        public String type = "UNKNOWN";
    }
}
