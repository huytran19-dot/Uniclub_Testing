"use client"

import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { Package } from "lucide-react"
import { PageLayout } from "@/components/page-layout"
import { TagStatus } from "@/components/tag-status"
import { Price } from "@/components/price"
import { Button } from "@/components/ui/button"
import { orders } from "@/lib/mock-data"
import { getCurrentUser } from "@/lib/auth"

export default function OrdersPage() {
  const router = useRouter()
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

  const userOrders = orders
    .filter((o) => o.id_user === user.id)
    .sort((a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime())

  if (userOrders.length === 0) {
    return (
      <PageLayout title="Đơn hàng của tôi" breadcrumbs={[{ label: "Đơn hàng" }]}>
        <div className="text-center py-16">
          <Package className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h2 className="text-2xl font-semibold text-foreground mb-2">Chưa có đơn hàng</h2>
          <p className="text-muted-foreground mb-6">Bạn chưa có đơn hàng nào</p>
          <Button onClick={() => router.push("/products")}>Khám phá sản phẩm</Button>
        </div>
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Đơn hàng của tôi" breadcrumbs={[{ label: "Đơn hàng" }]}>
      <div className="space-y-4">
        {userOrders.map((order) => (
          <Link key={order.id} href={`/orders/${order.id}`}>
            <div className="bg-white rounded-2xl border border-border p-6 shadow-sm hover:shadow-md transition-shadow">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="font-semibold text-foreground">Đơn hàng #{order.id}</h3>
                    <TagStatus value={order.status} />
                  </div>
                  <div className="text-sm text-muted-foreground">
                    {new Date(order.created_at).toLocaleDateString("vi-VN", {
                      year: "numeric",
                      month: "long",
                      day: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </div>
                </div>

                <div className="flex items-center gap-6">
                  <div className="text-right">
                    <div className="text-sm text-muted-foreground mb-1">Tổng tiền</div>
                    <Price value={order.total} className="text-xl font-bold text-primary" />
                  </div>
                  <Button variant="outline" className="bg-transparent">
                    Xem chi tiết
                  </Button>
                </div>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </PageLayout>
  )
}
