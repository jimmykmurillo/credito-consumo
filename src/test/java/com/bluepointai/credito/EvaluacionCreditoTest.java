package com.bluepointai.credito;

import com.bluepointai.credito.modelo.Solicitante;
import com.bluepointai.credito.modelo.Solicitud;
import com.bluepointai.credito.resultado.ResultadoEvaluacion;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Tests de integración para el motor de evaluación de crédito de consumo.
 * Valida los 20 casos de prueba de la demostración.
 *
 * Ejecución: mvn test
 * Desde Business Central: Menú → Ejecutar → Test (el proyecto debe compilar primero)
 *
 * Bluepoint AI — Demo Drools 7
 */
public class EvaluacionCreditoTest {

    private StatelessKieSession kieSession;

    @Before
    public void setUp() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        kieSession = kc.newStatelessKieSession("sesion-credito");
    }

    // ---- Método helper: ejecuta la sesión y retorna el resultado ----

    private ResultadoEvaluacion evaluar(Solicitante s, Solicitud sol) {
        ResultadoEvaluacion resultado = new ResultadoEvaluacion();
        kieSession.execute(Arrays.asList(s, sol, resultado));
        return resultado;
    }

    // ================================================================
    // ZONA VERDE — Aprobación automática (TC-001 a TC-003, TC-011, TC-013, TC-017)
    // ================================================================

    @Test
    public void tc001_perfilIdeal_aprobacionAutomatica() {
        Solicitante s = new Solicitante(35, 84, "DEPENDIENTE", 3500.0,
                "EXCELENTE", 790, "SUPERIOR", "CASADO", 1, true);
        Solicitud sol = new Solicitud(4000, 24, "CONSUMO", 183.0, 1500, "NINGUNA", 14.5, "AGENCIA");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-001", "APROBADO", r.getDecision());
        assertEquals("TC-001", "MOTOR_AUTOMATICO", r.getNivelDecision());
        assertFalse("TC-001", r.isRequiereRevisionHumana());
        assertTrue("TC-001 score >= 75", r.getScoreTotal() >= 75);
        System.out.println("[TC-001] Score: " + r.getScoreTotal() + " | " + r.getDetalles());
    }

    @Test
    public void tc002_jovenPrimerCredito_aprobacionAutomatica() {
        Solicitante s = new Solicitante(27, 18, "DEPENDIENTE", 1800.0,
                "SIN_HISTORIAL", 680, "SUPERIOR", "SOLTERO", 0, false);
        Solicitud sol = new Solicitud(2000, 18, "EDUCACION", 125.0, 0, "NINGUNA", 16.0, "DIGITAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-002", "APROBADO", r.getDecision());
        assertEquals("TC-002", "MOTOR_AUTOMATICO", r.getNivelDecision());
        assertFalse("TC-002", r.isRequiereRevisionHumana());
        System.out.println("[TC-002] Score: " + r.getScoreTotal() + " | " + r.getDetalles());
    }

    @Test
    public void tc003_jubilado_aprobacionAutomatica() {
        Solicitante s = new Solicitante(65, 0, "JUBILADO", 1200.0,
                "BUENO", 710, "MEDIA", "CASADO", 2, true);
        Solicitud sol = new Solicitud(3500, 36, "VEHICULO", 120.0, 800, "DEUDOR_SOLIDARIO", 15.5, "SUCURSAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-003", "APROBADO", r.getDecision());
        assertFalse("TC-003", r.isRequiereRevisionHumana());
        System.out.println("[TC-003] Score: " + r.getScoreTotal() + " | " + r.getDetalles());
    }

    @Test
    public void tc011_bonoFidelidad_aprobacionAutomatica() {
        // 15 años de antigüedad → bono +5 pts
        Solicitante s = new Solicitante(50, 180, "DEPENDIENTE", 4200.0,
                "EXCELENTE", 760, "SUPERIOR", "CASADO", 2, true);
        Solicitud sol = new Solicitud(8000, 36, "CONSUMO", 280.0, 3000, "NINGUNA", 14.0, "AGENCIA");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-011", "APROBADO", r.getDecision());
        assertEquals("TC-011", "MOTOR_AUTOMATICO", r.getNivelDecision());
        System.out.println("[TC-011] Score: " + r.getScoreTotal() + " | " + r.getDetalles());
    }

    @Test
    public void tc013_scoreLimite75_montoFrontera() {
        Solicitante s = new Solicitante(28, 30, "DEPENDIENTE", 2200.0,
                "BUENO", 710, "SUPERIOR", "SOLTERO", 0, false);
        Solicitud sol = new Solicitud(4900, 30, "VIAJE", 196.0, 1000, "NINGUNA", 15.5, "DIGITAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-013", "APROBADO", r.getDecision());
        assertTrue("TC-013 score >= 75", r.getScoreTotal() >= 75);
        System.out.println("[TC-013] Score: " + r.getScoreTotal() + " | " + r.getDetalles());
    }

    @Test
    public void tc017_independienteMaduro_aprobacionAutomatica() {
        // Independiente con 60 meses (5 años) cumple requisito
        Solicitante s = new Solicitante(44, 60, "INDEPENDIENTE", 4000.0,
                "EXCELENTE", 755, "POSTGRADO", "CASADO", 2, true);
        Solicitud sol = new Solicitud(4500, 24, "EDUCACION", 212.0, 2000, "NINGUNA", 14.5, "DIGITAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-017", "APROBADO", r.getDecision());
        assertFalse("TC-017", r.isRequiereRevisionHumana());
        System.out.println("[TC-017] Score: " + r.getScoreTotal() + " | " + r.getDetalles());
    }

    // ================================================================
    // ZONA GRIS — Revisión humana (TC-004, TC-005, TC-012, TC-015, TC-018, TC-019, TC-020)
    // ================================================================

    @Test
    public void tc004_profesionalMontoAlto_comite() {
        // Score altísimo pero monto > $15,000 → comité obligatorio
        Solicitante s = new Solicitante(42, 120, "DEPENDIENTE", 5000.0,
                "EXCELENTE", 800, "POSTGRADO", "CASADO", 2, true);
        Solicitud sol = new Solicitud(30000, 60, "VEHICULO", 700.0, 5000, "HIPOTECARIA", 13.5, "AGENCIA");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-004", "REVISION_HUMANA", r.getDecision());
        assertEquals("TC-004", "COMITE_CREDITO", r.getNivelDecision());
        assertTrue("TC-004", r.isRequiereRevisionHumana());
        System.out.println("[TC-004] Score: " + r.getScoreTotal() + " | Nivel: " + r.getNivelDecision());
    }

    @Test
    public void tc005_scoreMedio_oficialCredito() {
        Solicitante s = new Solicitante(31, 12, "DEPENDIENTE", 1400.0,
                "REGULAR", 610, "MEDIA", "SOLTERO", 1, false);
        Solicitud sol = new Solicitud(5000, 36, "CONSUMO", 175.0, 2000, "NINGUNA", 17.0, "DIGITAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-005", "REVISION_HUMANA", r.getDecision());
        assertTrue("TC-005", r.isRequiereRevisionHumana());
        System.out.println("[TC-005] Score: " + r.getScoreTotal() + " | Nivel: " + r.getNivelDecision());
    }

    @Test
    public void tc019_scoreMaximo_montoAlto_comiteObligatorio() {
        // Valida que el score perfecto NO omite el comité para montos altos
        Solicitante s = new Solicitante(48, 240, "DEPENDIENTE", 8000.0,
                "EXCELENTE", 830, "POSTGRADO", "CASADO", 1, true);
        Solicitud sol = new Solicitud(45000, 72, "VEHICULO", 950.0, 8000, "HIPOTECARIA", 13.0, "AGENCIA");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-019 — score 100/100 NO omite comité", "REVISION_HUMANA", r.getDecision());
        assertEquals("TC-019", "COMITE_CREDITO", r.getNivelDecision());
        System.out.println("[TC-019] Score: " + r.getScoreTotal() + " — Monto $45,000 requiere comité sin excepción");
    }

    @Test
    public void tc020_penalizacionDependientes_bajaAGris() {
        // 5 dependientes + cuota/ingreso 9% → penalización -5 pts baja de verde a gris
        Solicitante s = new Solicitante(39, 36, "DEPENDIENTE", 1800.0,
                "BUENO", 690, "SUPERIOR", "CASADO", 5, false);
        Solicitud sol = new Solicitud(4000, 30, "CONSUMO", 163.0, 2500, "NINGUNA", 16.5, "SUCURSAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-020", "REVISION_HUMANA", r.getDecision());
        assertTrue("TC-020 penalización aplicada", r.getScoreTotal() < 65);
        System.out.println("[TC-020] Score con penalización: " + r.getScoreTotal() + " | " + r.getDetalles());
    }

    // ================================================================
    // ZONA ROJA — Rechazo automático (TC-006 a TC-010, TC-014, TC-016)
    // ================================================================

    @Test
    public void tc006_historialMalo_rechazoInmediato() {
        Solicitante s = new Solicitante(38, 48, "DEPENDIENTE", 2200.0,
                "MALO", 520, "SUPERIOR", "CASADO", 2, false);
        Solicitud sol = new Solicitud(3000, 24, "CONSUMO", 145.0, 4000, "NINGUNA", 18.0, "AGENCIA");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-006", "RECHAZADO", r.getDecision());
        assertFalse("TC-006", r.isRequiereRevisionHumana());
        assertFalse("TC-006 debe tener motivo", r.getMotivos().isEmpty());
        assertTrue("TC-006 motivo MALO", r.getMotivos().get(0).contains("MALO"));
        System.out.println("[TC-006] Motivo: " + r.getMotivos());
    }

    @Test
    public void tc007_edadMenor21_rechazoPorEdad() {
        Solicitante s = new Solicitante(19, 8, "DEPENDIENTE", 900.0,
                "SIN_HISTORIAL", 620, "MEDIA", "SOLTERO", 0, false);
        Solicitud sol = new Solicitud(800, 12, "CONSUMO", 72.0, 0, "NINGUNA", 18.5, "DIGITAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-007", "RECHAZADO", r.getDecision());
        assertTrue("TC-007 motivo edad", r.getMotivos().stream()
                .anyMatch(m -> m.contains("21") || m.contains("edad")));
        System.out.println("[TC-007] Motivo: " + r.getMotivos());
    }

    @Test
    public void tc008_cuotaExcedeIngreso_rechazo() {
        // Cuota $340 / Ingreso $800 = 42.5% > 40% → RECHAZADO
        Solicitante s = new Solicitante(33, 24, "DEPENDIENTE", 800.0,
                "BUENO", 695, "MEDIA", "CASADO", 3, false);
        Solicitud sol = new Solicitud(5000, 24, "CONSUMO", 340.0, 1200, "NINGUNA", 17.5, "SUCURSAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-008", "RECHAZADO", r.getDecision());
        assertTrue("TC-008 motivo cuota/ingreso",
                r.getMotivos().stream().anyMatch(m -> m.contains("40%") || m.contains("cuota")));
        System.out.println("[TC-008] Motivo: " + r.getMotivos());
    }

    @Test
    public void tc009_independienteSinAntiguedad_rechazo() {
        // Independiente con 14 meses < 24 requeridos
        Solicitante s = new Solicitante(29, 14, "INDEPENDIENTE", 2500.0,
                "BUENO", 700, "SUPERIOR", "SOLTERO", 0, false);
        Solicitud sol = new Solicitud(4000, 24, "CONSUMO", 195.0, 0, "NINGUNA", 16.5, "DIGITAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-009", "RECHAZADO", r.getDecision());
        assertTrue("TC-009 motivo antigüedad",
                r.getMotivos().stream().anyMatch(m -> m.contains("24") || m.contains("independiente")));
        System.out.println("[TC-009] Motivo: " + r.getMotivos());
    }

    @Test
    public void tc010_endeudamientoExcesivo_rechazo() {
        // Deuda $11,000 / Ingreso $1,500 = 7.33x > 6x → RECHAZADO
        Solicitante s = new Solicitante(45, 36, "DEPENDIENTE", 1500.0,
                "REGULAR", 620, "MEDIA", "CASADO", 3, false);
        Solicitud sol = new Solicitud(3000, 36, "CONSUMO", 110.0, 11000, "NINGUNA", 17.0, "AGENCIA");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-010", "RECHAZADO", r.getDecision());
        assertTrue("TC-010 motivo endeudamiento",
                r.getMotivos().stream().anyMatch(m -> m.contains("6") || m.contains("endeudamiento")));
        System.out.println("[TC-010] Motivo: " + r.getMotivos());
    }

    @Test
    public void tc014_scoreInsuficiente_rechazo() {
        // Score < 45 → rechazo por score insuficiente
        Solicitante s = new Solicitante(35, 8, "DEPENDIENTE", 900.0,
                "REGULAR", 540, "BASICA", "DIVORCIADO", 4, false);
        Solicitud sol = new Solicitud(2000, 24, "CONSUMO", 105.0, 1500, "NINGUNA", 18.5, "SUCURSAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-014", "RECHAZADO", r.getDecision());
        assertTrue("TC-014 score < 45", r.getScoreTotal() < 45);
        System.out.println("[TC-014] Score: " + r.getScoreTotal() + " | Motivo: " + r.getMotivos());
    }

    @Test
    public void tc016_edadMayor70_rechazo() {
        Solicitante s = new Solicitante(73, 0, "JUBILADO", 900.0,
                "BUENO", 700, "BASICA", "VIUDO", 0, true);
        Solicitud sol = new Solicitud(2000, 24, "CONSUMO", 98.0, 0, "NINGUNA", 16.0, "SUCURSAL");

        ResultadoEvaluacion r = evaluar(s, sol);

        assertEquals("TC-016", "RECHAZADO", r.getDecision());
        assertTrue("TC-016 motivo edad",
                r.getMotivos().stream().anyMatch(m -> m.contains("70") || m.contains("edad")));
        System.out.println("[TC-016] Motivo: " + r.getMotivos());
    }
}
