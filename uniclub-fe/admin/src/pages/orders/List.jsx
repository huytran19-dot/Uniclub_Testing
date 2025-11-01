"use client"

import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import Card from "../../components/Card"
import Table from "../../components/Table"
import Badge from "../../components/Badge"
import Breadcrumb from "../../components/Breadcrumb"
import Toast from "../../components/Toast"
import { api } from "../../lib/api"
import { formatDateTime, formatMoney, getStatusLabel, getStatusType } from "../../lib/utils"

export default function OrderList() {
  const navigate = useNavigate()
  const [orders, setOrders] = useState([])
  const [toast, setToast] = useState(null)

  useEffect(() => {
    loadOrders()
  }, [])

  const loadOrders = async () => {
    try {
      const data = await api.list("orders")
      setOrders(data || [])
    } catch (error) {
      console.error("Error loading orders:", error)
      setToast({ message: "Không thể tải danh sách đơn hàng", type: "error" })
    }
  }

  const columns = [
    { key: "id", label: "Mã đơn" },
    {
      key: "user", // Thay key thành "user" cho hợp lý
      label: "Khách hàng",
      render: (row) => row.user?.fullname || row.user?.email || "N/A", // Lấy fullName hoặc email từ object user
    },
    {
      key: "total",
      label: "Tổng tiền",
      render: (row) => formatMoney(row.total),
    },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => (
        <div className="flex items-center gap-2">
          <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />
        </div>
      ),
    },
    { key: "createdAt", label: "Ngày tạo", render: (row) => formatDateTime(row.createdAt) },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Đơn hàng" }]} />
      <h1 className="text-3xl font-bold mb-6">Đơn hàng</h1>

      <Card>
        <div className="p-6">
          <Table columns={columns} data={orders} onView={(row) => navigate(`/orders/${row.id}`)} />
        </div>
      </Card>

      {toast && (
        <Toast
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}
    </div>
  )
}
