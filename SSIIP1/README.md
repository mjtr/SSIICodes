#Protección de un archivo
El código presentado en el archivo "protector.java" representa el funcionamiento (de manera simplificada) de un HIDS (SISTEMA DE DETECCIÓN	DE INTRUSOS	PARA HOST).

EL funcionamiento es muy sencillo: tenemos un archivo que queremos proteger, y dos ficheros de Logs (cada uno en un lugar distinto) que se encargarán de anotar cuándo el archivo que queremos proteger ha sido modificado y si han tocado alguno de ellos sin permiso.
Hacemos uso de los hash de los archivos para comprobar si éstos han sido o no modificados(se emplea SHA-256).
