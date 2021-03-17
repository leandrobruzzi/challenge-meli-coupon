# Challenge Meli Cupón

Este proyecto consiste en la resolución de un ejercicio técnico para ser evaluado y poder comenzar a formar parte del equipo de Mercado Libre! Se busca poder calcular de una forma eficiente la mejor combinación de productos para canjear por un cupón de regalo, basándonos en los productos favoritos del cliente.

## Comenzando 🚀

Para comenzar clonate el proyecto a tu pc y asegurate de tener instaladas todas las herramientas necesarias!
Estas son las Urls productivas por si queres ir chusmeando algo.
(Tene en cuenta que la primera vez en ingresar tarda bastante tiempo)
```
Simulador Coupon Fe: https://challenge-meli-coupon-fe.herokuapp.com/
API Rest Coupon:     https://challenge-meli-coupon.herokuapp.com/
```

### Pre-requisitos 📋
Aplicaciones y herramientas necesarias:

Obligatorias:
    JKD8 o superior, Maven, IntelliJ, Postman, Git bash 

Opcionales:
    Docker, Jmeter

```
Ejemplo configuracion variables de entorno:
JAVA_HOME = C:/Program Files/Java/jdk1.8.0_281
M2_HOME = C:/programas/apache-maven-3.6.3

En la variable Path agregar:
%JAVA_HOME%/bin
%M2_HOME%/bin

Abrir cmd y ejecutar "mvn -v" para verificar la correcta instalacion.

----------Ejemplo----------
C:/Users/Leandro>mvn -v
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: C:/programas/apache-maven-3.6.3/bin/..
Java version: 8.0.281, vendor: Oracle Corporation, runtime: C:/Program Files/Java/jdk1.8.0_281
Default locale: es_AR, platform encoding: Cp1252
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
---------------------------
```

### Instalación 🔧

Para tener nuestra API corriendo de forma local es muy sencillo. 

Opción con Docker: Solo basta con hacer doble click sobre D:/java/challenge-meli-coupon/startDocker.
Se iniciara la ejecución de un shell script en el cual se realizan los siguientes pasos:

```
mvn clean install
docker build
docker run (puerto 8080)
--------The container is ready--------
Press enter to stop container and remove image

Stopping docker container...
docker stop
docker rm
docker image rm
Done!
```

Opciones sin docker:
```
A) Ejecutar "mvn clean install" en la raiz del proyecto. 
Luego "cd target" y por ultimo "java -jar coupon-0.0.1-SNAPSHOT.jar"
```
```
B) Importar proyecto en IntelliJ ir a la clase CouponApplication y darle "Run"
```

Para verificar que la API este corriendo vamos a ejecutar el HealthCheck.
Desde un navegador ingresar a http://localhost:8080/ nos tiene que mostrar como respuesta un "OK".
También, desde postman, podes probar el servicio solicitado.
```
POST - http://localhost:8080/coupon
Body:
{
"item_ids": [
"MLA656039997",
"MLA839687279"
],
"amount": 600
}
```

## Ejecutando las pruebas ⚙️

Para ejecutar las pruebas podemos hacer un "mvn clean install". Tambien desde IntelliJ podemos iniciar las pruebas con Run With Coverage para poder ver la covertura de codigo que tenemos.


## Despliegue 📦

El despliegue se realiza en Heroku de forma automática al detectar un merge sobre la rama "master".
https://dashboard.heroku.com/apps

## Construido con 🛠️

* [Spring Initializr](https://start.spring.io/) - Creación de proyecto base
* [Maven](https://maven.apache.org/) - Gestor de dependencias

## Autores ✒️

* **Leandro Bruzzi** - *Trabajo Inicial & Documentación*



