/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.googleCalendarIntegracao.api;

import com.super_bits.modulosSB.SBCore.ConfigGeral.arquivosConfiguracao.ItfFabConfigModulo;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.tipoModulos.integracaoOauth.FabPropriedadeModuloIntegracaoOauth;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.tipoModulos.integracaoOauth.InfoPropriedadeConfigRestIntegracao;

/**
 *
 * @author SalvioF
 */
public enum FabConfigModuloGoogleCalendario implements ItfFabConfigModulo {

    @InfoPropriedadeConfigRestIntegracao(tipoPropriedade = FabPropriedadeModuloIntegracaoOauth.URL_SERVIDOR_API_RECEPCAO_TOKEN_OAUTH)
    URL_SERVIDOR_API_RECEPCAO_TOKEN_OAUTH,
    @InfoPropriedadeConfigRestIntegracao(tipoPropriedade = FabPropriedadeModuloIntegracaoOauth.CHAVE_PUBLICA)
    CLIENTE_ID,
    @InfoPropriedadeConfigRestIntegracao(tipoPropriedade = FabPropriedadeModuloIntegracaoOauth.CHAVE_PRIVADA)
    CLIENT_SECRET,
    PROJECT_ID,
    @InfoPropriedadeConfigRestIntegracao(tipoPropriedade = FabPropriedadeModuloIntegracaoOauth.URL_SERVIDOR_API)
    AUTH_URI,
    TOKEN_URI,
    AUTH_PROVIDER_X509_CERT_URL;

    @Override
    public String getValorPadrao() {

        switch (this) {
            case URL_SERVIDOR_API_RECEPCAO_TOKEN_OAUTH:
                return "https://meuservidor.com.br";

            case CLIENTE_ID:

            case CLIENT_SECRET:
                return this.toString().concat(" não definido");

            case PROJECT_ID:
                return "nomedoSeuprojeto-codigo";

            case AUTH_URI:
                return "https://accounts.google.com/o/oauth2/auth";

            case TOKEN_URI:
                return "https://accounts.google.com/o/oauth2/token";

            case AUTH_PROVIDER_X509_CERT_URL:
                return "https://www.googleapis.com/oauth2/v1/certs";

            default:
                throw new AssertionError(this.name());

        }

    }

}
