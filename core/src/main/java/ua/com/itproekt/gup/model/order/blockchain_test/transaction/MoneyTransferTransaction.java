package ua.com.itproekt.gup.model.order.blockchain_test.transaction;

import ua.com.itproekt.gup.model.order.blockchain_test.Transaction;
import ua.com.itproekt.gup.model.order.blockchain_test.TransactionSignature;
import ua.com.itproekt.gup.util.FileKeyGenerator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;


public class MoneyTransferTransaction extends Transaction {

    private String                              type; /* String                                */
    private String                             _hash; /* (type + <random> + timestamp) SHA-256 */
    private String                              data; /* (JSON) //SHA-256                      */
    private TransactionSignature           signature; /* Class                                 */
    private long                           timestamp; /* Long                                  */
    private MoneyTransferTransactionInput[]   inputs;
    private MoneyTransferTransactionOutput[] outputs;

    /**
     *
     * @param selerId   String
     * @param timestamp Long
     * @param price     Long
     * @param productID String
     */
//    public MoneyTransferTransaction(String selerId, long timestamp, String productID, long price)
    public MoneyTransferTransaction(String userCardDetails, long amount, String publicHashStore, String signatureStore, long bankTransactionID)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, NoSuchProviderException, IllegalArgumentException {
        this("MONEY_TRANSACTION", null, userCardDetails, amount, publicHashStore, signatureStore, bankTransactionID); //this("MONEY_TRANSACTION", null, selerId, timestamp, 0, productID, price);
    }
//    private MoneyTransferTransaction(String type, String _hash, String selerId, long timestamp, int logicRef, String productID, long price)
    private MoneyTransferTransaction(String type, String _hash, String userCardDetails, long amount, String publicHashStore, String signatureStore, long bankTransactionID)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, NoSuchProviderException, IllegalArgumentException {
        this.type = type;
        this.timestamp = timestamp;
        this._hash = (_hash!=null && this._hash==null) ? _hash : get_hash();
//        setData(getData(logicRef, new String[]{selerId}, productID, price));
        setData(getData(userCardDetails, amount, publicHashStore, signatureStore, bankTransactionID));
        setSignature(publicHashStore); //TODO ???
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public TransactionSignature getSignature(){
        return signature;
    }

    @Override
    public void setSignature(TransactionSignature signature){
        this.signature = signature;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MoneyTransferTransactionInput[] getInputs() {
        return inputs;
    }

    public void setInputs(MoneyTransferTransactionInput[] inputs) {
        this.inputs = inputs;
    }

    public MoneyTransferTransactionOutput[] getOutputs() {
        return outputs;
    }

    public void setOutputs(MoneyTransferTransactionOutput[] outputs) {
        this.outputs = outputs;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "type='" + type + '\'' +
                ", _hash='" + _hash + '\'' +
                ", data='" + data + '\'' +
                ", signature=" + signature +
                ", timestamp=" + timestamp +
                ", inputs=" + Arrays.toString(inputs) +
                ", outputs=" + Arrays.toString(outputs) +
                '}';
    }
}
