import React, { useMemo } from "react"
import { Link } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/Price"
import { orders, order_variants, payment_methods } from "@/lib/mock-data"
import { Package, ChevronRight } from "lucide-react"

export default function OrdersPage() {
  const userOrders = useMemo(() => {
    // In real app, filter by current user ID
    return orders.map((order) => {
      const items = order_variants.filter((ov) => ov.id_order === order.id)
      const payment = payment_methods.find((pm) => pm.id === order.id_payment)
      return {
        ...order,
        itemCount: items.reduce((sum, item) => sum + item.quantity, 0),
        payment,
      }
    })
  }, [])

  const statusConfig = {
    PENDING: { label: "Chờ xác nhận", color: "hsl(38 92% 50%)", bgColor: "hsl(38 92% 50% / 0.1)" },
    CONFIRMED: { label: "Đã xác nhận", color: "hsl(199 89% 48%)", bgColor: "hsl(199 89% 48% / 0.1)" },
    SHIPPING: { label: "Đang giao", color: "hsl(217.2 91.2% 55%)", bgColor: "hsl(217.2 91.2% 55% / 0.1)" },
    DELIVERED: { label: "Đã giao", color: "hsl(142 76% 36%)", bgColor: "hsl(142 76% 36% / 0.1)" },
    CANCELLED: { label: "Đã hủy", color: "hsl(0 84.2% 60.2%)", bgColor: "hsl(0 84.2% 60.2% / 0.1)" },
  }

  if (userOrders.length === 0) {
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
          {userOrders.map((order) => {
            const status = statusConfig[order.status] || statusConfig.PENDING
            return (
              <Link key={order.id} to={`/orders/${order.id}`}>
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
                        {new Date(order.created_at).toLocaleDateString("vi-VN", {
                          year: "numeric",
                          month: "long",
                          day: "numeric",
                        })}
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {order.itemCount} sản phẩm • {order.payment?.name}
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
