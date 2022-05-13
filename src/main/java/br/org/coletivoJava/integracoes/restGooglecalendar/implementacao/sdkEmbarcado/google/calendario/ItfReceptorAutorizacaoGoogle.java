/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.google.calendario;

import br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.authGoogle.VerificationCodeReceiver;

/**
 *
 * @author SalvioF
 */
public interface ItfReceptorAutorizacaoGoogle extends VerificationCodeReceiver {

    public void InformarTokenAutorizacao(String pCodigo);

    public void desistir();

}
