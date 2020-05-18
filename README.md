Este proyecto representa un CALCULADORA SIMPLE la cual soporta las siguientes operaciones:

	- Suma
	- Resta
	- Multipliación
	- División

Tambien nos brinda la posibilidad de GUARDAR UNA SESIÓN DE CÁLCULO como asi tambien la de RECUPERAR UNA SESIÓN DE CÁLCULO ALMACENADA.

HERRAMIENTAS UTILIZADAS:

Maven: Automatiza el build (construye el software) describiendo sus dependencias y como es el proceso de build. Utiliza un achivo llamado pom.xml para la descripción de las dependencias POM (Project Object Model).

MySql: Base de datos relacional utilizada.

ActiveJDBC: Es el intermediario entre nuestra aplicación y la base de datos. Es un ORM (Object Relational Mapping) rápido y ágil.

Spark: El motor de cálculo de esta aplicación esta en un servidor JAVA en particular SPARK.

REST: Interfaz en REST.

ACLARACIONES:

Una vez clonado el proyecto debera: 

	- Dirigirse a la linea 93 del archivo pom.xml (<mainClass>/home/agustin/Documents/calculator-app/src/main/java/calculator/App</mainClass>) y cambiar la dirección que aparece por la nueva direccion donde se encuentra la App en se PC.

	- Correr el archivo /calculator/scriptSQL/createDB.sql el cual genera la base de datos con sus respectivas tablas en MySql.

	-Dirigirse a /calculator/calculator-app/src/main/java/calculator/db/dbConexion.java y cambiar el usuario y contraeña por su usuario de mysql con su respectiva contraseña, para que no exista algun problema en la conexión.

EJECUTAR APLICACIÓN:

Desde consola: Abrir una consola dentro de /calculator/calculator-app, luego correr el archivo run.sh como ./run.sh
Un vez ejecutado el ./run.sh el mismo correra todo el poryecto, cargando sus respectivas dependencias definidad en el POM.xml. Cuando esto finaliza le aparecera algo del estilo {0.0.0.0:4567}, ahora vaya al navegador y ponga               http://localhost:4567/init y la aplicación arrancara con exito.