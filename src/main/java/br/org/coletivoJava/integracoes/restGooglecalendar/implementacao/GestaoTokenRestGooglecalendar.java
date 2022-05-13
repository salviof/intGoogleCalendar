package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao;

import br.org.coletivoJava.integracoes.restGooglecalendar.api.InfoIntegracaoRestGooglecalendarCalendar;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.super_bits.Super_Bits.googleCalendarIntegracao.api.FabConfigModuloGoogleCalendario;
import com.super_bits.Super_Bits.googleCalendarIntegracao.api.FabIntGoogleCalendar;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.ConfigGeral.arquivosConfiguracao.ConfigModulo;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreDataHora;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.oauth.FabStatusToken;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.oauth.InfoTokenOauth2;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.gestaoToken.GestaoTokenOath2;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.token.ItfTokenDeAcessoExterno;
import org.json.simple.JSONObject;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.FabTipoAgenteClienteApi;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.ChamadaHttpSimples;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.UtilSBApiRestClientOauth2;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfUsuario;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

@InfoIntegracaoRestGooglecalendarCalendar(tipo = FabIntGoogleCalendar.AGENDAR_ATIVIDADES)
public class GestaoTokenRestGooglecalendar extends GestaoTokenOath2 {

    protected final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    protected static HttpTransport HTTP_TRANSPORT;
    protected final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);
    private final ConfigModulo configuracao;
    private GoogleAuthorizationCodeFlow flow = null;

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    static {

    }

    private GoogleAuthorizationCodeFlow gerarFluxoOauth() {
        try {
            JSONObject layoutJsonConfiguracaoPadraoGoogleJson = new JSONObject();
            layoutJsonConfiguracaoPadraoGoogleJson.put("web", new JSONObject());
            JSONObject webconfig = (JSONObject) layoutJsonConfiguracaoPadraoGoogleJson.get("web");
            webconfig.put("project_id", getConfig().getPropriedade(FabConfigModuloGoogleCalendario.PROJECT_ID));
            webconfig.put("client_id", getConfig().getPropriedade(FabConfigModuloGoogleCalendario.CLIENTE_ID));
            webconfig.put("client_secret", getConfig().getPropriedade(FabConfigModuloGoogleCalendario.CLIENT_SECRET));
            webconfig.put("auth_uri", getConfig().getPropriedade(FabConfigModuloGoogleCalendario.AUTH_URI));
            webconfig.put("token_uri", getConfig().getPropriedade(FabConfigModuloGoogleCalendario.TOKEN_URI));
            webconfig.put("auth_provider_x509_cert_url", getConfig().getPropriedade(FabConfigModuloGoogleCalendario.AUTH_PROVIDER_X509_CERT_URL));
            String conteudo = layoutJsonConfiguracaoPadraoGoogleJson.toString();
            System.out.println("CONTEUDO CRIADO");
            System.out.println(conteudo);
            //       conteudo = UTilSBCoreInputs.getStringByArquivoLocal("/home/sfurbino/Downloads/client_secret_838575863048-1j01tps0jrv949hjrvteo47s445ekmvr.apps.googleusercontent.com.json");
            //   System.out.println(conte);
            GoogleClientSecrets clientSecrets
                    = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new ByteArrayInputStream(conteudo.getBytes())));

            System.out.println(JSON_FACTORY.getClass().getSimpleName());
            // Build flow and trigger user authorization request.
            flow
                    = new GoogleAuthorizationCodeFlow.Builder(
                            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(DATA_STORE_FACTORY)
                            .setAccessType("offline")
                            .build();

            return flow;
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public boolean validarToken() {
        return false;
    }

    @Override
    public ItfTokenDeAcessoExterno extrairToken(JSONObject jsonObject) {
        return null;
    }

    public GestaoTokenRestGooglecalendar(
            final FabTipoAgenteClienteApi pTipoAgente,
            final ItfUsuario pUsuario) {
        super(FabIntGoogleCalendar.class, pTipoAgente, pUsuario);
        configuracao = SBCore.getConfigModulo(FabConfigModuloGoogleCalendario.class);
        if (HTTP_TRANSPORT == null) {
            try {
                HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            } catch (GeneralSecurityException ex) {
                Logger.getLogger(GestaoTokenRestGooglecalendar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GestaoTokenRestGooglecalendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String pastaRepositorio = SBCore.getCentralDeArquivos().getEndrLocalResources() + "/" + getClass().getSimpleName();
        String caminhoArquivo = pastaRepositorio + "/chavesDeAcesso";
        if (!new File(pastaRepositorio).exists()) {
            new File(pastaRepositorio).mkdirs();
        }

        if (DATA_STORE_FACTORY == null) {

            try {
                DATA_STORE_FACTORY = new FileDataStoreFactory(new File(caminhoArquivo));
            } catch (IOException ex) {
                Logger.getLogger(GestaoTokenRestGooglecalendar.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        flow = gerarFluxoOauth();
        loadDadosIniciais();
        loadTokenArmazenado();
    }

    @Override
    protected final void loadDadosIniciais() {
        super.loadDadosIniciais(); //To change body of generated methods, choose Tools | Templates.
    }

    public ItfTokenDeAcessoExterno extrairToken(Credential credencial) {

        InfoTokenOauth2 token = new InfoTokenOauth2(credencial.getAccessToken());
        Long segundosExpiracao = credencial.getExpiresInSeconds();
        token.setDataHoraExpirarToken(UtilSBCoreDataHora.incrementaSegundos(new Date(), segundosExpiracao.intValue() - 10));
        token.setTokenRefresh(credencial.getRefreshToken());
        return token;

    }

    @Override
    public final ItfTokenDeAcessoExterno loadTokenArmazenado() {

        try {
            if (flow == null) {
                return null;
            }
            Credential credential = flow.loadCredential(SBCore.getUsuarioLogado().getEmail());
            if (credential != null
                    && (credential.getRefreshToken() != null
                    || credential.getExpiresInSeconds() == null
                    || credential.getExpiresInSeconds() > 60)) {
                setToken(extrairToken(credential));
                return getTokenCompleto();
            }
            return null;
        } catch (Throwable t) {
            return null;
        }

    }

    @Override
    public InfoTokenOauth2 gerarNovoToken() {

        try {
            if (getStatusToken().equals(FabStatusToken.ATIVO)) {
                return getTokenCompleto();
            }
            if (flow == null) {
                throw new UnsupportedOperationException("As configurações iniciais não foram definidas, impossível gerir os tokens com " + this.getClass().getSimpleName());
            }
            if (codigoSolicitacao == null) {
                SBCore.enviarAvisoAoUsuario("O código de solicitação não foi encontrado");
                if (SBCore.isEmModoDesenvolvimento()) {
                    UtilSBApiRestClientOauth2.solicitarAutenticacaoExterna(this);

                    Thread.sleep(20000);
                }
            }

            if (codigoSolicitacao == null) {
                throw new UnsupportedOperationException("O código de solicitação não foi recebido");
            } else {

                String sitelcliente = getSiteCliente();
                TokenResponse response = flow.newTokenRequest(codigoSolicitacao).setRedirectUri(getUrlRetornoReceberCodigoSolicitacao()).execute();

                Credential credencial = flow.createAndStoreCredential(response, SBCore.getUsuarioLogado().getEmail());

                return (InfoTokenOauth2) extrairToken(credencial);

                // store credential and return it
            }
        } catch (TokenResponseException t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro gerando token de acesso" + t.getMessage(), t);

            return null;
        } catch (Throwable ex) {
            Logger.getLogger(GestaoTokenRestGooglecalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    protected ChamadaHttpSimples gerarChamadaTokenObterChaveAcesso() {

        if (codigoSolicitacao != null) {

            GoogleAuthorizationCodeTokenRequest token = flow.newTokenRequest(codigoSolicitacao);
            try {
                GoogleTokenResponse resposta = token.execute();
            } catch (IOException ex) {
                Logger.getLogger(GestaoTokenRestGooglecalendar.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return new ChamadaHttpSimples();
    }

    @Override
    protected String gerarUrlServicoReceberCodigoSolicitacao() {
        return super.gerarUrlServicoReceberCodigoSolicitacao(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String gerarUrlRetornoSucessoGeracaoTokenDeAcesso() {

        String urlServicoReceberCodigiodeAcesso = getUrlServidorApiRest() + "/conexao/googleCalendar/sucesso.wp";
        return urlServicoReceberCodigiodeAcesso;
    }

    @Override
    protected String gerarUrlAutenticaoObterCodigoSolicitacaoToken() {
        if (flow == null) {
            return "Aguardando configuração para determinar url de retorno";
        } else {
            String urlReceberCodigoSolicitao = getUrlRetornoReceberCodigoSolicitacao();
            GoogleAuthorizationCodeRequestUrl googleUrlCodeREquisicao = flow.newAuthorizationUrl();
            googleUrlCodeREquisicao.setRedirectUri(urlReceberCodigoSolicitao);
            String urlAutenticacao = googleUrlCodeREquisicao.build();
            return urlAutenticacao;
        }
    }

    public GoogleAuthorizationCodeFlow getFlow() {
        return flow;
    }

    @Override
    public void setCodigoSolicitacao(String pCodigoSolicitacao) {
        codigoSolicitacao = pCodigoSolicitacao;

    }

}
