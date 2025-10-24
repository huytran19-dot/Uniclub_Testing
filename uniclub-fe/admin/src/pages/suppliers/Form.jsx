"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import FormField from "../../components/FormField"
import Toast from "../../components/Toast"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"

export default function SupplierForm() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [form, setForm] = useState({
    name: "",
    contactPerson: "",
    phone: "",
    email: "",
    address: "",
    status: 1,
  })
  const [errors, setErrors] = useState({})
  const [toast, setToast] = useState(null)

  useEffect(() => {
    if (id) loadSupplier()
  }, [id])

  const loadSupplier = async () => {
    const data = await api.get("suppliers", id)
    if (data) setForm(data)
  }

  const validate = () => {
    const newErrors = {}
    if (!form.name.trim()) newErrors.name = "Tên nhà cung cấp là bắt buộc"
    return newErrors
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const newErrors = validate()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    try {
      if (id) {
        await api.update("suppliers", id, form)
        setToast({ message: "Cập nhật nhà cung cấp thành công", type: "success" })
      } else {
        await api.create("suppliers", form)
        setToast({ message: "Tạo nhà cung cấp thành công", type: "success" })
      }

      // Delay navigation to show toast
      setTimeout(() => {
        navigate("/suppliers")
      }, 1500)
    } catch (error) {
      console.error("Error creating/updating supplier:", error)
      setToast({ 
        message: error.message || "Có lỗi xảy ra khi tạo/cập nhật nhà cung cấp", 
        type: "error" 
      })
    }
  }

  return (
    <div>
      <Breadcrumb items={[{ label: "Nhà cung cấp", path: "/suppliers" }, { label: id ? "Sửa" : "Thêm mới" }]} />
      <h1 className="text-3xl font-bold mb-6">{id ? "Sửa nhà cung cấp" : "Thêm nhà cung cấp mới"}</h1>

      <Card className="max-w-2xl">
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <FormField
            label="Tên nhà cung cấp"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            error={errors.name}
            required
          />

          <FormField
            label="Người liên hệ"
            value={form.contactPerson}
            onChange={(e) => setForm({ ...form, contactPerson: e.target.value })}
          />

          <FormField
            label="Số điện thoại"
            value={form.phone}
            onChange={(e) => setForm({ ...form, phone: e.target.value })}
          />

          <FormField
            label="Email"
            type="email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
          />

          <FormField
            label="Địa chỉ"
            type="textarea"
            value={form.address}
            onChange={(e) => setForm({ ...form, address: e.target.value })}
            rows={3}
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
              onClick={() => navigate("/suppliers")}
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
