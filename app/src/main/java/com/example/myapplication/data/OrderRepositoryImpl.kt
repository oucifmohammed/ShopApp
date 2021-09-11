package com.example.myapplication.data

import com.example.myapplication.data.models.CartProductDto
import com.example.myapplication.data.models.OrderDto
import com.example.myapplication.data.models.ProductDto
import com.example.myapplication.data.util.OrderDtoMapper
import com.example.myapplication.data.util.ProductDtoMapper
import com.example.myapplication.domain.OrderRepository
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.domain.models.Order
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDtoMapper: OrderDtoMapper,
    private val productDtoMapper: ProductDtoMapper
) : OrderRepository {

    private val orders = FirebaseFirestore.getInstance().collection("Orders")
    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseFirestore.getInstance().collection("Users")
    private val userCartProducts =
        users.document(auth.currentUser?.uid!!).collection("cartProducts")

    private val products = FirebaseFirestore.getInstance().collection("products")

    override suspend fun addOrder(order: Order): ProcessUiState = withContext(Dispatchers.IO) {
        try {

            val orderDto = orderDtoMapper.mapFromDomainModel(order)
            val orderListSize = orders.get().await().size()

            orderDto.orderNumber = orderListSize + 1
            orderDto.userId = auth.currentUser?.uid!!

            val parentProductIds = getCartProducts().map {
                it.parentProductId
            }

            orderDto.orderProductsList = parentProductIds

            deleteCartProducts()

            orders.add(orderDto).await()

            ProcessUiState.Success("Order has been added successfully")

        } catch (e: Exception) {
            ProcessUiState.Error(e.message!!)
        }
    }

    override suspend fun getUserOrders(): Resource<List<Order>> = withContext(Dispatchers.IO){

        try {
            val ordersDtoList = orders.whereEqualTo("userId",auth.currentUser!!.uid).get().await().toObjects(OrderDto::class.java)

            val ordersList = orderDtoMapper.toDomainList(ordersDtoList).sortedBy {
                it.orderNumber
            }

            Resource.success(ordersList)
        } catch (e: Exception) {
            Resource.error(e.message!!,null)
        }
    }

    private suspend fun getCartProducts(): List<CartProductDto> = withContext(Dispatchers.IO) {
        userCartProducts.get().await().toObjects(CartProductDto::class.java)
    }

    private suspend fun deleteCartProducts() = withContext(Dispatchers.IO) {
        userCartProducts.get().await().forEach { element ->
            element.reference.delete().await()
        }
    }

    override suspend fun getOrderProducts(orderId: String): Resource<List<Product>> = withContext(Dispatchers.IO){
        try {
            val ordersDtoList = orders.whereEqualTo("userId",auth.currentUser!!.uid).get().await().toObjects(OrderDto::class.java)

            val order = ordersDtoList.find {
                it.id == orderId
            }

            val orderProductsDto = products.whereIn("id",order!!.orderProductsList).get().await().toObjects(ProductDto::class.java)

            val orderProducts = productDtoMapper.toDomainList(orderProductsDto)

            Resource.success(orderProducts)

        }catch (e: Exception) {
            Resource.error(e.message!!,null)
        }
    }
}