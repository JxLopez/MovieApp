# Movie App

## MVVM + Hilt + Retrofit + TMDB Api + Navigation Component + Flow + Clean architecture + Paging3 + Firestore + Firebase Storage + Room + WorkManager

# ¿Qué quieres ver?

Movie App te ayudará a buscar las mejores películas.

# Pantallas
## Lista de películas
Muestra las películas por más populares, más calificadas y recomendadas, se añadió un apartado "Tendencias". Esta información se consume desde el API de TmDb y se guarda de manera local, esto con el fin de que si no se cuenta con conexión a internet se pueda seguir viendo información.


<img width="337" alt="image" src="https://github.com/JxLopez/MovieApp/assets/5014603/b1ba7d5f-96dc-4753-aa2e-90beabbcb522">

## Perfil
Muestra la información del usuario como es su foto, nombre, username y la lista de películas calificadas. Esta información se consume desde el API de TmDb y se guarda de manera local.


<img width="337" alt="image" src="https://github.com/JxLopez/MovieApp/assets/5014603/967a998c-8fb5-4b8e-8318-1d9cd464f380">

## Ubicaciones
Consulta en Firestore las ubicaciones enviadas por el usuario cada 5 minutos.


<img width="337" alt="image" src="https://github.com/JxLopez/MovieApp/assets/5014603/869dd2f9-60a9-46a8-8c78-b9e3ba922114">

## Media
Sección donde podemos subir imágenes y las guarda en Firebase Storage, se usa WorkManager para subir en segundo plano y bloquear el uso de la app. Al momento de estar subiendo llas imágenes muestra una animación que se ejecuta con Lottie.


<img width="337" alt="image" src="https://github.com/JxLopez/MovieApp/assets/5014603/ab4f25b5-bebc-42f9-86fc-d929d3261cc0">

## Error
Dialog encargado de mostrar los errores que ocurren dentro de la app, contiene una animación que se ejecuta con Lottie.

<img width="258" alt="image" src="https://github.com/JxLopez/MovieApp/assets/5014603/213a7bd2-d51d-4a6a-8ff0-974168c765dc">

## Firestore
<img width="1130" alt="image" src="https://github.com/JxLopez/MovieApp/assets/5014603/057fee92-f1e8-4589-b6ed-e99610f2ec32">

## Storage
<img width="1130" alt="image" src="https://github.com/JxLopez/MovieApp/assets/5014603/91b32a5a-e5d6-4343-b2cc-4de094bbc91a">



# Tecnologías
Arquitectura MVVM: Es una arquitectura de software que elimina el estrecho acoplamiento entre los componentes. Lo más importante, en esta arquitectura, los hijos no tienen la referencia directa al padre, solo tienen la referencia por observables.
Android KTX: Conjunto de extensiones de Kotlin que se incluyen con Android Jetpack y otras bibliotecas de Android
AndroidX: reemplazan por completo la biblioteca de compatibilidad, ya que proporcionan paridad de funciones y bibliotecas nuevas.
Ciclo de vida: Realice acciones en respuesta a un cambio en el estado del ciclo de vida de otro componente, como actividades y fragmentos.
Room: Proporciona una capa de abstracción sobre SQLite que se utiliza para el almacenamiento en caché de datos sin conexión.
ViewModel: Diseñado para almacenar y administrar datos relacionados con la interfaz de usuario teniendo en cuenta el ciclo de vida.
Retrofit:Es un cliente HTTP con seguridad de tipo y admite corrutinas listas para usar.
Lottie: Lottie es una biblioteca que analiza las animaciones de Adobe After Effects exportadas como json con Bodymovin y las renderiza de forma nativa en dispositivos móviles y en la web.
Gson: JSON Parser, utilizado para analizar solicitudes en la capa de datos para Entidades y comprende los parámetros predeterminados y no anulables de Kotlin.
OkHttp Logging Interceptor: registra datos de solicitud y respuesta HTTP.
Corrutinas - Compatibilidad con la biblioteca para corrutinas.
Flow: los flujos se crean sobre corrutinas y pueden proporcionar múltiples valores. Un flujo es conceptualmente un flujo de datos que se puede calcular de forma asíncrona.
Hilt: Proporciona una forma estándar de incorporar la inyección de dependencia de Dagger en una aplicación de Android.
WorkManager: Es una biblioteca de Android que ejecuta en segundo plano procesos diferibles cuando se cumplen restricciones del trabajo.
Paging3: Ayuda a cargar y mostrar páginas de datos de un conjunto de datos más grande desde el almacenamiento local o la red

# Estructura del proyecto
![image](https://github.com/JxLopez/MovieApp/assets/5014603/0b73d8c3-c7ba-458d-a95e-637facb9761e)

- Data: Esta capa se encarga de todos la fuente de datos, tanto de red como del local
- Di: Contiene objects en donde se declaran los clases que se van a inyectar
- model: Contiene los modelos de datos.
- services: Aloja los servicios para la monitoreo y recuperación de ubicación
- ui: Capa que contiene todos los adapters, fragments y activity
- workers: Contiene las clases worker para la subida de imágenes.


# Diagramas

## Arquitectura MVVM
![1_-yY0l4XD3kLcZz0rO1sfRA](https://github.com/JxLopez/MovieApp/assets/5014603/d58622e0-c95e-4e83-9c8c-0b6b7beae64f)

## Paging3
<img width="809" alt="image" src="https://github.com/JxLopez/MovieApp/assets/5014603/a4d8b673-c2ab-4f2d-a404-aed21892fcb0">

## ¿Qué añadiría o mejoraría?
- Añadiría una sección de detalle para ver toda la información de las películas.
- Añadiría pruebas unitarias y de UI.
- Añadiría más animaciones.
- Añadiría una sección de búsqueda de películas.
- Mejoraría el apartado de "Media" para mostrar un carrete con todas las fotos ya subidas.
- Mejoraría la UI e implementaría Jetpack Compose.






