package com.yahoo.imapnio.command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import com.sun.mail.iap.Protocol;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.util.MailLogger;

/**
 * Class used to serialize IMAP commands. kraman
 */
public class ProxyProtocol extends Protocol {
    /** The OutputStream used by ProxyProtocol. */
    private OutputStream outputStream;

    /**
     * Creates a ProxyProtocol object.
     *
     * @throws IOException
     *             on failure
     */
    protected ProxyProtocol() throws IOException {
        super(null, null, new Properties(), false);

        outputStream = new OutputStreamProxy();
    }

    /**
     * Close.
     *
     * @throws IOException
     *             on I/O failure
     */
    public void close() throws IOException {
        outputStream.close();
        outputStream = null;
    }

    /**
     * We wrap our internal outputStream as a DataOutputStream and return that for Argument to write to. The Protocol interface is really bad: this
     * actually has to be a DataOutputStream, not an OutputStream, thus the wrapping.
     *
     * @return the OutputStream
     */
    @Override
    protected OutputStream getOutputStream() {
        return new DataOutputStream(outputStream);
    }

    @Override
    public String toString() {
        return outputStream.toString();
    }

    @Override
    protected synchronized boolean supportsNonSyncLiterals() {
        return true;
    }
}

/**
 * Proxy class for the OutputStream.
 *
 * @author kraman
 *
 */
class OutputStreamProxy extends OutputStream {
    /** Serialized string. */
    private String result = "";

    @Override
    public void write(final int b) {
        result = result.concat(String.valueOf((char) b));
    }

    @Override
    public String toString() {
        return result;
    }
}
