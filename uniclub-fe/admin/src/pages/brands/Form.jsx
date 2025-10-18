"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import FormField from "../../components/FormField"
import Toast from "../../components/Toast"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"

export default function BrandForm() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [form, setForm] = useState({ name: "", status: 1 })
  const [errors, setErrors] = useState({})
  const [toast, setToast] = useState(null)

  useEffect(() => {
    if (id) loadBrand()
  }, [id])

  const loadBrand = async () => {
    const data = await api.get("brands", id)
    if (data) setForm(data)
  }

  const validate = () => {
    const newErrors = {}
    if (!form.name.trim()) newErrors.name = "Tên nhãn hàng là bắt buộc"
    return newErrors
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const newErrors = validate()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    if (id) {
      await api.update("brands", id, form)
      setToast({ message: "Cập nhật nhãn hàng thành công", type: "success" })
    } else {
      await api.create("brands", form)
      setToast({ message: "Tạo thương hiệu thành công", type: "success" })
    }

    navigate("/brands")
  }

  return (
    <div>
      <Breadcrumb
        items={[
          { label: "Sản phẩm", path: "/products" },
          { label: "Nhãn hàng", path: "/brands" },
          { label: id ? "Sửa" : "Thêm mới" },
        ]}
      />
      <h1 className="text-3xl font-bold mb-6">{id ? "Sửa nhãn hàng" : "Thêm nhãn hàng mới"}</h1>

      <Card className="max-w-md">
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <FormField
            label="Tên nhãn hàng"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            error={errors.name}
            required
          />

          <div className="mb-4">
            <label className="block text-sm font-medium text-neutral-700 mb-2">Trạng thái</label>
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                checked={form.status === 1}
                onChange={(e) => setForm({ ...form, status: e.target.checked ? 1 : 0 })}
                className="w-4 h-4"
              />
              <span className="text-sm">Hoạt động</span>
            </label>
          </div>

          <div className="flex gap-3 pt-4">
            <button
              type="button"
              onClick={() => navigate("/brands")}
              className="flex-1 px-4 py-2 border border-neutral-200 rounded-lg hover:bg-neutral-50"
            >
              Hủy
            </button>
            <button type="submit" className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              {id ? "Cập nhật" : "Tạo mới"}
            </button>
          </div>
        </form>
      </Card>

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
