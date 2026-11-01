package com.ebc.laguichaesnegraapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Presentacion(val nombre: String, val tamano: String, val precio: Double)

data class Fragancia(
    val id: Int,
    val nombreInspiracion: String,
    val categoria: String,
    val familiaOlfativa: String,
    val notasOlfativas: String,
    val fijacion: String,
    val presentaciones: List<Presentacion>,
    val emojiIcono: String
)

val presentacionesEstandar = listOf(
    Presentacion("Perfume", "60 ml", 280.0),
    Presentacion("Hair Mist", "50 ml", 160.0),
    Presentacion("Crema corporal", "200 g", 190.0),
    Presentacion("Aceite esencial", "15 ml", 150.0)
)

val catalogoFragancias = listOf(
    Fragancia(1, "Newclews - Xerjoff", "Nicho", "Aromática Fougère", "Notas herbales, lavanda, menta, sándalo y almizcle blanco.", "Alta (8 a 12 h) - Libre de alcohol", presentacionesEstandar, "✨"),
    Fragancia(2, "Baccarat Rouge 540 - MFK", "Nicho", "Ámbar Floral", "Azafrán, jazmín, madera de ámbar, ámbar gris y resina de abeto.", "Muy alta (10 a 14 h)", presentacionesEstandar, "💎"),
    Fragancia(3, "Khamrah - Lattafa", "Árabe", "Especiada Gourmand", "Canela, nuez moscada, dátiles, praliné, vainilla y haba tonka.", "Muy alta (12 h)", listOf(Presentacion("Perfume", "60 ml", 260.0), Presentacion("Crema corporal", "200 g", 180.0), Presentacion("Aceite esencial", "15 ml", 140.0)), "🌙"),
    Fragancia(4, "Santal 33 - Le Labo", "Nicho", "Amaderada Aromática", "Sándalo, cedro de Virginia, cardamomo, violeta, papiro y cuero.", "Alta (8 a 10 h)", presentacionesEstandar, "🌿"),
    Fragancia(5, "Yara - Lattafa", "Árabe", "Ámbar Vainilla", "Orquídea, heliotropo, mandarina, vainilla y frutas tropicales.", "Media-Alta (7 a 9 h)", listOf(Presentacion("Perfume", "60 ml", 250.0), Presentacion("Hair Mist", "50 ml", 150.0), Presentacion("Crema corporal", "200 g", 180.0)), "🌸"),
    Fragancia(6, "Good Girl - Carolina Herrera", "Diseñador", "Ámbar Floral", "Almendra, café, nardos, jazmín sambac, haba tonka y cacao.", "Alta (8 a 10 h)", presentacionesEstandar, "👠"),
    Fragancia(7, "Erba Pura - Xerjoff", "Nicho", "Cítrica Frutal", "Naranja siciliana, bergamota, limón, frutas mediterráneas y almizcle.", "Muy alta (12 h)", presentacionesEstandar, "🍊")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LaGuichaEsNegraApp() }
    }
}

@Composable
fun LaGuichaEsNegraApp() {
    var modoOscuro by remember { mutableStateOf(true) }
    var usuarioEmail by remember { mutableStateOf("") }
    var usuarioNombre by remember { mutableStateOf("") }
    var estaLogueado by remember { mutableStateOf(false) }
    var pantallaActual by remember { mutableStateOf("catalogo") }
    var pestanaInferior by remember { mutableStateOf("buscar") }
    var fraganciaSeleccionada by remember { mutableStateOf(catalogoFragancias.first()) }
    var favoritosIds by remember { mutableStateOf(setOf(1)) }
    var historialPedidos by remember { mutableStateOf(listOf<String>()) }
    var notificacionesActivas by remember { mutableStateOf(true) }

    val esquemaColores = if (modoOscuro) {
        darkColorScheme(
            primary = Color(0xFFE5A9B4), onPrimary = Color(0xFF1A161B),
            secondary = Color(0xFFD4AF37), background = Color(0xFF121216),
            surface = Color(0xFF1E1E26), onBackground = Color(0xFFF5F5F7), onSurface = Color(0xFFF5F5F7)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF9C4163), onPrimary = Color.White,
            secondary = Color(0xFF6B5322), background = Color(0xFFFFF8F9),
            surface = Color.White, onBackground = Color(0xFF1F1A1C), onSurface = Color(0xFF1F1A1C)
        )
    }

    MaterialTheme(colorScheme = esquemaColores) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (!estaLogueado) {
                PantallaAcceso { email, nombre ->
                    usuarioEmail = email
                    usuarioNombre = nombre
                    estaLogueado = true
                    pantallaActual = "catalogo"
                }
            } else {
                Scaffold(
                    bottomBar = {
                        BarraNavegacionInferior(pestanaInferior, favoritosIds.size) { nueva ->
                            pestanaInferior = nueva
                            pantallaActual = "catalogo"
                        }
                    }
                ) { padding ->
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        when (pantallaActual) {
                            "catalogo" -> PantallaCatalogo(
                                pestanaActiva = pestanaInferior,
                                favoritosIds = favoritosIds,
                                alAlternarFavorito = { id ->
                                    favoritosIds = if (favoritosIds.contains(id)) favoritosIds - id else favoritosIds + id
                                },
                                alSeleccionarFragancia = { frag ->
                                    fraganciaSeleccionada = frag
                                    pantallaActual = "detalle"
                                },
                                alAbrirPerfil = { pantallaActual = "perfil" }
                            )
                            "detalle" -> PantallaDetalle(
                                fragancia = fraganciaSeleccionada,
                                esFavorito = favoritosIds.contains(fraganciaSeleccionada.id),
                                alAlternarFavorito = {
                                    val id = fraganciaSeleccionada.id
                                    favoritosIds = if (favoritosIds.contains(id)) favoritosIds - id else favoritosIds + id
                                },
                                alRegistrarPedido = { historialPedidos = historialPedidos + it },
                                alVolver = { pantallaActual = "catalogo" },
                                alAbrirPerfil = { pantallaActual = "perfil" }
                            )
                            "perfil" -> PantallaPerfil(
                                nombre = usuarioNombre,
                                email = usuarioEmail,
                                cantidadSalvados = favoritosIds.size,
                                pedidos = historialPedidos,
                                notificacionesActivas = notificacionesActivas,
                                alCambiarNotificaciones = { notificacionesActivas = it },
                                modoOscuro = modoOscuro,
                                alCambiarModoOscuro = { modoOscuro = it },
                                alIrASalvados = { pestanaInferior = "favoritos"; pantallaActual = "catalogo" },
                                alVolver = { pantallaActual = "catalogo" },
                                alCerrarSesion = { estaLogueado = false; usuarioEmail = ""; usuarioNombre = "" }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaAcceso(alAutenticar: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    fun validar(esRegistro: Boolean) {
        val limpio = email.trim()
        when {
            limpio.isEmpty() || contrasena.isEmpty() -> error = "Validación: Completa el correo y la contraseña."
            !limpio.contains("@") || !limpio.contains(".") -> error = "Validación: Ingresa un correo válido (ejemplo@correo.com)."
            contrasena.length < 4 -> error = "Validación: La contraseña debe tener mínimo 4 caracteres."
            else -> {
                val nom = limpio.substringBefore("@").replaceFirstChar { it.uppercase() }
                alAutenticar(limpio, if (esRegistro) "$nom (Nuevo)" else nom)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(110.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🐈‍⬛", fontSize = 40.sp)
                Text("LA GÜICHA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("La Güicha Es Negra", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Alta perfumería e inspiraciones olfativas", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        Spacer(Modifier.height(24.dp))
        Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it; error = "" }, label = { Text("email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = contrasena, onValueChange = { contrasena = it; error = "" }, label = { Text("contraseña") }, visualTransformation = PasswordVisualTransformation(), singleLine = true, modifier = Modifier.fillMaxWidth())
        if (error.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = Color(0xFFFF6B6B), fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { validar(true) }, modifier = Modifier.weight(1f)) { Text("Registrarse") }
            Button(onClick = { validar(false) }, modifier = Modifier.weight(1f)) { Text("Entrar") }
        }
    }
}

@Composable
fun PantallaCatalogo(
    pestanaActiva: String,
    favoritosIds: Set<Int>,
    alAlternarFavorito: (Int) -> Unit,
    alSeleccionarFragancia: (Fragancia) -> Unit,
    alAbrirPerfil: () -> Unit
) {
    var busqueda by remember { mutableStateOf("") }
    var catFiltro by remember { mutableStateOf("Todas") }
    var presFiltro by remember { mutableStateOf("Todas") }
    val categorias = listOf("Todas", "Nicho", "Árabe", "Diseñador")
    val presentaciones = listOf("Todas", "Perfume", "Hair Mist", "Crema corporal", "Aceite esencial")

    val filtradas = catalogoFragancias.filter { f ->
        val okTexto = busqueda.isBlank() || f.nombreInspiracion.contains(busqueda, true) || f.notasOlfativas.contains(busqueda, true)
        val okFav = if (pestanaActiva == "favoritos") favoritosIds.contains(f.id) else true
        val okCat = catFiltro == "Todas" || f.categoria == catFiltro
        val okPres = presFiltro == "Todas" || f.presentaciones.any { it.nombre.equals(presFiltro, true) }
        okTexto && okFav && okCat && okPres
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(
                onClick = alAbrirPerfil,
                modifier = Modifier.size(42.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface).border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
            ) { Text("👤", fontSize = 18.sp) }
            Column(horizontalAlignment = Alignment.End) {
                Text("La Güicha Es Negra", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(if (pestanaActiva == "favoritos") "Favoritos / Salvados" else if (pestanaActiva == "filtros") "Filtros de Catálogo" else "Catálogo", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = busqueda, onValueChange = { busqueda = it },
            leadingIcon = { Text("🔍") },
            trailingIcon = { if (busqueda.isNotEmpty()) IconButton(onClick = { busqueda = "" }) { Text("✕") } },
            placeholder = { Text("Buscar por nombre, marca o nota...") },
            singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
        )
        if (pestanaActiva == "filtros" || catFiltro != "Todas" || presFiltro != "Todas") {
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Línea:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(categorias) { c ->
                            val activo = catFiltro == c
                            Button(
                                onClick = { catFiltro = c },
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                    contentColor = if (activo) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(c, fontSize = 12.sp, fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Presentación:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(presentaciones) { p ->
                            val activo = presFiltro == p
                            Button(
                                onClick = { presFiltro = p },
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                    contentColor = if (activo) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(p, fontSize = 12.sp, fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
        Text("${filtradas.size} resultados", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 6.dp))
        if (filtradas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay fragancias que coincidan.", textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
                items(filtradas) { frag ->
                    val disp = frag.presentaciones.joinToString(", ") { it.nombre.lowercase() }
                    val esFav = favoritosIds.contains(frag.id)
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { alSeleccionarFragancia(frag) },
                        shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(modifier = Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.background)) {
                                IconButton(onClick = { alAlternarFavorito(frag.id) }, modifier = Modifier.align(Alignment.TopStart)) {
                                    Text(if (esFav) "♥" else "♡", fontSize = 22.sp, color = if (esFav) Color(0xFFFF4D6D) else MaterialTheme.colorScheme.onBackground)
                                }
                                Text(frag.categoria, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.align(Alignment.TopEnd).padding(10.dp))
                                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(frag.emojiIcono, fontSize = 38.sp)
                                    Text(frag.familiaOlfativa, fontSize = 12.sp)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(frag.nombreInspiracion, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Disponible en $disp", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(4.dp))
                            Text(frag.notasOlfativas, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaDetalle(
    fragancia: Fragancia,
    esFavorito: Boolean,
    alAlternarFavorito: () -> Unit,
    alRegistrarPedido: (String) -> Unit,
    alVolver: () -> Unit,
    alAbrirPerfil: () -> Unit
) {
    val context = LocalContext.current
    var presElegida by remember(fragancia) { mutableStateOf(fragancia.presentaciones.first()) }
    var esRefill by remember { mutableStateOf(false) }
    var cantidad by remember { mutableStateOf(1) }
    var dialogoCompra by remember { mutableStateOf(false) }

    val desc = if (esRefill && presElegida.nombre == "Perfume") 30.0 else 0.0
    val total = (presElegida.precio - desc) * cantidad
    val disp = fragancia.presentaciones.joinToString(", ") { it.nombre.lowercase() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = alAbrirPerfil, modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface)) { Text("👤") }
            TextButton(onClick = alVolver) { Text("← Volver al catálogo") }
        }
        Spacer(Modifier.height(8.dp))
        Text(fragancia.nombreInspiracion, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Disponible en $disp", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.surface)) {
            IconButton(onClick = alAlternarFavorito, modifier = Modifier.align(Alignment.TopStart)) {
                Text(if (esFavorito) "♥" else "♡", fontSize = 24.sp, color = if (esFavorito) Color(0xFFFF4D6D) else MaterialTheme.colorScheme.onSurface)
            }
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(fragancia.emojiIcono, fontSize = 52.sp)
                Text("${fragancia.categoria} • ${fragancia.familiaOlfativa}", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Notas: ${fragancia.notasOlfativas}", fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                Text("Fijación: ${fragancia.fijacion}", fontSize = 13.sp)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Selecciona presentación:", fontWeight = FontWeight.Bold)
        fragancia.presentaciones.forEach { p ->
            val sel = presElegida.nombre == p.nombre
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(8.dp))
                    .background(if (sel) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface)
                    .clickable { presElegida = p }.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${if (sel) "✓ " else ""}${p.nombre} (${p.tamano})", fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal)
                Text("$${p.precio.toInt()} MXN", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }
        if (presElegida.nombre == "Perfume") {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Modalidad Refill ecológico (-$30 MXN):", fontSize = 13.sp)
                Switch(checked = esRefill, onCheckedChange = { esRefill = it })
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Cantidad:", fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = { if (cantidad > 1) cantidad-- }) { Text("-") }
                Text("$cantidad", modifier = Modifier.padding(horizontal = 14.dp), fontWeight = FontWeight.Bold)
                OutlinedButton(onClick = { if (cantidad < 10) cantidad++ }) { Text("+") }
            }
        }
        Text("Total: $${total.toInt()} MXN", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    alRegistrarPedido("${cantidad}x ${fragancia.nombreInspiracion} (${presElegida.nombre}) = $${total.toInt()} MXN")
                    dialogoCompra = true
                },
                modifier = Modifier.weight(1f)
            ) { Text("🛒 Comprar") }
            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "La Güicha Es Negra: ${fragancia.nombreInspiracion} disponible en $disp desde $${presElegida.precio.toInt()} MXN.")
                    }
                    context.startActivity(Intent.createChooser(intent, "Compartir"))
                },
                modifier = Modifier.weight(1f)
            ) { Text("↗ Compartir") }
        }
    }
    if (dialogoCompra) {
        AlertDialog(
            onDismissRequest = { dialogoCompra = false },
            title = { Text("¡Pedido registrado! 🐈‍⬛") },
            text = { Text("${cantidad}x ${fragancia.nombreInspiracion} (${presElegida.nombre})\nTotal: $${total.toInt()} MXN\nGuardado en tu Perfil.") },
            confirmButton = { Button(onClick = { dialogoCompra = false }) { Text("Aceptar") } }
        )
    }
}

@Composable
fun PantallaPerfil(
    nombre: String, email: String, cantidadSalvados: Int, pedidos: List<String>,
    notificacionesActivas: Boolean, alCambiarNotificaciones: (Boolean) -> Unit,
    modoOscuro: Boolean, alCambiarModoOscuro: (Boolean) -> Unit,
    alIrASalvados: () -> Unit, alVolver: () -> Unit, alCerrarSesion: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth()) { TextButton(onClick = alVolver) { Text("← Volver") } }
        Box(modifier = Modifier.size(84.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape), contentAlignment = Alignment.Center) {
            Text("👤", fontSize = 38.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(nombre.ifEmpty { "Usuaria La Güicha" }, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(email, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        Spacer(Modifier.height(18.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("👤 Información Personal", fontWeight = FontWeight.Bold)
                Text("Correo: $email | Pedidos: ${pedidos.size}", fontSize = 13.sp)
                pedidos.forEach { Text("• $it", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary) }
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("🔔 Notificaciones", fontWeight = FontWeight.Bold)
                Switch(checked = notificacionesActivas, onCheckedChange = alCambiarNotificaciones)
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxWidth().clickable { alIrASalvados() }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("♥ Salvados", fontWeight = FontWeight.Bold)
                Text("$cantidadSalvados guardados →", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("⚙️ Ajustes", fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Modo Oscuro", fontSize = 14.sp)
                    Switch(checked = modoOscuro, onCheckedChange = alCambiarModoOscuro)
                }
                OutlinedButton(onClick = alCerrarSesion, modifier = Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
            }
        }
    }
}

@Composable
fun BarraNavegacionInferior(pestanaActual: String, totalFavoritos: Int, alSeleccionarPestana: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(selected = pestanaActual == "buscar", onClick = { alSeleccionarPestana("buscar") }, icon = { Text("🔍") }, label = { Text("Buscar") })
        NavigationBarItem(selected = pestanaActual == "favoritos", onClick = { alSeleccionarPestana("favoritos") }, icon = { Text("♥", color = Color(0xFFFF4D6D)) }, label = { Text("Favoritos ($totalFavoritos)") })
        NavigationBarItem(selected = pestanaActual == "filtros", onClick = { alSeleccionarPestana("filtros") }, icon = { Text("🎚️") }, label = { Text("Filtros") })
    }
}