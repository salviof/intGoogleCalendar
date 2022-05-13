package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao;

import br.org.coletivoJava.integracoes.restGooglecalendar.api.InfoIntegracaoRestGooglecalendarCalendar;
import com.super_bits.Super_Bits.googleCalendarIntegracao.api.FabIntGoogleCalendar;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.AcaoApiIntegracaoComOauthAbstrato;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.FabTipoAgenteClienteApi;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.AcaoApiIntegracaoSDKEmbarcado;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfResposta;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfUsuario;

@InfoIntegracaoRestGooglecalendarCalendar(tipo = FabIntGoogleCalendar.AGENDAR_ATIVIDADES)
public class IntegracaoRestGooglecalendarAgendarAtividades
        extends
        AcaoApiIntegracaoSDKEmbarcado {

    public IntegracaoRestGooglecalendarAgendarAtividades(
            final FabTipoAgenteClienteApi pTipoAgente,
            final ItfUsuario pUsuario, final java.lang.Object... pParametro) {
        super(FabIntGoogleCalendar.AGENDAR_ATIVIDADES, pTipoAgente, pUsuario,
                pParametro);
    }

    @Override
    protected void executarAcao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ItfResposta getResposta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
