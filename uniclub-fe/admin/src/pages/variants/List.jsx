"use client"

import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import Card from "../../components/Card"
import Table from "../../components/Table"
import Badge from "../../components/Badge"
import Toast from "../../components/Toast"
import Confirm from "../../components/Confirm"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { formatMoney, getStatusLabel, getStatusType } from "../../lib/utils"

export default function VariantList() {
  const navigate = useNavigate()
  const [variants, setVariants] = useState([])
  const [products, setProducts] = useState([])
  const [colors, setColors] = useState([])
  const [sizes, setSizes] = useState([])
  const [toast, setToast] = useState(null)
  const [confirmDelete, setConfirmDelete] = useState(null)

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    const [vars, prods, cols, szs] = await Promise.all([
      api.list("variants"),
      api.list("products"),
      api.list("colors"),
      api.list("sizes"),
    ])
    setVariants(vars || [])
    setProducts(prods || [])
    setColors(cols || [])
    setSizes(szs || [])
  }

  const handleDelete = async () => {
    await api.delete("variants", confirmDelete.sku)
    setToast({ message: "Xóa biến thể thành công", type: "success" })
    setConfirmDelete(null)
    loadData()
  }

  const getProductName = (id) => products.find((p) => p.id === id)?.name || "-"
  const getColorName = (id) => colors.find((c) => c.id === id)?.name || "-"
  const getColorHex = (id) => colors.find((c) => c.id === id)?.hexCode || "#000000"
  const getSizeName = (id) => sizes.find((s) => s.id === id)?.name || "-"

  const columns = [
    { key: "sku", label: "SKU" },
    {
      key: "productId",
      label: "Sản phẩm",
      render: (row) => getProductName(row.productId),
    },
    {
      key: "colorId",
      label: "Màu",
      render: (row) => (
        <div className="flex items-center gap-2">
          <div
            className="w-4 h-4 rounded border border-neutral-300"
            style={{ backgroundColor: getColorHex(row.colorId) }}
          />
          <span>{getColorName(row.colorId)}</span>
        </div>
      ),
    },
    {
      key: "sizeId",
      label: "Kích cỡ",
      render: (row) => getSizeName(row.sizeId),
    },
    {
      key: "price",
      label: "Giá",
      render: (row) => formatMoney(row.price),
    },
    {
      key: "images",
      label: "Hình ảnh",
      render: (row) => (
        row.images ? (
          <img 
            src={row.images} 
            alt="Variant" 
            className="w-12 h-12 object-cover rounded border border-neutral-200"
            onError={(e) => {
              e.target.style.display = 'none'
              e.target.nextSibling.style.display = 'block'
            }}
          />
        ) : (
          <div className="w-12 h-12 bg-neutral-100 rounded border border-neutral-200 flex items-center justify-center text-neutral-400 text-xs">
            No image
          </div>
        )
      ),
    },
    {
      key: "quantity",
      label: "Tồn kho",
      render: (row) => (
        <div className="flex items-center gap-2">
          <Badge status={row.quantity < 5 ? "cancelled" : "completed"} label={row.quantity} />
          <span className="text-xs text-neutral-500 cursor-help" title="Tồn kho chỉ đọc. Vui lòng nhập kho qua GRN.">
            ℹ️
          </span>
        </div>
      ),
    },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Sản phẩm", path: "/products" }, { label: "Biến thể" }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Biến thể</h1>
        <button
          onClick={() => navigate("/variants/new")}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Thêm mới
        </button>
      </div>

      <Card>
        <div className="p-6">
          <Table
            columns={columns}
            data={variants}
            onEdit={(row) => navigate(`/variants/${row.sku}`)}
            onDelete={(row) => setConfirmDelete(row)}
          />
        </div>
      </Card>

      {confirmDelete && (
        <Confirm
          isOpen={true}
          title="Xóa biến thể"
          message={`Bạn có chắc muốn xóa biến thể SKU "${confirmDelete.sku}"?`}
          onConfirm={handleDelete}
          onCancel={() => setConfirmDelete(null)}
        />
      )}

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
