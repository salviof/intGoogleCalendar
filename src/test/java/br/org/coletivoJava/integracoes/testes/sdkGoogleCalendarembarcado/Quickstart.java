/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.testes.sdkGoogleCalendarembarcado;

import br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.google.calendario.AutorizacaoGoogleOauthReceptor;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author SalvioF
 */
public class Quickstart {

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME
            = "Google Calendar API Java Quickstart";
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
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES
            = Arrays.asList(CalendarScopes.CALENDAR_READONLY);
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            String nomeArquivo = "calendario.json";
            String caminhoArquivo = "/home/superBits/testes" + "/";

            DATA_STORE_FACTORY = new FileDataStoreFactory(new File(caminhoArquivo));
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in
                = Quickstart.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets
                = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        clientSecrets.getDetails();

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        //  Credential credential = new AutorizacaoGoogleOauthReceptor(
        //         flow, new LocalServerReceiver()).getCredencialAguardandoResposta("user");
        System.out.println(
                "Credentials savas no arquivo ");
        return null;
        //return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
            getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
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
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }

}
