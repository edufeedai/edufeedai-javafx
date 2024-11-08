
# EduFeedAI Java Project

Este proyecto es una aplicación Java desarrollada para procesar boletines de alumnos y proporcionar retroalimentación automática utilizando técnicas de OCR y análisis de texto. Originalmente iniciado como un proyecto de JavaFX, ha evolucionado para centrarse en pruebas y desarrollo con clases Java y procesamiento de archivos PDF.

## 🚀 Tecnologías Utilizadas

- **Java**: Lenguaje de programación principal.
- **Maven**: Herramienta de construcción.
- **JUnit** (`5.10.0`): Framework para pruebas unitarias.
- **Gson** (`2.10.1`): Librería para manejar JSON.
- **dotenv** (`3.0.0`): Para la gestión de variables de entorno.
- **Apache HttpClient** (`5.4`): Para realizar peticiones HTTP.
- **PDFBox** (`3.0.3`): Procesamiento de archivos PDF.
- **Tess4J** (`5.13.0`): Interfaz para Tesseract OCR.
- **Docker**: Contenedores para desarrollo y despliegue.
- **Dev Containers**: Configuración para desarrollo en contenedores con VSCode.

## 📋 Requisitos Previos

- **Java 17 o superior**
- **Maven 3.8 o superior**
- **Docker** (opcional para despliegue)
- **Tesseract OCR** instalado localmente si no se utiliza Docker.

## ⚙️ Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/edufeedai/edufeedai-javafx.git
   cd edufeedai-javafx
   ```

2. Compila el proyecto con Maven:
   ```bash
   mvn clean install
   ```

3. Ejecuta los tests:
   ```bash
   mvn test
   ```

## 🐳 Uso con Docker

El proyecto incluye un `Dockerfile` y un archivo `docker-compose.yaml` para facilitar el desarollo en contenedores. Sientete libre de investigar. Aún no controlo mucho esa parte pero estoy utilizando los dev containers de VSCode.

## 🧪 Pruebas

El proyecto utiliza JUnit para pruebas automatizadas. Puedes ejecutar todas las pruebas con:

```bash
mvn test
```

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.

## 🤝 Contribuir

Si deseas contribuir, por favor sigue los siguientes pasos:

1. Haz un fork del proyecto.
2. Crea una nueva rama:
   ```bash
   git checkout -b feature/nueva-feature
   ```
3. Realiza tus cambios y haz commit:
   ```bash
   git commit -m 'Añadir nueva feature'
   ```
4. Envía tus cambios a GitHub:
   ```bash
   git push origin feature/nueva-feature
   ```
5. Crea un Pull Request.

## 📧 Contacto

Para cualquier duda o sugerencia, puedes contactarme abrir una issue i me pondré en contacto contigo.
