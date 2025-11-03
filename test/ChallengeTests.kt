import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalTime

class ChallengeTests {

    /**
     * Datos de prueba:
     *  # Productos [id, nombre, precio]
     *      - 1, "Carne", 10.0
     *      - 2, "Pescado", 20.0
     *      - 3, "Pollo", 30.0
     *      - 4, "Cerdo", 45.0
     *      - 5, "Ternera", 50.0
     *      - 6, "Cordero", 65.0
     *
     *  # Supermercado [id, nombre]
     *    - 1, "Supermercado A"
     *    - 2, "Supermercado B"
     *    - 3, "Supermercado C"
     * */

    private lateinit var productos: List<Producto>
    private lateinit var supermercadoA: Supermercado
    private lateinit var supermercadoB: Supermercado
    private lateinit var supermercadoC: Supermercado
    private lateinit var cadena: CadenaSupercado

    @BeforeEach
    fun setUp() {
        // Crear productos
        productos = listOf(
            Producto(1, "Carne", 10.0),
            Producto(2, "Pescado", 20.0),
            Producto(3, "Pollo", 30.0),
            Producto(4, "Cerdo", 45.0),
            Producto(5, "Ternera", 50.0),
            Producto(6, "Cordero", 65.0)
        )

        // Crear supermercados
        supermercadoA = Supermercado(1, "Supermercado A")
        supermercadoB = Supermercado(2, "Supermercado B")
        supermercadoC = Supermercado(3, "Supermercado C")

        // Agregar productos a los supermercados con diferentes stocks
        productos.forEach { producto ->
            supermercadoA.addProducto(producto, 100)
            supermercadoB.addProducto(producto, 150)
            supermercadoC.addProducto(producto, 200)
        }

        // Crear cadena y agregar supermercados
        cadena = CadenaSupercado()
        productos.forEach { cadena.registrarProducto(it) }
        cadena.addSupermercado(supermercadoA)
        cadena.addSupermercado(supermercadoB)
        cadena.addSupermercado(supermercadoC)
    }

    // ========== Tests de Producto ==========

    @Test
    fun `producto con precio valido se crea correctamente`() {
        val producto = Producto(1, "Test", 10.0)
        assertEquals(1, producto.id)
        assertEquals("Test", producto.nombre)
        assertEquals(10.0, producto.precio)
    }

    @Test
    fun `producto con precio negativo lanza excepcion`() {
        assertThrows(IllegalArgumentException::class.java) {
            Producto(1, "Test", -10.0)
        }
    }

    @Test
    fun `producto con nombre vacio lanza excepcion`() {
        assertThrows(IllegalArgumentException::class.java) {
            Producto(1, "", 10.0)
        }
    }

    // ===== Tests de Supermercado - Ventas ==========

    @Test
    fun `registrar venta retorna precio total correcto`() {
        val total = supermercadoA.registrarVenta(1, 5) // 5 carnes a 10.0
        assertEquals(50.0, total)
    }

    @Test
    fun `registrar venta actualiza stock correctamente`() {
        val stockInicial = supermercadoA.getStockProducto(1)
        supermercadoA.registrarVenta(1, 5)
        assertEquals(stockInicial - 5, supermercadoA.getStockProducto(1))
    }

    @Test
    fun `registrar venta sin stock suficiente lanza excepcion`() {
        assertThrows(IllegalStateException::class.java) {
            supermercadoA.registrarVenta(1, 1000)
        }
    }

    @Test
    fun `registrar venta de producto inexistente lanza excepcion`() {
        assertThrows(IllegalStateException::class.java) {
            supermercadoA.registrarVenta(999, 1)
        }
    }

    @Test
    fun `obtener cantidad vendida de producto`() {
        supermercadoA.registrarVenta(1, 10)
        supermercadoA.registrarVenta(1, 5)
        assertEquals(15, supermercadoA.getCantProdVendida(1))
    }

    @Test
    fun `obtener ingresos por ventas de un producto`() {
        supermercadoA.registrarVenta(1, 10) // 10 * 10.0 = 100.0
        assertEquals(100.0, supermercadoA.getFacturacionProducto(1))
    }

    @Test
    fun `obtener ingresos totales del supermercado`() {
        supermercadoA.registrarVenta(1, 10) // 10 * 10.0 = 100.0
        supermercadoA.registrarVenta(2, 5)  // 5 * 20.0 = 100.0
        assertEquals(200.0, supermercadoA.getFacturacionTotal())
    }

    // ========== Tests de CadenaSupercado ==========

    @Test
    fun `obtener top 5 productos mas vendidos`() {
        // Simular ventas en diferentes supermercados
        supermercadoA.registrarVenta(1, 100) // Carne: 100
        supermercadoB.registrarVenta(1, 50)  // Carne: +50 = 150 total

        supermercadoA.registrarVenta(2, 80)  // Pescado: 80
        supermercadoB.registrarVenta(2, 60)  // Pescado: +60 = 140 total

        supermercadoA.registrarVenta(3, 120) // Pollo: 120
        supermercadoC.registrarVenta(4, 90)  // Cerdo: 90
        supermercadoC.registrarVenta(5, 70)  // Ternera: 70

        val result = cadena.getTop5Productos()

        // Verificar que contiene los productos mas vendidos
        assertTrue(result.contains("Carne"))
        assertTrue(result.contains("Pescado"))
        assertTrue(result.contains("Pollo"))
    }

    @Test
    fun `obtener ingresos totales de la cadena`() {
        supermercadoA.registrarVenta(1, 10) // 100.0
        supermercadoB.registrarVenta(2, 5)  // 100.0
        supermercadoC.registrarVenta(3, 2)  // 60.0

        assertEquals(260.0, cadena.getFacturacionTotal())
    }

    @Test
    fun `obtener supermercado con mayor ingresos`() {
        supermercadoA.registrarVenta(1, 10) // 100.0
        supermercadoB.registrarVenta(2, 20) // 400.0
        supermercadoC.registrarVenta(3, 5)  // 150.0

        val result = cadena.getTopFacturadoSupermercado()

        assertTrue(result.contains("Supermercado B"))
        assertTrue(result.contains("400.0"))
    }

    // ========== Tests de Horario =====

    @Test
    fun `horario valido se crea correctamente`() {
        val horario = Horario(
            LocalTime.of(8, 0),
            LocalTime.of(20, 0)
        )
        assertTrue(horario.isOpenAt(LocalTime.of(12, 0)))
    }

    @Test
    fun `horario con hora cierre antes de apertura lanza excepcion`() {
        assertThrows(IllegalArgumentException::class.java) {
            Horario(
                LocalTime.of(20, 0),
                LocalTime.of(8, 0)
            )
        }
    }

    @Test
    fun `verificar si supermercado esta abierto en horario`() {
        val horario = Horario(
            LocalTime.of(8, 0),
            LocalTime.of(20, 0)
        )
        val horarioSemanal = HorarioSemanal(
            horario,
            setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY)
        )

        val supermercado = Supermercado(1, "Test", horarioSemanal)

        // Abierto el lunes a las 10:00
        assertTrue(supermercado.isOpenAt(DayOfWeek.MONDAY, LocalTime.of(10, 0)))

        // Cerrado el domingo
        assertEquals(false, supermercado.isOpenAt(DayOfWeek.SUNDAY, LocalTime.of(10, 0)))

        // Cerrado el lunes a las 22:00
        assertEquals(false, supermercado.isOpenAt(DayOfWeek.MONDAY, LocalTime.of(22, 0)))
    }

    @Test
    fun `obtener supermercados abiertos en dia y hora especificos`() {
        val horario = Horario(LocalTime.of(8, 0), LocalTime.of(20, 0))
        val horarioSemanal = HorarioSemanal(
            horario,
            setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
        )

        val cadena2 = CadenaSupercado()
        val supermercado1 = Supermercado(1, "Supermercado Mañanero", horarioSemanal)
        val supermercado2 = Supermercado(2, "Supermercado 24/7", HorarioSemanal(
            Horario(LocalTime.of(0, 0), LocalTime.of(23, 59)),
            DayOfWeek.values().toSet()
        ))

        cadena2.addSupermercado(supermercado1)
        cadena2.addSupermercado(supermercado2)

        val result = cadena2.getSupermercadosAbiertos(DayOfWeek.MONDAY, LocalTime.of(10, 0))

        assertTrue(result.contains("Supermercado Mañanero"))
        assertTrue(result.contains("Supermercado 24/7"))
    }

    // ========== Tests de validaciones y casos =======

    @Test
    fun `agregar producto con stock negativo lanza excepcion`() {
        assertThrows(IllegalArgumentException::class.java) {
            supermercadoA.addProducto(Producto(7, "Test", 10.0), -1)
        }
    }

    @Test
    fun `registrar venta con cantidad cero o negativa lanza excepcion`() {
        assertThrows(IllegalArgumentException::class.java) {
            supermercadoA.registrarVenta(1, 0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            supermercadoA.registrarVenta(1, -5)
        }
    }

    @Test
    fun `obtener cantidad vendida de producto sin ventas retorna cero`() {
        assertEquals(0, supermercadoA.getCantProdVendida(1))
    }

    @Test
    fun `cadena sin supermercados al obtener top revenue lanza excepcion`() {
        val cadenaVacia = CadenaSupercado()
        assertThrows(IllegalStateException::class.java) {
            cadenaVacia.getTopFacturadoSupermercado()
        }
    }

}