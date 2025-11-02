import java.lang.IllegalStateException

class Supermercado(
    val id: Int,
    val nombre: String
){
    init {
        require(nombre.isNotBlank()) { "El nombre del supermercado no puede estar vacio" }
    }

    //Productos disponibles del stock actual
    private val inventario = mutableMapOf<Int, Int>()

    //ventas realizadas
    private val ventas = mutableMapOf<Int, Int>()

    //productos disponibles en el sistema
    private val productos = mutableMapOf<Int, Producto>()

    fun addProducto(producto: Producto, stock: Int){
        require(stock >= 0) { "El stock no puede ser negativo" }
        productos[producto.id] = producto
        inventario[producto.id] = stock
    }

    //Registra una venta de un producto y retorna precio total
    fun registrarVenta(idProducto: Int, cantidad: Int): Double {
        require(cantidad > 0) { "La cantidad a vender debe ser mayor a 0" }

        val producto = productos[idProducto]
            ?: throw IllegalStateException( "El producto con ID $idProducto no existe en este supermercado" )

        val stockActual = inventario[idProducto] ?: 0
        check(stockActual >= cantidad){
            "Stock insuficiente. Disponible: $stockActual, Solicitado: $cantidad"
        }

        //Actualiza stock
        inventario[idProducto] = stockActual - cantidad

        //Registra venta
        ventas[idProducto] = (ventas[idProducto] ?: 0) + cantidad

        //devuelve precio total
        return producto.precio * cantidad
    }

    //Obtiene la cantidad total vendida de un producto
    fun getCantProdVendida(idProducto: Int): Int {
        return ventas[idProducto] ?: 0
    }

    //Obtiene los ingresos totales generados por las ventas de un producto especifico
    fun getFacturacionProducto(idProducto: Int): Double {
        val producto = productos[idProducto] ?: return 0.0
        val cantidadVendida = ventas[idProducto] ?: 0
        return producto.precio * cantidadVendida
    }

    //Obtiene los ingresos totales del supermercado sumando todas las ventas
    fun getFacturacionTotal(): Double {
        return ventas.entries.sumOf { (idProducto, cantidadVendida) ->
            val producto = productos[idProducto] ?: return@sumOf 0.0
            producto.precio * cantidadVendida
        }
    }

    //Obtiene el map de ventas realizadas
    internal fun getVentasMap(): Map<Int, Int> = ventas.toMap()

    //Obtiene el stock actual de un producto
    fun getStockProducto(idProducto: Int): Int {
        return inventario[idProducto] ?: 0
    }

}