class CadenaSupermercado {
    // Lista de supermercados que pertenecen a la cadena
    private val supermercados = mutableListOf<Supermercado>()

    // catalogo global de productos disponibles
    private val catalogoProductos = mutableMapOf<Int, Producto>()

    //Agrega un supermercado a la cadena
    fun addSupermercado(supermercado: Supermercado) {
        require(supermercados.none { it.id == supermercado.id }) {
            "Ya existe un supermercado con id ${supermercado.id}"
        }
        supermercados.add(supermercado)
    }

    //Registra un producto en el catálogo global de la cadena
    fun registrarProducto(producto: Producto) {
        require(!catalogoProductos.containsKey(producto.id)) {
            "Ya existe un producto con id ${producto.id}"
        }
        catalogoProductos[producto.id] = producto
    }

    //Obtiene los 5 productos mas vendidos en toda la cadena
    fun getTop5Productos(): String {
        // Agregar ventas de todos los supermercados por producto
        val totalVentasProducto = mutableMapOf<Int, Int>()

        supermercados.forEach { supermercado ->
            supermercado.getVentasMap().forEach { (idProducto, cantidad) ->
                totalVentasProducto[idProducto] = (totalVentasProducto[idProducto] ?: 0) + cantidad
            }
        }

        // Ordenar por cantidad vendida (descendente) y tomar los top 5
        val top5 = totalVentasProducto.entries
            .sortedByDescending{ it.value }
            .take(5)

        // Formatear resultado
        return top5.joinToString(" - ") { (idProducto, cantidad) ->
            val nombreProducto = catalogoProductos[idProducto]?.nombre ?: "Producto $idProducto"
            "$nombreProducto: $cantidad"
        }
    }

    //Obtiene los ingresos totales de toda la cadena de supermercados
    fun getTotalFacturado(): Double {
        return supermercados.sumOf { it.getTotalFacturado() }
    }

    //Obtiene el supermercado con mayor cantidad de ingresos por ventas
    fun getTopFacturadoSupermercado(): String {
        check(supermercados.isNotEmpty()) { "No hay supermercados en la cadena" }

        val topSupermercado = supermercados.maxByOrNull { it.getTotalFacturado() }
            ?: throw IllegalStateException("No se pudo determinar el supermercado con mayores ingresos")

        val facturado = topSupermercado.getTotalFacturado()
        return "${topSupermercado.nombre} (${topSupermercado.id}). Ingresos totales: $facturado"
    }

    //Obtiene un supermercado segun id
    fun getSupermercadoById(id: Int): Supermercado? {
        return supermercados.find { it.id == id }
    }

    //Obtiene un producto del catalogo segun id
    fun getProductoById(id: Int): Producto? {
        return catalogoProductos[id]
    }

    //Obtiene la lista de todos los supermercados de la cadena
    fun getSupermercados(): List<Supermercado> = supermercados.toList()
    
}