package ua.com.itproekt.gup.service.blockchain.contract;

import okhttp3.Response;
import ua.com.itproekt.gup.util.FileKeyGenerator;

import java.io.IOException;


public interface TransactionService {

    /**
     *
     * @return
     * @throws IOException
     */
    Response postTransaction() throws IOException;

    /**
     *
     * @return
     */
    String getHash();

    /**
     *
     * @return
     */
    FileKeyGenerator getKeyPair();

}
