import React, { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { AddressForm } from "@/components/checkout/AddressForm"
import { PaymentMethod } from "@/components/checkout/PaymentMethod"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/Price"
import { getCart, getCartTotal, clearCart } from "@/lib/cart"
import { ShoppingBag } from "lucide-react"

export default function CheckoutPage() {
  const navigate = useNavigate()
  const [cart, setCart] = useState([])
  const [formData, setFormData] = useState({
    full_name: "",
    phone: "",
    email: "",
    address: "",
    province: "",
    district: "",
    ward: "",
    note: "",
  })
  const [paymentId, setPaymentId] = useState(1)
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    const items = getCart()
    if (items.length === 0) {
      navigate("/cart")
    }
    setCart(items)
  }, [navigate])

  const subtotal = getCartTotal()
  const shipping = subtotal >= 499000 ? 0 : 30000
  const total = subtotal + shipping

  const handleSubmit = (e) => {
    e.preventDefault()
    setIsSubmitting(true)

    // Mock order creation - in real app, this would call an API
    setTimeout(() => {
      // Create mock order ID (in real app, this comes from API response)
      const mockOrderId = 1001 // Use existing order from mock data
      
      clearCart()
      window.dispatchEvent(new Event("cart-updated"))
      
      // Redirect to order detail page
      navigate(`/orders/${mockOrderId}`)
    }, 1000)
  }

  if (cart.length === 0) {
    return (
      <PageLayout title="Thanh toán" breadcrumbs={[{ label: "Thanh toán" }]}>
        <div className="section">
          <div className="card p-12 text-center">
            <ShoppingBag className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
            <div className="text-lg font-medium mb-2">Giỏ hàng trống</div>
            <div className="text-muted-foreground mb-6">Vui lòng thêm sản phẩm vào giỏ hàng trước khi thanh toán.</div>
            <Button onClick={() => navigate("/products")}>Khám phá sản phẩm</Button>
          </div>
        </div>
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Thanh toán" breadcrumbs={[{ label: "Giỏ hàng", href: "/cart" }, { label: "Thanh toán" }]}>
      <form onSubmit={handleSubmit}>
        <div className="section grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2 space-y-6">
            <div className="card p-6">
              <h2 className="text-lg font-semibold text-foreground mb-4">Thông tin giao hàng</h2>
              <AddressForm formData={formData} onChange={setFormData} />
            </div>

            <div className="card p-6">
              <h2 className="text-lg font-semibold text-foreground mb-4">Phương thức thanh toán</h2>
              <PaymentMethod selectedId={paymentId} onChange={setPaymentId} />
            </div>
          </div>

          <div className="lg:col-span-1">
            <div className="card p-6 space-y-4 sticky top-20">
              <h3 className="text-lg font-semibold text-foreground">Đơn hàng của bạn</h3>

              <div className="space-y-3 max-h-64 overflow-y-auto">
                {cart.map((item) => (
                  <div key={item.sku_variant} className="flex gap-3 text-sm">
                    <div className="w-16 h-16 rounded-lg overflow-hidden bg-surface flex-shrink-0">
                      <img src={item.image || "/placeholder.svg"} alt={item.productName} className="w-full h-full object-cover" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="font-medium text-foreground line-clamp-1">{item.productName}</div>
                      <div className="text-muted-foreground">
                        {item.sizeName} / {item.colorName} × {item.qty}
                      </div>
                      <div className="font-medium text-foreground">
                        <Price value={item.unitPrice * item.qty} />
                      </div>
                    </div>
                  </div>
                ))}
              </div>

              <div className="space-y-2 text-sm pt-4 border-t border-border">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Tạm tính</span>
                  <Price value={subtotal} />
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Phí vận chuyển</span>
                  {shipping === 0 ? <span className="text-green-600">Miễn phí</span> : <Price value={shipping} />}
                </div>
              </div>

              <div className="pt-4 border-t border-border flex justify-between items-center">
                <span className="font-semibold text-foreground">Tổng cộng</span>
                <span className="text-xl font-bold text-primary">
                  <Price value={total} />
                </span>
              </div>

              <Button type="submit" disabled={isSubmitting} className="w-full">
                {isSubmitting ? "Đang xử lý..." : "Đặt hàng"}
              </Button>

              <div className="text-xs text-muted-foreground text-center">
                Bằng cách đặt hàng, bạn đồng ý với điều khoản sử dụng của chúng tôi.
              </div>
            </div>
          </div>
        </div>
      </form>
    </PageLayout>
  )
}
