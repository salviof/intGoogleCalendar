/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.operacao.calendarioGoogle;

import br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.google.calendario.ApiCalendarioGoogle;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreDataHora;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.atividadades.ItfAtividadeProgramada;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author SalvioF
 */
public class ApiCalendarioGoogleAtvidadesCRM extends ApiCalendarioGoogle {

    public ApiCalendarioGoogleAtvidadesCRM(String pUrlSucesso) {
        super(pUrlSucesso);
    }

    @Override
    public void registrarAtividades(List<ItfAtividadeProgramada> pAtividades) {
        try {

            com.google.api.services.calendar.Calendar service
                    = getCalendarService();
            int quantiddeAgenda = 0;
            for (ItfAtividadeProgramada atividade : pAtividades) {

                String codigoAtividade = String.valueOf(Math.abs(atividade.getTitulo().hashCode()));
                Calendar.Events.Get eventoExistente = getCalendarService().events().get("primary", codigoAtividade);
                boolean existe = true;
                try {
                    Event evento = eventoExistente.execute();
                } catch (Throwable t) {
                    existe = false;
                }
                if (!existe) {
                    Event novoEvento = new Event();
                    novoEvento.setId(codigoAtividade);

                    novoEvento.setSummary(atividade.getTitulo());
                    TimeZone zonaTemporal = TimeZone.getTimeZone("America/Sao_Paulo");
                    Date datacorrigida = UtilSBCoreDataHora.incrementaHoras(atividade.getDataHoraPrevisaoExecucao(), 0);
                    novoEvento.setStart(new EventDateTime().setDateTime(new DateTime(datacorrigida, zonaTemporal)));
                    novoEvento.setEnd(new EventDateTime().setDateTime(new DateTime(UtilSBCoreDataHora.incrementaMinutos(datacorrigida, 10), zonaTemporal)));

                    String linkAtividade = atividade.getUrlAtividade();

                    novoEvento.setHtmlLink(linkAtividade);
                    novoEvento.setDescription(atividade.getDescritivo() + "\n" + linkAtividade);
                    Calendar.Events.Insert resposta = service.events().insert("primary", novoEvento);
                    System.out.println(resposta.getUserIp());
                    Event eventogerado = resposta.execute();
                    System.out.println(eventogerado);
                    quantiddeAgenda++;
                }

            }
            SBCore.enviarAvisoAoUsuario(quantiddeAgenda + " Eventos foram gravados em sua agenda.");
        } catch (IOException t) {
            SBCore.enviarAvisoAoUsuario("Erro adicionando atividades no servidor" + t.getMessage());
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Obtendo serviço do google Calendar ", t);
        } catch (Throwable t) {
            SBCore.enviarAvisoAoUsuario("Erro adicionando atividades no servidor" + t.getMessage());
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Obtendo serviço do google Calendar ", t);
        }

    }

}
