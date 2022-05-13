/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.google.calendario;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Semaphore;

/**
 *
 * @author SalvioF
 */
public class AutorizacaoGoogleOauthReceptor implements ItfReceptorAutorizacaoGoogle {

    /**
     * Port to use or {@code -1} to select an unused port in
     * {@link #getRedirectUri()}.
     */
    private final int porta;

    private boolean tokenFoiInformado = false;

    private final String urlColetaCodigoAcesso;
    /**
     * Host name to use.
     */
    private final String host;

    /**
     * Callback path of redirect_uri
     */
    private final String caminhoPaginaColeta;

    /**
     * Verification code or {@code null} for none.
     */
    String code;

    /**
     * Error code or {@code null} for none.
     */
    String error;

    /**
     * To block until receiving an authorization response or stop() is called.
     */
    final Semaphore waitUnlessSignaled = new Semaphore(0 /* initially zero permit */);

    /**
     * URL to an HTML page to be shown (via redirect) after successful login. If
     * null, a canned default landing page will be shown (via direct response).
     */
    private String successLandingPageUrl;

    /**
     * URL to an HTML page to be shown (via redirect) after failed login. If
     * null, a canned default landing page will be shown (via direct response).
     */
    private String failureLandingPageUrl;

    public AutorizacaoGoogleOauthReceptor(String pUrlColetaCOdigo,
            String successLandingPageUrl, String failureLandingPageUrl) {
        URL urlColeta = null;
        try {
            urlColeta = new URL(pUrlColetaCOdigo);
            String host = urlColeta.getHost();
            int porta = urlColeta.getPort();
            String caminho = urlColeta.getPath();
        } catch (Throwable t) {

        }
        host = urlColeta.getHost();
        porta = urlColeta.getPort();
        caminhoPaginaColeta = urlColeta.getPath();
        this.successLandingPageUrl = successLandingPageUrl;
        this.failureLandingPageUrl = failureLandingPageUrl;
        urlColetaCodigoAcesso = pUrlColetaCOdigo;

    }

    @Override
    public String getRedirectUri() throws IOException {
        //server = new Server(port != -1 ? port : 0);
        //Connector connector = server.getConnectors()[0];
        //connector.setHost(host);

        //server.addHandler(new CallbackHandler());
        //try {
        //    server.start();
        //    port = connector.getLocalPort();
        //} catch (Exception e) {
        //    Throwables.propagateIfPossible(e);
        //    throw new IOException(e);
        //}
        if (porta > 0) {
            return "http://" + host + ":" + porta + caminhoPaginaColeta;
        } else {
            return "https://" + host + "/" + caminhoPaginaColeta;
        }
    }

    /**
     * Blocks until the server receives a login result, or the server is stopped
     * by {@link #stop()}, to return an authorization code.
     *
     * @return authorization code if login succeeds; may return {@code null} if
     * the server is stopped by {@link #stop()}
     * @throws IOException if the server receives an error code (through an HTTP
     * request parameter {@code error})
     */
    @Override
    public String waitForCode() throws IOException {
        waitUnlessSignaled.acquireUninterruptibly();
        if (error != null) {
            throw new IOException("User authorization failed (" + error + ")");
        }
        return code;
    }

    @Override
    public void stop() throws IOException {
        waitUnlessSignaled.release();

    }

    @Override
    public void InformarTokenAutorizacao(String pCodigo) {

        code = pCodigo;
        waitUnlessSignaled.release();
        tokenFoiInformado = true;

    }

    @Override
    public void desistir() {

        waitUnlessSignaled.release();
    }

    public boolean isTokenFoiInformado() {
        return tokenFoiInformado;
    }

}
