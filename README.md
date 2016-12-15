# Survey backoffice

Este pequeño backoffice expone servicios para registrar una materia con sus horarios por comision, registrar un alumno al sistema, guardar una encuestra por alumno.
Las opciones para la encuestra de pre inscripcion son:
* Comision 
* APPROVED (Aprobada) 
* NOT_YET (Aun no la voy a cursar) 
* BAD_SCHEDULE (No puedo en ese horario)

## Crear oferta academica

POST: http://host:9090/subject
```javascript
{
	name: "Automatas",
	divisions: [
    	{
    		comision: "C1",
     		weekdays: ["Viernes de 18 a 22"],
	    	quota: 35
    	}
	],
	group: "basic",
	schoolYear:
}
```

## Ver oferta academica
GET: http://host:9090/subjects/:year

Respuesta:
```javascript
[
  {
    "subject_name": "Sistemas distribuidos",
    "date": [
      "C1: Jueves de 17 a 21"
    ],
    "options": [
      "NOT_YET",
      "APPROVED",
      "BAD_SCHEDULE"
    ]
  },
  {
    "subject_name": "Matematica 1",
    "date": [
      "C1: Lunes de 8 a 12, Jueves de 8 a 11",
      "C2: Lunes de 18 a 22, Jueves de 18 a 21"
    ],
    "options": [
      "NOT_YET",
      "APPROVED",
      "BAD_SCHEDULE"
    ]
  },
  {
    "subject_name": "Matematica 2",
    "date": [
      "C1: Lunes de 18 a 22, Jueves de 18 a 22"
    ],
    "options": [
      "NOT_YET",
      "APPROVED",
      "BAD_SCHEDULE"
    ]
  },
  {
    "subject_name": "Arquitectura 1",
    "date": [
      "C1: Lunes de 18 a 22"
    ],
    "options": [
      "NOT_YET",
      "APPROVED",
      "BAD_SCHEDULE"
    ]
  },
  {
    "subject_name": "Logica y programación",
    "date": [
      "C1: Martes de 18 a 22"
    ],
    "options": [
      "NOT_YET",
      "APPROVED",
      "BAD_SCHEDULE"
    ]
  },
  {
    "subject_name": "Teoria de la computación",
    "date": [
      "C1: Jueves de 18 a 22"
    ],
    "options": [
      "NOT_YET",
      "APPROVED",
      "BAD_SCHEDULE"
    ]
  },
  {
    "subject_name": "Lenguajes formales y automatas",
    "date": [
      "C1: Viernes de 18 a 22"
    ],
    "options": [
      "NOT_YET",
      "APPROVED",
      "BAD_SCHEDULE"
    ]
  },
  {
    "subject_name": "Probabilidad y estadistica",
    "date": [
      "C1: Miercoles de 18 a 21, Viernes de 18 a 21",
      "C2: Miercoles de 9 a 12, Viernes de 9 a 12",
      "C3: Lunes de 14 a 17, Jueves de 16 a 19"
    ],
    "options": [
      "NOT_YET",
      "APPROVED",
      "BAD_SCHEDULE"
    ]
  }
]
```

## Guardar encuesta
POST: http://host:9090/survey
```javascript
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

## Ver encuestra por legajo
GET: http://host:9090/survey/:legajo

Respuesta:
```javascript
{
  "id": "57fae120443a310499dd4a2e",
  "student_name": "Marina",
  "legajo": "34343",
  "selected_subjects": [
    {
      "subject": "Matematica 1",
      "status": "APPROVED"
    },
    {
      "subject": "Matematica 2",
      "status": "APPROVED"
    },
    {
      "subject": "Sistemas distribuidos",
      "status": "APPROVED"
    },
    {
      "subject": "Arquitectura 1",
      "status": "C1"
    },
    {
      "subject": "Logica y programación",
      "status": "APPROVED"
    },
    {
      "subject": "Teoria de la computación",
      "status": "C1"
    },
    {
      "subject": "Probabilidad y estadistica",
      "status": "NOT_YET"
    },
    {
      "subject": "Lenguajes formales y automatas",
      "status": "BAD_SCHEDULE"
    }
  ]
}
```

## Ver porcentaje de ocupacion de materias
GET: http://host:9090/survey/subjectsOccupation

Respuesta:
```javascript
[
  {
    "subject": "Lógica y programación",
    "comision": "C1: Lunes de 18 a 22, Jueves de 18 a 22",
    "occupation": 1,
    "percentage": 3
  },
  {
    "subject": "Automatas",
    "comision": "C1: Viernes de 18 a 22",
    "occupation": 5,
    "percentage": 14
  },
  {
    "subject": "Probabilidad y estadística",
    "comision": "C1: Lunes de 17 a 21, Jueves de 18 a 22",
    "occupation": 1,
    "percentage": 2
  },
  {
    "subject": "Probabilidad y estadística",
    "comision": "C2: Lunes de 8 a 12, Jueves de 8 a 12",
    "occupation": 1,
    "percentage": 2
  },
  {
    "subject": "Caracteristicas de lenguaje de programación",
    "comision": "C1: Lunes de 17 a 21, Jueves de 18 a 22",
    "occupation": 1,
    "percentage": 6
  },
  {
    "subject": "Sistemas distribuidos",
    "comision": "C1: Jueves de 17 a 21",
    "occupation": 2,
    "percentage": 8
  },
  {
    "subject": "Ingles 2",
    "comision": "C3: Miercoles de 14 a 18, Viernes de 14 a 18",
    "occupation": 1,
    "percentage": 2
  },
  {
    "subject": "Ingles 2",
    "comision": "C2: Lunes de 8 a 12, Jueves de 8 a 12",
    "occupation": 4,
    "percentage": 10
  }
]``