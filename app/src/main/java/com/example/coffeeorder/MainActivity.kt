
package com.example.coffeeorder
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeorder.databinding.ActivityMainBinding
import com.example.coffeeorder.model.Order
import com.example.coffeeorder.utils.toVnd
import com.example.coffeeorder.utils.toast
import com.example.coffeeorder.utils.trimmedText
import android.content.ActivityNotFoundException
import android.net.Uri
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
    private val editOrderLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                val updatedOrder =
                    result.data
                        ?.getSerializableExtra(
                            "UPDATED_ORDER"
                        ) as? Order

                if (updatedOrder != null) {
                    currentOrder = updatedOrder
                    bindOrderData(currentOrder)
                    toast("Đã cập nhật đơn hàng")
                }
            }
        }
    private fun safeStartIntent(
        intent: Intent,
        title: String
    ) {

        try {
            startActivity(
                Intent.createChooser(
                    intent,
                    title
                )
            )
        } catch (
            e: ActivityNotFoundException
        ) {
            toast("Không có ứng dụng phù hợp")
        }
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
        binding.btnEditOrder.setOnClickListener {

            val intent =
                Intent(
                    this,
                    EditOrderActivity::class.java
                )

            intent.putExtra(
                "EXTRA_ORDER",
                currentOrder
            )

            editOrderLauncher.launch(intent)
        }
        binding.btnCall.setOnClickListener {

            val intent =
                Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:0905123456")
                }

            safeStartIntent(
                intent,
                "Chọn ứng dụng gọi điện"
            )
        }
        binding.btnWebsite.setOnClickListener {

            val intent =
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(
                        "https://www.google.com"
                    )
                }

            safeStartIntent(
                intent,
                "Chọn trình duyệt"
            )
        }
        binding.btnEmail.setOnClickListener {

            val intent =
                Intent(Intent.ACTION_SENDTO).apply {

                    data =
                        Uri.parse(
                            "mailto:coffee@example.com"
                        )

                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        "Phản hồi đơn hàng ${currentOrder.id}"
                    )
                }

            safeStartIntent(
                intent,
                "Chọn ứng dụng Email"
            )
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
