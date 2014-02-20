# Grader daemon

[![Historias en Ready](https://badge.waffle.io/delucas/braid-grader.png?label=ready)](https://waffle.io/delucas/braid-grader)  
[![Build Status](https://travis-ci.org/delucas/braid-grader.png?branch=master)](https://travis-ci.org/delucas/braid-grader)

Un demonio hecho en **groovy** para recibir solicitudes de ejecución de corrección sobre un repositorio git, previamente diseñado el ambiente de test para el mismo.

## Funcionamiento

Un demonio programado en Groovy que se acopla a una cola, y al recibir un JSON que matchea con el protocolo, ejecuta los pasos de la corrección correspondiente.

## Cómo colaborar

Simplemente hacer un fork del proyecto, realizar las modificaciones y efectuar un pull-request (el mecanismo de colaboración estándar de GitHub).
No necesitás hacer gran código para colaborar... simplemente aportando ideas, documentación o mejorando la legibilidad se puede llegar a incrementar el valor de la herramienta.

## Próximos pasos

* Agregar más herramientas de corrección, con la configuración correspondiente (Cobertura, PMD, CPD, Emma, etc.)
* Soportar más lenguajes de programación (Roadmap sugerido: Groovy, Grails, C++, C, etc.)
