package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao;

import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.AcaoApiIntegracaoHeaderBuilder;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.transmissao_recepcao_rest_client.ItfAcaoApiRest;

public class IntegracaoRestGooglecalendar_HeaderPadrao
        extends
        AcaoApiIntegracaoHeaderBuilder {

    public IntegracaoRestGooglecalendar_HeaderPadrao(final ItfAcaoApiRest pAcao) {
        super(pAcao);
    }

}
