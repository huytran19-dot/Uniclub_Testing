"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import Badge from "../../components/Badge"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { formatMoney, getStatusLabel, getStatusType } from "../../lib/utils"

export default function OrderDetail() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [order, setOrder] = useState(null)
  const [items, setItems] = useState([])

  useEffect(() => {
    loadOrder()
  }, [id])

  const loadOrder = async () => {
    const data = await api.get("orders", id)
    if (data) {
      setOrder(data)
      // Mock items - in real app would fetch from order_variant table
      setItems([
        {
          sku: 1001,
          product_name: "Áo thun cổ tròn",
          color: "Đen",
          size: "M",
          price: 199000,
          quantity: 2,
        },
      ])
    }
  }

  if (!order) return <div>Đang tải...</div>

  return (
    <div>
      <Breadcrumb items={[{ label: "Đơn hàng", path: "/orders" }, { label: `Chi tiết #${id}` }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Chi tiết đơn hàng #{id}</h1>
        <button
          onClick={() => navigate("/orders")}
          className="px-4 py-2 border border-neutral-200 rounded-lg hover:bg-neutral-50"
        >
          Quay lại
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <Card className="p-6">
            <h2 className="text-lg font-semibold mb-4">Thông tin đơn hàng</h2>
            <div className="space-y-3">
              <div className="flex justify-between">
                <span className="text-neutral-600">Mã đơn:</span>
                <span className="font-medium">#{order.id}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-neutral-600">Khách hàng:</span>
                <span className="font-medium">ID: {order.id_user}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-neutral-600">Trạng thái:</span>
                <Badge status={getStatusType(order.status)} label={getStatusLabel(order.status)} />
              </div>
              <div className="flex justify-between">
                <span className="text-neutral-600">Ghi chú:</span>
                <span className="font-medium">{order.note || "-"}</span>
              </div>
            </div>
          </Card>

          <Card className="p-6">
            <h2 className="text-lg font-semibold mb-4">Chi tiết sản phẩm</h2>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-neutral-200">
                    <th className="px-4 py-2 text-left">SKU</th>
                    <th className="px-4 py-2 text-left">Sản phẩm</th>
                    <th className="px-4 py-2 text-left">Màu</th>
                    <th className="px-4 py-2 text-left">Size</th>
                    <th className="px-4 py-2 text-right">Giá</th>
                    <th className="px-4 py-2 text-right">SL</th>
                    <th className="px-4 py-2 text-right">Thành tiền</th>
                  </tr>
                </thead>
                <tbody>
                  {items.map((item, idx) => (
                    <tr key={idx} className="border-b border-neutral-200">
                      <td className="px-4 py-2">{item.sku}</td>
                      <td className="px-4 py-2">{item.product_name}</td>
                      <td className="px-4 py-2">{item.color}</td>
                      <td className="px-4 py-2">{item.size}</td>
                      <td className="px-4 py-2 text-right">{formatMoney(item.price)}</td>
                      <td className="px-4 py-2 text-right">{item.quantity}</td>
                      <td className="px-4 py-2 text-right font-medium">{formatMoney(item.price * item.quantity)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </Card>
        </div>

        <div>
          <Card className="p-6">
            <h2 className="text-lg font-semibold mb-4">Tóm tắt</h2>
            <div className="space-y-3">
              <div className="flex justify-between">
                <span className="text-neutral-600">Tổng cộng:</span>
                <span className="font-bold text-lg">{formatMoney(order.total)}</span>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </div>
  )
}
