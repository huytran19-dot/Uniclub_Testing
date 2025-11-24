import React, { useState, useEffect } from "react"
import { Link, useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/Price"
import { Package, ChevronRight, Loader2 } from "lucide-react"

export default function OrdersPage() {
  const navigate = useNavigate()
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetchOrders()
  }, [])

  const fetchOrders = async () => {
    try {
      const userStr = localStorage.getItem('uniclub_user')
      if (!userStr) {
        navigate("/login")
        return
      }

      const user = JSON.parse(userStr)
      const response = await fetch(`http://localhost:8080/api/orders/user/${user.id}`)

      if (!response.ok) {
        throw new Error("Không thể tải danh sách đơn hàng")
      }

      const data = await response.json()
      setOrders(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const statusConfig = {
    PENDING: { label: "Chờ xác nhận", color: "hsl(38 92% 50%)", bgColor: "hsl(38 92% 50% / 0.1)" },
    CONFIRMED: { label: "Đã xác nhận", color: "hsl(199 89% 48%)", bgColor: "hsl(199 89% 48% / 0.1)" },
    SHIPPING: { label: "Đang giao", color: "hsl(217.2 91.2% 55%)", bgColor: "hsl(217.2 91.2% 55% / 0.1)" },
    DELIVERED: { label: "Đã giao", color: "hsl(142 76% 36%)", bgColor: "hsl(142 76% 36% / 0.1)" },
    CANCELLED: { label: "Đã hủy", color: "hsl(0 84.2% 60.2%)", bgColor: "hsl(0 84.2% 60.2% / 0.1)" },
  }

  const getStatusConfig = (order) => {
    // If VNPay + PENDING, show "Chờ thanh toán"
    if (order.paymentMethod === "VNPay" && order.status === "PENDING") {
      return { label: "Chờ thanh toán", color: "hsl(38 92% 50%)", bgColor: "hsl(38 92% 50% / 0.1)" }
    }
    return statusConfig[order.status] || statusConfig.PENDING
  }

  if (loading) {
    return (
      <PageLayout title="Đơn hàng" breadcrumbs={[{ label: "Đơn hàng" }]}>
        <div className="section">
          <div className="flex items-center justify-center py-20">
            <Loader2 className="w-12 h-12 animate-spin text-primary" />
          </div>
        </div>
      </PageLayout>
    )
  }

  if (error) {
    return (
      <PageLayout title="Đơn hàng" breadcrumbs={[{ label: "Đơn hàng" }]}>
        <div className="section">
          <div className="card p-12 text-center">
            <div className="text-lg font-medium mb-2 text-red-600">Lỗi</div>
            <div className="text-muted-foreground mb-6">{error}</div>
            <Button onClick={fetchOrders}>Thử lại</Button>
          </div>
        </div>
      </PageLayout>
    )
  }

  if (orders.length === 0) {
    return (
      <PageLayout title="Đơn hàng" breadcrumbs={[{ label: "Đơn hàng" }]}>
        <div className="section">
          <div className="card p-12 text-center">
            <Package className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
            <div className="text-lg font-medium mb-2">Chưa có đơn hàng</div>
            <div className="text-muted-foreground mb-6">Bạn chưa có đơn hàng nào. Hãy bắt đầu mua sắm ngay!</div>
            <Button asChild>
              <Link to="/products">Khám phá sản phẩm</Link>
            </Button>
          </div>
        </div>
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Đơn hàng của tôi" breadcrumbs={[{ label: "Đơn hàng" }]}>
      <div className="section">
        <div className="space-y-4">
          {orders.map((order) => {
            const status = getStatusConfig(order)
            const itemCount = order.orderVariants?.length || 0
            const paymentMethodLabel = order.paymentMethod === "VNPay" ? "VNPay" : "COD"
            return (
              <Link key={order.id} to={`/orders/${order.id}`} className="block">
                <div className="card p-6 hover:shadow-md transition-shadow">
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex-1 space-y-3">
                      <div className="flex items-center gap-3">
                        <div className="font-semibold text-foreground">Đơn hàng #{order.id}</div>
                        <div
                          className="px-2.5 py-1 rounded-full text-xs font-medium"
                          style={{ backgroundColor: status.bgColor, color: status.color }}
                        >
                          {status.label}
                        </div>
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {new Date(order.createdAt).toLocaleDateString("vi-VN", {
                          year: "numeric",
                          month: "long",
                          day: "numeric",
                        })}
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {itemCount} sản phẩm • {paymentMethodLabel}
                      </div>
                    </div>
                    <div className="text-right space-y-2">
                      <div className="text-xl font-bold" style={{ color: "hsl(217.2 91.2% 55%)" }}>
                        <Price value={order.total} />
                      </div>
                      <Button variant="outline" size="sm" className="gap-1">
                        Xem chi tiết
                        <ChevronRight className="w-4 h-4" />
                      </Button>
                    </div>
                  </div>
                </div>
              </Link>
            )
          })}
        </div>
      </div>
    </PageLayout>
  )
}
