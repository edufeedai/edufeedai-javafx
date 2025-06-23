# edufeedai-cli

Este módulo es la **interfaz de línea de comandos** y el **punto de entrada principal** del proyecto EduFeedAI.

## 🚀 Ejecución

Desde la raíz del proyecto, compila todo:
```bash
mvn clean install
```

Luego ejecuta la CLI:
```bash
cd edufeedai-cli
mvn exec:java
```

### JAR ejecutable (opcional)
Si configuras el plugin Shade en el `pom.xml`, puedes generar un JAR ejecutable:
```bash
mvn clean package
java -jar target/edufeedai-cli-1.0-SNAPSHOT-shaded.jar
```

## 📄 Notas
- Este módulo depende de `edufeedai-lib` para toda la lógica de negocio.
- Si añades argumentos o comandos, documenta su uso aquí.

---
> "Este es el módulo principal para usuarios finales. Wesley recomienda ejecutarlo desde aquí."
