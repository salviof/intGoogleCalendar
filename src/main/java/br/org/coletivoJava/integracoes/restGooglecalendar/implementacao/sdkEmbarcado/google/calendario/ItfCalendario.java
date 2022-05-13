/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restGooglecalendar.implementacao.sdkEmbarcado.google.calendario;

import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.atividadades.ItfAtividadeProgramada;
import java.util.List;

/**
 *
 * @author SalvioF
 */
public interface ItfCalendario {

    public boolean isAutorizado();

    public void autorizar();

    public void registrarAtividades(List<ItfAtividadeProgramada> pAtividade);

}
