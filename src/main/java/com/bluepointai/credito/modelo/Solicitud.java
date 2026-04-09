package com.bluepointai.credito.modelo;

import java.io.Serializable;

/**
 * Data Object: Solicitud
 * Representa la solicitud de crédito de consumo.
 * Los campos relacionCuotaIngreso y relacionDeudaIngreso
 * son calculados automáticamente por Drools al inicio de la sesión.
 *
 * Bluepoint AI — Demo evaluación crédito de consumo
 */
public class Solicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---- Datos de la operación ----

    /** Monto solicitado en USD. */
    private double montoSolicitado;

    /** Plazo de amortización en meses. */
    private int plazoMeses;

    /**
     * Destino del crédito.
     * Valores válidos: CONSUMO | VEHICULO | EDUCACION | VIAJE | OTROS
     */
    private String destinoCredito;

    /**
     * Cuota mensual calculada por el sistema originador (en USD).
     * Drools la usa para calcular relacionCuotaIngreso.
     */
    private double cuotaMensual;

    /**
     * Ratio cuota mensual / ingreso mensual neto.
     * CALCULADO POR DROOLS — no enviar desde el cliente.
     * Umbral de rechazo: >= 0.40 (40%).
     */
    private double relacionCuotaIngreso;

    /** Total de deudas financieras vigentes del solicitante en USD. */
    private double deudaActualTotal;

    /**
     * Ratio deuda total / ingreso mensual neto.
     * CALCULADO POR DROOLS — no enviar desde el cliente.
     * Alerta: >= 4.0. Rechazo: >= 6.0.
     */
    private double relacionDeudaIngreso;

    /**
     * Tipo de garantía ofrecida.
     * Valores válidos: NINGUNA | DEUDOR_SOLIDARIO | HIPOTECARIA
     */
    private String garantia;

    /** Tasa de interés anual aplicable (%). */
    private double tasaInteres;

    /**
     * Canal de origen de la solicitud.
     * Valores válidos: AGENCIA | DIGITAL | CALL_CENTER | SUCURSAL
     */
    private String entidadOrigen;

    // ---- Constructores ----

    public Solicitud() {
    }

    public Solicitud(double montoSolicitado, int plazoMeses, String destinoCredito,
                     double cuotaMensual, double deudaActualTotal,
                     String garantia, double tasaInteres, String entidadOrigen) {
        this.montoSolicitado = montoSolicitado;
        this.plazoMeses = plazoMeses;
        this.destinoCredito = destinoCredito;
        this.cuotaMensual = cuotaMensual;
        this.deudaActualTotal = deudaActualTotal;
        this.garantia = garantia;
        this.tasaInteres = tasaInteres;
        this.entidadOrigen = entidadOrigen;
    }

    // ---- Getters y Setters ----

    public double getMontoSolicitado() { return montoSolicitado; }
    public void setMontoSolicitado(double montoSolicitado) { this.montoSolicitado = montoSolicitado; }

    public int getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(int plazoMeses) { this.plazoMeses = plazoMeses; }

    public String getDestinoCredito() { return destinoCredito; }
    public void setDestinoCredito(String destinoCredito) { this.destinoCredito = destinoCredito; }

    public double getCuotaMensual() { return cuotaMensual; }
    public void setCuotaMensual(double cuotaMensual) { this.cuotaMensual = cuotaMensual; }

    public double getRelacionCuotaIngreso() { return relacionCuotaIngreso; }
    public void setRelacionCuotaIngreso(double relacionCuotaIngreso) { this.relacionCuotaIngreso = relacionCuotaIngreso; }

    public double getDeudaActualTotal() { return deudaActualTotal; }
    public void setDeudaActualTotal(double deudaActualTotal) { this.deudaActualTotal = deudaActualTotal; }

    public double getRelacionDeudaIngreso() { return relacionDeudaIngreso; }
    public void setRelacionDeudaIngreso(double relacionDeudaIngreso) { this.relacionDeudaIngreso = relacionDeudaIngreso; }

    public String getGarantia() { return garantia; }
    public void setGarantia(String garantia) { this.garantia = garantia; }

    public double getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(double tasaInteres) { this.tasaInteres = tasaInteres; }

    public String getEntidadOrigen() { return entidadOrigen; }
    public void setEntidadOrigen(String entidadOrigen) { this.entidadOrigen = entidadOrigen; }

    @Override
    public String toString() {
        return "Solicitud{" +
               "montoSolicitado=" + montoSolicitado +
               ", plazoMeses=" + plazoMeses +
               ", destinoCredito='" + destinoCredito + '\'' +
               ", cuotaMensual=" + cuotaMensual +
               ", garantia='" + garantia + '\'' +
               '}';
    }
}
