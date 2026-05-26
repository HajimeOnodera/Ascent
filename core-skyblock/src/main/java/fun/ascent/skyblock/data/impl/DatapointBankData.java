package fun.ascent.skyblock.data.impl;

import fun.ascent.skyblock.data.Datapoint;

public class DatapointBankData extends Datapoint<BankData> {

    public DatapointBankData(String key, BankData value) {
        super(key, value, new GsonSerializer<>(BankData.class));
    }

    public DatapointBankData(String key) {
        super(key, new BankData(), new GsonSerializer<>(BankData.class));
    }

    @Override
    public Datapoint<BankData> deepClone() {
        return new DatapointBankData(key, serializer.clone(value));
    }
}
