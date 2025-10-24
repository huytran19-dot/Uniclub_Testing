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
import { formatDate, formatDateTime, getStatusLabel, getStatusType } from "../../lib/utils"

export default function CategoryList() {
  const navigate = useNavigate()
  const [categories, setCategories] = useState([])
  const [toast, setToast] = useState(null)
  const [confirmDelete, setConfirmDelete] = useState(null)

  useEffect(() => {
    loadCategories()
  }, [])

  const loadCategories = async () => {
    const data = await api.list("categories")
    setCategories(data || [])
  }

  const handleDelete = async () => {
    await api.delete("categories", confirmDelete.id)
    setToast({ message: "Xóa danh mục thành công", type: "success" })
    setConfirmDelete(null)
    loadCategories()
  }

  const columns = [
    { key: "id", label: "ID" },
    { key: "name", label: "Tên danh mục" },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
    { key: "createdAt", label: "Ngày tạo", render: (row) => formatDateTime(row.createdAt) },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Sản phẩm", path: "/products" }, { label: "Danh mục" }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Danh mục</h1>
        <button
          onClick={() => navigate("/categories/new")}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Thêm mới
        </button>
      </div>

      <Card>
        <div className="p-6">
          <Table
            columns={columns}
            data={categories}
            onEdit={(row) => navigate(`/categories/${row.id}`)}
            onDelete={(row) => setConfirmDelete(row)}
          />
        </div>
      </Card>

      {confirmDelete && (
        <Confirm
          isOpen={true}
          title="Xóa danh mục"
          message={`Bạn có chắc muốn xóa danh mục "${confirmDelete.name}"?`}
          onConfirm={handleDelete}
          onCancel={() => setConfirmDelete(null)}
        />
      )}

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
