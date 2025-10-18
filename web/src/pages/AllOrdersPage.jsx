import React, { useState, useMemo } from "react"
import { Link } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/Price"
import { orders, order_variants, payment_methods, billing_details } from "@/lib/mock-data"
import { Package, ChevronRight, Filter, Search, Calendar, Download } from "lucide-react"

export default function AllOrdersPage() {
  const [statusFilter, setStatusFilter] = useState("ALL")
  const [searchQuery, setSearchQuery] = useState("")
  const [sortBy, setSortBy] = useState("date-desc")

  const allOrders = useMemo(() => {
    return orders.map((order) => {
      const items = order_variants.filter((ov) => ov.id_order === order.id)
      const payment = payment_methods.find((pm) => pm.id === order.id_payment)
      const billing = billing_details.find((b) => b.id_order === order.id)
      return {
        ...order,
        itemCount: items.reduce((sum, item) => sum + item.quantity, 0),
        payment,
        billing,
      }
    })
  }, [])

  const filteredOrders = useMemo(() => {
    let filtered = allOrders

    // Filter by status
    if (statusFilter !== "ALL") {
      filtered = filtered.filter((order) => order.status === statusFilter)
    }

    // Filter by search query (order ID or customer name)
    if (searchQuery) {
      filtered = filtered.filter(
        (order) =>
          order.id.toString().includes(searchQuery) ||
          order.billing?.full_name?.toLowerCase().includes(searchQuery.toLowerCase())
      )
    }

    // Sort
    filtered = [...filtered].sort((a, b) => {
      switch (sortBy) {
        case "date-desc":
          return new Date(b.created_at) - new Date(a.created_at)
        case "date-asc":
          return new Date(a.created_at) - new Date(b.created_at)
        case "total-desc":
          return b.total - a.total
        case "total-asc":
          return a.total - b.total
        default:
          return 0
      }
    })

    return filtered
  }, [allOrders, statusFilter, searchQuery, sortBy])

  const statusConfig = {
    ALL: { label: "Tất cả", count: allOrders.length },
    PENDING: {
      label: "Chờ xác nhận",
      color: "hsl(38 92% 50%)",
      bgColor: "hsl(38 92% 50% / 0.1)",
      count: allOrders.filter((o) => o.status === "PENDING").length,
    },
    CONFIRMED: {
      label: "Đã xác nhận",
      color: "hsl(199 89% 48%)",
      bgColor: "hsl(199 89% 48% / 0.1)",
      count: allOrders.filter((o) => o.status === "CONFIRMED").length,
    },
    SHIPPING: {
      label: "Đang giao",
      color: "hsl(217.2 91.2% 55%)",
      bgColor: "hsl(217.2 91.2% 55% / 0.1)",
      count: allOrders.filter((o) => o.status === "SHIPPING").length,
    },
    DELIVERED: {
      label: "Đã giao",
      color: "hsl(142 76% 36%)",
      bgColor: "hsl(142 76% 36% / 0.1)",
      count: allOrders.filter((o) => o.status === "DELIVERED").length,
    },
    CANCELLED: {
      label: "Đã hủy",
      color: "hsl(0 84.2% 60.2%)",
      bgColor: "hsl(0 84.2% 60.2% / 0.1)",
      count: allOrders.filter((o) => o.status === "CANCELLED").length,
    },
  }

  const totalRevenue = useMemo(() => {
    return filteredOrders
      .filter((o) => o.status !== "CANCELLED")
      .reduce((sum, order) => sum + order.total, 0)
  }, [filteredOrders])

  return (
    <PageLayout title="Quản lý đơn hàng" breadcrumbs={[{ label: "Đơn hàng" }, { label: "Tất cả" }]}>
      <div className="section space-y-6">
        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="card p-4">
            <div className="text-sm text-muted-foreground mb-1">Tổng đơn hàng</div>
            <div className="text-2xl font-bold text-foreground">{allOrders.length}</div>
          </div>
          <div className="card p-4">
            <div className="text-sm text-muted-foreground mb-1">Chờ xử lý</div>
            <div className="text-2xl font-bold" style={{ color: "hsl(38 92% 50%)" }}>
              {statusConfig.PENDING.count}
            </div>
          </div>
          <div className="card p-4">
            <div className="text-sm text-muted-foreground mb-1">Đang giao</div>
            <div className="text-2xl font-bold" style={{ color: "hsl(217.2 91.2% 55%)" }}>
              {statusConfig.SHIPPING.count}
            </div>
          </div>
          <div className="card p-4">
            <div className="text-sm text-muted-foreground mb-1">Doanh thu</div>
            <div className="text-2xl font-bold" style={{ color: "hsl(142 76% 36%)" }}>
              <Price value={totalRevenue} />
            </div>
          </div>
        </div>

        {/* Filters */}
        <div className="card p-6 space-y-4">
          {/* Status Tabs */}
          <div className="flex items-center gap-2 overflow-x-auto pb-2">
            {Object.entries(statusConfig).map(([key, config]) => (
              <button
                key={key}
                onClick={() => setStatusFilter(key)}
                className={`px-4 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-all ${
                  statusFilter === key
                    ? "shadow-sm"
                    : "hover:bg-gray-50"
                }`}
                style={
                  statusFilter === key
                    ? {
                        backgroundColor: config.bgColor || "hsl(210 40% 96.1%)",
                        color: config.color || "hsl(222.2 84% 8%)",
                      }
                    : { color: "hsl(215.4 16.3% 42%)" }
                }
              >
                {config.label} ({config.count})
              </button>
            ))}
          </div>

          {/* Search and Sort */}
          <div className="flex flex-col md:flex-row gap-3">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input
                type="text"
                placeholder="Tìm theo mã đơn hoặc tên khách hàng..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 h-10 rounded-lg border border-border bg-white text-foreground text-sm outline-none focus:ring-2 focus:ring-ring"
              />
            </div>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="px-4 h-10 rounded-lg border border-border bg-white text-foreground text-sm outline-none focus:ring-2 focus:ring-ring"
            >
              <option value="date-desc">Mới nhất</option>
              <option value="date-asc">Cũ nhất</option>
              <option value="total-desc">Giá cao nhất</option>
              <option value="total-asc">Giá thấp nhất</option>
            </select>
            <Button variant="outline" className="gap-2">
              <Download className="w-4 h-4" />
              Xuất Excel
            </Button>
          </div>
        </div>

        {/* Orders Table */}
        {filteredOrders.length === 0 ? (
          <div className="card p-12 text-center">
            <Package className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
            <div className="text-lg font-medium mb-2">Không tìm thấy đơn hàng</div>
            <div className="text-muted-foreground">Thử thay đổi bộ lọc hoặc tìm kiếm khác</div>
          </div>
        ) : (
          <div className="card overflow-hidden">
            {/* Desktop Table */}
            <div className="hidden md:block overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50 border-b border-border">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                      Mã đơn
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                      Khách hàng
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                      Ngày đặt
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                      Sản phẩm
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                      Tổng tiền
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                      Trạng thái
                    </th>
                    <th className="px-6 py-3 text-right text-xs font-medium text-muted-foreground uppercase tracking-wider">
                      Thao tác
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-border">
                  {filteredOrders.map((order) => {
                    const status = statusConfig[order.status]
                    return (
                      <tr key={order.id} className="hover:bg-gray-50 transition-colors">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <Link
                            to={`/orders/${order.id}`}
                            className="font-medium text-primary hover:underline"
                          >
                            #{order.id}
                          </Link>
                        </td>
                        <td className="px-6 py-4">
                          <div className="text-sm font-medium text-foreground">
                            {order.billing?.full_name || "N/A"}
                          </div>
                          <div className="text-sm text-muted-foreground">{order.billing?.phone}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-foreground">
                            {new Date(order.created_at).toLocaleDateString("vi-VN")}
                          </div>
                          <div className="text-xs text-muted-foreground">
                            {new Date(order.created_at).toLocaleTimeString("vi-VN", {
                              hour: "2-digit",
                              minute: "2-digit",
                            })}
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-foreground">{order.itemCount} sản phẩm</div>
                          <div className="text-xs text-muted-foreground">{order.payment?.name}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-semibold text-foreground">
                            <Price value={order.total} />
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span
                            className="px-2.5 py-1 rounded-full text-xs font-medium"
                            style={{ backgroundColor: status.bgColor, color: status.color }}
                          >
                            {status.label}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-right">
                          <Link to={`/orders/${order.id}`}>
                            <Button variant="ghost" size="sm" className="gap-1">
                              Chi tiết
                              <ChevronRight className="w-4 h-4" />
                            </Button>
                          </Link>
                        </td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>

            {/* Mobile Cards */}
            <div className="md:hidden divide-y divide-border">
              {filteredOrders.map((order) => {
                const status = statusConfig[order.status]
                return (
                  <Link key={order.id} to={`/orders/${order.id}`} className="block p-4 hover:bg-gray-50">
                    <div className="flex items-start justify-between mb-2">
                      <div className="font-medium text-primary">#{order.id}</div>
                      <span
                        className="px-2.5 py-1 rounded-full text-xs font-medium"
                        style={{ backgroundColor: status.bgColor, color: status.color }}
                      >
                        {status.label}
                      </span>
                    </div>
                    <div className="text-sm text-foreground mb-1">{order.billing?.full_name}</div>
                    <div className="text-xs text-muted-foreground mb-2">
                      {new Date(order.created_at).toLocaleDateString("vi-VN")} • {order.itemCount} sản phẩm
                    </div>
                    <div className="flex items-center justify-between">
                      <div className="font-semibold text-foreground">
                        <Price value={order.total} />
                      </div>
                      <ChevronRight className="w-4 h-4 text-muted-foreground" />
                    </div>
                  </Link>
                )
              })}
            </div>
          </div>
        )}

        {/* Pagination Info */}
        {filteredOrders.length > 0 && (
          <div className="text-center text-sm text-muted-foreground">
            Hiển thị {filteredOrders.length} đơn hàng
          </div>
        )}
      </div>
    </PageLayout>
  )
}
