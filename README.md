# EduFeedAI

> **Proyecto modularizado: CLI y librería reutilizable**

EduFeedAI es una aplicación Java para procesar boletines de alumnos en PDF, usando OCR (Tess4J, OpenCV) y análisis de texto para generar retroalimentación automática. El proyecto está organizado en dos módulos principales:

- **edufeedai-lib**: Lógica de negocio, utilidades, modelos y recursos reutilizables.
- **edufeedai-cli**: Interfaz de línea de comandos y punto de entrada principal.

## 📦 Estructura del proyecto

```
/edufeedai
├── pom.xml                # POM padre (gestiona módulos y dependencias)
├── edufeedai-lib/         # Lógica, modelos, utilidades, recursos y tests
│   └── README.md
├── edufeedai-cli/         # CLI y main
│   └── README.md
```

## 🚀 Compilación y ejecución

1. **Compilar todo el proyecto:**
   ```bash
   mvn clean install
   ```
2. **Ejecutar la CLI:**
   ```bash
   cd edufeedai-cli
   mvn exec:java
   ```
   O, si usas el plugin Shade (ver README de edufeedai-cli):
   ```bash
   java -jar target/edufeedai-cli-1.0-SNAPSHOT-shaded.jar
   ```

## 🧩 Módulos

- **[edufeedai-lib](./edufeedai-lib/README.md):** Lógica, utilidades y modelos reutilizables.
- **[edufeedai-cli](./edufeedai-cli/README.md):** Interfaz de línea de comandos y punto de entrada.

## 🛠️ Contribución
- Haz commit tras cada cambio importante.
- Los tests están en `edufeedai-lib/src/test/java`.
- Si añades variables de entorno, documenta su uso en el README correspondiente.

---

> "Wesley, el robot existencial, recomienda leer los README de cada módulo para detalles específicos."
