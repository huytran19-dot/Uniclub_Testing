"use client"

import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import Card from "../../components/Card"
import FormField from "../../components/FormField"
import Toast from "../../components/Toast"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { formatMoney } from "../../lib/utils"

export default function GrnNew() {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    id_supplier: "",
    received_date: "",
    note: "",
  })
  const [details, setDetails] = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [variants, setVariants] = useState([])
  const [searchVariant, setSearchVariant] = useState("")
  const [errors, setErrors] = useState({})
  const [toast, setToast] = useState(null)

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    const [suppData, varData] = await Promise.all([api.list("suppliers"), api.list("variants")])
    setSuppliers(suppData)
    setVariants(varData)
  }

  const handleAddRow = () => {
    setDetails([...details, { id_variant: "", quantity: "", unit_cost: "", subtotal: 0 }])
  }

  const handleRemoveRow = (idx) => {
    setDetails(details.filter((_, i) => i !== idx))
  }

  const handleDetailChange = (idx, field, value) => {
    const newDetails = [...details]
    newDetails[idx][field] = value

    if (field === "quantity" || field === "unit_cost") {
      const qty = Number.parseInt(newDetails[idx].quantity) || 0
      const cost = Number.parseInt(newDetails[idx].unit_cost) || 0
      newDetails[idx].subtotal = qty * cost
    }

    setDetails(newDetails)
  }

  const totalCost = details.reduce((sum, d) => sum + (d.subtotal || 0), 0)

  const validate = () => {
    const newErrors = {}
    if (!form.id_supplier) newErrors.id_supplier = "Nhà cung cấp là bắt buộc"
    if (!form.received_date) newErrors.received_date = "Ngày nhận là bắt buộc"
    if (details.length === 0) newErrors.details = "Phải có ít nhất 1 dòng chi tiết"
    return newErrors
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const newErrors = validate()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    const payload = {
      ...form,
      id_supplier: Number.parseInt(form.id_supplier),
      total_cost: totalCost,
      status: "PENDING",
      details,
    }

    await api.create("grn", payload)
    toast.success("Tạo phiếu nhập kho thành công")
    navigate("/grn")
  }

  return (
    <div>
      <Breadcrumb items={[{ label: "Phiếu nhập", path: "/grn" }, { label: "Tạo phiếu mới" }]} />
      <h1 className="text-3xl font-bold mb-6">Tạo phiếu nhập mới</h1>

      <form onSubmit={handleSubmit} className="space-y-6">
        <Card className="p-6">
          <h2 className="text-lg font-semibold mb-4">Thông tin phiếu</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <FormField
              label="Nhà cung cấp"
              type="select"
              value={form.id_supplier}
              onChange={(e) => setForm({ ...form, id_supplier: e.target.value })}
              options={suppliers}
              error={errors.id_supplier}
              required
            />

            <FormField
              label="Ngày nhận"
              type="date"
              value={form.received_date}
              onChange={(e) => setForm({ ...form, received_date: e.target.value })}
              error={errors.received_date}
              required
            />
          </div>

          <FormField
            label="Ghi chú"
            type="textarea"
            value={form.note}
            onChange={(e) => setForm({ ...form, note: e.target.value })}
            rows={3}
          />
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold">Chi tiết phiếu</h2>
            <button
              type="button"
              onClick={handleAddRow}
              className="px-3 py-1 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700"
            >
              + Thêm dòng
            </button>
          </div>

          {errors.details && <p className="text-red-500 text-sm mb-4">{errors.details}</p>}

          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-neutral-200 bg-neutral-50">
                  <th className="px-4 py-2 text-left">SKU / Sản phẩm</th>
                  <th className="px-4 py-2 text-right">Số lượng</th>
                  <th className="px-4 py-2 text-right">Giá nhập</th>
                  <th className="px-4 py-2 text-right">Thành tiền</th>
                  <th className="px-4 py-2 text-center">Thao tác</th>
                </tr>
              </thead>
              <tbody>
                {details.map((detail, idx) => (
                  <tr key={idx} className="border-b border-neutral-200">
                    <td className="px-4 py-2">
                      <select
                        value={detail.id_variant}
                        onChange={(e) => handleDetailChange(idx, "id_variant", e.target.value)}
                        className="w-full px-2 py-1 border border-neutral-200 rounded text-sm"
                      >
                        <option value="">Chọn SKU</option>
                        {variants.map((v) => (
                          <option key={v.sku} value={v.sku}>
                            {v.sku}
                          </option>
                        ))}
                      </select>
                    </td>
                    <td className="px-4 py-2">
                      <input
                        type="number"
                        value={detail.quantity}
                        onChange={(e) => handleDetailChange(idx, "quantity", e.target.value)}
                        className="w-full px-2 py-1 border border-neutral-200 rounded text-sm text-right"
                        placeholder="0"
                      />
                    </td>
                    <td className="px-4 py-2">
                      <input
                        type="number"
                        value={detail.unit_cost}
                        onChange={(e) => handleDetailChange(idx, "unit_cost", e.target.value)}
                        className="w-full px-2 py-1 border border-neutral-200 rounded text-sm text-right"
                        placeholder="0"
                      />
                    </td>
                    <td className="px-4 py-2 text-right font-medium">{formatMoney(detail.subtotal || 0)}</td>
                    <td className="px-4 py-2 text-center">
                      <button
                        type="button"
                        onClick={() => handleRemoveRow(idx)}
                        className="text-red-600 hover:underline text-sm"
                      >
                        Xóa
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="mt-4 flex justify-end">
            <div className="text-right">
              <p className="text-neutral-600 mb-2">Tổng tiền:</p>
              <p className="text-2xl font-bold text-blue-600">{formatMoney(totalCost)}</p>
            </div>
          </div>
        </Card>

        <div className="flex gap-3">
          <button
            type="button"
            onClick={() => navigate("/grn")}
            className="flex-1 px-4 py-2 border border-neutral-200 rounded-lg hover:bg-neutral-50"
          >
            Hủy
          </button>
          <button type="submit" className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            Lưu nháp
          </button>
        </div>
      </form>

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
