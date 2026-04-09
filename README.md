# Guía de importación en Business Central — Demo Crédito de Consumo
## Bluepoint AI · Drools 7.74.1.Final

---

## Prerrequisitos

- Business Central corriendo: `http://localhost:8080/business-central`
- KIE Server corriendo: `http://localhost:8180/kie-server`
- Ambos enlazados (verificar en Menú → Deploy → Execution Servers)
- Java 8 u 11, Maven 3.6+

---

## Opción A — Importar proyecto completo desde Git (recomendado)

1. Inicializa un repositorio Git con este directorio:
   ```bash
   cd demo-credito-bc/
   git init
   git add .
   git commit -m "feat: motor de evaluación crédito de consumo v1.0"
   ```

2. En Business Central:
   - Menú → **Design** → **Projects**
   - Clic en **Import Project**
   - URL del repo Git → Import
   - Selecciona la rama `main`

3. El proyecto importado ya incluye todas las clases Java y los DRL. Continúa en el **Paso 3** de la Opción B.

---

## Opción B — Crear el proyecto manualmente en Business Central

### Paso 1: Crear el espacio y proyecto

1. Menú → **Design** → **Projects**
2. Clic en **Add Project**
3. Completar:
   - **Name:** `credito-consumo`
   - **Group ID:** `com.bluepointai`
   - **Artifact ID:** `credito-consumo`
   - **Version:** `1.0.0-SNAPSHOT`
4. Clic en **Add** → confirmar

---

### Paso 2: Crear los Data Objects

Hay que crear 3 Data Objects. Para cada uno:

**Menú → + Add Asset → Data Object**

#### 2.1 — Data Object: `Solicitante`

- **Name:** `Solicitante`
- **Package:** `com.bluepointai.credito.modelo`

Agregar los siguientes campos (+ Add Field):

| Identifier (nombre) | Type | Label |
|---|---|---|
| edad | int | Edad (años) |
| antiguedadLaboral | int | Antigüedad laboral (meses) |
| tipoEmpleo | String | Tipo de empleo |
| ingresoMensualNeto | double | Ingreso mensual neto (USD) |
| historialCrediticio | String | Historial crediticio |
| scoreExterno | int | Score externo (buró) |
| nivelEducacion | String | Nivel de educación |
| estadoCivil | String | Estado civil |
| numeroDependientes | int | Número de dependientes |
| propietarioVivienda | boolean | Propietario de vivienda |

Clic en **Save**.

---

#### 2.2 — Data Object: `Solicitud`

- **Name:** `Solicitud`
- **Package:** `com.bluepointai.credito.modelo`

Campos:

| Identifier | Type | Label |
|---|---|---|
| montoSolicitado | double | Monto solicitado (USD) |
| plazoMeses | int | Plazo (meses) |
| destinoCredito | String | Destino del crédito |
| cuotaMensual | double | Cuota mensual (USD) |
| relacionCuotaIngreso | double | Relación cuota/ingreso |
| deudaActualTotal | double | Deuda total vigente (USD) |
| relacionDeudaIngreso | double | Relación deuda/ingreso |
| garantia | String | Tipo de garantía |
| tasaInteres | double | Tasa de interés anual (%) |
| entidadOrigen | String | Canal de origen |

Clic en **Save**.

---

#### 2.3 — Data Object: `ResultadoEvaluacion`

- **Name:** `ResultadoEvaluacion`
- **Package:** `com.bluepointai.credito.resultado`

Campos:

| Identifier | Type | Label |
|---|---|---|
| decision | String | Decisión |
| nivelDecision | String | Nivel de decisión |
| requiereRevisionHumana | boolean | Requiere revisión humana |
| scoreTotal | int | Score total |
| puntosScore | int | Puntos score externo |
| puntosCapacidad | int | Puntos capacidad de pago |
| puntosHistorial | int | Puntos historial crediticio |
| puntosEstabilidad | int | Puntos estabilidad laboral |
| puntosGarantia | int | Puntos garantía |
| puntosEducacion | int | Puntos educación |
| puntosDestino | int | Puntos destino |
| motivos | List | Motivos de rechazo |
| detalles | List | Detalles del scoring |

> **IMPORTANTE:** Los campos `motivos` y `detalles` son de tipo `java.util.List`.
> En Business Central selecciona tipo `List` y marca **Is Collection**.

Clic en **Save**.

---

### Paso 3: Importar los archivos DRL

Para cada archivo DRL de la carpeta `reglas/`:

1. Menú → **+ Add Asset** → **DRL file**
2. **Name:** nombre del archivo sin extensión (ej: `01-reglas-admision`)
3. **Package:** `com.bluepointai.credito.reglas`
4. Clic en **Ok**
5. En el editor, **pegar el contenido completo** del archivo `.drl` correspondiente
6. Clic en **Save** → **Save** (confirmar)

Archivos a importar en orden:
1. `01-reglas-admision.drl`
2. `02-scoring-capacidad.drl`
3. `03-scorecard-perfil.drl`
4. `04-autonomias-decision.drl`

---

### Paso 4: Verificar la compilación

1. Desde la vista del proyecto → **Build**
2. Verificar que no haya errores en el panel inferior
3. Si hay errores de importación de clases, revisar que los packages en los DRL coincidan exactamente:
   - `import com.bluepointai.credito.modelo.Solicitante;`
   - `import com.bluepointai.credito.modelo.Solicitud;`
   - `import com.bluepointai.credito.resultado.ResultadoEvaluacion;`

---

### Paso 5: Deploy al KIE Server

1. Desde la vista del proyecto → **Deploy**
2. Verificar en el banner: `"Deployment successful"`
3. Confirmar en: Menú → Deploy → **Execution Servers**
4. El contenedor `credito-consumo_1.0.0-SNAPSHOT` debe aparecer en **STARTED**

---

### Paso 6: Probar con el KIE Server REST API

```bash
# Verificar que el contenedor está activo
curl -u kieserver:kieserver1! \
  http://localhost:8180/kie-server/services/rest/server/containers

# Ejecutar TC-001 (perfil ideal)
curl -X POST \
  -u kieserver:kieserver1! \
  -H "Content-Type: application/json" \
  -H "X-KIE-ContentType: JSON" \
  http://localhost:8180/kie-server/services/rest/server/containers/credito-consumo_1.0.0-SNAPSHOT/sessions/sesion-credito/execute \
  -d '{
    "lookup": "sesion-credito",
    "commands": [
      {
        "insert": {
          "object": {
            "com.bluepointai.credito.modelo.Solicitante": {
              "edad": 35,
              "antiguedadLaboral": 84,
              "tipoEmpleo": "DEPENDIENTE",
              "ingresoMensualNeto": 3500.0,
              "historialCrediticio": "EXCELENTE",
              "scoreExterno": 790,
              "nivelEducacion": "SUPERIOR",
              "estadoCivil": "CASADO",
              "numeroDependientes": 1,
              "propietarioVivienda": true
            }
          },
          "out-identifier": "solicitante"
        }
      },
      {
        "insert": {
          "object": {
            "com.bluepointai.credito.modelo.Solicitud": {
              "montoSolicitado": 4000,
              "plazoMeses": 24,
              "destinoCredito": "CONSUMO",
              "cuotaMensual": 183.0,
              "deudaActualTotal": 1500,
              "garantia": "NINGUNA",
              "tasaInteres": 14.5,
              "entidadOrigen": "AGENCIA"
            }
          },
          "out-identifier": "solicitud"
        }
      },
      {
        "insert": {
          "object": {
            "com.bluepointai.credito.resultado.ResultadoEvaluacion": {}
          },
          "out-identifier": "resultado"
        }
      },
      {
        "fire-all-rules": {}
      }
    ]
  }'
```

**Respuesta esperada TC-001:**
```json
{
  "decision": "APROBADO",
  "nivelDecision": "MOTOR_AUTOMATICO",
  "requiereRevisionHumana": false,
  "scoreTotal": 91
}
```

---

## Estructura de archivos del proyecto

```
demo-credito-bc/
├── pom.xml                                          ← Maven KJAR config
├── README.md                                        ← Este archivo
├── src/
│   ├── main/
│   │   ├── java/com/bluepointai/credito/
│   │   │   ├── modelo/
│   │   │   │   ├── Solicitante.java                ← Data Object 1
│   │   │   │   └── Solicitud.java                  ← Data Object 2
│   │   │   └── resultado/
│   │   │       └── ResultadoEvaluacion.java         ← Data Object 3
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── kmodule.xml                     ← Descriptor KIE
│   │       └── com/bluepointai/credito/reglas/
│   │           ├── 01-reglas-admision.drl           ← Tabla Decisión 1
│   │           ├── 02-scoring-capacidad.drl         ← Tabla Decisión 2
│   │           ├── 03-scorecard-perfil.drl          ← Scorecard 1 y 2
│   │           └── 04-autonomias-decision.drl       ← TD 3 + Reglas guiadas
│   └── test/
│       └── java/com/bluepointai/credito/
│           └── EvaluacionCreditoTest.java           ← 20 casos de prueba
└── casos-prueba/
    └── 20-casos-prueba.json                         ← Payloads REST listos
```

---

## Valores de referencia para la demo

| Variable | Valores válidos |
|---|---|
| tipoEmpleo | DEPENDIENTE, INDEPENDIENTE, JUBILADO |
| historialCrediticio | EXCELENTE, BUENO, REGULAR, MALO, SIN_HISTORIAL |
| nivelEducacion | BASICA, MEDIA, SUPERIOR, POSTGRADO |
| estadoCivil | SOLTERO, CASADO, DIVORCIADO, VIUDO, UNION_LIBRE |
| destinoCredito | CONSUMO, VEHICULO, EDUCACION, VIAJE, OTROS |
| garantia | NINGUNA, DEUDOR_SOLIDARIO, HIPOTECARIA |
| entidadOrigen | AGENCIA, DIGITAL, CALL_CENTER, SUCURSAL |
| decision (output) | APROBADO, REVISION_HUMANA, RECHAZADO |
| nivelDecision (output) | MOTOR_AUTOMATICO, OFICIAL_CREDITO, COMITE_CREDITO |
