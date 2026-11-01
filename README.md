✨ Descripción del Proyecto

La Güicha Es Negra App es una aplicación para Android desarrollada con Kotlin y Jetpack Compose que permite a los amantes de la perfumería explorar fragancias de nicho, árabes y de diseñador, verificando en un solo lugar la disponibilidad inmediata de presentaciones complementarias para rutinas de layering (Perfume, Hair Mist, Crema corporal y Aceite esencial).
Problema que resuelve

Facilita la consulta ágil de aromas, notas olfativas y formatos disponibles sin depender de catálogos estáticos, permitiendo filtrar productos en tiempo real, resguardar una lista de deseos (Favoritos / Salvados) y calcular automáticamente el costo de cada pedido (incluyendo la modalidad de Refill ecológico).
🚀 Funcionalidades Principales (Fase 3)

    Acceso con Validaciones de Entrada:

        Inicio de sesión y registro con validación de campos vacíos, formato de correo electrónico (@ y .) y longitud mínima de contraseña (4 caracteres).

    Exploración y Búsqueda en Tiempo Real:

        Buscador dinámico por nombre de inspiración, casa perfumera o nota olfativa con contador instantáneo de resultados ("X resultados").

    Filtros Combinables de Alto Contraste:

        Filtrado interactivo por línea (Todas, Nicho, Árabe, Diseñador) y por presentación disponible (Perfume, Hair Mist, Crema corporal, Aceite esencial).

    Gestión de Favoritos (Salvados):

        Marcado y desmarcado rápido mediante el ícono de corazón (♡ / ♥) en tarjetas y vista de detalle, con contador actualizado en la barra de navegación inferior.

    Detalle de Producto y Cálculo de Compra:

        Consulta de familia olfativa, notas y fijación estimada.

        Selector interactivo de presentación, interruptor de descuento para Modalidad Refill ecológico (-$30 MXN) en perfumes y control de cantidad (- / +) con cálculo automático del total.

        Registro de pedidos en el historial de la sesión y botón Compartir conectado al selector nativo de Android (Intent.ACTION_SEND).

    Perfil de Usuario y Tema Adaptable:

        Consulta de datos de la sesión activa, historial de pedidos realizados, control de notificaciones, acceso rápido a Salvados y cambio en tiempo real entre Modo Oscuro y Modo Claro.

🛠️ Tecnologías Utilizadas

    Lenguaje: Kotlin

    UI Toolkit: Jetpack Compose (Material Design 3)

    Arquitectura y Estado: Manejo de estado reactivo en memoria (remember / mutableStateOf) autocontenido en MainActivity.kt para garantizar compilación inmediata sin dependencias externas adicionales.

    Entorno de Desarrollo: Android Studio


📝 Observaciones de Implementación (Fases 1-2 vs. Fase 3)

    Cero controles sin acción: Se retiró el ícono de menú hamburguesa superior contemplado en el boceto inicial para concentrar toda la navegación funcional en la barra inferior (Buscar, Favoritos, Filtros) y en el botón superior de acceso al Perfil.

    Interacción y cálculos visibles: La lista de presentaciones disponibles se implementó como un selector interactivo que calcula el importe total en función del formato, modalidad Refill y número de piezas.

    Persistencia de sesión: Conforme a los lineamientos de alcance de la Fase 3, la autenticación, los favoritos y los pedidos se gestionan en memoria durante la sesión activa sin requerir conexión a bases de datos externas.
