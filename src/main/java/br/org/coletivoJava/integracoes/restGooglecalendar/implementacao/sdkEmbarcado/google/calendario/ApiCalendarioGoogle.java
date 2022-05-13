/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.google.calendario;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.FabMensagens;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.atividadades.ItfAtividadeProgramada;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author SalvioF
 */
public class ApiCalendarioGoogle extends ApiCalendarioAbstrato implements ItfCalendario {

    private GoogleAuthorizationCodeFlow fluxoAutorizacao;
    private AutorizacaoGoogleOauthReceptor receptorCodigo;
    private AutorizacaoGoogleOauth autorizador;
    private String urlRetornoCodigo;
    private Map<String, Credential> credenciais = new HashMap<>();

    private enum ETAPA_CREDENCIAL {
        SOLICITAR_AUTORIZACAO, CRIAR_NOVA_SOLICITACAO
    }

    public Credential getCredencial() {
        if (credenciais.containsKey(SBCore.getUsuarioLogado().getEmail())) {

            return credenciais.get(SBCore.getUsuarioLogado().getEmail());
        }
        Credential cred = autorizador.getCredencialArmazenada(SBCore.getUsuarioLogado().getEmail());
        if (cred != null) {
            credenciais.put(SBCore.getUsuarioLogado().getEmail(), cred);
            return cred;
        }
        return null;
    }

    public void limparFormularioDeAutenticacao() {
        if (receptorCodigo != null) {
            try {
                receptorCodigo.stop();
            } catch (Throwable t) {

            }
        }
        autorizador = null;
        receptorCodigo = null;
        fluxoAutorizacao = null;
    }

    public final void renovarFormularioDeAutenticacao() {
        limparFormularioDeAutenticacao();
        try {

            // Load client secrets.
            InputStream in = ApiCalendarioGoogle.class
                    .getResourceAsStream("/client_secret.json");
            GoogleClientSecrets clientSecrets
                    = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            fluxoAutorizacao = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(ARMAZENAMENTO_CREDENCIAIS)
                    .setAccessType("offline")
                    .build();

            receptorCodigo = new AutorizacaoGoogleOauthReceptor(urlRetornoCodigo, "", "");
            autorizador = new AutorizacaoGoogleOauth(fluxoAutorizacao, receptorCodigo);
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro criando Api Calendário google", t);
        }
    }

    /**
     * Application name.
     *
     * @param pUrlRetornoCodigo
     */
    public ApiCalendarioGoogle(String pUrlRetornoCodigo) {
        super();
        urlRetornoCodigo = pUrlRetornoCodigo;

        renovarFormularioDeAutenticacao();

    }

    public ApiCalendarioGoogle() {

        this("http://localhost:8080/EspecifiqueUmaUrlParaColetaDoCodigoDeAutenticacao");

    }

    public String getUrlAutenticacao() {
        if (isAutorizado()) {
            return null;
        }
        return autorizador.getUrlSolicitacaoAutorizacao().toString();
    }

    public boolean autorizar(HttpServletRequest respostaOAuth) {
        if (isAutorizado()) {
            return true;
        }
        String error = respostaOAuth.getParameter("error");
        String codigo = respostaOAuth.getParameter("code");
        if (codigo != null) {
            informarTokenAutorizacao(codigo);
        }
        if (error != null) {
            return false;
        } else {
            try {
                receptorCodigo.stop();
            } catch (IOException t) {
                return false;
            }
        }
        return false;
    }

    public void informarTokenAutorizacao(String codigo) {
        if (!isAutorizado()) {
            receptorCodigo.InformarTokenAutorizacao(codigo);
            Credential credencial = autorizador.gerarCredencialComEsteToken(codigo, SBCore.getUsuarioLogado().getEmail());
            if (credencial != null) {
                credenciais.put(SBCore.getUsuarioLogado().getEmail(), credencial);
            }
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential obterCredencialAguardandoToken() throws IOException {
        if (isAutorizado()) {
            return getCredencial();
        }
        return autorizador.getCredencialAguardandoResposta(SBCore.getUsuarioLogado().getEmail());
    }

    public String gerarNovaUrlAutorizacao() {
        try {
            if (isAutorizado()) {
                return null;
            }
            String redirectUri = receptorCodigo.getRedirectUri();
            AuthorizationCodeRequestUrl authorizationUrl = fluxoAutorizacao.newAuthorizationUrl().setRedirectUri(redirectUri);
            return authorizationUrl.toString();
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     * @throws IOException
     */
    protected com.google.api.services.calendar.Calendar
            getCalendarService() throws IOException {
        if (isAutorizado()) {
            return new com.google.api.services.calendar.Calendar.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, getCredencial())
                    .setApplicationName(nomeApp)
                    .build();
        }
        SBCore.enviarMensagemUsuario("Vocè precisa autenticar para acessar o calendário do google", FabMensagens.AVISO);
        return null;
    }

    @Override
    public boolean isAutorizado() {
        return getCredencial() != null;
    }

    @Override
    public void autorizar() {
        try {
            if (!isAutorizado()) {
                obterCredencialAguardandoToken();
            }
        } catch (Throwable t) {

        }
    }

    @Override
    public void registrarAtividades(List<ItfAtividadeProgramada> pAtividades) {
        try {

            com.google.api.services.calendar.Calendar service
                    = getCalendarService();

            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = service.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();
            if (items.size() == 0) {
                System.out.println("No upcoming events found.");
            } else {
                System.out.println("Upcoming events");
                for (Event event : items) {
                    DateTime dataHoraInicio = event.getStart().getDateTime();
                    if (dataHoraInicio == null) {
                        dataHoraInicio = event.getStart().getDate();
                    }
                    System.out.printf("%s (%s)\n", event.getSummary(), dataHoraInicio);
                }
            }
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Obtendo serviço do google Calendar ", t);
        }

    }

}
