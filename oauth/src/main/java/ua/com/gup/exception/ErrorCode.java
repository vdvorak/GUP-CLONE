package ua.com.gup.exception;

public interface ErrorCode {

    /**
     *
     * @return
     */
	int getErrorCode();

    /**
     *
     * @return
     */
	String getName();

    /**
     *
     * @return
     */
	int getServiceId();

    /**
     *
     * @return
     */
	String getMessageKey();

    /**
     *
     * @return
     */
	String getDefaultMessage();

}
