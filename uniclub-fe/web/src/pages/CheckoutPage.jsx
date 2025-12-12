import React, { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { AddressForm } from "@/components/checkout/AddressForm"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/Price"
import { getFullCart } from "@/lib/cart-api"
import { Loader2, ShoppingBag } from "lucide-react"

export default function CheckoutPage() {
  const navigate = useNavigate()
  const [cart, setCart] = useState([])
  const [loading, setLoading] = useState(true)
  const [formData, setFormData] = useState({
    full_name: "",
    phone: "",
    email: "",
    address: "",
    province: "",
    provinceName: "",
    district: "",
    districtName: "",
    ward: "",
    wardName: "",
    note: "",
  })
  const [paymentId, setPaymentId] = useState(1)
  const [paymentMethod, setPaymentMethod] = useState("COD") // COD or VNPay
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [useDefaultAddress, setUseDefaultAddress] = useState(true)

  useEffect(() => {
    const fetchCart = async () => {
      const userStr = localStorage.getItem('uniclub_user')
      if (!userStr) {
        navigate("/login")
        return
      }

      try {
        const user = JSON.parse(userStr)
        const { items } = await getFullCart(user.id)
        
        if (items.length === 0) {
          navigate("/cart")
          return
        }
        
        setCart(items)
        
        // Auto-fill user info including default address
        const hasDefaultAddress = user.address && user.provinceCode && user.districtCode && user.wardCode
        
        setFormData(prev => ({
          ...prev,
          full_name: user.full_name || user.fullName || "",
          email: user.email || "",
          phone: user.phone || "",
          address: hasDefaultAddress ? user.address : "",
          province: hasDefaultAddress ? user.provinceCode : "",
          provinceName: hasDefaultAddress ? user.provinceName : "",
          district: hasDefaultAddress ? user.districtCode : "",
          districtName: hasDefaultAddress ? user.districtName : "",
          ward: hasDefaultAddress ? user.wardCode : "",
          wardName: hasDefaultAddress ? user.wardName : "",
        }))
        
        setUseDefaultAddress(hasDefaultAddress)
      } catch (error) {
        console.error('Error fetching cart:', error)
        navigate("/cart")
      } finally {
        setLoading(false)
      }
    }

    fetchCart()
  }, [navigate])

  const subtotal = cart.reduce((sum, item) => sum + (item.subtotal || item.unitPrice * item.quantity), 0)
  const shipping = subtotal >= 499000 ? 0 : 30000
  const total = subtotal + shipping

  const handleSubmit = async (e) => {
    e.preventDefault()
    setIsSubmitting(true)

    try {
      const userStr = localStorage.getItem('uniclub_user')
      if (!userStr) {
        navigate("/login")
        return
      }

      const user = JSON.parse(userStr)

      // Validate user has ID
      if (!user.id) {
        alert("Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.")
        navigate("/login")
        return
      }

      // Prepare order data
      const orderData = {
        userId: user.id,
        recipientName: formData.full_name,
        recipientPhone: formData.phone,
        shippingAddress: `${formData.address}, ${formData.wardName || formData.ward}, ${formData.districtName || formData.district}, ${formData.provinceName || formData.province}`,
        note: formData.note || "",
        paymentMethod: paymentMethod, // Use selected payment method
        orderVariants: cart.map((item) => ({
          variantSku: item.sku_variant,
          quantity: item.quantity,
          price: item.unitPrice,
        })),
      }

      const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
      const response = await fetch(`${API_URL}/orders`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(orderData),
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || "Không thể tạo đơn hàng")
      }

      const order = await response.json()

      // If VNPay payment, redirect to VNPay
      if (paymentMethod === "VNPay") {
        try {
          const paymentResponse = await fetch(`${API_URL}/vnpay/create-payment`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              orderId: order.id,
              orderInfo: `Thanh toan don hang ${order.id}`,
              locale: "vn",
            }),
          })

          if (!paymentResponse.ok) {
            throw new Error("Không thể tạo link thanh toán VNPay")
          }

          const paymentData = await paymentResponse.json()
          
          // Redirect to VNPay
          window.location.href = paymentData.paymentUrl
          return
        } catch (error) {
          console.error('Error creating VNPay payment:', error)
          alert("Có lỗi xảy ra khi tạo link thanh toán. Vui lòng thử lại.")
          setIsSubmitting(false)
          return
        }
      }

      // COD payment - show success and redirect to orders
      alert("Đặt hàng thành công! Đơn hàng của bạn đang được xử lý.")
      navigate("/orders")
    } catch (error) {
      console.error('Error creating order:', error)
      alert(error.message || "Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.")
    } finally {
      setIsSubmitting(false)
    }
  }

  if (loading) {
    return (
      <PageLayout title="Thanh toán" breadcrumbs={[{ label: "Thanh toán" }]}>
        <div className="section">
          <div className="flex items-center justify-center py-20">
            <Loader2 className="w-12 h-12 animate-spin text-primary" />
          </div>
        </div>
      </PageLayout>
    )
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
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-lg font-semibold text-foreground">Thông tin giao hàng</h2>
                {useDefaultAddress && formData.address && (
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    onClick={() => {
                      setUseDefaultAddress(false)
                      setFormData(prev => ({
                        ...prev,
                        address: "",
                        province: "",
                        provinceName: "",
                        district: "",
                        districtName: "",
                        ward: "",
                        wardName: "",
                      }))
                    }}
                  >
                    Nhập địa chỉ khác
                  </Button>
                )}
              </div>
              {useDefaultAddress && formData.address && (
                <div className="mb-4 p-3 bg-green-50 dark:bg-green-950 border border-green-200 dark:border-green-800 rounded-lg">
                  <p className="text-sm text-green-800 dark:text-green-200">
                    ✓ Đang sử dụng địa chỉ mặc định từ hồ sơ của bạn
                  </p>
                </div>
              )}
              <AddressForm formData={formData} onChange={setFormData} />
            </div>

            <div className="card p-6">
              <h2 className="text-lg font-semibold text-foreground mb-4">Phương thức thanh toán</h2>
              <div className="space-y-3">
                <label
                  className={`card p-4 flex items-center gap-3 cursor-pointer transition-colors ${
                    paymentMethod === "COD" ? "ring-2 ring-ring" : ""
                  }`}
                >
                  <input
                    type="radio"
                    name="payment"
                    value="COD"
                    checked={paymentMethod === "COD"}
                    onChange={() => setPaymentMethod("COD")}
                    className="w-4 h-4 text-primary"
                  />
                  <div>
                    <div className="font-medium text-foreground">Thanh toán khi nhận hàng (COD)</div>
                    <div className="text-sm text-muted-foreground">Thanh toán bằng tiền mặt khi nhận hàng</div>
                  </div>
                </label>

                <label
                  className={`card p-4 flex items-center gap-3 cursor-pointer transition-colors ${
                    paymentMethod === "VNPay" ? "ring-2 ring-ring" : ""
                  }`}
                >
                  <input
                    type="radio"
                    name="payment"
                    value="VNPay"
                    checked={paymentMethod === "VNPay"}
                    onChange={() => setPaymentMethod("VNPay")}
                    className="w-4 h-4 text-primary"
                  />
                  <div>
                    <div className="font-medium text-foreground">Thanh toán qua VNPay</div>
                    <div className="text-sm text-muted-foreground">Thanh toán trực tuyến qua cổng VNPay</div>
                  </div>
                </label>
              </div>
            </div>
          </div>

          <div className="lg:col-span-1">
            <div className="card p-6 space-y-4 sticky top-20">
              <h3 className="text-lg font-semibold text-foreground">Đơn hàng của bạn</h3>

              <div className="space-y-3 max-h-64 overflow-y-auto">
                {cart.map((item) => {
                  const quantity = item.quantity || item.qty || 0
                  const unitPrice = item.unitPrice || 0
                  const productName = item.variant?.product?.name || item.productName || "Sản phẩm"
                  const sizeName = item.variant?.size?.name || item.sizeName || ""
                  const colorName = item.variant?.color?.name || item.colorName || ""
                  const imageUrl = item.variant?.product?.primaryImage || item.image || "/placeholder.svg"
                  
                  return (
                    <div key={item.id} className="flex gap-3 text-sm">
                      <div className="w-16 h-16 rounded-lg overflow-hidden bg-surface flex-shrink-0">
                        <img src={imageUrl} alt={productName} className="w-full h-full object-cover" />
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="font-medium text-foreground line-clamp-1">{productName}</div>
                        <div className="text-muted-foreground">
                          {sizeName} / {colorName} × {quantity}
                        </div>
                        <div className="font-medium text-foreground">
                          <Price value={unitPrice * quantity} />
                        </div>
                      </div>
                    </div>
                  )
                })}
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
