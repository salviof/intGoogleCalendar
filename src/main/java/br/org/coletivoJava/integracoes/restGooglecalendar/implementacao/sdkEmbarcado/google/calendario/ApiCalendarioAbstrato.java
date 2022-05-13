/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.google.calendario;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author SalvioF
 */
public abstract class ApiCalendarioAbstrato {

// 838575863048-1j01tps0jrv949hjrvteo47s445ekmvr.apps.googleusercontent.com
    // 2y6lgtHNnYD5H5GZmHSoMfLr
    /**
     * Directory to store user credentials for this application.
     */
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    /**
     * Global instance of the JSON factory.
     */
    protected final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    protected HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/calendar-java-quickstart
     */
    protected final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    protected FileDataStoreFactory ARMAZENAMENTO_CREDENCIAIS;

    protected String nomeApp = "Agenda Casa Nova";

    public ApiCalendarioAbstrato() {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            String caminhoArquivo = SBCore.getCentralDeArquivos().getEndrLocalResources() + "/autCalendarioGoogle";
            if (!new File(caminhoArquivo).exists()) {
                new File(caminhoArquivo).mkdirs();
            }
            ARMAZENAMENTO_CREDENCIAIS = new FileDataStoreFactory(new File(caminhoArquivo));
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

}
