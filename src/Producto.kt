data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double
) {
    init {
        require(precio > 0) {"El precio del producto debe ser mayor a 0"}
        require(nombre.isNotBlank()) {"El nombre del producto no puede estar vacio"}
    }
}