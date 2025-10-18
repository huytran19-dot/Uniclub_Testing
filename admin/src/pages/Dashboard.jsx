"use client"

import { useEffect, useState } from "react"
import Card from "../components/Card"
import { BarChart, Bar, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts"
import { api } from "../lib/api"
import { formatMoney } from "../lib/utils"

const revenueData = [
  { month: "Tháng 1", revenue: 4000000 },
  { month: "Tháng 2", revenue: 3000000 },
  { month: "Tháng 3", revenue: 2000000 },
  { month: "Tháng 4", revenue: 2780000 },
  { month: "Tháng 5", revenue: 1890000 },
  { month: "Tháng 6", revenue: 2390000 },
]

const orderStatusData = [
  { name: "Chờ xử lý", value: 12 },
  { name: "Đã thanh toán", value: 28 },
  { name: "Đã giao", value: 45 },
  { name: "Đã hủy", value: 5 },
]

const COLORS = ["#FBBF24", "#3B82F6", "#10B981", "#EF4444"]

export default function Dashboard() {
  const [stats, setStats] = useState({
    revenue: 0,
    orders: 0,
    activeUsers: 0,
    lowStock: 0,
  })

  useEffect(() => {
    const loadStats = async () => {
      const variants = await api.list("variants")
      const orders = await api.list("orders")

      const lowStockCount = variants.filter((v) => v.quantity < 5).length
      const totalRevenue = orders.reduce((sum, o) => sum + o.total, 0)

      setStats({
        revenue: totalRevenue,
        orders: orders.length,
        activeUsers: 156,
        lowStock: lowStockCount,
      })
    }

    loadStats()
  }, [])

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-neutral-900">Tổng quan</h1>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card className="p-6">
          <p className="text-neutral-600 text-sm mb-2">Doanh thu</p>
          <p className="text-2xl font-bold text-neutral-900">{formatMoney(stats.revenue)}</p>
          <p className="text-green-600 text-sm mt-2">+12% so với tháng trước</p>
        </Card>

        <Card className="p-6">
          <p className="text-neutral-600 text-sm mb-2">Số đơn hàng</p>
          <p className="text-2xl font-bold text-neutral-900">{stats.orders}</p>
          <p className="text-green-600 text-sm mt-2">+5% so với tháng trước</p>
        </Card>

        <Card className="p-6">
          <p className="text-neutral-600 text-sm mb-2">Người dùng hoạt động</p>
          <p className="text-2xl font-bold text-neutral-900">{stats.activeUsers}</p>
          <p className="text-green-600 text-sm mt-2">+8% so với tháng trước</p>
        </Card>

        <Card className="p-6">
          <p className="text-neutral-600 text-sm mb-2">Sản phẩm sắp hết</p>
          <p className="text-2xl font-bold text-neutral-900">{stats.lowStock}</p>
          <p className="text-red-600 text-sm mt-2">Cần nhập kho</p>
        </Card>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="p-6">
          <h2 className="text-lg font-semibold mb-4">Doanh thu theo tháng</h2>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={revenueData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip formatter={(value) => formatMoney(value)} />
              <Bar dataKey="revenue" fill="#3B82F6" />
            </BarChart>
          </ResponsiveContainer>
        </Card>

        <Card className="p-6">
          <h2 className="text-lg font-semibold mb-4">Đơn hàng theo trạng thái</h2>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={orderStatusData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, value }) => `${name}: ${value}`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {orderStatusData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </Card>
      </div>

      {/* Top Products */}
      <Card className="p-6">
        <h2 className="text-lg font-semibold mb-4">Top sản phẩm bán chạy</h2>
        <div className="space-y-3">
          {[
            { name: "Áo thun cổ tròn", sales: 245, revenue: 48755000 },
            { name: "Quần jean slim", sales: 189, revenue: 37611000 },
            { name: "Áo sơ mi trắng", sales: 156, revenue: 31200000 },
            { name: "Áo khoác denim", sales: 123, revenue: 30750000 },
            { name: "Quần short", sales: 98, revenue: 14700000 },
          ].map((product, idx) => (
            <div key={idx} className="flex items-center justify-between p-3 border border-neutral-200 rounded-lg">
              <div>
                <p className="font-medium">{product.name}</p>
                <p className="text-sm text-neutral-600">{product.sales} bán</p>
              </div>
              <p className="font-semibold">{formatMoney(product.revenue)}</p>
            </div>
          ))}
        </div>
      </Card>
    </div>
  )
}
