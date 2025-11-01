"use client"

import { useEffect, useState } from "react"
import Card from "../components/Card"
import { BarChart, Bar, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts"
import { api } from "../lib/api"
import { formatMoney } from "../lib/utils"

const COLORS = ["#FBBF24", "#3B82F6", "#10B981", "#EF4444"]

const STATUS_MAP = {
  PENDING: "Chờ xử lý",
  PAID: "Đã thanh toán",
  SHIPPED: "Đã giao",
  DELIVERED: "Đã giao",
  CANCELLED: "Đã hủy"
}

export default function Dashboard() {
  const [stats, setStats] = useState({
    revenue: 0,
    orders: 0,
    activeUsers: 0,
    lowStock: 0,
  })
  
  const [revenueData, setRevenueData] = useState([])
  const [orderStatusData, setOrderStatusData] = useState([])
  const [topProducts, setTopProducts] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const loadStats = async () => {
      try {
        setLoading(true)
        
        // Fetch data from API
        const [variants, orders, users] = await Promise.all([
          api.list("variants"),
          api.list("orders"),
          api.list("users")
        ])

        // Calculate basic stats
        const lowStockCount = variants.filter((v) => v.quantity < 5).length
        const totalRevenue = orders.reduce((sum, o) => sum + (o.total || 0), 0)

        setStats({
          revenue: totalRevenue,
          orders: orders.length,
          activeUsers: users.length,
          lowStock: lowStockCount,
        })

        // Calculate revenue by month from orders
        const monthlyRevenue = calculateMonthlyRevenue(orders)
        setRevenueData(monthlyRevenue)

        // Calculate order status distribution
        const statusDistribution = calculateOrderStatus(orders)
        setOrderStatusData(statusDistribution)

        // Calculate top products from order variants (nested in orders)
        const topSellingProducts = calculateTopProducts(orders, variants)
        setTopProducts(topSellingProducts)
        
      } catch (error) {
        console.error("Error loading dashboard data:", error)
      } finally {
        setLoading(false)
      }
    }

    loadStats()
  }, [])

  // Helper function to calculate monthly revenue
  const calculateMonthlyRevenue = (orders) => {
    const monthNames = ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", 
                        "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"]
    
    const revenueByMonth = {}
    
    orders.forEach(order => {
      if (order.createdAt && order.total) {
        const date = new Date(order.createdAt)
        const monthIndex = date.getMonth()
        const monthName = monthNames[monthIndex]
        
        if (!revenueByMonth[monthName]) {
          revenueByMonth[monthName] = 0
        }
        revenueByMonth[monthName] += order.total
      }
    })

    // Get last 6 months
    const currentMonth = new Date().getMonth()
    const last6Months = []
    for (let i = 5; i >= 0; i--) {
      const monthIndex = (currentMonth - i + 12) % 12
      const monthName = monthNames[monthIndex]
      last6Months.push({
        month: monthName,
        revenue: revenueByMonth[monthName] || 0
      })
    }
    
    return last6Months
  }

  // Helper function to calculate order status distribution
  const calculateOrderStatus = (orders) => {
    const statusCount = {}
    
    orders.forEach(order => {
      const status = order.status || "PENDING"
      const displayName = STATUS_MAP[status] || status
      
      if (!statusCount[displayName]) {
        statusCount[displayName] = 0
      }
      statusCount[displayName]++
    })

    return Object.entries(statusCount).map(([name, value]) => ({
      name,
      value
    }))
  }

  // Helper function to calculate top products
  const calculateTopProducts = (orders, variants) => {
    const productStats = {}

    // Extract order variants from orders
    orders.forEach(order => {
      if (order.orderVariants && Array.isArray(order.orderVariants)) {
        order.orderVariants.forEach(ov => {
          const variantSku = ov.variantSku || ov.variant?.sku
          const quantity = ov.quantity || 0
          const price = ov.price || 0

          if (!variantSku) return

          if (!productStats[variantSku]) {
            productStats[variantSku] = {
              sales: 0,
              revenue: 0,
              variantSku: variantSku
            }
          }

          productStats[variantSku].sales += quantity
          productStats[variantSku].revenue += quantity * price
        })
      }
    })

    // Get variant names and sort by revenue
    const productsArray = Object.values(productStats).map(stat => {
      const variant = variants.find(v => v.sku === stat.variantSku)
      return {
        name: variant?.product?.name || `Variant ${stat.variantSku}`,
        sales: stat.sales,
        revenue: stat.revenue
      }
    })

    // Sort by revenue and get top 5
    return productsArray
      .sort((a, b) => b.revenue - a.revenue)
      .slice(0, 5)
  }

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-neutral-900">Tổng quan</h1>

      {loading && (
        <div className="text-center py-8">
          <p className="text-neutral-600">Đang tải dữ liệu...</p>
        </div>
      )}

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card className="p-6">
          <p className="text-neutral-600 text-sm mb-2">Doanh thu</p>
          <p className="text-2xl font-bold text-neutral-900">{formatMoney(stats.revenue)}</p>
          <p className="text-neutral-500 text-sm mt-2">Tổng doanh thu</p>
        </Card>

        <Card className="p-6">
          <p className="text-neutral-600 text-sm mb-2">Số đơn hàng</p>
          <p className="text-2xl font-bold text-neutral-900">{stats.orders}</p>
          <p className="text-neutral-500 text-sm mt-2">Tổng đơn hàng</p>
        </Card>

        <Card className="p-6">
          <p className="text-neutral-600 text-sm mb-2">Người dùng</p>
          <p className="text-2xl font-bold text-neutral-900">{stats.activeUsers}</p>
          <p className="text-neutral-500 text-sm mt-2">Tổng người dùng</p>
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
          {revenueData.length > 0 ? (
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={revenueData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip formatter={(value) => formatMoney(value)} />
                <Bar dataKey="revenue" fill="#3B82F6" />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <div className="h-[300px] flex items-center justify-center text-neutral-500">
              Chưa có dữ liệu doanh thu
            </div>
          )}
        </Card>

        <Card className="p-6">
          <h2 className="text-lg font-semibold mb-4">Đơn hàng theo trạng thái</h2>
          {orderStatusData.length > 0 ? (
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
          ) : (
            <div className="h-[300px] flex items-center justify-center text-neutral-500">
              Chưa có dữ liệu đơn hàng
            </div>
          )}
        </Card>
      </div>

      {/* Top Products */}
      <Card className="p-6">
        <h2 className="text-lg font-semibold mb-4">Top sản phẩm bán chạy</h2>
        {topProducts.length > 0 ? (
          <div className="space-y-3">
            {topProducts.map((product, idx) => (
              <div key={idx} className="flex items-center justify-between p-3 border border-neutral-200 rounded-lg">
                <div>
                  <p className="font-medium">{product.name}</p>
                  <p className="text-sm text-neutral-600">{product.sales} đã bán</p>
                </div>
                <p className="font-semibold">{formatMoney(product.revenue)}</p>
              </div>
            ))}
          </div>
        ) : (
          <div className="py-8 text-center text-neutral-500">
            Chưa có dữ liệu sản phẩm bán chạy
          </div>
        )}
      </Card>
    </div>
  )
}
