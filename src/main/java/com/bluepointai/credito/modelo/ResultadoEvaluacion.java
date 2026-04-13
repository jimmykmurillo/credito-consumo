package com.bluepointai.credito.modelo;

import java.util.ArrayList;
import java.util.List;

public class ResultadoEvaluacion implements java.io.Serializable {

    private String decision;
    private String nivelDecision;
    private boolean requiereRevisionHumana;
    private int scoreTotal;
    private int puntosScore;
    private int puntosCapacidad;
    private int puntosHistorial;
    private int puntosEstabilidad;
    private int puntosGarantia;
    private int puntosEducacion;
    private int puntosDestino;
    private List<String> motivos = new ArrayList<>();
    private List<String> detalles = new ArrayList<>();

    public ResultadoEvaluacion() {
        this.motivos = new ArrayList<>();
        this.detalles = new ArrayList<>();
        this.decision = "PENDIENTE";
    }

    public void agregarPuntosScore(int p) { puntosScore += p; recalcular(); }
    public void agregarPuntosCapacidad(int p) { puntosCapacidad += p; recalcular(); }
    public void agregarPuntosHistorial(int p) { puntosHistorial += p; recalcular(); }
    public void agregarPuntosEstabilidad(int p) { puntosEstabilidad += p; recalcular(); }
    public void agregarPuntosGarantia(int p) { puntosGarantia += p; recalcular(); }
    public void agregarPuntosEducacion(int p) { puntosEducacion += p; recalcular(); }
    public void agregarPuntosDestino(int p) { puntosDestino += p; recalcular(); }
    public void addMotivo(String m) { motivos.add(m); }
    public void addDetalle(String d) { detalles.add(d); }

    private void recalcular() {
        scoreTotal = puntosScore + puntosCapacidad + puntosHistorial
                   + puntosEstabilidad + puntosGarantia + puntosEducacion + puntosDestino;
    }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public String getNivelDecision() { return nivelDecision; }
    public void setNivelDecision(String nivelDecision) { this.nivelDecision = nivelDecision; }

    public boolean isRequiereRevisionHumana() { return requiereRevisionHumana; }
    public void setRequiereRevisionHumana(boolean r) { this.requiereRevisionHumana = r; }

    public int getScoreTotal() { return scoreTotal; }
    public void setScoreTotal(int scoreTotal) { this.scoreTotal = scoreTotal; }

    public int getPuntosScore() { return puntosScore; }
    public void setPuntosScore(int p) { this.puntosScore = p; }

    public int getPuntosCapacidad() { return puntosCapacidad; }
    public void setPuntosCapacidad(int p) { this.puntosCapacidad = p; }

    public int getPuntosHistorial() { return puntosHistorial; }
    public void setPuntosHistorial(int p) { this.puntosHistorial = p; }

    public int getPuntosEstabilidad() { return puntosEstabilidad; }
    public void setPuntosEstabilidad(int p) { this.puntosEstabilidad = p; }

    public int getPuntosGarantia() { return puntosGarantia; }
    public void setPuntosGarantia(int p) { this.puntosGarantia = p; }

    public int getPuntosEducacion() { return puntosEducacion; }
    public void setPuntosEducacion(int p) { this.puntosEducacion = p; }

    public int getPuntosDestino() { return puntosDestino; }
    public void setPuntosDestino(int p) { this.puntosDestino = p; }

    public List<String> getMotivos() { return motivos; }
    public void setMotivos(List<String> motivos) { this.motivos = motivos; }

    public List<String> getDetalles() { return detalles; }
    public void setDetalles(List<String> detalles) { this.detalles = detalles; }
}