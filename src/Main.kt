import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Ejemplo de uso del sistema de cadena de supermercados
 * demuestra todas las funcionalidades implementadas
 */
fun main() {
    println("Sistema de Cadena de Supermercados")

    // Crear productos
    val productos = listOf(
        Producto(1, "Carne", 10.0),
        Producto(2, "Pescado", 20.0),
        Producto(3, "Pollo", 30.0),
        Producto(4, "Cerdo", 45.0),
        Producto(5, "Ternera", 50.0),
        Producto(6, "Cordero", 65.0)
    )

    println("Productos registrados:")
    productos.forEach { println("${it.nombre}: $${it.precio}") }
    

    // Crear horarios
    val horariosSemanales = HorarioSemanal(
        Horario(LocalTime.of(8, 0), LocalTime.of(20, 0)),
        setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
    )

    val horario247 = HorarioSemanal(
        Horario(LocalTime.of(0, 0), LocalTime.of(23, 59)),
        DayOfWeek.values().toSet()
    )

    // Crear supermercados
    val supermercadoA = Supermercado(1, "Supermercado A", horariosSemanales)
    val supermercadoB = Supermercado(2, "Supermercado B", horario247)
    val supermercadoC = Supermercado(3, "Supermercado C", horariosSemanales)

    // Agregar productos a los supermercados
    productos.forEach { producto ->
        supermercadoA.addProducto(producto, 100)
        supermercadoB.addProducto(producto, 150)
        supermercadoC.addProducto(producto, 200)
    }

    println("Supermercados creados:")
    println("${supermercadoA.nombre} (Lun-Vie 8:00-20:00)")
    println("${supermercadoB.nombre} (24/7)")
    println("${supermercadoC.nombre} (Lun-Vie 8:00-20:00)")
    

    // Crear cadena y registrar supermercados
    val cadena = SupermercadoChain()
    productos.forEach { cadena.registrarProducto(it) }
    cadena.addSupermercado(supermercadoA)
    cadena.addSupermercado(supermercadoB)
    cadena.addSupermercado(supermercadoC)

    println("Simulación de Ventas")

    // Simular ventas en Supermercado A
    println("Ventas en ${supermercadoA.nombre}:")
    val ventaA1 = supermercadoA.registrarVenta(1, 50) // Carne
    println("50 unidades de Carne: $$ventaA1")
    val ventaA2 = supermercadoA.registrarVenta(3, 30) // Pollo
    println("30 unidades de Pollo: $$ventaA2")
    val ventaA3 = supermercadoA.registrarVenta(2, 20) // Pescado
    println("20 unidades de Pescado: $$ventaA3")
    

    // Simular ventas en Supermercado B
    println("Ventas en ${supermercadoB.nombre}:")
    val ventaB1 = supermercadoB.registrarVenta(1, 70) // Carne
    println("70 unidades de Carne: $$ventaB1")
    val ventaB2 = supermercadoB.registrarVenta(2, 40) // Pescado
    println("40 unidades de Pescado: $$ventaB2")
    val ventaB3 = supermercadoB.registrarVenta(5, 25) // Ternera
    println("25 unidades de Ternera: $$ventaB3")
    

    // Simular ventas en Supermercado C
    println("Ventas en ${supermercadoC.nombre}:")
    val ventaC1 = supermercadoC.registrarVenta(3, 60) // Pollo
    println("60 unidades de Pollo: $$ventaC1")
    val ventaC2 = supermercadoC.registrarVenta(4, 35) // Cerdo
    println("35 unidades de Cerdo: $$ventaC2")
    

    // Consultas individuales de supermercados
    println("Información de Supermercados")

    println("${supermercadoA.nombre}:")
    println("Cantidad vendida de Carne: ${supermercadoA.getCantProdVendida(1)} unidades")
    println("Ingresos por Carne: $${supermercadoA.getFacturacionProducto(1)}")
    println("Ingresos totales: $${supermercadoA.getTotalFacturado()}")
    

    println("${supermercadoB.nombre}:")
    println("Ingresos totales: $${supermercadoB.getTotalFacturado()}")
    

    println("${supermercadoC.nombre}:")
    println("Ingresos totales: $${supermercadoC.getTotalFacturado()}")
    

    // Consultas de la cadena
    println("Informacion de la Cadena")

    println("Top 5 productos mas vendidos:")
    println("${cadena.getTop5Productos()}")
    

    println("Ingresos totales de la cadena: $${cadena.getTotalFacturado()}")
    

    println("Supermercado con mayores ingresos:")
    println("  ${cadena.getTopFacturadoSupermercado()}")
    

    //supermercados abiertos
    println("Consulta de Horarios")

    val testDay = DayOfWeek.MONDAY
    val testTime = LocalTime.of(10, 0)
    println("Supermercados abiertos el $testDay a las $testTime:")
    val supermercadosAbiertos = cadena.getSupermercadosAbiertos(testDay, testTime)
    println("  $supermercadosAbiertos")
    

    val testDay2 = DayOfWeek.SUNDAY
    val testTime2 = LocalTime.of(15, 0)
    println("Supermercados abiertos el $testDay2 a las $testTime2:")
    val supermercadosAbiertos2 = cadena.getSupermercadosAbiertos(testDay2, testTime2)
    if (supermercadosAbiertos2.isEmpty()) {
        println("No hay supermercados abiertos")
    } else {
        println("$supermercadosAbiertos2")
    }
}