/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.googleCalendarIntegracao.api;

import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.ItfFabricaIntegracaoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.FabTipoConexaoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.InfoConsumoRestService;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.servicoRegistrado.FabTipoAutenticacaoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.servicoRegistrado.InfoConfigRestClientIntegracao;

import com.super_bits.modulosSB.SBCore.modulos.ManipulaArquivo.importacao.FabTipoArquivoImportacao;

/**
 *
 * @author SalvioF
 */
@InfoConfigRestClientIntegracao(configuracao = FabConfigModuloGoogleCalendario.class,
        enderecosDocumentacao = "https://developer.mautic.org/#rest-api",
        nomeIntegracao = "googleCalendar",
        tipoAutenticacao = FabTipoAutenticacaoRest.OAUTHV2
)
public enum FabIntGoogleCalendar implements ItfFabricaIntegracaoRest {

    @InfoConsumoRestService(getPachServico = "/api/companies/{0}/contact/{1}/add",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            adicionarAutenticacaoBearer = true,
            tipoConexao = FabTipoConexaoRest.POST,
            parametrosPost = {"id", "email", "firstname", "lastname", "mobile", "tags"}
    )
    AGENDAR_ATIVIDADES;

}
