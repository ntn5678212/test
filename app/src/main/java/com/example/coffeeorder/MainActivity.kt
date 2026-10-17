
package com.example.coffeeorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeorder.databinding.ActivityMainBinding
import com.example.coffeeorder.model.Order
import com.example.coffeeorder.utils.toVnd
import com.example.coffeeorder.utils.toast
import com.example.coffeeorder.utils.trimmedText

class MainActivity : AppCompatActivity() {
    companion object {
        private const val KEY_ORDER = "KEY_ORDER"
    }
    private lateinit var binding: ActivityMainBinding

    private val defaultOrder = Order(
        id = "HD001",
        customerName = "Ngô Thanh Nguyên",
        productName = "Cà phê sữa",
        quantity = 2,
        price = 25000.0
    )

    private var currentOrder = defaultOrder
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable(
            KEY_ORDER,
            currentOrder
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val savedOrder =
            savedInstanceState?.getSerializable(KEY_ORDER) as? Order

        if (savedOrder != null) {
            currentOrder = savedOrder
        }
        bindOrderData(currentOrder)

        binding.btnUpdateQuantity.setOnClickListener {
            val quantity = binding.edtQuantity
                .trimmedText()
                .toIntOrNull()

            if (quantity == null || quantity !in 1..20) {
                binding.edtQuantity.error =
                    "Số lượng phải từ 1 đến 20"
                return@setOnClickListener
            }

            currentOrder = currentOrder.copy(quantity = quantity)
            bindOrderData(currentOrder)
            toast("Cập nhật số lượng thành công")
        }

        binding.btnReset.setOnClickListener {
            currentOrder = defaultOrder
            bindOrderData(currentOrder)
            toast("Đã khôi phục đơn hàng mặc định")
        }
    }

    private fun bindOrderData(order: Order) {
        with(binding) {
            tvOrderId.text = "Mã đơn: ${order.id}"
            tvCustomerName.text = "Khách hàng: ${order.customerName}"
            tvProductName.text = "Tên món: ${order.productName}"
            tvQuantity.text = "Số lượng: ${order.quantity}"
            tvPrice.text = "Đơn giá: ${order.price.toVnd()}"
            tvTotalPrice.text =
                "Tổng tiền: ${order.totalPrice.toVnd()}"
            edtQuantity.setText(order.quantity.toString())
        }
    }
}
