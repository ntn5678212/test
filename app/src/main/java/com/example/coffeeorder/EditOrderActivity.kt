package com.example.coffeeorder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeorder.databinding.ActivityEditOrderBinding
import com.example.coffeeorder.model.Order
import com.example.coffeeorder.utils.toast
import com.example.coffeeorder.utils.trimmedText

class EditOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val order =
            intent.getSerializableExtra("EXTRA_ORDER") as? Order

        if (order == null) {
            toast("Không tìm thấy đơn hàng")
            finish()
            return
        }

        binding.edtCustomerName.setText(order.customerName)
        binding.edtProductName.setText(order.productName)
        binding.edtPrice.setText(order.price.toString())

        binding.btnSaveOrder.setOnClickListener {

            val customerName =
                binding.edtCustomerName.trimmedText()

            val productName =
                binding.edtProductName.trimmedText()

            val price =
                binding.edtPrice.trimmedText().toDoubleOrNull()

            if (customerName.isBlank()) {
                binding.edtCustomerName.error =
                    "Không được để trống"
                return@setOnClickListener
            }

            if (productName.isBlank()) {
                binding.edtProductName.error =
                    "Không được để trống"
                return@setOnClickListener
            }

            if (price == null || price <= 0) {
                binding.edtPrice.error =
                    "Đơn giá phải lớn hơn 0"
                return@setOnClickListener
            }

            val updatedOrder =
                order.copy(
                    customerName = customerName,
                    productName = productName,
                    price = price
                )

            val resultIntent =
                Intent().apply {
                    putExtra(
                        "UPDATED_ORDER",
                        updatedOrder
                    )
                }

            setResult(
                RESULT_OK,
                resultIntent
            )

            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}