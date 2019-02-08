# PAI: Conexiones SSL/TLS

La infraestructura de la aplicación ha sido llevada a cabo en el lenguaje de programación
“java”, siendo el entorno “eclipse” el seleccionado para desarrollarla y hacer los primeros
testeos (después hacemos uso de Sniffers como RawCap y WhireShark, y la terminal del
Ordenador).El ordenador empleado hace uso del sistema operativo Windows 10.

Lo que he hecho ha sido establecer una conexión segura entre el cliente y el
servidor mediante el uso de Sockets SSL. Para ello se ha tenido que generar los
correspondientes certificados, y configurar en el Path del PC la ruta exacta del almacén del
KeyStore de Java, para que al lanzar los respectivos comandos desde la terminal para
ejecutar la aplicación estos puedan ser reconocidos.

Una segunda opción menos engorrosa para ejecutar la aplicación y poder testearla ha sido
configurar, desde el propio código java, todo lo relacionado con la keyStore, y,
posteriormente, exportarlo todo a un .jar para que pueda ser lanzado desde la terminal con
un comando tan simple como: “java -jar NombreArchivo.jar”. Claro está el
certificado/keyStore tiene que estar generado, ya que es esencial su uso en el
establecimiento de la conexión segura.

La aplicación funciona de tal forma: el cliente solicita conectarse al servidor, el servidor le
pide tanto el nombre de usuario como la contraseña, si es correcto, le salta el cliente el
respectivo mensaje para que pueda mandar su mensaje secreto, sino se le niega la
conexión con el servidor.

Para que el cliente no tenga que estar conectándose más de una vez si quiere enviar más
de un mensaje, la conexión con él seguirá abierta después de haber mandado el primero,
es decir, se le solicitará mensajes hasta que el cliente ya no quiera mandar más, y para salir
de la conexión es tan simple como que él diga “quit”. Después de eso ya la conexión con el
servidor se cierra.

Para comprobar que ningún dato comprometido del cliente esté expuesto durante la
transmisión, hemos hecho uso de los analizadores de tráfico RawCap y Whireshark, y
hemos obtenido de conclusión que no ha sido posible obtener datos sobre el nombre del
usuario, la contraseña usada o el mensaje enviado, aunque sí que hemos podido ver el
Cipher Suite usado en la transmisión o los datos de la keystore utilizados por ejemplo.
En el intercambio de claves que se produce al inicio de la conexión hemos podido conseguir
estos dos datos, y se ha podido ver que tanto el cliente como el servidor utilizan la misma
keystore (en este caso se llama “za.store”).
También, en el propio código se ha especificado que se use esa keystore, por lo tanto,
aunque se conecten diferentes máquinas, la keystore que se empleará será la misma.

En el ámbito del cifrado, he visto que el cipher suite empleado es el:
“TLS_DHE_DSS_WITH_AES_256_CBC_SHA256” . Si lo analizamos podemos sacar que el
cifrado empleado es bastante seguro, ya que utiliza AES256, CBC Y SHA256.
La versión TLS empleada es la 1.2, que es la última que hay actualmente disponible, y
utiliza el algoritmo de Diffie-Hellman (DHE).
Sin embargo presenta un fallo grave y es que usa el cifrado DSS, uno que ya está en
desuso y es bastante vulnerable. Proponemos mejor usar el ​ ECDSA o RSA, quedando una
Cipher Suite tal que: “TLS_DHE_RSA_WITH_AES256_CBC_SHA256”.
Con este cipher suite se garantiza la seguridad y confidencialidad del mensaje y de los
datos del usuario(también se podría usar SHA384 para mayor seguridad, aunque
ralentizaría un poco más la transmisión ).
