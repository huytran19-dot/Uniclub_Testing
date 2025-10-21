"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import Badge from "../../components/Badge"
import Toast from "../../components/Toast"
import Confirm from "../../components/Confirm"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { formatDate, formatMoney, getStatusLabel, getStatusType } from "../../lib/utils"

export default function GrnDetail() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [grn, setGrn] = useState(null)
  const [details, setDetails] = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [variants, setVariants] = useState([])
  const [toast, setToast] = useState(null)
  const [confirmApprove, setConfirmApprove] = useState(false)

  useEffect(() => {
    loadData()
  }, [id])

  const loadData = async () => {
    const [grnData, suppData, varData, detailData] = await Promise.all([
      api.get("grn", id),
      api.list("suppliers"),
      api.list("variants"),
      api.getGrnDetails(id),
    ])
    setGrn(grnData)
    setSuppliers(suppData)
    setVariants(varData)
    setDetails(detailData)
  }

  const handleApprove = async () => {
    await api.approveGrn(id)
    setToast({ message: "Đã duyệt phiếu nhập và cộng tồn kho", type: "success" })
    setConfirmApprove(false)
    loadData()
  }

  const getSupplierName = (supplierId) => suppliers.find((s) => s.id === supplierId)?.name || "-"
  const getVariantInfo = (sku) => variants.find((v) => v.sku === sku) || {}

  if (!grn) return <div>Đang tải...</div>

  const isLocked = grn.status !== "PENDING"

  return (
    <div>
      <Breadcrumb items={[{ label: "Phiếu nhập", path: "/grn" }, { label: `Chi tiết #${id}` }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Chi tiết phiếu nhập #{id}</h1>
        <button
          onClick={() => navigate("/grn")}
          className="px-4 py-2 border border-neutral-200 rounded-lg hover:bg-neutral-50"
        >
          Quay lại
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <Card className="p-6">
            <h2 className="text-lg font-semibold mb-4">Thông tin phiếu</h2>
            <div className="space-y-3">
              <div className="flex justify-between">
                <span className="text-neutral-600">Mã phiếu:</span>
                <span className="font-medium">#{grn.id}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-neutral-600">Nhà cung cấp:</span>
                <span className="font-medium">{getSupplierName(grn.id_supplier)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-neutral-600">Ngày nhận:</span>
                <span className="font-medium">{formatDate(grn.received_date)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-neutral-600">Trạng thái:</span>
                <Badge status={getStatusType(grn.status)} label={getStatusLabel(grn.status)} />
              </div>
              <div className="flex justify-between">
                <span className="text-neutral-600">Ghi chú:</span>
                <span className="font-medium">{grn.note || "-"}</span>
              </div>
            </div>
          </Card>

          <Card className="p-6">
            <h2 className="text-lg font-semibold mb-4">Chi tiết hàng nhập</h2>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-neutral-200 bg-neutral-50">
                    <th className="px-4 py-2 text-left">SKU</th>
                    <th className="px-4 py-2 text-left">Sản phẩm</th>
                    <th className="px-4 py-2 text-left">Màu</th>
                    <th className="px-4 py-2 text-left">Size</th>
                    <th className="px-4 py-2 text-right">Số lượng</th>
                    <th className="px-4 py-2 text-right">Giá nhập</th>
                    <th className="px-4 py-2 text-right">Thành tiền</th>
                  </tr>
                </thead>
                <tbody>
                  {details.map((detail, idx) => {
                    const variant = getVariantInfo(detail.id_variant)
                    return (
                      <tr key={idx} className="border-b border-neutral-200">
                        <td className="px-4 py-2">{detail.id_variant}</td>
                        <td className="px-4 py-2">-</td>
                        <td className="px-4 py-2">-</td>
                        <td className="px-4 py-2">-</td>
                        <td className="px-4 py-2 text-right">{detail.quantity}</td>
                        <td className="px-4 py-2 text-right">{formatMoney(detail.unit_cost)}</td>
                        <td className="px-4 py-2 text-right font-medium">{formatMoney(detail.subtotal)}</td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>
          </Card>
        </div>

        <div>
          <Card className="p-6">
            <h2 className="text-lg font-semibold mb-4">Tóm tắt</h2>
            <div className="space-y-4">
              <div className="flex justify-between">
                <span className="text-neutral-600">Tổng tiền:</span>
                <span className="font-bold text-lg">{formatMoney(grn.total_cost)}</span>
              </div>

              {grn.status === "PENDING" && (
                <button
                  onClick={() => setConfirmApprove(true)}
                  className="w-full px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
                >
                  Duyệt phiếu nhập
                </button>
              )}

              {isLocked && (
                <div className="p-3 bg-blue-50 border border-blue-200 rounded-lg text-sm text-blue-800">
                  ℹ️ Phiếu này đã {grn.status === "COMPLETED" ? "hoàn thành" : "bị hủy"}, không thể chỉnh sửa.
                </div>
              )}
            </div>
          </Card>
        </div>
      </div>

      {confirmApprove && (
        <Confirm
          isOpen={true}
          title="Duyệt phiếu nhập"
          message="Bạn có chắc muốn duyệt phiếu nhập này? Tồn kho sẽ được cộng tự động."
          onConfirm={handleApprove}
          onCancel={() => setConfirmApprove(false)}
        />
      )}

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
