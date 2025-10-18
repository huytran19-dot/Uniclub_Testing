"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import FormField from "../../components/FormField"
import Toast from "../../components/Toast"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"

export default function ProductForm() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [form, setForm] = useState({
    name: "",
    description: "",
    information: "",
    id_brand: "",
    id_category: "",
    status: 1,
  })
  const [categories, setCategories] = useState([])
  const [brands, setBrands] = useState([])
  const [errors, setErrors] = useState({})
  const [toast, setToast] = useState(null)

  useEffect(() => {
    loadData()
  }, [])

  useEffect(() => {
    if (id) loadProduct()
  }, [id])

  const loadData = async () => {
    const [cats, brds] = await Promise.all([api.list("categories"), api.list("brands")])
    setCategories(cats)
    setBrands(brds)
  }

  const loadProduct = async () => {
    const data = await api.get("products", id)
    if (data) setForm(data)
  }

  const validate = () => {
    const newErrors = {}
    if (!form.name.trim()) newErrors.name = "Tên sản phẩm là bắt buộc"
    if (!form.id_brand) newErrors.id_brand = "Nhãn hàng là bắt buộc"
    if (!form.id_category) newErrors.id_category = "Danh mục là bắt buộc"
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
      await api.update("products", id, form)
      setToast({ message: "Cập nhật sản phẩm thành công", type: "success" })
    } else {
      await api.create("products", form)
      setToast({ message: "Tạo sản phẩm thành công", type: "success" })
    }

    navigate("/products")
  }

  return (
    <div>
      <Breadcrumb items={[{ label: "Sản phẩm", path: "/products" }, { label: id ? "Sửa" : "Thêm mới" }]} />
      <h1 className="text-3xl font-bold mb-6">{id ? "Sửa sản phẩm" : "Thêm sản phẩm mới"}</h1>

      <Card className="max-w-2xl">
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <FormField
            label="Tên sản phẩm"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            error={errors.name}
            required
          />

          <FormField
            label="Mô tả"
            type="textarea"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
          />

          <FormField
            label="Thông tin chi tiết"
            type="textarea"
            value={form.information}
            onChange={(e) => setForm({ ...form, information: e.target.value })}
          />

          <FormField
            label="Nhãn hàng"
            type="select"
            value={form.id_brand}
            onChange={(e) => setForm({ ...form, id_brand: Number.parseInt(e.target.value) })}
            options={brands}
            error={errors.id_brand}
            required
          />

          <FormField
            label="Danh mục"
            type="select"
            value={form.id_category}
            onChange={(e) => setForm({ ...form, id_category: Number.parseInt(e.target.value) })}
            options={categories}
            error={errors.id_category}
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
              onClick={() => navigate("/products")}
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
