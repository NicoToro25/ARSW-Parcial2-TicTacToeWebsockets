# Arquitecturas de Software (ARSW) - Parcial #2

## Tic Tac Toe con WebSockets

#### Nicolás Toro

[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-Build-brightgreen.svg)](https://maven.apache.org/)

---

En este repositorio se muestra la solucion al parcial #2 del curso de arquitectura de software (ARSW), se busca implementar
el juego tic tac toe con WebScockets, demostrando las habilidades técnicas adquiridas en el curso.


## Estructura del laboratorio

```bash

```
---

### Ejecutar el Proyecto

A continuación, se describen los pasos para ejecutar ambos proyectos en cualquier sistema operativo compatible con Java y Maven.

#### 1. Requisitos previos

- **Java 17** o superior instalado y configurado en el `PATH`.
- **Apache Maven** instalado y configurado en el `PATH`.
- (Opcional) Un IDE como IntelliJ IDEA, Eclipse o VS Code para facilitar la edición y ejecución.

Verifica las versiones instaladas ejecutando en la terminal:

```bash
java -version
mvn -version
```

#### 2. Clonar el repositorio

Si aún no tiene el repositorio localmente, clónelo con:

```bash
git clone https://github.com/NicoToro25/ARSW-Parcial2-TicTacToeWebsockets.git
```

#### 3. Compilar los proyectos

Ejecutar el siguiente código

```bash
mvn clean package
```

#### 4. Ejecutar los proyectos

Ejecutar el siguiente código:

```bash
mvn exec:java@
```

> **Nota:** Si su IDE lo permite, también puede ejecutar directamente las clases principales desde la interfaz gráfica del IDE.

Si se tiene algún inconveniente con la ejecución, asegúrarse de que las variables de entorno de Java y Maven estén correctamente configuradas y de estar ubicado en la carpeta correspondiente antes de ejecutar los comandos.


---

1. Como primer paso, se crea el proyecto usando spring initializr y se modifica el poim.xml con las dependencias necesarias.
Se recomienda mirar el pom.xml y verificar las siguientes dependencias:

```` bash
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-websocket</artifactId>
		</dependency>
````

2. Previamente se construyo todo el FRONT, se encuentra en la carpeta src/main/resources/static.

3. Como en un comienzo el proyecto fue hecho con toda la lógica desde el FRONT, se migra dicha lógica al back con el modelo: MVC.

(Se tuvo que configurar el JDK)

4. Se comienza identificando el modelo que consta de dos clases jugador y y juego.

(Revisar en src/main/java/edu/eci/arsw/TicTacToe/model)

Se crea la clase player y se agregan los atributos principales getters y setters.

En la clase Game, se identifica toda la lógica para implementar un tablero concurrente, es decir, acá se encuentra toda 
la lógica de los hilos (threads).

5. Se agrega el controlador que, en pocas palabras, agrupa los endpoints en métodos que atienden peticiones HTTP.

(Revisar src/main/java/edu/eci/arsw/TicTacToe/controllers/TTTController.java)

6. Endpoints
7. Se añade la clase Config que permite 

