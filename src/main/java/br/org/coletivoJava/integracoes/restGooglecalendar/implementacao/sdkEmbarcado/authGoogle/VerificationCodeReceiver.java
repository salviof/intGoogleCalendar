/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.authGoogle;

import java.io.IOException;

/**
 *
 * @author SalvioF
 */
public interface VerificationCodeReceiver {

    /**
     * Returns the redirect URI.
     */
    String getRedirectUri() throws IOException;

    /**
     * Waits for a verification code.
     */
    String waitForCode() throws IOException;

    /**
     * Releases any resources and stops any processes started.
     */
    void stop() throws IOException;
}
