# Graph Report - grafo  (2026-11-24)

## Corpus Check
- 52 files · ~4,157 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 1 file(s) not represented in the graph (top: .properties 1)

## Summary
- 378 nodes · 941 edges · 12 communities (8 shown, 4 thin omitted)
- Extraction: 91% EXTRACTED · 9% INFERRED · 0% AMBIGUOUS · INFERRED: 80 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Lote
- org.junit.jupiter.api.Test
- Cata
- Usuario
- CataService.java
- FincaService.java
- RegistroClima
- list
- ManejadorErrores.java
- MongoIndexConfig.java
- TipoEtapa
- OrigenQuindioApplication.java

## God Nodes (most connected - your core abstractions)
1. `Lote` - 29 edges
2. `Cata` - 22 edges
3. `Finca` - 21 edges
4. `RegistroClima` - 21 edges
5. `LoteService` - 20 edges
6. `Usuario` - 19 edges
7. `CataService` - 19 edges
8. `Rol` - 18 edges
9. `FincaService` - 18 edges
10. `UsuarioService` - 17 edges

## Surprising Connections (you probably didn't know these)
- `LoteController` --references--> `LoteService`  [EXTRACTED]
  src/main/java/com/nosql/origenquindio/controllers/LoteController.java → src/main/java/com/nosql/origenquindio/service/LoteService.java
- `UsuarioController` --references--> `UsuarioService`  [EXTRACTED]
  src/main/java/com/nosql/origenquindio/controllers/UsuarioController.java → src/main/java/com/nosql/origenquindio/service/UsuarioService.java
- `Lote` --references--> `Etapa`  [EXTRACTED]
  src/main/java/com/nosql/origenquindio/entity/Lote.java → src/main/java/com/nosql/origenquindio/entity/Etapa.java
- `FincaService` --references--> `FincaRepository`  [EXTRACTED]
  src/main/java/com/nosql/origenquindio/service/FincaService.java → src/main/java/com/nosql/origenquindio/repository/FincaRepository.java
- `LoteService` --references--> `LoteRepository`  [EXTRACTED]
  src/main/java/com/nosql/origenquindio/service/LoteService.java → src/main/java/com/nosql/origenquindio/repository/LoteRepository.java

## Import Cycles
- None detected.

## Communities (12 total, 4 thin omitted)

### Community 0 - "Lote"
Cohesion: 0.06
Nodes (11): LoteController, LoteRequest, LoteUpdateRequest, FincaResumen, Lote, Proceso, HONEY, LAVADO (+3 more)

### Community 1 - "org.junit.jupiter.api.Test"
Cohesion: 0.10
Nodes (5): CataApiTest, FincaApiTest, LoteApiTest, PruebaApi, UsuarioApiTest

### Community 2 - "Cata"
Cohesion: 0.10
Nodes (7): CataController, CataRequest, PuntajePorVariedad, AtributosCata, Cata, CataRepository, CataService

### Community 3 - "Usuario"
Cohesion: 0.09
Nodes (10): UsuarioController, UsuarioRequest, UsuarioResponse, UsuarioUpdateRequest, Rol, ADMIN, CATADOR, PRODUCTOR (+2 more)

### Community 4 - "CataService.java"
Cohesion: 0.11
Nodes (7): FincaController, ConflictoException, RecursoNoEncontradoException, FincaService, LoteService, ReferenciaService, UsuarioService

### Community 5 - "FincaService.java"
Cohesion: 0.10
Nodes (4): FincaRequest, FincaResponse, Finca, FincaRepository

### Community 6 - "RegistroClima"
Cohesion: 0.11
Nodes (6): RegistroClimaController, RegistroClimaRequest, RegistroClima, ReglaNegocioException, RegistroClimaRepository, RegistroClimaService

### Community 10 - "TipoEtapa"
Cohesion: 0.15
Nodes (8): EtapaRequest, Etapa, TipoEtapa, ALMACENAMIENTO, BENEFICIO, COSECHA, SECADO, TRILLA

## Knowledge Gaps
- **11 isolated node(s):** `LAVADO`, `NATURAL`, `HONEY`, `ADMIN`, `PRODUCTOR` (+6 more)
  These have ≤1 connection - possible missing edges. (Counts symbols only; 107 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Lote` connect `Lote` to `TipoEtapa`, `CataService.java`, `FincaService.java`, `list`?**
  _High betweenness centrality (0.119) - this node is a cross-community bridge._
- **What connects `LAVADO`, `NATURAL`, `HONEY` to the rest of the system?**
  _11 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Lote` be split into smaller, more focused modules?**
  _Cohesion score 0.05925925925925926 - nodes in this community are weakly interconnected._
- **Why does `PruebaApi` connect `org.junit.jupiter.api.Test` to `CataService.java`?**
  _High betweenness centrality (0.109) - this node is a cross-community bridge._
- **Should `org.junit.jupiter.api.Test` be split into smaller, more focused modules?**
  _Cohesion score 0.09653092006033183 - nodes in this community are weakly interconnected._
- **Why does `Cata` connect `Cata` to `CataService.java`, `FincaService.java`, `list`?**
  _High betweenness centrality (0.060) - this node is a cross-community bridge._
- **Should `Cata` be split into smaller, more focused modules?**
  _Cohesion score 0.09743589743589744 - nodes in this community are weakly interconnected._