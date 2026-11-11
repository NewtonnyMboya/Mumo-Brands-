package ke.infinitycyber.mumobrands.data
import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao interface BusinessDao {
 @Query("SELECT * FROM orders ORDER BY id DESC") fun orders():Flow<List<OrderEntity>>
 @Insert suspend fun addOrder(item:OrderEntity)
 @Query("SELECT * FROM products ORDER BY name") fun products():Flow<List<ProductEntity>>
 @Insert suspend fun addProduct(item:ProductEntity)
 @Query("SELECT * FROM clients ORDER BY name") fun clients():Flow<List<ClientEntity>>
 @Insert suspend fun addClient(item:ClientEntity)
}
@Database(entities=[OrderEntity::class,ProductEntity::class,ClientEntity::class],version=1,exportSchema=false)
abstract class AppDatabase:RoomDatabase(){
 abstract fun dao():BusinessDao
 companion object {
  @Volatile private var instance:AppDatabase?=null
  fun get(context:Context):AppDatabase = instance ?: synchronized(this) {
   instance ?: Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"mumo_brands_private.db").build().also { instance=it }
  }
 }
}