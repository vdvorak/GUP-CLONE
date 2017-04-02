package ua.com.itproekt.gup.teeest.my_transaction;

import ua.com.itproekt.gup.teeest.MyTransaction;
import ua.com.itproekt.gup.teeest.MyTransactionSignature;


public class MyMoneyTransferTransaction extends MyTransaction {

    private String type;
    private long timestamp;
    private MyTransactionSignature signature;

    public MyMoneyTransferTransaction(String bankId, long timestamp){
        this("MONEY_TRANSFER", bankId, timestamp);
    }

    private MyMoneyTransferTransaction(String type, String bankId, long timestamp){
        this.type = type;
        this.timestamp = timestamp;
        signature = new MyTransactionSignature(bankId);
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public MyTransactionSignature getSignature(){
        return signature;
    }
}
