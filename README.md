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
