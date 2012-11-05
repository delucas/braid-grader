# Grader daemon

Un demonio hecho en **groovy** para recibir solicitudes de ejecución de corrección sobre un repositorio git, previamente diseñado el ambiente de test para el mismo.

## Funcionamiento

Un socket programado en Groovy que escucha en un puerto del servidor, y al recibir un JSON que matchea con el protocolo, ejecuta los pasos de la corrección correspondiente.

## Cómo colaborar

Simplemente hacer un fork del proyecto, realizar las modificaciones y efectuar un pull-request (el mecanismo de colaboración estándar de GitHub).
No necesitás hacer gran código para colaborar... simplemente aportando ideas, documentación o mejorando la legibilidad se puede llegar a incrementar el valor de la herramienta.

## Próximos pasos

* Agregar más herramientas de corrección, con la configuración correspondiente (Cobertura, PMD, CPD, Emma, etc.)
* Soportar más lenguajes de programación (Roadmap sugerido: Groovy, Grails, C++, C, etc.)

## Créditos (hasta el momento)

* [Matías González](http://www.twitter.com/matitanio)
* [Lucas Videla](http://www.twitter.com/luke_ar)
