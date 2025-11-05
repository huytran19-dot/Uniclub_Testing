"use client"

import { useEffect, useState, useRef } from "react"
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
    supplierId: "",
    receivedDate: "",
    note: "",
  })
  const [details, setDetails] = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [products, setProducts] = useState([])
  const [variants, setVariants] = useState([])
  const [errors, setErrors] = useState({})
  const [toast, setToast] = useState(null)
  const rowRefs = useRef([])

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    try {
      const [suppData, prodData, varData, colorsData, sizesData] = await Promise.all([
        api.list("suppliers"),
        api.list("products"),
        api.list("variants"),
        api.list("colors"),
        api.list("sizes"),
      ])
      
      setSuppliers(suppData || [])
      setProducts(prodData || [])
      
      // Enrich variants with color and size names
      if (varData && varData.length > 0) {
        const colors = colorsData || []
        const sizes = sizesData || []
        const products = prodData || []
        
        const enrichedVariants = varData.map((v) => {
          const product = products.find((p) => p.id === v.productId)
          const color = colors.find((c) => c.id === v.colorId)
          const size = sizes.find((s) => s.id === v.sizeId)
          
          return {
            id: v.sku,
            sku: v.sku,
            productId: v.productId,
            productName: product?.name || "Unknown",
            color: color?.name || "N/A",
            size: size?.name || "N/A",
            price: v.price,
          }
        })
        setVariants(enrichedVariants)
      } else {
        setVariants([])
      }
    } catch (error) {
      console.error("Error loading data:", error)
      setToast({ 
        message: "Không thể tải dữ liệu. Vui lòng thử lại sau.", 
        type: "error" 
      })
      setSuppliers([])
      setProducts([])
      setVariants([])
    }
  }


  const handleAddRow = () => {
    setDetails([
      ...details,
      {
        productId: "",
        variantSku: "",
        sku: "",
        color: "",
        size: "",
        quantity: "",
        unitCost: "",
        subtotal: 0,
      },
    ])
    // Focus on the new row's product select
    setTimeout(() => {
      const newIndex = details.length
      rowRefs.current[newIndex]?.focus()
    }, 100)
  }

  const handleRemoveRow = (idx) => {
    setDetails(details.filter((_, i) => i !== idx))
    rowRefs.current = rowRefs.current.filter((_, i) => i !== idx)
  }

  const handleProductChange = (idx, productId) => {
    const newDetails = [...details]
    newDetails[idx] = {
      ...newDetails[idx],
      productId: productId,
      variantSku: "",
      sku: "",
      color: "",
      size: "",
      unitCost: "",
    }
    setDetails(newDetails)
  }

  const handleVariantChange = (idx, variantId) => {
    const variant = variants.find((v) => v.id === parseInt(variantId))
    
    if (!variant) {
      return
    }

    // Check for duplicate SKU
    const isDuplicate = details.some((d, i) => i !== idx && d.sku === variant.sku)
    if (isDuplicate) {
      setToast({ type: "error", message: `Biến thể ${variant.sku} đã tồn tại trong danh sách` })
      // Focus the existing row
      const existingIdx = details.findIndex((d, i) => i !== idx && d.sku === variant.sku)
      if (existingIdx >= 0) {
        rowRefs.current[existingIdx]?.scrollIntoView({ behavior: "smooth", block: "center" })
      }
      return
    }

    const newDetails = [...details]
    const unitCost = variant.price || ""
    
    newDetails[idx] = {
      ...newDetails[idx],
      variantSku: variant.id,
      sku: variant.sku,
      color: variant.color,
      size: variant.size,
      unitCost: unitCost,
    }

    // Recalculate subtotal if quantity exists
    if (newDetails[idx].quantity && unitCost) {
      const qty = parseInt(newDetails[idx].quantity) || 0
      const cost = parseInt(unitCost) || 0
      newDetails[idx].subtotal = qty * cost
    } else {
      newDetails[idx].subtotal = 0
    }

    setDetails(newDetails)
  }

  const handleDetailChange = (idx, field, value) => {
    const newDetails = [...details]
    newDetails[idx][field] = value

    if (field === "quantity" || field === "unitCost") {
      const qty = parseInt(newDetails[idx].quantity) || 0
      const cost = parseInt(newDetails[idx].unitCost) || 0
      newDetails[idx].subtotal = qty * cost
    }

    setDetails(newDetails)
  }

  const getVariantsByProduct = (productId) => {
    if (!productId) return []
    return variants.filter((v) => v.productId === parseInt(productId))
  }

  const totalCost = details.reduce((sum, d) => sum + (d.subtotal || 0), 0)

  const validate = () => {
    const newErrors = {}
    if (!form.supplierId) newErrors.supplierId = "Nhà cung cấp là bắt buộc"
    // receivedDate is optional - backend will use current date as default
    if (details.length === 0) {
      newErrors.details = "Phải có ít nhất 1 dòng chi tiết"
    }
    return newErrors
  }

  const isFormValid = () => {
    // Check basic form fields
    if (!form.supplierId) return false
    
    // Check details
    if (details.length === 0) return false
    
    // Check each detail row
    return details.every((detail) => {
      return (
        detail.productId &&
        detail.variantSku &&
        detail.quantity &&
        parseInt(detail.quantity) > 0 &&
        detail.unitCost &&
        parseInt(detail.unitCost) > 0
      )
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const newErrors = validate()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    try {
      // Create GRN Header first
      const grnHeaderPayload = {
        supplierId: parseInt(form.supplierId),
        note: form.note,
      }

      // Only include receivedDate if it has a value
      if (form.receivedDate && form.receivedDate.trim() !== '') {
        grnHeaderPayload.receivedDate = form.receivedDate
      }

      const grnHeader = await api.create("grn-headers", grnHeaderPayload)
      
      // Create GRN Details
      const grnDetailsPayload = details.map((d) => ({
        grnHeaderId: grnHeader.id,
        variantSku: parseInt(d.variantSku),
        quantity: parseInt(d.quantity),
        unitCost: parseInt(d.unitCost),
      }))

      // Create all details
      for (const detail of grnDetailsPayload) {
        await api.create("grn-details", detail)
      }
      
      setToast({ type: "success", message: "Tạo phiếu nhập kho thành công" })
      setTimeout(() => navigate("/grn"), 1500)
    } catch (error) {
      console.error("Error creating GRN:", error)
      
      // Extract error message from different error formats
      let errorMessage = "Có lỗi xảy ra khi tạo phiếu nhập kho"
      
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message
      } else if (error.response?.data?.error) {
        errorMessage = error.response.data.error
      } else if (error.message) {
        errorMessage = error.message
      }
      
      setToast({ 
        type: "error", 
        message: errorMessage
      })
    }
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
              value={form.supplierId}
              onChange={(e) => setForm({ ...form, supplierId: e.target.value })}
              options={suppliers}
              error={errors.supplierId}
              required
            />

            <FormField
              label="Ngày nhận"
              type="date"
              value={form.receivedDate}
              onChange={(e) => setForm({ ...form, receivedDate: e.target.value })}
              error={errors.receivedDate}
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
                  <th className="px-4 py-2 text-left w-48">Sản phẩm</th>
                  <th className="px-4 py-2 text-left w-48">Biến thể (SKU)</th>
                  <th className="px-4 py-2 text-left w-24">Màu</th>
                  <th className="px-4 py-2 text-left w-24">Size</th>
                  <th className="px-4 py-2 text-right w-24">Số lượng</th>
                  <th className="px-4 py-2 text-right w-32">Giá nhập</th>
                  <th className="px-4 py-2 text-right w-32">Thành tiền</th>
                  <th className="px-4 py-2 text-center w-20">Thao tác</th>
                </tr>
              </thead>
              <tbody>
                {details.length === 0 ? (
                  <tr>
                  </tr>
                ) : (
                  details.map((detail, idx) => (
                    <tr key={idx} className="border-b border-neutral-200 hover:bg-neutral-50">
                      {/* Product Select */}
                      <td className="px-4 py-2">
                        <select
                          ref={(el) => (rowRefs.current[idx] = el)}
                          value={detail.productId}
                          onChange={(e) => handleProductChange(idx, e.target.value)}
                          className="w-full px-2 py-1 border border-neutral-200 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                          <option value="">Chọn sản phẩm</option>
                          {products.map((p) => (
                            <option key={p.id} value={p.id}>
                              {p.name}
                            </option>
                          ))}
                        </select>
                      </td>

                      {/* Variant Select */}
                      <td className="px-4 py-2">
                        <select
                          value={detail.variantSku}
                          onChange={(e) => handleVariantChange(idx, e.target.value)}
                          disabled={!detail.productId}
                          className="w-full px-2 py-1 border border-neutral-200 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-neutral-100 disabled:cursor-not-allowed"
                        >
                          <option value="">
                            {detail.productId ? "Chọn biến thể" : "Chọn sản phẩm trước"}
                          </option>
                          {detail.productId &&
                            getVariantsByProduct(detail.productId).map((v) => (
                              <option key={v.id} value={v.id}>
                                {v.sku} ({v.color} - {v.size})
                              </option>
                            ))}
                        </select>
                      </td>

                      {/* Color (Read-only) */}
                      <td className="px-4 py-2">
                        <input
                          type="text"
                          value={detail.color}
                          readOnly
                          className="w-full px-2 py-1 bg-neutral-50 border border-neutral-200 rounded text-sm text-neutral-600"
                          placeholder="-"
                        />
                      </td>

                      {/* Size (Read-only) */}
                      <td className="px-4 py-2">
                        <input
                          type="text"
                          value={detail.size}
                          readOnly
                          className="w-full px-2 py-1 bg-neutral-50 border border-neutral-200 rounded text-sm text-neutral-600"
                          placeholder="-"
                        />
                      </td>

                      {/* Quantity */}
                      <td className="px-4 py-2">
                        <input
                          type="number"
                          value={detail.quantity}
                          onChange={(e) => handleDetailChange(idx, "quantity", e.target.value)}
                          className="w-full px-2 py-1 border border-neutral-200 rounded text-sm text-right focus:outline-none focus:ring-2 focus:ring-blue-500"
                          placeholder="0"
                          min="1"
                        />
                      </td>

                      {/* Unit Cost */}
                      <td className="px-4 py-2">
                        <input
                          type="number"
                          value={detail.unitCost}
                          onChange={(e) => handleDetailChange(idx, "unitCost", e.target.value)}
                          className="w-full px-2 py-1 border border-neutral-200 rounded text-sm text-right focus:outline-none focus:ring-2 focus:ring-blue-500"
                          placeholder="0"
                          min="0"
                        />
                      </td>

                      {/* Subtotal */}
                      <td className="px-4 py-2 text-right font-medium">{formatMoney(detail.subtotal || 0)}</td>

                      {/* Actions */}
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
                  ))
                )}
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
          <button
            type="submit"
            disabled={!isFormValid()}
            className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-blue-600"
          >
            Lưu nháp
          </button>
        </div>
      </form>

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
