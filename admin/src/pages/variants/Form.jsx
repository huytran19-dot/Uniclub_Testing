"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import Card from "../../components/Card"
import FormField from "../../components/FormField"
import Toast from "../../components/Toast"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"

export default function VariantForm() {
  const navigate = useNavigate()
  const { sku } = useParams()
  const [form, setForm] = useState({
    id_product: "",
    id_color: "",
    id_size: "",
    price: "",
    images: "",
    status: 1,
  })
  const [products, setProducts] = useState([])
  const [colors, setColors] = useState([])
  const [sizes, setSizes] = useState([])
  const [errors, setErrors] = useState({})
  const [toast, setToast] = useState(null)

  useEffect(() => {
    loadData()
  }, [])

  useEffect(() => {
    if (sku) loadVariant()
  }, [sku])

  const loadData = async () => {
    const [prods, cols, szs] = await Promise.all([api.list("products"), api.list("colors"), api.list("sizes")])
    setProducts(prods)
    setColors(cols)
    setSizes(szs)
  }

  const loadVariant = async () => {
    const data = await api.get("variants", sku)
    if (data) setForm(data)
  }

  const validate = () => {
    const newErrors = {}
    if (!form.id_product) newErrors.id_product = "Sản phẩm là bắt buộc"
    if (!form.id_color) newErrors.id_color = "Màu sắc là bắt buộc"
    if (!form.id_size) newErrors.id_size = "Kích cỡ là bắt buộc"
    if (!form.price || form.price <= 0) newErrors.price = "Giá phải lớn hơn 0"
    return newErrors
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const newErrors = validate()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    if (sku) {
      await api.update("variants", sku, form)
      setToast({ message: "Cập nhật biến thể thành công", type: "success" })
    } else {
      await api.create("variants", form)
      setToast({ message: "Tạo biến thể thành công", type: "success" })
    }

    navigate("/variants")
  }

  return (
    <div>
      <Breadcrumb
        items={[
          { label: "Sản phẩm", path: "/products" },
          { label: "Biến thể", path: "/variants" },
          { label: sku ? "Sửa" : "Thêm mới" },
        ]}
      />
      <h1 className="text-3xl font-bold mb-6">{sku ? "Sửa biến thể" : "Thêm biến thể mới"}</h1>

      <Card className="max-w-2xl">
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <FormField
            label="Sản phẩm"
            type="select"
            value={form.id_product}
            onChange={(e) => setForm({ ...form, id_product: Number.parseInt(e.target.value) })}
            options={products}
            error={errors.id_product}
            required
          />

          <FormField
            label="Màu sắc"
            type="select"
            value={form.id_color}
            onChange={(e) => setForm({ ...form, id_color: Number.parseInt(e.target.value) })}
            options={colors}
            error={errors.id_color}
            required
          />

          <FormField
            label="Kích cỡ"
            type="select"
            value={form.id_size}
            onChange={(e) => setForm({ ...form, id_size: Number.parseInt(e.target.value) })}
            options={sizes}
            error={errors.id_size}
            required
          />

          <FormField
            label="Giá"
            type="number"
            value={form.price}
            onChange={(e) => setForm({ ...form, price: Number.parseInt(e.target.value) })}
            error={errors.price}
            required
          />

          <FormField
            label="Hình ảnh (URL)"
            value={form.images}
            onChange={(e) => setForm({ ...form, images: e.target.value })}
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

          <div className="p-3 bg-blue-50 border border-blue-200 rounded-lg text-sm text-blue-800">
            ℹ️ Tồn kho chỉ đọc. Vui lòng nhập kho qua Phiếu nhập (GRN).
          </div>

          <div className="flex gap-3 pt-4">
            <button
              type="button"
              onClick={() => navigate("/variants")}
              className="flex-1 px-4 py-2 border border-neutral-200 rounded-lg hover:bg-neutral-50"
            >
              Hủy
            </button>
            <button type="submit" className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              {sku ? "Cập nhật" : "Tạo mới"}
            </button>
          </div>
        </form>
      </Card>

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
