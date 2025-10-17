import React, { useMemo } from "react"
import { useParams, useNavigate, Link } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/Price"
import { orders, order_variants, billing_details, variants, products, sizes, colors, payment_methods } from "@/lib/mock-data"
import { CheckCircle2, Package, Truck, MapPin, CreditCard, Calendar } from "lucide-react"

export default function OrderDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const orderId = Number(id)

  const order = useMemo(() => orders.find((o) => o.id === orderId), [orderId])
  const orderItems = useMemo(() => order_variants.filter((ov) => ov.id_order === orderId), [orderId])
  const billing = useMemo(() => billing_details.find((b) => b.id_order === orderId), [orderId])
  const paymentMethod = useMemo(() => payment_methods.find((pm) => pm.id === order?.id_payment), [order])

  const itemsWithDetails = useMemo(() => {
    return orderItems.map((oi) => {
      const variant = variants.find((v) => v.sku === oi.sku_variant)
      const product = products.find((p) => p.id === variant?.id_product)
      const size = sizes.find((s) => s.id === variant?.id_size)
      const color = colors.find((c) => c.id === variant?.id_color)
      return {
        ...oi,
        variant,
        product,
        size,
        color,
      }
    })
  }, [orderItems])

  if (!order) {
    return (
      <PageLayout breadcrumbs={[{ label: "Đơn hàng", href: "/account/orders" }, { label: "Không tìm thấy" }]}>
        <div className="section">
          <div className="card p-8 text-center">
            <div className="text-lg font-medium mb-2">Đơn hàng không tồn tại</div>
            <div className="text-muted-foreground mb-4">Vui lòng kiểm tra lại mã đơn hàng.</div>
            <Button onClick={() => navigate("/account/orders")}>Về danh sách đơn hàng</Button>
          </div>
        </div>
      </PageLayout>
    )
  }

  const statusConfig = {
    PENDING: { label: "Chờ xác nhận", color: "hsl(38 92% 50%)", bgColor: "hsl(38 92% 50% / 0.1)", icon: Package },
    CONFIRMED: { label: "Đã xác nhận", color: "hsl(199 89% 48%)", bgColor: "hsl(199 89% 48% / 0.1)", icon: CheckCircle2 },
    SHIPPING: { label: "Đang giao", color: "hsl(217.2 91.2% 55%)", bgColor: "hsl(217.2 91.2% 55% / 0.1)", icon: Truck },
    DELIVERED: { label: "Đã giao", color: "hsl(142 76% 36%)", bgColor: "hsl(142 76% 36% / 0.1)", icon: CheckCircle2 },
    CANCELLED: { label: "Đã hủy", color: "hsl(0 84.2% 60.2%)", bgColor: "hsl(0 84.2% 60.2% / 0.1)", icon: Package },
  }

  const currentStatus = statusConfig[order.status] || statusConfig.PENDING

  return (
    <PageLayout
      title={`Đơn hàng #${order.id}`}
      breadcrumbs={[
        { label: "Đơn hàng", href: "/account/orders" },
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
              Đơn hàng của bạn đã được đặt thành công. Cảm ơn bạn đã mua sắm!
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
                {itemsWithDetails.map((item) => (
                  <div key={item.sku_variant} className="flex gap-4 pb-4 border-b border-border last:border-0 last:pb-0">
                    <Link to={`/products/${item.product?.id}`} className="flex-shrink-0">
                      <div className="w-20 h-20 rounded-lg overflow-hidden bg-surface">
                        <img
                          src={item.variant?.images || "/placeholder.svg"}
                          alt={item.product?.name}
                          className="w-full h-full object-cover"
                        />
                      </div>
                    </Link>
                    <div className="flex-1 min-w-0">
                      <Link
                        to={`/products/${item.product?.id}`}
                        className="font-medium text-foreground hover:text-primary line-clamp-1"
                      >
                        {item.product?.name || "Sản phẩm"}
                      </Link>
                      <div className="text-sm text-muted-foreground mt-1">
                        {item.size?.name} / {item.color?.name}
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
            {billing && (
              <div className="card p-6">
                <div className="flex items-center gap-2 mb-4">
                  <MapPin className="w-5 h-5 text-primary" />
                  <h2 className="text-lg font-semibold">Địa chỉ giao hàng</h2>
                </div>
                <div className="space-y-2 text-sm">
                  <div className="font-medium text-foreground">{billing.full_name}</div>
                  <div className="text-muted-foreground">{billing.phone}</div>
                  {billing.email && <div className="text-muted-foreground">{billing.email}</div>}
                  <div className="text-muted-foreground">
                    {billing.address}, {billing.ward}, {billing.district}, {billing.province}
                  </div>
                  {billing.note && (
                    <div className="text-muted-foreground pt-2 border-t border-border">
                      <span className="font-medium">Ghi chú:</span> {billing.note}
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* Order Summary */}
          <div className="lg:col-span-1 space-y-6">
            <div className="card p-6 space-y-4">
              <h3 className="text-lg font-semibold">Thông tin đơn hàng</h3>

              <div className="space-y-3 text-sm">
                <div className="flex items-center gap-2 text-muted-foreground">
                  <Calendar className="w-4 h-4" />
                  <span>Ngày đặt: {new Date(order.created_at).toLocaleDateString("vi-VN")}</span>
                </div>
                {paymentMethod && (
                  <div className="flex items-center gap-2 text-muted-foreground">
                    <CreditCard className="w-4 h-4" />
                    <span>{paymentMethod.name}</span>
                  </div>
                )}
              </div>

              <div className="pt-4 border-t border-border space-y-2 text-sm">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Tạm tính</span>
                  <Price value={order.total} />
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Phí vận chuyển</span>
                  <span className="text-green-600">Miễn phí</span>
                </div>
              </div>

              <div className="pt-4 border-t border-border flex justify-between items-center">
                <span className="font-semibold text-foreground">Tổng cộng</span>
                <span className="text-xl font-bold" style={{ color: "hsl(217.2 91.2% 55%)" }}>
                  <Price value={order.total} />
                </span>
              </div>
            </div>

            <Button variant="outline" className="w-full" onClick={() => navigate("/orders")}>
              Xem tất cả đơn hàng
            </Button>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
