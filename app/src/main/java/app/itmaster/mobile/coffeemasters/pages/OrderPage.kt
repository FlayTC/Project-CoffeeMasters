package app.itmaster.mobile.coffeemasters.pages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.itmaster.mobile.coffeemasters.data.DataManager
import app.itmaster.mobile.coffeemasters.data.ItemInCart
import app.itmaster.mobile.coffeemasters.ui.theme.Alternative1
import app.itmaster.mobile.coffeemasters.ui.theme.Alternative2
import app.itmaster.mobile.coffeemasters.ui.theme.BackgroundOrder
import app.itmaster.mobile.coffeemasters.ui.theme.TextColor
import kotlinx.coroutines.delay


@Composable
fun OrderPage(dataManager: DataManager) {
    var snackbarVisible by remember { mutableStateOf(false) }
    var orderNumber by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(446.dp)
    ) {
        OrderItemList(dataManager = dataManager)
    }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .offset(y = 289.dp)
            .height(135.dp)
    ) {
        val showForm = dataManager.cart.isNotEmpty()
        val context = LocalContext.current
        OrderForm(
            showForm = showForm,
            context = context,
            dataManager = dataManager,
            onOrderPlaced = { number ->
                orderNumber = number
                snackbarVisible = true
            }
        )

        LaunchedEffect(snackbarVisible) {
            if (snackbarVisible) {
                delay(5000)
                snackbarVisible = false
            }
        }

        if (snackbarVisible) {
            Snackbar(
                modifier = Modifier
                    .padding(8.dp)
                    .offset(y = 76.dp),
            ) {
                Text("Your order number is #$orderNumber, it will be ready soon.")
            }
        }
    }
}

@Composable
fun OrderItemList(dataManager: DataManager) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(BackgroundOrder)
    ) {
        Text(
            text = "ITEMS",
            color = TextColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(12.dp)
        )
        LazyColumn {
            itemsIndexed(dataManager.cart) { index, itemInCart ->
                OrderItem(itemInCart = itemInCart,
                    onRemoveClick = { dataManager.cartRemove(itemInCart) }
                )

                if (index < dataManager.cart.size - 1) {
                    Divider(
                        color = Alternative2,
                        thickness = 1.dp,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderForm(
    showForm: Boolean,
    context: Context,
    dataManager: DataManager,
    onOrderPlaced: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .background(BackgroundOrder)
    )

    {

        if (showForm) {
            var orderName by remember {
                mutableStateOf(
                    context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        .getString("orderName", "") ?: ""
                )
            }
            OutlinedTextField(
                value = orderName,
                onValueChange = {
                    if (it.length <= 20) {
                        orderName = it
                    }
                },
                label = { Text("Nombre del pedido") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)

            )
            Button(modifier = Modifier
                .padding(start = 100.dp, top = 80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Alternative1,
                    contentColor = Color.White
                ), onClick = {
                    val sharedPreferences =
                        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("orderName", orderName).apply()
                    dataManager.clearCart()

                    val orderNumber = (1000..9999).random().toString()

                    onOrderPlaced(orderNumber)
                }

            ) {
                Text(
                    text = "Realizar pedido",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun OrderItem(itemInCart: ItemInCart, onRemoveClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)

    ) {
        Box(modifier = Modifier.padding(top = 22.dp)) {
            Text(
                "${itemInCart.quantity}x",
                fontSize = 15.sp,
                color = TextColor
            )

            Text(
                text = itemInCart.product.name,
                modifier = Modifier.padding(start = 36.dp)
            )
        }

        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "$${itemInCart.product.price}",
                modifier = Modifier.padding(top = 15.dp)
            )

            IconButton(
                onClick = { onRemoveClick() }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = TextColor)
            }
        }
    }
}

