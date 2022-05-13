/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao;

import com.super_bits.Super_Bits.googleCalendarIntegracao.api.FabIntGoogleCalendar;
import com.super_bits.Super_Bits.mktMauticIntegracao.configAppp.ConfiguradorCoreTestesGoogleCalendar;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import org.junit.Test;
import testes.testesSupers.ServicoRecepcaoOauthTestes;

/**
 *
 * @author sfurbino
 */
public class GestaoTokenRestGooglecalendarTest {

    /**
     * Test of validarToken method, of class GestaoTokenRestGooglecalendar.
     */
    @Test
    public void testValidarToken() {

        SBCore.configurar(new ConfiguradorCoreTestesGoogleCalendar(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
        GestaoTokenRestGooglecalendar gestao = (GestaoTokenRestGooglecalendar) FabIntGoogleCalendar.AGENDAR_ATIVIDADES.getGestaoToken();
        ServicoRecepcaoOauthTestes.iniciarServico();
        gestao.gerarNovoToken();
        gestao.setCodigoSolicitacao(null);
        gestao.gerarNovoToken();
        gestao.setCodigoSolicitacao(null);
        gestao.gerarNovoToken();
        gestao.gerarChamadaTokenObterChaveAcesso();

    }

}
