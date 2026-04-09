package com.bluepointai.credito.modelo;

import java.io.Serializable;

/**
 * Data Object: Solicitante
 * Representa al cliente que solicita el crédito de consumo.
 * Importar en Business Central como Data Object.
 *
 * Bluepoint AI — Demo evaluación crédito de consumo
 */
public class Solicitante implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---- Datos personales ----

    /** Edad en años. Rango válido: 21-70. */
    private int edad;

    /** Antigüedad laboral en meses en el empleo actual. */
    private int antiguedadLaboral;

    /**
     * Tipo de empleo.
     * Valores válidos: DEPENDIENTE | INDEPENDIENTE | JUBILADO
     */
    private String tipoEmpleo;

    /** Ingreso mensual neto en USD (después de impuestos y deducciones). */
    private double ingresoMensualNeto;

    /**
     * Calificación del historial crediticio en buró.
     * Valores válidos: EXCELENTE | BUENO | REGULAR | MALO | SIN_HISTORIAL
     */
    private String historialCrediticio;

    /**
     * Score externo del buró crediticio (Equifax / DataCrédito).
     * Rango: 300 (muy bajo) – 850 (excelente).
     */
    private int scoreExterno;

    /**
     * Nivel de educación alcanzado.
     * Valores válidos: BASICA | MEDIA | SUPERIOR | POSTGRADO
     */
    private String nivelEducacion;

    /**
     * Estado civil del solicitante.
     * Valores válidos: SOLTERO | CASADO | DIVORCIADO | VIUDO | UNION_LIBRE
     */
    private String estadoCivil;

    /** Número de dependientes económicos a cargo del solicitante. */
    private int numeroDependientes;

    /** true si el solicitante es propietario de vivienda propia. */
    private boolean propietarioVivienda;

    // ---- Constructores ----

    public Solicitante() {
    }

    public Solicitante(int edad, int antiguedadLaboral, String tipoEmpleo,
                       double ingresoMensualNeto, String historialCrediticio,
                       int scoreExterno, String nivelEducacion, String estadoCivil,
                       int numeroDependientes, boolean propietarioVivienda) {
        this.edad = edad;
        this.antiguedadLaboral = antiguedadLaboral;
        this.tipoEmpleo = tipoEmpleo;
        this.ingresoMensualNeto = ingresoMensualNeto;
        this.historialCrediticio = historialCrediticio;
        this.scoreExterno = scoreExterno;
        this.nivelEducacion = nivelEducacion;
        this.estadoCivil = estadoCivil;
        this.numeroDependientes = numeroDependientes;
        this.propietarioVivienda = propietarioVivienda;
    }

    // ---- Getters y Setters ----

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public int getAntiguedadLaboral() { return antiguedadLaboral; }
    public void setAntiguedadLaboral(int antiguedadLaboral) { this.antiguedadLaboral = antiguedadLaboral; }

    public String getTipoEmpleo() { return tipoEmpleo; }
    public void setTipoEmpleo(String tipoEmpleo) { this.tipoEmpleo = tipoEmpleo; }

    public double getIngresoMensualNeto() { return ingresoMensualNeto; }
    public void setIngresoMensualNeto(double ingresoMensualNeto) { this.ingresoMensualNeto = ingresoMensualNeto; }

    public String getHistorialCrediticio() { return historialCrediticio; }
    public void setHistorialCrediticio(String historialCrediticio) { this.historialCrediticio = historialCrediticio; }

    public int getScoreExterno() { return scoreExterno; }
    public void setScoreExterno(int scoreExterno) { this.scoreExterno = scoreExterno; }

    public String getNivelEducacion() { return nivelEducacion; }
    public void setNivelEducacion(String nivelEducacion) { this.nivelEducacion = nivelEducacion; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public int getNumeroDependientes() { return numeroDependientes; }
    public void setNumeroDependientes(int numeroDependientes) { this.numeroDependientes = numeroDependientes; }

    public boolean isPropietarioVivienda() { return propietarioVivienda; }
    public void setPropietarioVivienda(boolean propietarioVivienda) { this.propietarioVivienda = propietarioVivienda; }

    @Override
    public String toString() {
        return "Solicitante{" +
               "edad=" + edad +
               ", tipoEmpleo='" + tipoEmpleo + '\'' +
               ", ingresoMensualNeto=" + ingresoMensualNeto +
               ", historialCrediticio='" + historialCrediticio + '\'' +
               ", scoreExterno=" + scoreExterno +
               '}';
    }
}
