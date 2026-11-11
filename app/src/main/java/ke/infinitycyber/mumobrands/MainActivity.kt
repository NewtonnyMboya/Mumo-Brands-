package ke.infinitycyber.mumobrands
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ke.infinitycyber.mumobrands.data.*
import ke.infinitycyber.mumobrands.ui.*
import kotlinx.coroutines.launch

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);val dao=AppDatabase.get(this).dao();setContent{MumoTheme{MumoApp(dao)}}}
}
private enum class Page(val label:String){Home("Home"),Orders("My Orders"),Inventory("Inventory"),Clients("Clients"),Reports("Reports"),Settings("Settings"),About("About Us"),Help("Help & Support")}
@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun MumoApp(dao:BusinessDao){
 var page by remember{mutableStateOf(Page.Home)}
 val drawer= rememberDrawerState(DrawerValue.Closed)
 var addOrder by remember{mutableStateOf(false)}
 var addProduct by remember{mutableStateOf(false)}
 var addClient by remember{mutableStateOf(false)}
 val orders by dao.orders().collectAsStateWithLifecycle(emptyList())
 val products by dao.products().collectAsStateWithLifecycle(emptyList())
 val clients by dao.clients().collectAsStateWithLifecycle(emptyList())
 val scope= rememberCoroutineScope()
 ModalNavigationDrawer(drawerState=drawer,drawerContent={
  ModalDrawerSheet(modifier=Modifier.fillMaxWidth(.84f)){
   Column(Modifier.fillMaxWidth().background(Navy).padding(20.dp)){Text("MB",fontSize=42.sp,color=Magenta,fontWeight=FontWeight.Black);Text("MUMO BRANDS",color=Color.White,fontWeight=FontWeight.Bold);Text("Printing and Branding Services",color=Color.White)}
   Page.values().forEach{p->NavigationDrawerItem(label={Text(p.label)},selected=page==p,onClick={page=p;scope.launch{drawer.close()}},icon={Icon(iconFor(p),null)},modifier=Modifier.padding(horizontal=10.dp))}
   Spacer(Modifier.weight(1f));Text("∞  Infinity Cyber Technologies",Modifier.padding(18.dp),color=Navy,fontSize=12.sp)
  }
 }){
  Scaffold(topBar={TopAppBar(title={Text("Mumo Brands Printing\nand Branding Services",fontSize=16.sp,lineHeight=19.sp,color=Color.White,fontWeight=FontWeight.Bold)},navigationIcon={IconButton(onClick={scope.launch{drawer.open()}}){Icon(Icons.Default.Menu,"Menu",tint=Color.White)}},actions={IconButton(onClick={}){Icon(Icons.Default.Notifications,null,tint=Color.White)}},colors=TopAppBarDefaults.topAppBarColors(containerColor=Navy))},
   bottomBar={NavigationBar(containerColor=Color.White){listOf(Page.Home,Page.Orders,Page.Inventory,Page.Clients,Page.Reports).forEach{p->NavigationBarItem(selected=page==p,onClick={page=p},icon={Icon(iconFor(p),null)},label={Text(p.label,fontSize=9.sp)},colors=NavigationBarItemDefaults.colors(selectedIconColor=Magenta,selectedTextColor=Magenta))}}},
   containerColor=Pale){pad->Box(Modifier.padding(pad).fillMaxSize()){
    when(page){
     Page.Home->HomeScreen(orders.size,products.size,clients.size,{page=it},{addOrder=true})
     Page.Orders->OrdersScreen(orders){addOrder=true}
     Page.Inventory->ProductsScreen(products){addProduct=true}
     Page.Clients->ClientsScreen(clients){addClient=true}
     Page.Reports->ReportsScreen(orders,products.size,clients.size)
     Page.Settings->Info("Settings","Business profile and application preferences")
     Page.About->Info("About Mumo Brands","Quality • Creativity • Your Brand")
     Page.Help->Info("Help & Support","Call 0741 167 612 for assistance")
    }
   }}
 }
 if(addOrder) Entry("New Order",listOf("Client name","Item / branding","Quantity","Amount (KSh)"),{v->scope.launch{dao.addOrder(OrderEntity(client=v[0],item=v[1],quantity=v[2].toIntOrNull()?:1,amount=v[3].toIntOrNull()?:0))};addOrder=false},{addOrder=false})
 if(addProduct) Entry("Add Product",listOf("Product name","Category","Stock quantity","Unit price (KSh)"),{v->scope.launch{dao.addProduct(ProductEntity(name=v[0],category=v[1],stock=v[2].toIntOrNull()?:0,unitPrice=v[3].toIntOrNull()?:0))};addProduct=false},{addProduct=false})
 if(addClient) Entry("Add Client",listOf("Client name","Phone number","Organization"),{v->scope.launch{dao.addClient(ClientEntity(name=v[0],phone=v[1],organization=v[2]))};addClient=false},{addClient=false})
}
@Composable private fun HomeScreen(o:Int,p:Int,c:Int,go:(Page)->Unit,new:()->Unit){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp)){
  Card(colors=CardDefaults.cardColors(containerColor=Color.White),shape=RoundedCornerShape(22.dp)){Column(Modifier.fillMaxWidth().background(androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color.White,Color(0xFFE8E9FF)))).padding(20.dp)){Text("MB",fontSize=48.sp,color=Magenta,fontWeight=FontWeight.Black);Text("MUMO BRANDS",fontSize=23.sp,color=Magenta,fontWeight=FontWeight.ExtraBold);Text("...Quality and perfection",color=Navy,fontWeight=FontWeight.Bold);Text("Your Brand, Our Priority",Modifier.padding(top=10.dp).background(Navy,RoundedCornerShape(18.dp)).padding(horizontal=12.dp,vertical=6.dp),color=Color.White)}}
  Spacer(Modifier.height(16.dp));Text("Quick actions",fontSize=18.sp,color=Navy,fontWeight=FontWeight.Bold)
  val tiles=listOf(Triple("New Order",Icons.Default.ShoppingCart,Magenta),Triple("My Orders",Icons.Default.Receipt,RoyalBlue),Triple("Inventory",Icons.Default.Checkroom,DeepNavy),Triple("Clients",Icons.Default.Groups,RoyalBlue),Triple("Reports",Icons.Default.BarChart,Magenta),Triple("Settings",Icons.Default.Settings,DeepNavy),Triple("Print Labels",Icons.Default.Print,RoyalBlue),Triple("About Us",Icons.Default.Info,Magenta),Triple("Help",Icons.Default.SupportAgent,RoyalBlue))
  tiles.chunked(3).forEach{row->Row(horizontalArrangement=Arrangement.spacedBy(9.dp)){row.forEach{(name,ic,col)->Card(onClick={when(name){"New Order"->new();"My Orders"->go(Page.Orders);"Inventory"->go(Page.Inventory);"Clients"->go(Page.Clients);"Reports"->go(Page.Reports);"Settings"->go(Page.Settings);"About Us"->go(Page.About);else->go(Page.Help)}},modifier=Modifier.weight(1f).height(102.dp),colors=CardDefaults.cardColors(containerColor=col),shape=RoundedCornerShape(18.dp)){Column(Modifier.fillMaxSize().padding(8.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Center){Icon(ic,null,tint=Color.White,modifier=Modifier.size(32.dp));Text(name,color=Color.White,fontSize=11.sp,fontWeight=FontWeight.Bold)}}};if(row.size<3)repeat(3-row.size){Spacer(Modifier.weight(1f))}};Spacer(Modifier.height(9.dp))}
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Metric("Orders",o.toString());Metric("Products",p.toString());Metric("Clients",c.toString())}
  Spacer(Modifier.height(12.dp));Text("Quality • Creativity • Your Brand",Modifier.fillMaxWidth().background(Navy,RoundedCornerShape(12.dp)).padding(14.dp),color=Color.White,fontWeight=FontWeight.Bold)
  Text("☎  0741 167 612",Modifier.align(Alignment.CenterHorizontally).padding(10.dp),color=Navy,fontWeight=FontWeight.Bold)
 }
}
@Composable private fun Metric(label:String,value:String){Card(Modifier.weight(1f),colors=CardDefaults.cardColors(containerColor=Color.White)){Column(Modifier.padding(12.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(value,fontSize=22.sp,color=Magenta,fontWeight=FontWeight.ExtraBold);Text(label,fontSize=10.sp,color=Navy)}}}
@Composable private fun OrdersScreen(data:List<OrderEntity>,add:()->Unit){
 var filter by remember{mutableStateOf("All")}
 Column(Modifier.fillMaxSize().padding(12.dp)){Row(horizontalArrangement=Arrangement.spacedBy(4.dp)){listOf("All","Pending","Processing","Delivered").forEach{FilterChip(selected=filter==it,onClick={filter=it},label={Text(it,fontSize=10.sp)})}}
 LazyColumn(Modifier.weight(1f)){items(data.filter{filter=="All"||it.status==filter}){o->Card(Modifier.fillMaxWidth().padding(vertical=4.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){Column(Modifier.padding(14.dp)){Text("Order #ORD-${o.id.toString().padStart(6,'0')}",fontWeight=FontWeight.Bold,color=Navy);Text("Client: ${o.client}");Text("${o.quantity} × ${o.item}");Text("KSh ${o.amount}",color=Magenta,fontWeight=FontWeight.Bold);Text(o.status,color=RoyalBlue)}}}}
 Button(onClick=add,modifier=Modifier.fillMaxWidth()){Icon(Icons.Default.Add,null);Text(" New Order")}
 }
}
@Composable private fun ProductsScreen(data:List<ProductEntity>,add:()->Unit){Column(Modifier.fillMaxSize().padding(12.dp)){LazyColumn(Modifier.weight(1f)){items(data){p->Card(Modifier.fillMaxWidth().padding(vertical=4.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Checkroom,null,tint=Magenta,modifier=Modifier.size(38.dp));Column(Modifier.padding(start=12.dp)){Text(p.name,fontWeight=FontWeight.Bold,color=Navy);Text("${p.category} • Stock: ${p.stock}");Text("Price: KSh ${p.unitPrice}")}}}}};Button(onClick=add,modifier=Modifier.fillMaxWidth()){Icon(Icons.Default.Add,null);Text(" Add Product")}}}
@Composable private fun ClientsScreen(data:List<ClientEntity>,add:()->Unit){Column(Modifier.fillMaxSize().padding(12.dp)){LazyColumn(Modifier.weight(1f)){items(data){c->Card(Modifier.fillMaxWidth().padding(vertical=4.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){Row(Modifier.padding(14.dp)){Icon(Icons.Default.Business,null,tint=RoyalBlue);Column(Modifier.padding(start=12.dp)){Text(c.name,fontWeight=FontWeight.Bold,color=Navy);Text(c.phone);Text(c.organization,fontSize=12.sp)}}}}};Button(onClick=add,modifier=Modifier.fillMaxWidth()){Icon(Icons.Default.Add,null);Text(" Add Client")}}}
@Composable private fun ReportsScreen(orders:List<OrderEntity>,products:Int,clients:Int){Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)){Text("Business overview",fontSize=20.sp,color=Navy,fontWeight=FontWeight.Bold);Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Metric("Orders",orders.size.toString());Metric("Items",products.toString());Metric("Clients",clients.toString())};Spacer(Modifier.height(14.dp));Card(colors=CardDefaults.cardColors(containerColor=Color.White)){Column(Modifier.padding(20.dp)){Text("Recorded order value",color=Navy);Text("KSh ${orders.sumOf{it.amount}}",fontSize=27.sp,color=Magenta,fontWeight=FontWeight.ExtraBold);Text("Calculated from saved orders",fontSize=12.sp)}}}}
@Composable private fun Entry(title:String,labels:List<String>,save:(List<String>)->Unit,dismiss:()->Unit){val values=remember{mutableStateListOf<String>().also{l->repeat(labels.size){l.add("")}}};AlertDialog(onDismissRequest=dismiss,title={Text(title,color=Navy,fontWeight=FontWeight.Bold)},text={Column(verticalArrangement=Arrangement.spacedBy(6.dp)){labels.forEachIndexed{i,label->OutlinedTextField(value=values[i],onValueChange={values[i]=it},label={Text(label)},singleLine=true)}}},confirmButton={TextButton(onClick={if(values.take(2).all{it.isNotBlank()})save(values.toList())}){Text("Save")}},dismissButton={TextButton(onClick=dismiss){Text("Cancel")}})}
@Composable private fun Info(title:String,body:String){Column(Modifier.fillMaxSize().padding(24.dp),verticalArrangement=Arrangement.Center,horizontalAlignment=Alignment.CenterHorizontally){Text(title,fontSize=24.sp,color=Navy,fontWeight=FontWeight.Bold);Spacer(Modifier.height(10.dp));Text(body,color=Navy)}}
private fun iconFor(p:Page)=when(p){Page.Home->Icons.Default.Home;Page.Orders->Icons.Default.ShoppingCart;Page.Inventory->Icons.Default.Inventory2;Page.Clients->Icons.Default.Groups;Page.Reports->Icons.Default.BarChart;Page.Settings->Icons.Default.Settings;Page.About->Icons.Default.Info;Page.Help->Icons.Default.SupportAgent}
