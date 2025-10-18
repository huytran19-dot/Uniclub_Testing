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
import { formatDate, getStatusLabel, getStatusType } from "../../lib/utils"

export default function ProductList() {
  const navigate = useNavigate()
  const [products, setProducts] = useState([])
  const [categories, setCategories] = useState([])
  const [brands, setBrands] = useState([])
  const [toast, setToast] = useState(null)
  const [confirmDelete, setConfirmDelete] = useState(null)

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    const [prods, cats, brds] = await Promise.all([api.list("products"), api.list("categories"), api.list("brands")])
    setProducts(prods || [])
    setCategories(cats || [])
    setBrands(brds || [])
  }

  const handleDelete = async () => {
    await api.delete("products", confirmDelete.id)
    setToast({ message: "Xóa sản phẩm thành công", type: "success" })
    setConfirmDelete(null)
    loadData()
  }

  const getCategoryName = (id) => categories.find((c) => c.id === id)?.name || "-"
  const getBrandName = (id) => brands.find((b) => b.id === id)?.name || "-"

  const columns = [
    { key: "id", label: "ID" },
    { key: "name", label: "Tên" },
    {
      key: "id_category",
      label: "Danh mục",
      render: (row) => getCategoryName(row.id_category),
    },
    {
      key: "id_brand",
      label: "Nhãn hàng",
      render: (row) => getBrandName(row.id_brand),
    },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
    { key: "created_at", label: "Ngày tạo", render: (row) => formatDate(row.created_at) },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Sản phẩm", path: "/products" }, { label: "Danh sách" }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Sản phẩm</h1>
        <button
          onClick={() => navigate("/products/new")}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Thêm mới
        </button>
      </div>

      <Card>
        <div className="p-6">
          <Table
            columns={columns}
            data={products}
            onEdit={(row) => navigate(`/products/${row.id}`)}
            onDelete={(row) => setConfirmDelete(row)}
          />
        </div>
      </Card>

      {confirmDelete && (
        <Confirm
          isOpen={true}
          title="Xóa sản phẩm"
          message={`Bạn có chắc muốn xóa sản phẩm "${confirmDelete.name}"?`}
          onConfirm={handleDelete}
          onCancel={() => setConfirmDelete(null)}
        />
      )}

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
