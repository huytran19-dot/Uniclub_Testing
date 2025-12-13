import React, { useState, useEffect } from "react"
import { useParams, useNavigate, Link } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/Price"
import { parseShippingAddress } from "@/lib/address-parser"
import { CheckCircle2, Package, Truck, MapPin, CreditCard, Calendar, Loader2, Clock } from "lucide-react"

export default function OrderDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [order, setOrder] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [parsedAddress, setParsedAddress] = useState("")
  const [isCancelling, setIsCancelling] = useState(false)
  const [isRetryingPayment, setIsRetryingPayment] = useState(false)

  useEffect(() => {
    fetchOrderDetail()
  }, [id])

  // Parse address when order changes
  useEffect(() => {
    const parseAddress = async () => {
      if (order?.shippingAddress) {
        const parsed = await parseShippingAddress(order.shippingAddress)
        setParsedAddress(parsed)
      }
    }
    parseAddress()
  }, [order])

  const fetchOrderDetail = async () => {
    try {
      const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
      const response = await fetch(`${API_URL}/orders/${id}`)
      
      if (!response.ok) {
        throw new Error("Không thể tải thông tin đơn hàng")
      }

      const data = await response.json()
      setOrder(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleCancelOrder = async () => {
    if (!confirm("Bạn có chắc chắn muốn hủy đơn hàng này?")) {
      return
    }

    setIsCancelling(true)
    try {
      const response = await fetch(`${API_URL}/orders/${id}/cancel`, {
        method: "PUT",
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(errorText || "Không thể hủy đơn hàng")
      }

      const updatedOrder = await response.json()
      setOrder(updatedOrder)
      alert("Đơn hàng đã được hủy thành công")
    } catch (err) {
      alert(err.message)
    } finally {
      setIsCancelling(false)
    }
  }

  const handleRetryPayment = async () => {
    if (!confirm("Bạn có muốn thanh toán lại đơn hàng này?")) {
      return
    }

    setIsRetryingPayment(true)
    try {
      // Call retry payment endpoint
      const response = await fetch(`${API_URL}/orders/${id}/retry-payment`, {
        method: "POST",
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(errorText || "Không thể tạo link thanh toán mới")
      }

      const paymentUrl = await response.text()
      
      // Redirect to VNPay
      window.location.href = paymentUrl
    } catch (err) {
      alert(err.message || "Có lỗi xảy ra khi tạo link thanh toán")
      setIsRetryingPayment(false)
    }
  }

  if (loading) {
    return (
      <PageLayout breadcrumbs={[{ label: "Đơn hàng", href: "/orders" }, { label: "Chi tiết" }]}>
        <div className="section">
          <div className="flex items-center justify-center py-20">
            <Loader2 className="w-12 h-12 animate-spin text-primary" />
          </div>
        </div>
      </PageLayout>
    )
  }

  if (error || !order) {
    return (
      <PageLayout breadcrumbs={[{ label: "Đơn hàng", href: "/orders" }, { label: "Không tìm thấy" }]}>
        <div className="section">
          <div className="card p-8 text-center">
            <div className="text-lg font-medium mb-2">Đơn hàng không tồn tại</div>
            <div className="text-muted-foreground mb-4">{error || "Vui lòng kiểm tra lại mã đơn hàng."}</div>
            <Button onClick={() => navigate("/orders")}>Về danh sách đơn hàng</Button>
          </div>
        </div>
      </PageLayout>
    )
  }

  const statusConfig = {
    PENDING: { 
      label: "Chờ xác nhận", 
      color: "hsl(38 92% 50%)", 
      bgColor: "hsl(38 92% 50% / 0.1)", 
      icon: Package,
      message: "Đơn hàng của bạn đang chờ xác nhận. Chúng tôi sẽ xử lý trong thời gian sớm nhất!"
    },
    CONFIRMED: { 
      label: "Đã xác nhận", 
      color: "hsl(199 89% 48%)", 
      bgColor: "hsl(199 89% 48% / 0.1)", 
      icon: CheckCircle2,
      message: "Đơn hàng đã được xác nhận. Chúng tôi đang chuẩn bị hàng để giao cho bạn!"
    },
    SHIPPING: { 
      label: "Đang giao", 
      color: "hsl(217.2 91.2% 55%)", 
      bgColor: "hsl(217.2 91.2% 55% / 0.1)", 
      icon: Truck,
      message: "Đơn hàng đang trên đường giao đến bạn. Vui lòng chú ý điện thoại!"
    },
    DELIVERED: { 
      label: "Đã giao", 
      color: "hsl(142 76% 36%)", 
      bgColor: "hsl(142 76% 36% / 0.1)", 
      icon: CheckCircle2,
      message: "Đơn hàng đã được giao thành công. Cảm ơn bạn đã mua sắm!"
    },
    CANCELLED: { 
      label: "Đã hủy", 
      color: "hsl(0 84.2% 60.2%)", 
      bgColor: "hsl(0 84.2% 60.2% / 0.1)", 
      icon: Package,
      message: "Đơn hàng đã bị hủy. Nếu có thắc mắc, vui lòng liên hệ với chúng tôi."
    },
  }

  // Check if this is a VNPay order waiting for payment
  const isVNPayPending = order?.paymentMethod === "VNPay" && order?.status === "PENDING"
  const currentStatus = isVNPayPending 
    ? {
        label: "Chờ thanh toán",
        color: "hsl(38 92% 50%)",
        bgColor: "hsl(38 92% 50% / 0.1)",
        icon: Clock,
        message: "Vui lòng hoàn tất thanh toán để xác nhận đơn hàng của bạn."
      }
    : statusConfig[order?.status] || statusConfig.PENDING

  return (
    <PageLayout
      title={`Đơn hàng #${order.id}`}
      breadcrumbs={[
        { label: "Đơn hàng", href: "/orders" },
        { label: `#${order.id}` },
      ]}
    >
      <div className="section space-y-6">
        {/* Success Banner */}
        <div 
          className="card p-6 flex items-center gap-4"
          style={{ backgroundColor: currentStatus.bgColor }}
        >
          <div 
            className="w-12 h-12 rounded-full flex items-center justify-center"
            style={{ backgroundColor: currentStatus.color, color: "white" }}
          >
            <currentStatus.icon className="w-6 h-6" />
          </div>
          <div className="flex-1">
            <div className="font-semibold text-lg" style={{ color: currentStatus.color }}>
              {currentStatus.label}
            </div>
            <div className="text-sm text-muted-foreground">
              {currentStatus.message}
            </div>
          </div>
          <Button variant="outline" onClick={() => navigate("/products")}>
            Tiếp tục mua sắm
          </Button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Order Items */}
          <div className="lg:col-span-2 space-y-6">
            <div className="card p-6">
              <h2 className="text-lg font-semibold mb-4">Sản phẩm đã đặt</h2>
              <div className="space-y-4">
                {order.orderVariants?.map((item, index) => (
                  <div key={index} className="flex gap-4 pb-4 border-b border-border last:border-0 last:pb-0">
                    <Link to={`/products/${item?.productId}`} className="flex-shrink-0">
                      <div className="w-20 h-20 rounded-lg overflow-hidden bg-surface">
                        <img
                          src={item?.images || "/placeholder.svg"}
                          alt={item?.productName || "Sản phẩm"}
                          className="w-full h-full object-cover"
                        />
                      </div>
                    </Link>
                    <div className="flex-1 min-w-0">
                      <Link
                        to={`/products/${item?.productId}`}
                        className="font-medium text-foreground hover:text-primary line-clamp-1"
                      >
                        {item?.productName || "Sản phẩm"}
                      </Link>
                      <div className="text-sm text-muted-foreground mt-1">
                        {item?.sizeName} / {item?.colorName}
                      </div>
                      <div className="text-sm text-muted-foreground">Số lượng: {item.quantity}</div>
                    </div>
                    <div className="text-right">
                      <div className="font-semibold text-foreground">
                        <Price value={item.price * item.quantity} />
                      </div>
                      <div className="text-sm text-muted-foreground">
                        <Price value={item.price} /> × {item.quantity}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Shipping Address */}
            <div className="card p-6">
              <div className="flex items-center gap-2 mb-4">
                <MapPin className="w-5 h-5 text-primary" />
                <h2 className="text-lg font-semibold">Địa chỉ giao hàng</h2>
              </div>
              <div className="space-y-2 text-sm">
                <div className="font-medium text-foreground">{order.recipientName}</div>
                <div className="text-muted-foreground">{order.recipientPhone}</div>
                <div className="text-muted-foreground">{parsedAddress || order.shippingAddress}</div>
                {order.note && (
                  <div className="text-muted-foreground pt-2 border-t border-border">
                    <span className="font-medium">Ghi chú:</span> {order.note}
                  </div>
                )}
              </div>
            </div>
          </div>

          {/* Order Summary */}
          <div className="lg:col-span-1 space-y-6">
            <div className="card p-6 space-y-4">
              <h3 className="text-lg font-semibold">Thông tin đơn hàng</h3>

              <div className="space-y-3 text-sm">
                <div className="flex items-center gap-2 text-muted-foreground">
                  <Calendar className="w-4 h-4" />
                  <span>Ngày đặt: {new Date(order.createdAt).toLocaleDateString("vi-VN")}</span>
                </div>
                <div className="flex items-center gap-2 text-muted-foreground">
                  <CreditCard className="w-4 h-4" />
                  <span>
                    {order.paymentMethod === "VNPay" 
                      ? "Thanh toán qua VNPay" 
                      : "Thanh toán khi nhận hàng (COD)"}
                  </span>
                </div>
              </div>

              <div className="pt-4 border-t border-border space-y-2 text-sm">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Tạm tính</span>
                  <Price value={order.total - order.shippingFee} />
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Phí vận chuyển</span>
                  {order.shippingFee === 0 ? (
                    <span className="text-green-600">Miễn phí</span>
                  ) : (
                    <Price value={order.shippingFee} />
                  )}
                </div>
              </div>

              <div className="pt-4 border-t border-border flex justify-between items-center">
                <span className="font-semibold text-foreground">Tổng cộng</span>
                <span className="text-xl font-bold" style={{ color: "hsl(217.2 91.2% 55%)" }}>
                  <Price value={order.total} />
                </span>
              </div>
            </div>

            <div className="space-y-2">
              {/* Show retry payment button only if VNPay + PENDING */}
              {isVNPayPending && (
                <Button 
                  className="w-full bg-blue-600 hover:bg-blue-700" 
                  onClick={handleRetryPayment}
                  disabled={isRetryingPayment}
                >
                  {isRetryingPayment ? "Đang tạo link thanh toán..." : "Thanh toán lại"}
                </Button>
              )}

              {/* Show cancel button only if order is PENDING or CONFIRMED */}
              {(order.status === "PENDING" || order.status === "CONFIRMED") && (
                <Button 
                  variant="destructive" 
                  className="w-full" 
                  onClick={handleCancelOrder}
                  disabled={isCancelling}
                >
                  {isCancelling ? "Đang hủy..." : "Hủy đơn hàng"}
                </Button>
              )}
              
              <Button variant="outline" className="w-full" onClick={() => navigate("/orders")}>
                Xem tất cả đơn hàng
              </Button>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
