/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.google.calendario;

import br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.authGoogle.VerificationCodeReceiver;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import java.io.IOException;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author SalvioF
 */
public class AutorizacaoGoogleOauth {

    public AuthorizationCodeRequestUrl urlSolicitacaoAutorizacao;

    /**
     * Authorization code flow.
     */
    private final AuthorizationCodeFlow fluxo;

    /**
     * Verification code receiver.
     */
    private final AutorizacaoGoogleOauthReceptor receiver;

    public AutorizacaoGoogleOauth(AuthorizationCodeFlow pFlow, VerificationCodeReceiver pReceiver) {
        fluxo = pFlow;
        receiver = (AutorizacaoGoogleOauthReceptor) pReceiver;
    }

    public AuthorizationCodeRequestUrl getUrlSolicitacaoAutorizacao() {
        try {
            if (urlSolicitacaoAutorizacao == null) {
                String redirectUri = receiver.getRedirectUri();
                urlSolicitacaoAutorizacao = fluxo.newAuthorizationUrl().setRedirectUri(redirectUri);
            }
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro gerando url de solicitação de autorização", t);
        }
        return urlSolicitacaoAutorizacao;
    }

    public Credential getCredencialArmazenada(String userId) {
        try {
            Credential credential = fluxo.loadCredential(userId);
            if (credential != null
                    && (credential.getRefreshToken() != null
                    || credential.getExpiresInSeconds() == null
                    || credential.getExpiresInSeconds() > 60)) {
                return credential;
            }
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

    public Credential gerarCredencialComEsteToken(String token, String pCodigoUsuario) {
        try {
            Credential credArm = getCredencialArmazenada(pCodigoUsuario);
            if (credArm != null) {
                return credArm;
            }

            TokenResponse response = fluxo.newTokenRequest(token).setRedirectUri(receiver.getRedirectUri()).execute();
            // store credential and return it
            return fluxo.createAndStoreCredential(response, pCodigoUsuario);
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro gerando credencial", t);
            return null;
        }
    }

    public Credential getCredencialAguardandoResposta(String userId) throws IOException {
        try {
            Credential credencialar = getCredencialArmazenada(userId);
            if (credencialar != null) {
                return credencialar;
            }
            // open in browser
            //  onAuthorization(authorizationUrl);
            // receive authorization code and exchange it for an access token
            String code = receiver.waitForCode();
            TokenResponse response = fluxo.newTokenRequest(code).setRedirectUri(receiver.getRedirectUri()).execute();
            // store credential and return it
            return fluxo.createAndStoreCredential(response, userId);
        } finally {
            receiver.stop();
        }
    }

}
