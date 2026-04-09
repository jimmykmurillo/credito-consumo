package com.bluepointai.credito.resultado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Object: ResultadoEvaluacion
 * Objeto que Drools construye y modifica durante la evaluación.
 * Se inserta vacío en la KieSession al inicio y contiene
 * la decisión final al terminar fireAllRules().
 *
 * Estructura del resultado retornado al core bancario via KIE Server REST API.
 *
 * Bluepoint AI — Demo evaluación crédito de consumo
 */
public class ResultadoEvaluacion implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---- Decisión final ----

    /**
     * Decisión del motor.
     * Valores posibles: APROBADO | REVISION_HUMANA | RECHAZADO
     */
    private String decision;

    /**
     * Nivel de autonomía que tomó (o tomará) la decisión.
     * Valores: MOTOR_AUTOMATICO | OFICIAL_CREDITO | COMITE_CREDITO
     */
    private String nivelDecision;

    /**
     * true si la solicitud requiere intervención humana
     * (REVISION_HUMANA o campos de scoring borderline).
     */
    private boolean requiereRevisionHumana;

    // ---- Score total acumulado ----

    /** Score total final (suma de todos los componentes). Máximo teórico: 100. */
    private int scoreTotal;

    // ---- Componentes del score (para trazabilidad) ----

    /** Puntos del score externo de buró (0-40). */
    private int puntosScore;

    /** Puntos por relación cuota/ingreso (0-30). */
    private int puntosCapacidad;

    /** Puntos por historial crediticio (0-15). */
    private int puntosHistorial;

    /** Puntos por estabilidad laboral (0-8). */
    private int puntosEstabilidad;

    /** Puntos por garantía (0-6 de scorecard + 0-6 de solicitud). */
    private int puntosGarantia;

    /** Puntos por nivel de educación (0-3). */
    private int puntosEducacion;

    /** Puntos por destino del crédito (0-4). */
    private int puntosDestino;

    // ---- Mensajes ----

    /**
     * Lista de motivos de rechazo.
     * Solo se popula si decision = RECHAZADO.
     */
    private List<String> motivos;

    /**
     * Lista de detalles del scoring (para auditoría y transparencia).
     * Contiene una línea por cada componente evaluado.
     */
    private List<String> detalles;

    // ---- Constructor ----

    public ResultadoEvaluacion() {
        this.motivos = new ArrayList<>();
        this.detalles = new ArrayList<>();
        this.scoreTotal = 0;
        this.puntosScore = 0;
        this.puntosCapacidad = 0;
        this.puntosHistorial = 0;
        this.puntosEstabilidad = 0;
        this.puntosGarantia = 0;
        this.puntosEducacion = 0;
        this.puntosDestino = 0;
        this.requiereRevisionHumana = false;
    }

    // ---- Métodos helper para las reglas DRL ----

    /**
     * Agrega puntos del score externo y recalcula el total.
     * Llamado por las reglas de scoring cuantitativo.
     */
    public void agregarPuntosScore(int puntos) {
        this.puntosScore += puntos;
        recalcularTotal();
    }

    /**
     * Agrega puntos de capacidad de pago y recalcula el total.
     */
    public void agregarPuntosCapacidad(int puntos) {
        this.puntosCapacidad += puntos;
        recalcularTotal();
    }

    /**
     * Agrega puntos de historial crediticio y recalcula el total.
     */
    public void agregarPuntosHistorial(int puntos) {
        this.puntosHistorial += puntos;
        recalcularTotal();
    }

    /**
     * Agrega puntos de estabilidad laboral y recalcula el total.
     */
    public void agregarPuntosEstabilidad(int puntos) {
        this.puntosEstabilidad += puntos;
        recalcularTotal();
    }

    /**
     * Agrega puntos de garantía y recalcula el total.
     */
    public void agregarPuntosGarantia(int puntos) {
        this.puntosGarantia += puntos;
        recalcularTotal();
    }

    /**
     * Agrega puntos de educación y recalcula el total.
     */
    public void agregarPuntosEducacion(int puntos) {
        this.puntosEducacion += puntos;
        recalcularTotal();
    }

    /**
     * Agrega puntos por destino del crédito y recalcula el total.
     */
    public void agregarPuntosDestino(int puntos) {
        this.puntosDestino += puntos;
        recalcularTotal();
    }

    /**
     * Recalcula el score total como suma de todos los componentes.
     * Llamado automáticamente por cada agregar*.
     */
    private void recalcularTotal() {
        this.scoreTotal = puntosScore
                        + puntosCapacidad
                        + puntosHistorial
                        + puntosEstabilidad
                        + puntosGarantia
                        + puntosEducacion
                        + puntosDestino;
    }

    /**
     * Agrega un motivo de rechazo a la lista.
     */
    public void addMotivo(String motivo) {
        if (this.motivos == null) this.motivos = new ArrayList<>();
        this.motivos.add(motivo);
    }

    /**
     * Agrega una línea de detalle de scoring para auditoría.
     */
    public void addDetalle(String detalle) {
        if (this.detalles == null) this.detalles = new ArrayList<>();
        this.detalles.add(detalle);
    }

    // ---- Getters y Setters ----

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public String getNivelDecision() { return nivelDecision; }
    public void setNivelDecision(String nivelDecision) { this.nivelDecision = nivelDecision; }

    public boolean isRequiereRevisionHumana() { return requiereRevisionHumana; }
    public void setRequiereRevisionHumana(boolean requiereRevisionHumana) { this.requiereRevisionHumana = requiereRevisionHumana; }

    public int getScoreTotal() { return scoreTotal; }
    public void setScoreTotal(int scoreTotal) { this.scoreTotal = scoreTotal; }

    public int getPuntosScore() { return puntosScore; }
    public void setPuntosScore(int puntosScore) { this.puntosScore = puntosScore; }

    public int getPuntosCapacidad() { return puntosCapacidad; }
    public void setPuntosCapacidad(int puntosCapacidad) { this.puntosCapacidad = puntosCapacidad; }

    public int getPuntosHistorial() { return puntosHistorial; }
    public void setPuntosHistorial(int puntosHistorial) { this.puntosHistorial = puntosHistorial; }

    public int getPuntosEstabilidad() { return puntosEstabilidad; }
    public void setPuntosEstabilidad(int puntosEstabilidad) { this.puntosEstabilidad = puntosEstabilidad; }

    public int getPuntosGarantia() { return puntosGarantia; }
    public void setPuntosGarantia(int puntosGarantia) { this.puntosGarantia = puntosGarantia; }

    public int getPuntosEducacion() { return puntosEducacion; }
    public void setPuntosEducacion(int puntosEducacion) { this.puntosEducacion = puntosEducacion; }

    public int getPuntosDestino() { return puntosDestino; }
    public void setPuntosDestino(int puntosDestino) { this.puntosDestino = puntosDestino; }

    public List<String> getMotivos() { return motivos; }
    public void setMotivos(List<String> motivos) { this.motivos = motivos; }

    public List<String> getDetalles() { return detalles; }
    public void setDetalles(List<String> detalles) { this.detalles = detalles; }

    @Override
    public String toString() {
        return "ResultadoEvaluacion{" +
               "decision='" + decision + '\'' +
               ", nivelDecision='" + nivelDecision + '\'' +
               ", scoreTotal=" + scoreTotal +
               ", requiereRevisionHumana=" + requiereRevisionHumana +
               ", motivos=" + motivos +
               '}';
    }
}
