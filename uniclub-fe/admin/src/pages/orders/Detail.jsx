"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import Breadcrumb from "../../components/Breadcrumb"
import Toast from "../../components/Toast"
import { api } from "../../lib/api"
import { formatMoney, getStatusLabel, getStatusType } from "../../lib/utils"

export default function OrderDetail() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [order, setOrder] = useState(null)
  const [items, setItems] = useState([])
  const [currentStatus, setCurrentStatus] = useState("")
  const [originalStatus, setOriginalStatus] = useState("")
  const [isSaving, setIsSaving] = useState(false)
  const [toast, setToast] = useState(null)

  useEffect(() => {
    loadOrder()
  }, [id])

  const loadOrder = async () => {
    try {
      const data = await api.get(`orders`, id)
      if (data) {
        setOrder(data)
        setOriginalStatus(data.status) // Lưu trạng thái ban đầu
        setItems(data.orderVariants) // Lấy danh sách items từ API
      }
    } catch (error) {
      console.error("Error loading order:", error)
    }
  }

  const handleStatusChange = (e) => {
    setCurrentStatus(e.target.value)
  }

  const handleSaveStatus = async () => {
    if (currentStatus === originalStatus) {
      setToast({ message: "Trạng thái không thay đổi", type: "warning" })
      return
    }

    // Check if order is already completed or cancelled
    if (originalStatus === "DELIVERED" || originalStatus === "CANCELLED") {
      setToast({ message: "Không thể cập nhật trạng thái đơn hàng đã hoàn thành hoặc bị hủy", type: "error" })
      return
    }

    setIsSaving(true)
    try {
      await api.updateOrderStatus(id, { status: currentStatus })
      setOriginalStatus(currentStatus)
      setOrder({ ...order, status: currentStatus })
      setToast({ message: "Cập nhật trạng thái thành công", type: "success" })
    } catch (error) {
      console.error("Error updating order status:", error)
      setToast({ 
        message: error.message || "Có lỗi xảy ra khi cập nhật trạng thái", 
        type: "error" 
      })
      // Revert status on error
      setCurrentStatus(originalStatus)
    } finally {
      setIsSaving(false)
    }
  }

  const isStatusChanged = currentStatus !== originalStatus

  const statusOptions = [
    { value: "PENDING", label: "Chờ xử lý" },
    { value: "CONFIRMED", label: "Đã xác nhận" },
    { value: "SHIPPING", label: "Đang giao" },
    { value: "DELIVERED", label: "Đã giao" },
    { value: "CANCELLED", label: "Đã hủy" },
  ]

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
                <span className="font-medium">ID: {order.user.id}</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-neutral-600">Trạng thái:</span>
                <div className="flex items-center gap-2">
                  <select
                    value={currentStatus || originalStatus}
                    onChange={handleStatusChange}
                    disabled={originalStatus === "DELIVERED" || originalStatus === "CANCELLED"}
                    className={`px-3 py-1.5 border border-neutral-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-neutral-900 ${
                      originalStatus === "DELIVERED" || originalStatus === "CANCELLED" 
                        ? "bg-gray-100 cursor-not-allowed" 
                        : ""
                    }`}
                  >
                    {statusOptions.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </select>
                  {originalStatus !== "DELIVERED" && originalStatus !== "CANCELLED" && (
                    <button
                      onClick={handleSaveStatus}
                      disabled={!isStatusChanged || isSaving}
                      className="px-3 py-1.5 text-sm bg-neutral-900 text-white rounded-lg hover:bg-neutral-800 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-neutral-900"
                    >
                      {isSaving ? "Đang lưu..." : "Lưu"}
                    </button>
                  )}
                </div>
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
                      <td className="px-4 py-2">{item.variantSku}</td>
                      <td className="px-4 py-2">{item.productName}</td>
                      <td className="px-4 py-2">{item.colorName}</td>
                      <td className="px-4 py-2">{item.sizeName}</td>
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

      {toast && <Toast type={toast.type} message={toast.message} onClose={() => setToast(null)} />}
    </div>
  )
}
