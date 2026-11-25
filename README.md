# Origen Quindío

API REST con MongoDB para la trazabilidad de lotes de café. Primera entrega del proyecto de Bases de datos NoSQL, Universidad del Valle.

## Ejecutar

Necesita Java 25 y Docker.

```powershell
docker compose up -d
.\gradlew.bat bootRun
```

Swagger queda en http://localhost:8080/swagger-ui/index.html.

Para usar otra base, como Atlas, se define la variable `ORIGEN_MONGODB_URI`.

## Pruebas

Usan la base `origen_quindio_test` del contenedor local.

```powershell
.\gradlew.bat test
```

## Partes

| Parte | Responsable | Contenido |
|---|---|---|
| 1 | Juan Guzmán | Estructura, conexión y usuarios |
| 2 | Juan Wallens | Fincas y clima |
| 3 | Juan Gutiérrez | Lotes y etapas |
| 4 | Natalia Martínez | Catas, agregación y documento |

Se suben en ese orden. Cada parte compila y pasa sus pruebas con las anteriores.
