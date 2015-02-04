package com.gp.updater.utils;

import android.content.Context;

import com.gp.updater.R;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import java.io.InputStream;
import java.security.KeyStore;

/**
 * Created by dragorn on 5/20/14.
 */
public class BlackphoneHttpClient extends DefaultHttpClient {
    final Context context;

    public BlackphoneHttpClient(Context c) {
        context = c;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();

        /* We don't allow http at all
        registry.register(
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                */

        registry.register(new Scheme("https", BlackphoneSslSocketFactory(), 443));
        return new ThreadSafeClientConnManager(getParams(), registry);
    }

    private SSLSocketFactory BlackphoneSslSocketFactory() {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            InputStream in = context.getResources().openRawResource(R.raw.blackphone);
            try {
                trusted.load(in, "blackphone".toCharArray());
            } finally {
                in.close();
            }
            return new SSLSocketFactory(trusted);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
