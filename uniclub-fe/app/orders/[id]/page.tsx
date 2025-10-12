"use client"

import { useEffect, useState } from "react"
import { useParams, useRouter } from "next/navigation"
import Image from "next/image"
import { PageLayout } from "@/components/page-layout"
import { TagStatus } from "@/components/tag-status"
import { Price } from "@/components/price"
import { Button } from "@/components/ui/button"
import {
  orders,
  order_variants,
  billing_details,
  variants,
  products,
  sizes,
  colors,
  payment_methods,
} from "@/lib/mock-data"
import { getCurrentUser } from "@/lib/auth"

export default function OrderDetailPage() {
  const params = useParams()
  const router = useRouter()
  const orderId = Number(params.id)
  const [mounted, setMounted] = useState(false)

  useEffect(() => {
    setMounted(true)
    const user = getCurrentUser()
    if (!user) {
      router.push("/login?redirect=/orders")
    }
  }, [router])

  if (!mounted) return null

  const user = getCurrentUser()
  if (!user) return null

  const order = orders.find((o) => o.id === orderId && o.id_user === user.id)

  if (!order) {
    return (
      <PageLayout title="Không tìm thấy đơn hàng" breadcrumbs={[{ label: "Đơn hàng", href: "/orders" }]}>
        <div className="text-center py-16">
          <p className="text-lg text-muted-foreground mb-4">Đơn hàng không tồn tại hoặc bạn không có quyền truy cập</p>
          <Button onClick={() => router.push("/orders")}>Quay lại danh sách</Button>
        </div>
      </PageLayout>
    )
  }

  const orderItems = order_variants.filter((ov) => ov.id_order === order.id)
  const billing = billing_details.find((bd) => bd.id_order === order.id)
  const paymentMethod = payment_methods.find((pm) => pm.id === order.id_payment)

  const handleCancelOrder = () => {
    if (order.status === "PENDING") {
      order.status = "CANCELLED"
      order.updated_at = new Date().toISOString()
      router.refresh()
    }
  }

  return (
    <PageLayout
      title={`Đơn hàng #${order.id}`}
      breadcrumbs={[{ label: "Đơn hàng", href: "/orders" }, { label: `#${order.id}` }]}
    >
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Main Content */}
        <div className="lg:col-span-2 space-y-6">
          {/* Order Header */}
          <div className="bg-white rounded-2xl border border-border p-6 shadow-sm">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-4">
              <div>
                <h2 className="text-2xl font-bold text-foreground mb-2">Đơn hàng #{order.id}</h2>
                <div className="text-sm text-muted-foreground">
                  Đặt ngày{" "}
                  {new Date(order.created_at).toLocaleDateString("vi-VN", {
                    year: "numeric",
                    month: "long",
                    day: "numeric",
                    hour: "2-digit",
                    minute: "2-digit",
                  })}
                </div>
              </div>
              <TagStatus value={order.status} />
            </div>

            {order.status === "PENDING" && (
              <Button variant="outline" onClick={handleCancelOrder} className="bg-transparent">
                Hủy đơn hàng
              </Button>
            )}
          </div>

          {/* Order Items */}
          <div className="bg-white rounded-2xl border border-border p-6 shadow-sm">
            <h3 className="text-lg font-semibold text-foreground mb-4">Sản phẩm</h3>
            <div className="space-y-4">
              {orderItems.map((item) => {
                const variant = variants.find((v) => v.sku === item.sku_variant)
                const product = variant ? products.find((p) => p.id === variant.id_product) : null
                const size = variant ? sizes.find((s) => s.id === variant.id_size) : null
                const color = variant ? colors.find((c) => c.id === variant.id_color) : null

                return (
                  <div key={item.sku_variant} className="flex gap-4 pb-4 border-b border-border last:border-0">
                    <div className="w-20 h-20 flex-shrink-0 rounded-lg overflow-hidden bg-surface">
                      <Image
                        src={variant?.images || "/placeholder.svg"}
                        alt={product?.name || "Product"}
                        width={80}
                        height={80}
                        className="w-full h-full object-cover"
                      />
                    </div>

                    <div className="flex-1">
                      <h4 className="font-semibold text-foreground mb-1">{product?.name || "Sản phẩm"}</h4>
                      <div className="text-sm text-muted-foreground mb-2">
                        {size?.name} / {color?.name}
                      </div>
                      <div className="flex items-center justify-between">
                        <span className="text-sm text-muted-foreground">Số lượng: {item.quantity}</span>
                        <Price value={item.price * item.quantity} className="font-semibold text-foreground" />
                      </div>
                    </div>
                  </div>
                )
              })}
            </div>

            <div className="border-t border-border pt-4 mt-4">
              <div className="flex justify-between items-center">
                <span className="text-lg font-semibold text-foreground">Tổng cộng</span>
                <Price value={order.total} className="text-2xl font-bold text-primary" />
              </div>
            </div>
          </div>
        </div>

        {/* Sidebar */}
        <div className="lg:col-span-1 space-y-6">
          {/* Billing Details */}
          {billing && (
            <div className="bg-white rounded-2xl border border-border p-6 shadow-sm">
              <h3 className="text-lg font-semibold text-foreground mb-4">Thông tin giao hàng</h3>
              <div className="space-y-3 text-sm">
                <div>
                  <div className="text-muted-foreground mb-1">Người nhận</div>
                  <div className="font-medium text-foreground">{billing.full_name}</div>
                </div>
                <div>
                  <div className="text-muted-foreground mb-1">Số điện thoại</div>
                  <div className="font-medium text-foreground">{billing.phone}</div>
                </div>
                <div>
                  <div className="text-muted-foreground mb-1">Email</div>
                  <div className="font-medium text-foreground">{billing.email}</div>
                </div>
                <div>
                  <div className="text-muted-foreground mb-1">Địa chỉ</div>
                  <div className="font-medium text-foreground">
                    {billing.address}, {billing.ward}, {billing.district}, {billing.province}
                  </div>
                </div>
                {billing.note && (
                  <div>
                    <div className="text-muted-foreground mb-1">Ghi chú</div>
                    <div className="font-medium text-foreground">{billing.note}</div>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* Payment Method */}
          <div className="bg-white rounded-2xl border border-border p-6 shadow-sm">
            <h3 className="text-lg font-semibold text-foreground mb-4">Thanh toán</h3>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-muted-foreground">Phương thức</span>
                <span className="font-medium text-foreground">{paymentMethod?.name}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Trạng thái</span>
                <TagStatus value={order.status} />
              </div>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
