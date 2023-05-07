package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.entities.ArticuloConCategoriaETipoIva
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import com.pedro.expresstpv.domain.model.Articulo
import javax.inject.Inject

class ArticuloRepository @Inject constructor(private val articuloDao: ArticuloDao, private val categoriaRepository: CategoriaRepository, private val tipoIvaRepository: TipoIvaRepository){

    private val articulosMap = mutableMapOf<Int, Articulo>()


    suspend fun reloadFromDataBase(){
        val articulosEntities = articuloDao.getAll()
        //vamos a mapear cada entidad a su correspondiente de la clase de dominio trallendonos la informacion que neceitemos de los otros repositorios
        articulosEntities.forEach { entity ->
            //Obtendremos una referencia de los objetos cargados en memoria, sin crear uno nuevo
            val categoria = categoriaRepository.getCategoriaById(entity.idCategoria)
            val tipoIva = tipoIvaRepository.getTipoIvaById(entity.idIva)
            //Si el iva o la categoria no existen, saltaremos este articulo para evitar excepciones
            if (categoria == null || tipoIva == null) {
                return@forEach
            }
            val articulo = Articulo(
                id = entity.id,
                categoria = categoria,
                tipoIva = tipoIva,
                nombre = entity.nombre,
                precio = entity.precio
            )

            //Le asignamos como key la id del articulo
            articulosMap[entity.id] = articulo
        }
    }

    suspend fun getAllArticulos() : MutableMap<Int, Articulo>{
        if (articulosMap.isEmpty()){
            reloadFromDataBase()
        }
        return articulosMap
    }

    suspend fun getArticuloById(id : Int) : Articulo?{
        if (articulosMap.isEmpty()){
            reloadFromDataBase()
        }
        return articulosMap.getOrDefault(id, null)
    }

    suspend fun insertarArticulo(articulo : Articulo){
        articuloDao.insert(articulo.toEntity())
        reloadFromDataBase()
    }
}

/* ARTICULO */

fun ArticuloConCategoriaETipoIva.toDomain() = Articulo(
    id = articuloEntity.id,
    categoria = categoriaEntity.toDomain(),
    tipoIva = tipoIvaEntity.toDomain(),
    nombre = articuloEntity.nombre,
    precio = articuloEntity.precio
)

fun Articulo.toEntity() = ArticuloEntity(
    id = id,
    idCategoria = categoria.id,
    idIva = tipoIva.id,
    nombre = nombre,
    precio = precio
)
