"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import FormField from "../../components/FormField"
import Toast from "../../components/Toast"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"

export default function CategoryForm() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [form, setForm] = useState({ name: "", status: 1 })
  const [errors, setErrors] = useState({})
  const [toast, setToast] = useState(null)

  useEffect(() => {
    if (id) loadCategory()
  }, [id])

  const loadCategory = async () => {
    const data = await api.get("categories", id)
    if (data) setForm(data)
  }

  const validate = () => {
    const newErrors = {}
    if (!form.name.trim()) newErrors.name = "Tên danh mục là bắt buộc"
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
        await api.update("categories", id, form)
        setToast({ message: "Cập nhật danh mục thành công", type: "success" })
      } else {
        await api.create("categories", form)
        setToast({ message: "Tạo danh mục thành công", type: "success" })
      }

      // Delay navigation to show toast
      setTimeout(() => {
        navigate("/categories")
      }, 1500)
    } catch (error) {
      console.error("Error creating/updating category:", error)
      setToast({ 
        message: error.message || "Có lỗi xảy ra khi tạo/cập nhật danh mục", 
        type: "error" 
      })
    }
  }

  return (
    <div>
      <Breadcrumb
        items={[
          { label: "Sản phẩm", path: "/products" },
          { label: "Danh mục", path: "/categories" },
          { label: id ? "Sửa" : "Thêm mới" },
        ]}
      />
      <h1 className="text-3xl font-bold mb-6">{id ? "Sửa danh mục" : "Thêm danh mục mới"}</h1>

      <Card className="max-w-md">
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <FormField
            label="Tên danh mục"
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
              onClick={() => navigate("/categories")}
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
