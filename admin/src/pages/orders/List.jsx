"use client"

import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import Card from "../../components/Card"
import Table from "../../components/Table"
import Badge from "../../components/Badge"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { formatDate, formatMoney, getStatusLabel, getStatusType } from "../../lib/utils"

export default function OrderList() {
  const navigate = useNavigate()
  const [orders, setOrders] = useState([])

  useEffect(() => {
    loadOrders()
  }, [])

  const loadOrders = async () => {
    const data = await api.list("orders")
    setOrders(data || [])
  }

  const columns = [
    { key: "id", label: "Mã đơn" },
    { key: "id_user", label: "Khách hàng" },
    {
      key: "total",
      label: "Tổng tiền",
      render: (row) => formatMoney(row.total),
    },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
    { key: "created_at", label: "Ngày tạo", render: (row) => formatDate(row.created_at) },
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
    </div>
  )
}
