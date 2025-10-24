"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import FormField from "../../components/FormField"
import Toast from "../../components/Toast"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"

export default function ColorForm() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [form, setForm] = useState({ name: "", hexCode: "#000000", status: 1 })
  const [errors, setErrors] = useState({})
  const [toast, setToast] = useState(null)

  useEffect(() => {
    if (id) loadColor()
  }, [id])

  const loadColor = async () => {
    const data = await api.get("colors", id)
    if (data) setForm(data)
  }

  const validate = () => {
    const newErrors = {}
    if (!form.name.trim()) newErrors.name = "Tên màu là bắt buộc"
    if (!form.hexCode.match(/^#[0-9A-F]{6}$/i)) newErrors.hexCode = "Mã màu không hợp lệ"
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
      console.log("Sending form data:", form) // Debug log
      
      if (id) {
        await api.update("colors", id, form)
        setToast({ message: "Cập nhật màu sắc thành công", type: "success" })
      } else {
        await api.create("colors", form)
        setToast({ message: "Tạo màu sắc thành công", type: "success" })
      }

      // Delay navigation to show toast
      setTimeout(() => {
        navigate("/colors")
      }, 1500)
    } catch (error) {
      console.error("Error creating/updating color:", error)
      setToast({ 
        message: error.message || "Có lỗi xảy ra khi tạo/cập nhật màu sắc", 
        type: "error" 
      })
    }
  }

  return (
    <div>
      <Breadcrumb
        items={[
          { label: "Sản phẩm", path: "/products" },
          { label: "Màu sắc", path: "/colors" },
          { label: id ? "Sửa" : "Thêm mới" },
        ]}
      />
      <h1 className="text-3xl font-bold mb-6">{id ? "Sửa màu sắc" : "Thêm màu sắc mới"}</h1>

      <Card className="max-w-md">
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <FormField
            label="Tên màu"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            error={errors.name}
            required
          />

          <div className="mb-4">
            <label className="block text-sm font-medium text-neutral-700 mb-2">
              Mã màu <span className="text-red-500">*</span>
            </label>
            <div className="flex gap-2">
              <input
                type="color"
                value={form.hexCode}
                onChange={(e) => setForm({ ...form, hexCode: e.target.value })}
                className="w-12 h-10 border border-neutral-200 rounded-lg cursor-pointer"
              />
              <input
                type="text"
                value={form.hexCode}
                onChange={(e) => setForm({ ...form, hexCode: e.target.value })}
                placeholder="#000000"
                className={`flex-1 px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                  errors.hexCode ? "border-red-500" : "border-neutral-200"
                }`}
              />
            </div>
            {errors.hexCode && <p className="text-red-500 text-sm mt-1">{errors.hexCode}</p>}
          </div>

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
              onClick={() => navigate("/colors")}
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
