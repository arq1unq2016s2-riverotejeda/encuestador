# Survey backoffice

Este pequeño backoffice expone servicios para registrar una materia con sus horarios por comision, registrar un alumno al sistema, guardar una encuestra por alumno.
Las opciones para la encuestra de pre inscripcion son:
* Comision 
* APPROVED (Aprobada) 
* NOT_YET (Aun no la voy a cursar) 
* BAD_SCHEDULE (No puedo en ese horario)

#### Crear oferta academica

POST: http://host:9090/subject
```json
{
	name: "Probabilidad y estadistica",
	dates: { C1: ["Miercoles de 18 a 21", "Viernes de 18 a 21"], 
			 C2: ["Miercoles de 9 a 12", "Viernes de 9 a 12"], 
			 C3: ["Lunes de 14 a 17", "Jueves de 16 a 19"] 
			}
	
}
```

#### Ver oferta academica
GET: http://host:9090/subjects

#### Guardar encuesta
POST: http://host:9090/survey
```json
{
	student_name: "Marina",
	legajo: "34343",
	selected_subjects: [
		{
			subject: "Matematica 1",
			status: "APPROVED"
			
		},
		{
			subject: "Matematica 2", 
			status: "APPROVED"
		},
		{
			subject: "Sistemas distribuidos", 
			status: "APPROVED"
		},
		{
			subject: "Arquitectura 1", 
			status: "C1"
		},
		{
			subject: "Logica y programación", 
			status: "APPROVED"
		},
		{
			subject: "Teoria de la computación", 
			status: "C1"
		},
		{
			subject: "Probabilidad y estadistica", 
			status: "NOT_YET"
		},
		{
			subject: "Lenguajes formales y automatas", 
			status: "BAD_SCHEDULE"
		}]
}
```

#### Ver encuestra por lejago
GET: http://host:9090/survey/:legajo