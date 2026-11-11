package ke.infinitycyber.mumobrands.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName="orders")
data class OrderEntity(@PrimaryKey(autoGenerate=true) val id:Long=0, val client:String, val item:String, val quantity:Int, val amount:Int, val status:String="Pending")
@Entity(tableName="products")
data class ProductEntity(@PrimaryKey(autoGenerate=true) val id:Long=0, val name:String, val category:String, val stock:Int, val unitPrice:Int)
@Entity(tableName="clients")
data class ClientEntity(@PrimaryKey(autoGenerate=true) val id:Long=0, val name:String, val phone:String, val organization:String="")