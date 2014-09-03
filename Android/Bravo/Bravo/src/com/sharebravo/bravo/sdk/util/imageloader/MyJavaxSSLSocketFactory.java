package com.sharebravo.bravo.sdk.util.imageloader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * MySSLSocketFactory class supports making https connections
 * 
 * @author Visva
 */
public class MyJavaxSSLSocketFactory extends SSLSocketFactory {
	SSLContext sslContext = SSLContext.getInstance("TLS");

	/**
	 * Constructor
	 * 
	 * @param truststore
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 */
	public MyJavaxSSLSocketFactory(KeyStore truststore)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {

		TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.http.conn.ssl.SSLSocketFactory#createSocket(java.net.Socket,
	 * java.lang.String, int, boolean)
	 */
	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.conn.ssl.SSLSocketFactory#createSocket()
	 */
	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}

    @Override
    public String[] getDefaultCipherSuites() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getSupportedCipherSuites() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
}